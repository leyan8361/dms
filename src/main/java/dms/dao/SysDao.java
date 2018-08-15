package dms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import dms.entity.Role;
import dms.entity.UserGroup;

public interface SysDao {

	public Role checkRoleName(String name);

	public int addRole(Role role);

	public List<Role> getRoleList(@Param("name") String name);

	public int updateRoleInfo(@Param("id") int id, @Param("name") String name,
			@Param("description") String description);

	public int delRoleInfo(int id);

	public UserGroup checkUserGroupName(String name);

	public int addUserGroup(UserGroup ug);

	public List<UserGroup> getUserGroupList(@Param("name") String name);

	public int updateUserGroupInfo(@Param("id") int id, @Param("name") String name,
			@Param("description") String description);

	public int delUserGroupInfo(int id);

	public List<Role> getAllRoleInfo();

	public List<UserGroup> getAllUserGroupInfo();
}
