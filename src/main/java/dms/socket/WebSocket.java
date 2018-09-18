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

//websocket����URL��ַ�Ϳɱ���������
@Component("Ws")
@ServerEndpoint(value = "/webSocket/{userId}", configurator = SpringConfigurator.class)
public class WebSocket {
	// ��־��¼
	private Logger logger = LoggerFactory.getLogger(WebSocket.class);
	// ��̬������������¼��ǰ������������Ӧ�ð�����Ƴ��̰߳�ȫ�ġ�
	public static int onlineCount = 0;

	private static String flag = ""; // ��¼��ͻ��˶�ʱ�������ݵ�ʱ���
	private static Map<String, String> flagMap = new HashMap<String, String>(); // ��¼�ͻ����Ƿ��з��ؼ�¼��map(�����ж��Ƿ�����)

	// ��¼ÿ���û��¶���ն˵�����
	public static Map<String, Session> userSocket = new HashMap<String, Session>();

	// ��Ҫsession�����û���������, ��ȡ��������userId
	// private Session session;
	// private String userId;

	/**
	 * @Title: onOpen
	 * @Description: websocekt���ӽ���ʱ�Ĳ���
	 * @param @param
	 *            userId �û�id
	 * @param @param
	 *            session websocket���ӵ�session����
	 * @param @throws
	 *            IOException
	 */
	@OnOpen
	public void onOpen(@PathParam("userId") String userId, Session session) throws IOException {

		if (userSocket.containsKey(userId)) {
			JSONObject jo = new JSONObject();
			jo.put("status", Constants.noticeDownStatus);
			jo.put("info", "�㱻��������");
			sendMessageToUser(userId, userSocket.get(userId), JSON.toJSONString(jo));
			userSocket.put(userId, session);
		} else {
			onlineCount++;
			logger.debug("��ǰ�û�id:{}��¼", userId);
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
	 * @Description: ���ӹرյĲ���
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
	 * @Description: �յ���Ϣ��Ĳ���
	 * @param @param
	 *            message �յ�����Ϣ
	 * @param @param
	 *            session �����ӵ�session����
	 */
	@OnMessage
	public void onMessage(String message, Session session, @PathParam("userId") String userId) {

		logger.debug("�յ������û�idΪ��{}����Ϣ��{}", userId, message);
		if (session == null)
			logger.debug("session null");
	}

	/**
	 * @Title: onError
	 * @Description: ���ӷ�������ʱ��Ĳ���
	 * @param @param
	 *            session �����ӵ�session
	 * @param @param
	 *            error �����Ĵ���
	 */
	@OnError
	public void onError(Session session, @PathParam("userId") String userId, Throwable error) {
		logger.debug("�û�idΪ��{}�����ӷ��ʹ���", userId);
		error.printStackTrace();
	}

	/**
	 * @Title: sendMessageToUser
	 * @Description: ������Ϣ���û��µ������ն�
	 * @param @param
	 *            WS �ͻ���
	 * @param @param
	 *            message ���͵���Ϣ
	 * @param @return
	 *            ���ͳɹ�����true�����򷵻�false
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
					logger.debug(" ���û�idΪ��{}������Ϣʧ��", key);
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