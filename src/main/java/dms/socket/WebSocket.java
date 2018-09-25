package dms.socket;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

	// 记录下线状态的集合
	public static Set<String> downLineSet = new HashSet<String>();

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
		logger.debug("用户Id:{}准备登陆啦！！！", userId);
		// System.out.println("当前的flag : " + flag);
		if (userSocket.containsKey(userId)) {
			JSONObject jo = new JSONObject();
			jo.put("status", Constants.noticeDownStatus);
			jo.put("info", "你被挤下线了");
			logger.debug("用户Id:{}被挤下线了", userId);
			sendMessageToUser(userId, userSocket.get(userId), JSON.toJSONString(jo));
			userSocket.put(userId, session);
			// logger.debug("用户id:{}被顶掉,当前在线人数{}");
			onlineCount++;
		} else {
			if (downLineSet.contains(userId)) {
				downLineSet.remove(userId);
			}
			onlineCount++;
			logger.debug("当前用户id:{}登录,当前在线人数{}", userId, onlineCount);
			userSocket.put(userId, session);
			logger.debug("当前人员信息：" + JSON.toJSONString(userSocket.keySet()));
			flagMap.put(flag + "_" + userId, "校验信息");
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

		if (session == userSocket.get(userId)) {
			if (!downLineSet.contains(userId)) {
				userSocket.remove(userId, session);
				onlineCount--;
				logger.debug("用户id:{}下线,当前在线人数为{}", userId, onlineCount);
				logger.debug("当前人员信息：" + JSON.toJSONString(userSocket.keySet()));
				downLineSet.add(userId);
				if (!userSocket.keySet().contains(userId)) {
					for (String key : userSocket.keySet()) {
						JSONObject jo = new JSONObject();
						jo.put("status", Constants.downlineTip);
						jo.put("info", userId);
						sendMessageToUser(userId, userSocket.get(key), JSON.toJSONString(jo));
					}
				}

			}
		} else {
			onlineCount--;
			logger.debug("用户id:{}下线,当前在线人数为{}", userId, onlineCount);
			logger.debug("当前人员信息：" + JSON.toJSONString(userSocket.keySet()));
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

		// logger.debug("收到来自用户id为：{}的消息：{}", userId, message)
		JSONObject jo = JSONObject.parseObject(message);
		if (jo.getString("status").equals(Constants.judgeStatus)) {
			// 校验对方是否在线(向对方发送数据)
			flagMap.put(jo.getString("info"), "校验信息");
		} else if (jo.getString("status").equals(Constants.judgeStatus2)) {
			sendMessageToUser(userId, userSocket.get(userId), JSON.toJSONString(jo));
			// 校验对方是否在线(从对方接收数据)
			String tempUserId = jo.getString("info");
			// 若当前用户组不包含发送数据过来的userId, 则视为断线重连, 将用户重新加入用户组中
			if (!userSocket.containsKey(tempUserId)) {
				onlineCount++;
				logger.debug("将用户id:{}加入用户组,当前人数{}", userId, onlineCount);
				userSocket.put(userId, session);
				flagMap.put(flag + "_" + userId, "校验信息");
				for (String key : userSocket.keySet()) {
					if (!key.equals(userId)) {
						JSONObject jo2 = new JSONObject();
						jo2.put("status", Constants.onlineTip);
						jo2.put("info", userId);
						sendMessageToUser(userId, userSocket.get(key), JSON.toJSONString(jo2));
					}
				}
			}
		}
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
		// error.printStackTrace();
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
	public static synchronized Boolean sendMessageToUser(String userId, Session session, String message) {
		try {
			session.getBasicRemote().sendText(message);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static synchronized Boolean sendMessageToUser2(String userId, String message) {
		try {
			if (userSocket.containsKey(userId)) {
				userSocket.get(userId).getBasicRemote().sendText(message);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static synchronized Boolean sendMessageToUser3(List<String> userIdList, String message) {
		try {
			for (String userId : userIdList) {
				if (userSocket.containsKey(userId)) {
					userSocket.get(userId).getBasicRemote().sendText(message);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Scheduled
	public Boolean sendJudgeMessage() {

		if (flagMap.size() == 0) {
			if (onlineCount != 0) {
				// System.out.println("yi kai shi!!!");
				flag = String.valueOf(System.currentTimeMillis());
				for (String key : userSocket.keySet()) {
					Session session = userSocket.get(key);
					try {
						JSONObject jo = new JSONObject();
						jo.put("status", Constants.judgeStatus);
						jo.put("info", flag);
						session.getBasicRemote().sendText(JSON.toJSONString(jo));
					} catch (IOException e) {
						e.printStackTrace();
						// logger.debug(" 给用户id为：{}发送消息失败", key);
						return false;
					}
				}
			}
		} else {
			// System.out.println("di er ci!!!!!");
			Set<String> set = new HashSet<String>();
			for (String key : flagMap.keySet()) {
				if (key.split("_")[0].equals(flag)) {
					set.add(key.split("_")[1]);
				}
			}
			for (String userId : userSocket.keySet()) {
				if (!set.contains(userId)) {
					if (!downLineSet.contains(userId)) {
						userSocket.remove(userId);
						onlineCount--;
						logger.debug("用户Id：{}因为断网下线,当前用户数量{}", userId, onlineCount);
						downLineSet.add(userId);
						if (!userSocket.keySet().contains(userId)) {
							for (String key : userSocket.keySet()) {
								JSONObject jo = new JSONObject();
								jo.put("status", Constants.downlineTip);
								jo.put("info", userId);
								sendMessageToUser(userId, userSocket.get(key), JSON.toJSONString(jo));
							}
						}
					}
				}
			}
			flag = String.valueOf(System.currentTimeMillis());
			for (String key : userSocket.keySet()) {
				Session session = userSocket.get(key);
				try {
					JSONObject jo = new JSONObject();
					jo.put("status", Constants.judgeStatus);
					jo.put("info", flag);
					session.getBasicRemote().sendText(JSON.toJSONString(jo));
				} catch (IOException e) {
					e.printStackTrace();
					// logger.debug(" 给用户id为：{}发送消息失败", key);
					return false;
				}
			}
		}
		return true;
	}

}