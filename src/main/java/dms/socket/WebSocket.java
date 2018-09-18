package dms.socket;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import dms.utils.Constants;

//websocket连接URL地址和可被调用配置
@Component("Ws")
@ServerEndpoint(value = "/webSocket/{userId}", configurator = SpringConfigurator.class)
public class WebSocket {
	// 日志记录
	private Logger logger = LoggerFactory.getLogger(WebSocket.class);
	// 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	public static int onlineCount = 0;

	private static String flag = ""; // 记录向客户端定时发送数据的时间戳
	private static Map<String, String> flagMap = new HashMap<String, String>(); // 记录客户端是否有返回记录的map(用于判断是否在线)

	// 记录每个用户下多个终端的连接
	public static Map<String, Session> userSocket = new HashMap<String, Session>();

	// 需要session来对用户发送数据, 获取连接特征userId
	// private Session session;
	// private String userId;

	/**
	 * @Title: onOpen
	 * @Description: websocekt连接建立时的操作
	 * @param @param
	 *            userId 用户id
	 * @param @param
	 *            session websocket连接的session属性
	 * @param @throws
	 *            IOException
	 */
	@OnOpen
	public void onOpen(@PathParam("userId") String userId, Session session) throws IOException {

		if (userSocket.containsKey(userId)) {
			JSONObject jo = new JSONObject();
			jo.put("status", Constants.noticeDownStatus);
			jo.put("info", "你被挤下线了");
			sendMessageToUser(userId, userSocket.get(userId), JSON.toJSONString(jo));
			userSocket.put(userId, session);
		} else {
			onlineCount++;
			logger.debug("当前用户id:{}登录", userId);
			userSocket.put(userId, session);
			for (String key : userSocket.keySet()) {
				if (!key.equals(userId)) {
					JSONObject jo = new JSONObject();
					jo.put("status", Constants.onlineTip);
					jo.put("info", userId);
					sendMessageToUser(userId, userSocket.get(key), JSON.toJSONString(jo));
				}
			}
		}
	}

	/**
	 * @Title: onClose
	 * @Description: 连接关闭的操作
	 */
	@OnClose
	public void onClose(@PathParam("userId") String userId, Session session) {

		userSocket.remove(userId, session);
		onlineCount--;
		if (!userSocket.keySet().contains(userId)) {
			for (String key : userSocket.keySet()) {
				JSONObject jo = new JSONObject();
				jo.put("status", Constants.downlineTip);
				jo.put("info", userId);
				sendMessageToUser(userId, userSocket.get(key), JSON.toJSONString(jo));
			}
		}
	}

	/**
	 * @Title: onMessage
	 * @Description: 收到消息后的操作
	 * @param @param
	 *            message 收到的消息
	 * @param @param
	 *            session 该连接的session属性
	 */
	@OnMessage
	public void onMessage(String message, Session session, @PathParam("userId") String userId) {

		logger.debug("收到来自用户id为：{}的消息：{}", userId, message);
		if (session == null)
			logger.debug("session null");
	}

	/**
	 * @Title: onError
	 * @Description: 连接发生错误时候的操作
	 * @param @param
	 *            session 该连接的session
	 * @param @param
	 *            error 发生的错误
	 */
	@OnError
	public void onError(Session session, @PathParam("userId") String userId, Throwable error) {
		logger.debug("用户id为：{}的连接发送错误", userId);
		error.printStackTrace();
	}

	/**
	 * @Title: sendMessageToUser
	 * @Description: 发送消息给用户下的所有终端
	 * @param @param
	 *            WS 客户端
	 * @param @param
	 *            message 发送的消息
	 * @param @return
	 *            发送成功返回true，反则返回false
	 */
	public static Boolean sendMessageToUser(String userId, Session session, String message) {
		try {
			session.getBasicRemote().sendText(message);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Scheduled
	public Boolean sendJudgeMessage() {
//@PathParam("userId") String userId
//		if ("".equals(flag)) {
			for (String key : userSocket.keySet()) {
				Session session = userSocket.get(key);
				try {
					JSONObject jo = new JSONObject();
					jo.put("status", Constants.judgeStatus);
					jo.put("info", String.valueOf(System.currentTimeMillis()));
					session.getBasicRemote().sendText(JSON.toJSONString(jo));
				} catch (IOException e) {
					e.printStackTrace();
					logger.debug(" 给用户id为：{}发送消息失败", key);
					return false;
				}

			}
//		} else {
//			Set<String> tempSet = new HashSet<String>();
			// for(String key:flagMap)
//		}
		return true;
	}

}