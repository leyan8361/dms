package dms.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import dms.dao.SysDao;
import dms.entity.Role;
import dms.entity.UserGroup;
import dms.service.SysService;
import dms.utils.Constants;

@Service("sysService")
public class SysServiceImpl implements SysService {

	@Autowired
	private SysDao sysDao;

	public Role checkRoleName(String name) {

		return sysDao.checkRoleName(name);
	}

	public int addRole(Role role) {

		return sysDao.addRole(role);
	}

	public PageInfo<Role> getRoleList(int currentPage, String name) {

		PageHelper.startPage(currentPage, Constants.pageSize);
		List<Role> list = sysDao.getRoleList(name);
		PageInfo<Role> roleList = new PageInfo<>(list);
		return roleList;
	}

	public int updateRoleInfo(int id, String name, String description) {

		return sysDao.updateRoleInfo(id, name, description);
	}

	public int delRoleInfo(int id) {

		return sysDao.delRoleInfo(id);
	}

	public UserGroup checkUserGroupName(String name) {

		return sysDao.checkUserGroupName(name);
	}

	public int addUserGroup(UserGroup ug) {

		return sysDao.addUserGroup(ug);
	}

	public PageInfo<UserGroup> getUserGroupList(int currentPage, String name) {

		PageHelper.startPage(currentPage, Constants.pageSize);
		List<UserGroup> list = sysDao.getUserGroupList(name);
		PageInfo<UserGroup> userGroupList = new PageInfo<UserGroup>(list);
		return userGroupList;
	}

	public int updateUserGroupInfo(int id, String name, String description) {

		return sysDao.updateUserGroupInfo(id, name, description);
	}

	public int delUserGroupInfo(int id) {

		return sysDao.delUserGroupInfo(id);
	}

	public List<Role> getAllRoleInfo() {

		return sysDao.getAllRoleInfo();
	}

	public List<UserGroup> getAllUserGroupInfo() {

		return sysDao.getAllUserGroupInfo();
	}
}
