package dms.socket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.ibatis.annotations.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.server.standard.SpringConfigurator;

//websocket����URL��ַ�Ϳɱ���������
@ServerEndpoint(value = "/webSocket/{userId}", configurator = SpringConfigurator.class)
public class WebSocket {
	// ��־��¼
	private Logger logger = LoggerFactory.getLogger(WebSocket.class);
	// ��̬������������¼��ǰ������������Ӧ�ð�����Ƴ��̰߳�ȫ�ġ�
	public static int onlineCount = 0;

	// ��¼ÿ���û��¶���ն˵�����
	private static Map<String, WebSocket> userSocket = new HashMap<>();

	// ��Ҫsession�����û���������, ��ȡ��������userId
	private Session session;
	private String userId;

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
		this.session = session;
		this.userId = userId;
		// if (!userSocket.containsKey(userId)) {
		// onlineCount++;
		// }
		// ���ݸ��û���ǰ�Ƿ��Ѿ��ڱ���ն˵�¼������Ӳ���
		if (userSocket.containsKey(this.userId)) {
			// logger.debug("��ǰ�û�id:{}���������ն˵�¼", this.userId);
			// userSocket.get(this.userId).add(this); // ���Ӹ��û�set�е�����ʵ��
			logger.debug("�ѵ�¼");
		} else {
			onlineCount++;
			logger.debug("��ǰ�û�id:{}��¼", this.userId);
			userSocket.put(this.userId, this);
		}
		logger.debug("��ǰ�����û���Ϊ��{},�����ն˸���Ϊ��{}", userSocket.size(), onlineCount);
	}

	/**
	 * @Title: onClose
	 * @Description: ���ӹرյĲ���
	 */
	@OnClose
	public void onClose() {
		// �Ƴ���ǰ�û��ն˵�¼��websocket��Ϣ,������û��������ն˶������ˣ���ɾ�����û��ļ�¼
		// if (userSocket.get(this.userId).size() == 0) {
		// userSocket.remove(this.userId);
		// onlineCount--;
		// } else {
		// userSocket.get(this.userId).remove(this);
		// onlineCount--;
		// }
		userSocket.remove(this.userId);
		onlineCount--;
		// logger.debug("�û�{}��¼���ն˸�����Ϊ{}", this.userId,
		// userSocket.get(this.userId).size());
		logger.debug("��ǰ�����û���Ϊ��{},�����ն˸���Ϊ��{}", userSocket.size(), onlineCount);
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
	public void onMessage(String message, Session session) {
		logger.debug("�յ������û�idΪ��{}����Ϣ��{}", this.userId, message);
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
	public void onError(Session session, Throwable error) {
		logger.debug("�û�idΪ��{}�����ӷ��ʹ���", this.userId);
		error.printStackTrace();
	}

	/**
	 * @Title: sendMessageToUser
	 * @Description: ������Ϣ���û��µ������ն�
	 * @param @param
	 *            userId �û�id
	 * @param @param
	 *            message ���͵���Ϣ
	 * @param @return
	 *            ���ͳɹ�����true�����򷵻�false
	 */
	public Boolean sendMessageToUser(String userId, String message) {
		if (userSocket.containsKey(userId)) {
			logger.debug(" ���û�idΪ��{}�������ն˷�����Ϣ��{}", userId, message);
			// for (WebSocket WS : userSocket.get(userId)) {
			WebSocket WS = userSocket.get(userId);
			logger.debug("sessionIdΪ:{}", WS.session.getId());
			try {
				WS.session.getBasicRemote().sendText(message);
			} catch (IOException e) {
				e.printStackTrace();
				logger.debug(" ���û�idΪ��{}������Ϣʧ��", userId);
				return false;
			}
			// }
			return true;
		}
		logger.debug("���ʹ��󣺵�ǰ���Ӳ�����idΪ��{}���û�", userId);
		return false;
	}

}