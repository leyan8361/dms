package dms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;

import dms.entity.Log;
import dms.entity.Role;
import dms.entity.UserGroup;
import dms.service.SysService;
import dms.utils.Constants;
import dms.utils.Utils;

@CrossOrigin
@RestController
@RequestMapping("sys")
public class SysController {

	@Resource(name = "sysService")
	private SysService sysService;

	/**
	 * 校验角色名是否重复
	 * 
	 * @param name
	 *            角色名
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "checkRoleName", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String checkRoleName(@RequestParam("name") String name) {

		Map<String, String> resMap = new HashMap<String, String>();
		Role role = sysService.checkRoleName(name);
		resMap.put("status", "0");
		if (role == null) {
			resMap.put("info", "yes");
		} else {
			resMap.put("info", "no");
		}
		return JSON.toJSONString(resMap);
	}

	/**
	 * 新增角色
	 * 
	 * @param name
	 *            角色名
	 * @param description
	 *            角色描述
	 * @param userId
	 *            创建人Id
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "addRole", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String addRole(@RequestParam("name") String name, @RequestParam("description") String description,
			@RequestParam("userId") int userId, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		sysService.addRole(new Role(name, description, userId, Utils.getNowDate("yyyy-MM-dd")));
		resMap.put("status", Constants.successStatus);
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "创建角色:" + name));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 查询角色列表
	 * 
	 * @param currentPage
	 *            当前页码(从1开始)
	 * @param name
	 *            (角色名)
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "getRoleList", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getRoleList(@RequestParam("currentPage") int currentPage, @RequestParam("name") String name) {

		Map<String, String> resMap = new HashMap<String, String>();
		PageInfo<Role> pageInfo = sysService.getRoleList(currentPage, name);
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(pageInfo.getList()));
		resMap.put("totalNum", String.valueOf(pageInfo.getTotal()));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 编辑角色信息
	 * 
	 * @param id
	 *            角色Id
	 * @param name
	 *            角色名
	 * @param description
	 *            角色描述
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "updateRoleInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String updateRoleInfo(@RequestParam("id") int id, @RequestParam("name") String name,
			@RequestParam("description") String description, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		sysService.updateRoleInfo(id, name, description);
		resMap.put("status", Constants.successStatus);
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "编辑角色:" + name));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 删除角色信息
	 * 
	 * @param id
	 *            角色Id
	 * @param name
	 *            角色名
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "delRoleInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String delRoleInfo(@RequestParam("id") int id, @RequestParam("name") String name, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		sysService.delRoleInfo(id);
		resMap.put("status", Constants.successStatus);
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "删除角色:" + name));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 校验群组名是否重复
	 * 
	 * @param name
	 *            群组名
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "checkUserGroupName", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String checkUserGroupName(@RequestParam("name") String name) {

		Map<String, String> resMap = new HashMap<String, String>();
		UserGroup ug = sysService.checkUserGroupName(name);
		resMap.put("status", Constants.successStatus);
		if (ug == null) {
			resMap.put("info", "yes");
		} else {
			resMap.put("info", "no");
		}
		return JSON.toJSONString(resMap);
	}

	/**
	 * 新增用户群组信息
	 * 
	 * @param name
	 *            群组名
	 * @param description
	 *            群组描述
	 * @param userId
	 *            创建人Id
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "addUserGroup", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String addUserGroup(@RequestParam("name") String name, @RequestParam("description") String description,
			@RequestParam("userId") int userId, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		sysService.addUserGroup(new UserGroup(name, description, userId, Utils.getNowDate("yyyy-MM-dd")));
		resMap.put("status", Constants.successStatus);
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "创建用户群组:" + name));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 查询用户群组列表
	 * 
	 * @param currentPage
	 *            当前页码(从1开始)
	 * @param name
	 *            群组名
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "getUserGroupList", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getUserGroupList(@RequestParam("currentPage") int currentPage, @RequestParam("name") String name) {

		Map<String, String> resMap = new HashMap<String, String>();
		PageInfo<UserGroup> pageInfo = sysService.getUserGroupList(currentPage, name);
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(pageInfo.getList()));
		resMap.put("totalNum", String.valueOf(pageInfo.getTotal()));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 编辑用户群组信息
	 * 
	 * @param id
	 *            群组Id
	 * @param name
	 *            群组名
	 * @param description
	 *            描述
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "updateUserGroupInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String updateUserGroupInfo(@RequestParam("id") int id, @RequestParam("name") String name,
			@RequestParam("description") String description, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		sysService.updateUserGroupInfo(id, name, description);
		resMap.put("status", Constants.successStatus);
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "编辑用户群组:" + name));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 删除用户群组信息
	 * 
	 * @param id
	 *            用户群组Id
	 * @param name
	 *            群组名
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "delUserGroupInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String delUserGroupInfo(@RequestParam("id") int id, @RequestParam("name") String name,
			HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		sysService.delUserGroupInfo(id);
		resMap.put("status", Constants.successStatus);
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "删除用户群组:" + name));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 获取角色下拉列表
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "getAllRoleInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getAllRoleInfo() {

		Map<String, String> resMap = new HashMap<String, String>();
		List<Role> list = sysService.getAllRoleInfo();
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(list));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 获取用户群组下拉列表
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "getAllUserGroupInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getAllUserGroupInfo() {

		Map<String, String> resMap = new HashMap<String, String>();
		List<UserGroup> list = sysService.getAllUserGroupInfo();
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(list));
		return JSON.toJSONString(resMap);
	}
}
