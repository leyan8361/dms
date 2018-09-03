package dms.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;

import dms.entity.Log;
import dms.entity.Role;
import dms.entity.UserGroup;

public interface SysService {

	public int addLog(Log log);

	public PageInfo<Log> getLogList(int currentPage, String startDate, String endDate, String userName, String content);

	public int batchDelLog(String delStr);
	
	public Role checkRoleName(String name);

	public int addRole(Role role);

	public PageInfo<Role> getRoleList(int currentPage, String name);

	public int updateRoleInfo(int id, String name, String description);

	public int delRoleInfo(int id);

	public UserGroup checkUserGroupName(String name);

	public int addUserGroup(UserGroup ug);

	public PageInfo<UserGroup> getUserGroupList(int currentPage, String name);

	public int updateUserGroupInfo(int id, String name, String description);

	public int delUserGroupInfo(int id);

	public List<Role> getAllRoleInfo();

	public List<UserGroup> getAllUserGroupInfo();
	
	public JSONArray getPageFunctionList();
}
