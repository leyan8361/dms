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

	// ��¼����״̬�ļ���
	public static Set<String> downLineSet = new HashSet<String>();

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
		logger.debug("�û�Id:{}׼����½��������", userId);
		// System.out.println("��ǰ��flag : " + flag);
		if (userSocket.containsKey(userId)) {
			JSONObject jo = new JSONObject();
			jo.put("status", Constants.noticeDownStatus);
			jo.put("info", "�㱻��������");
			logger.debug("�û�Id:{}����������", userId);
			sendMessageToUser(userId, userSocket.get(userId), JSON.toJSONString(jo));
			userSocket.put(userId, session);
			// logger.debug("�û�id:{}������,��ǰ��������{}");
			onlineCount++;
		} else {
			if (downLineSet.contains(userId)) {
				downLineSet.remove(userId);
			}
			onlineCount++;
			logger.debug("��ǰ�û�id:{}��¼,��ǰ��������{}", userId, onlineCount);
			userSocket.put(userId, session);
			logger.debug("��ǰ��Ա��Ϣ��" + JSON.toJSONString(userSocket.keySet()));
			flagMap.put(flag + "_" + userId, "У����Ϣ");
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

		if (session == userSocket.get(userId)) {
			if (!downLineSet.contains(userId)) {
				userSocket.remove(userId, session);
				onlineCount--;
				logger.debug("�û�id:{}����,��ǰ��������Ϊ{}", userId, onlineCount);
				logger.debug("��ǰ��Ա��Ϣ��" + JSON.toJSONString(userSocket.keySet()));
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
			logger.debug("�û�id:{}����,��ǰ��������Ϊ{}", userId, onlineCount);
			logger.debug("��ǰ��Ա��Ϣ��" + JSON.toJSONString(userSocket.keySet()));
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

		// logger.debug("�յ������û�idΪ��{}����Ϣ��{}", userId, message)
		JSONObject jo = JSONObject.parseObject(message);
		if (jo.getString("status").equals(Constants.judgeStatus)) {
			// У��Է��Ƿ�����(��Է���������)
			flagMap.put(jo.getString("info"), "У����Ϣ");
		} else if (jo.getString("status").equals(Constants.judgeStatus2)) {
			sendMessageToUser(userId, userSocket.get(userId), JSON.toJSONString(jo));
			// У��Է��Ƿ�����(�ӶԷ���������)
			String tempUserId = jo.getString("info");
			// ����ǰ�û��鲻�����������ݹ�����userId, ����Ϊ��������, ���û����¼����û�����
			if (!userSocket.containsKey(tempUserId)) {
				onlineCount++;
				logger.debug("���û�id:{}�����û���,��ǰ����{}", userId, onlineCount);
				userSocket.put(userId, session);
				flagMap.put(flag + "_" + userId, "У����Ϣ");
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
	 * @Description: ���ӷ�������ʱ��Ĳ���
	 * @param @param
	 *            session �����ӵ�session
	 * @param @param
	 *            error �����Ĵ���
	 */
	@OnError
	public void onError(Session session, @PathParam("userId") String userId, Throwable error) {

		logger.debug("�û�idΪ��{}�����ӷ��ʹ���", userId);
		// error.printStackTrace();
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
						// logger.debug(" ���û�idΪ��{}������Ϣʧ��", key);
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
						logger.debug("�û�Id��{}��Ϊ��������,��ǰ�û�����{}", userId, onlineCount);
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
					// logger.debug(" ���û�idΪ��{}������Ϣʧ��", key);
					return false;
				}
			}
		}
		return true;
	}

}