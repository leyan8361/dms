package dms.entity;

public class Message {

	private int id;
	private int fromId; // 发送人Id
	private String fromName;
	private int toId; // 接受人Id或接受群组Id
	private String toName;
	private int isGroupMessage; // 是否为群组消息 0是 、 1否
	private String content; // 消息内容 或者 附件地址
	private int type; // 是否是文字消息 0是、1否
	private String sendDate; // 发送日期
	private String fileSize; // 文件大小
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
