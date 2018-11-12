package dms.entity;

import java.util.List;

/**
 * 用户信息
 * 
 * @author ACER
 *
 */
/**
 * @author ACER
 *
 */
public class UserInfo {

	private int id;
	private String uid;
	private String userName;
	private String password;
	private int roleId;
	private String roleName;
	private int userGroupId;
	private String userGroupName;
	private String lastLoginTime;
	private String createDate;
	private String remark;
	private int status; // 0启用/1停用;
	private List<Page> lp;
	private String lastDate; // 最后使用日期(yyyy-MM-dd)
	private String isShow;

	// 还需添加List<模块权限> 属性

	public UserInfo() {

	}

	public UserInfo(String userName, int roleId, String roleName, int userGroupId, String userGroupName,
			String createDate, String remark) {
		this.userName = userName;
		this.roleId = roleId;
		this.roleName = roleName;
		this.userGroupId = userGroupId;
		this.userGroupName = userGroupName;
		this.createDate = createDate;
		this.remark = remark;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(int userGroupId) {
		this.userGroupId = userGroupId;
	}

	public String getUserGroupName() {
		return userGroupName;
	}

	public void setUserGroupName(String userGroupName) {
		this.userGroupName = userGroupName;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<Page> getLp() {
		return lp;
	}

	public void setLp(List<Page> lp) {
		this.lp = lp;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getLastDate() {
		return lastDate;
	}

	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}

}
