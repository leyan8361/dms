package dms.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import dms.dao.SysDao;
import dms.entity.Log;
import dms.entity.Page;
import dms.entity.PageFunction;
import dms.entity.Role;
import dms.entity.UserFunction;
import dms.entity.UserGroup;
import dms.entity.UserInfo;
import dms.service.SysService;
import dms.utils.Constants;
import dms.utils.Utils;

@Service("sysService")
public class SysServiceImpl implements SysService {

	@Autowired
	private SysDao sysDao;

	public int addLog(Log log) {

		return sysDao.addLog(log);
	}

	public PageInfo<Log> getLogList(int currentPage, String startDate, String endDate, String userName,
			String content) {

		PageHelper.startPage(currentPage, Constants.pageSize);
		List<Log> list = sysDao.getLogList(startDate, endDate, userName, content);
		PageInfo<Log> planList = new PageInfo<>(list);
		return planList;
	}

	public int batchDelLog(String delStr) {

		String str = " in (" + delStr + ")";
		return sysDao.batchDelLog(str);
	}

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

	public Role getRoleInfo(int id) {

		return sysDao.getRoleInfo(id);
	}

	public int updateRoleInfo(int id, String name, String description) {

		return sysDao.updateRoleInfo(id, name, description);
	}

	public int delRoleInfo(int id) {

		sysDao.delRoleInfo(id);
		return sysDao.delUserOwnRoleInfo(id);
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

	public UserGroup getUserGroupInfo(int id) {

		return sysDao.getUserGroupInfo(id);
	}

	public int updateUserGroupInfo(int id, String name, String description) {

		return sysDao.updateUserGroupInfo(id, name, description);
	}

	public int delUserGroupInfo(int id) {

		sysDao.delUserGroupInfo(id);

		return sysDao.delUserOwnGroupInfo(id);
	}

	public List<Role> getAllRoleInfo() {

		return sysDao.getAllRoleInfo();
	}

	public List<UserGroup> getAllUserGroupInfo() {

		return sysDao.getAllUserGroupInfo();
	}

	public JSONArray getPageFunctionList() {

		List<Page> list = sysDao.getPageFunctionList();
		JSONArray ja = new JSONArray();
		// Set<Integer> set = new HashSet<Integer>();
		for (Page page : list) {
			JSONObject jo = new JSONObject();
			JSONArray ja2 = new JSONArray();
			List<PageFunction> lpf = page.getLpf();
			jo.put("pageId", page.getId());
			jo.put("pageName", page.getTitle());
			for (PageFunction pf : lpf) {
				JSONObject jo2 = new JSONObject();
				jo2.put("functionId", pf.getId());
				jo2.put("fid", pf.getFid());
				jo2.put("functionName", pf.getFunctionName());
				ja2.add(jo2);
			}
			jo.put("functionInfo", ja2);
			ja.add(jo);
		}
		return ja;
	}

	public boolean addUserInfo(String userName, int roleId, String roleName, int userGroupId, String userGroupName,
			JSONArray functionArray) {

		UserInfo uf = new UserInfo(userName, roleId, roleName, userGroupId, userGroupName,
				Utils.getNowDate("yyyy-MM-dd"), "");
		uf.setPassword("123456");
		sysDao.addUserInfo(uf);
		int userId = uf.getId(); // 获取新增的用户的Id
		List<UserFunction> luf = new ArrayList<UserFunction>();
		if (functionArray != null) {
			for (Object o : functionArray) {
				JSONObject jo = (JSONObject) o;
				int pageId = jo.getIntValue("pageId");
				JSONArray functionInfo = jo.getJSONArray("functionInfo");
				for (Object o2 : functionInfo) {
					JSONObject jo2 = (JSONObject) o2;
					String fid = jo2.getString("fid");
					int functionId = jo2.getIntValue("functionId");
					String functionName = jo2.getString("functionName");
					luf.add(new UserFunction(userId, userName, pageId, functionId, fid, functionName));
				}
			}
		}
		if (!luf.isEmpty()) {
			sysDao.addUserFunction(luf);
		}
		sysDao.addTaskSaveStatus(userId);
		return true;
	}

	public PageInfo<UserInfo> getUserList(String userName, String roleId, String userGroupId, int currentPage) {

		PageHelper.startPage(currentPage, Constants.pageSize);
		List<UserInfo> list = sysDao.getUserList(userName, roleId, userGroupId);
		PageInfo<UserInfo> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}

	public JSONObject getUserInfo(int id) {

		JSONObject resObject = new JSONObject();
		UserInfo uf = sysDao.getUserInfo(id);
		List<Page> list = uf.getLp();
		// System.out.println(JSON.toJSONString(uf));
		// System.out.println(JSON.toJSONString(list));
		resObject.put("id", uf.getId());
		resObject.put("userName", uf.getUserName());
		resObject.put("roleId", uf.getRoleId());
		resObject.put("userGroupId", uf.getUserGroupId());
		resObject.put("roleName", uf.getRoleName() == null ? "" : uf.getRoleName());
		resObject.put("userGroupName", uf.getUserGroupName() == null ? "" : uf.getUserGroupName());
		resObject.put("pageInfo", new JSONArray());
		if (!list.isEmpty()) {
			JSONArray ja = resObject.getJSONArray("pageInfo");
			for (Page page : list) {
				List<PageFunction> lpf = page.getLpf();
				JSONObject jo = new JSONObject();
				jo.put("pageId", page.getId());
				jo.put("functionInfo", new JSONArray());
				if (!lpf.isEmpty()) {
					JSONArray ja2 = jo.getJSONArray("functionInfo");
					for (PageFunction pf : lpf) {
						JSONObject jo2 = new JSONObject();
						jo2.put("functionId", pf.getId());
						jo2.put("fid", pf.getFid());
						jo2.put("functionName", pf.getFunctionName());
						ja2.add(jo2);
					}
				}
				ja.add(jo);
			}
		}
		// resObject.put("roleName", value)
		return resObject;
	}

	public boolean updateUserInfo(int userId, String userName, String roleId, String roleName, String userGroupId,
			String userGroupName, JSONArray functionArray) {

		sysDao.updateUserInfo(userId, userName, roleId, roleName, userGroupId, userGroupName);
		sysDao.delUserFunction(userId);
		List<UserFunction> luf = new ArrayList<UserFunction>();
		if (!functionArray.isEmpty()) {
			for (Object o : functionArray) {
				JSONObject jo = (JSONObject) o;
				int pageId = jo.getIntValue("pageId");
				JSONArray functionInfo = jo.getJSONArray("functionInfo");
				for (Object o2 : functionInfo) {
					JSONObject jo2 = (JSONObject) o2;
					String fid = jo2.getString("fid");
					int functionId = jo2.getIntValue("functionId");
					String functionName = jo2.getString("functionName");
					luf.add(new UserFunction(userId, userName, pageId, functionId, fid, functionName));
				}
			}
		}
		if (!luf.isEmpty()) {
			sysDao.addUserFunction(luf);
		}
		return true;
	}

	public int delUser(int userId) {

		return sysDao.delUser(userId);
	}
}
