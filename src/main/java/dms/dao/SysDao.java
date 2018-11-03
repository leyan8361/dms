package dms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import dms.entity.Log;
import dms.entity.Page;
import dms.entity.Role;
import dms.entity.UserFunction;
import dms.entity.UserGroup;
import dms.entity.UserInfo;

public interface SysDao {

	public int addLog(Log log);

	public List<Log> getLogList(@Param("startDate") String startDate, @Param("endDate") String endDate,
			@Param("userName") String userName, @Param("content") String content);

	public int batchDelLog(@Param("str") String str);

	public Role checkRoleName(String name);

	public int addRole(Role role);

	public List<Role> getRoleList(@Param("name") String name);

	public Role getRoleInfo(int id);

	public int updateRoleInfo(@Param("id") int id, @Param("name") String name,
			@Param("description") String description);

	public int delRoleInfo(int id);

	public int delUserOwnRoleInfo(int role);
	
	public UserGroup checkUserGroupName(String name);

	public int addUserGroup(UserGroup ug);

	public List<UserGroup> getUserGroupList(@Param("name") String name);

	public UserGroup getUserGroupInfo(int id);

	public int updateUserGroupInfo(@Param("id") int id, @Param("name") String name,
			@Param("description") String description);

	public int delUserGroupInfo(int id);
	
	public int delUserOwnGroupInfo(int groupId);

	public List<Role> getAllRoleInfo();

	public List<UserGroup> getAllUserGroupInfo();

	public List<Page> getPageFunctionList();

	public int addUserInfo(UserInfo userInfo);

	public int addUserFunction(List<UserFunction> luf);

	public int addTaskSaveStatus(int userId);

	public List<UserInfo> getUserList(@Param("userName") String userName, @Param("roleId") String roleId,
			@Param("userGroupId") String userGroupId);

	public UserInfo getUserInfo(int id);

	public int updateUserInfo(@Param("userId") int userId, @Param("userName") String userName,
			@Param("roleId") String roleId, @Param("roleName") String roleName,
			@Param("userGroupId") String userGroupId, @Param("userGroupName") String userGroupName);

	public int delUserFunction(int userId);
	
	public int delUser(int userId);
}
