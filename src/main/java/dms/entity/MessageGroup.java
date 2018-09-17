package dms.entity;

import java.util.List;

public class MessageGroup {

	private int id;
	private String name;
	private int creator;
	private String createDate;
	private int effectiveDays; // 临时群有效天数 (若是信息群 则为0)
	private int type; // 0消息群/1临时群
	private int count;
	private List<MessageGroupDetail> lmgd;
	private String isShow;

	public MessageGroup() {

	}

	public MessageGroup(String name, int creator, String createDate, int effectiveDays, int type) {
		this.name = name;
		this.creator = creator;
		this.createDate = createDate;
		this.effectiveDays = effectiveDays;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCreator() {
		return creator;
	}

	public void setCreator(int creator) {
		this.creator = creator;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public int getEffectiveDays() {
		return effectiveDays;
	}

	public void setEffectiveDays(int effectiveDays) {
		this.effectiveDays = effectiveDays;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<MessageGroupDetail> getLmgd() {
		return lmgd;
	}

	public void setLmgd(List<MessageGroupDetail> lmgd) {
		this.lmgd = lmgd;
	}

}
