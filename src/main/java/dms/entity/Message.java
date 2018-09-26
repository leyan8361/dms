package dms.entity;

public class Message {

	private int id;
	private int fromId; // ������Id
	private String fromName;
	private int toId; // ������Id�����Ⱥ��Id
	private String toName;
	private int isGroupMessage; // �Ƿ�ΪȺ����Ϣ 0�� �� 1��
	private String content; // ��Ϣ���� ���� ������ַ
	private int type; // �Ƿ���������Ϣ 0�ǡ�1��
	private String sendDate; // ��������
	private String fileSize; // �ļ���С
	private String isShow;

	public Message() {

	}

	public Message(int fromId, int toId, int isGroupMessage, String content, int type, String sendDate,
			String fileSize) {
		this.fromId = fromId;
		this.toId = toId;
		this.isGroupMessage = isGroupMessage;
		this.content = content;
		this.type = type;
		this.sendDate = sendDate;
		this.fileSize = fileSize;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFromId() {
		return fromId;
	}

	public void setFromId(int fromId) {
		this.fromId = fromId;
	}

	public int getToId() {
		return toId;
	}

	public void setToId(int toId) {
		this.toId = toId;
	}

	public int getIsGroupMessage() {
		return isGroupMessage;
	}

	public void setIsGroupMessage(int isGroupMessage) {
		this.isGroupMessage = isGroupMessage;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

}
