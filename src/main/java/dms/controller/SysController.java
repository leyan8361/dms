package dms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;

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
	 * У���ɫ���Ƿ��ظ�
	 * 
	 * @param name
	 *            ��ɫ��
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
	 * ������ɫ
	 * 
	 * @param name
	 *            ��ɫ��
	 * @param description
	 *            ��ɫ����
	 * @param userId
	 *            ������Id
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "addRole", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String addRole(@RequestParam("name") String name, @RequestParam("description") String description,
			@RequestParam("userId") int userId) {

		Map<String, String> resMap = new HashMap<String, String>();
		sysService.addRole(new Role(name, description, userId, Utils.getNowDate("yyyy-MM-dd")));
		resMap.put("status", Constants.successStatus);
		return JSON.toJSONString(resMap);
	}

	/**
	 * ��ѯ��ɫ�б�
	 * 
	 * @param currentPage
	 *            ��ǰҳ��(��1��ʼ)
	 * @param name
	 *            (��ɫ��)
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
	 * �༭��ɫ��Ϣ
	 * 
	 * @param id
	 *            ��ɫId
	 * @param name
	 *            ��ɫ��
	 * @param description
	 *            ��ɫ����
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "updateRoleInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String updateRoleInfo(@RequestParam("id") int id, @RequestParam("name") String name,
			@RequestParam("description") String description) {

		Map<String, String> resMap = new HashMap<String, String>();
		sysService.updateRoleInfo(id, name, description);
		resMap.put("status", Constants.successStatus);
		return JSON.toJSONString(resMap);
	}

	/**
	 * ɾ����ɫ��Ϣ
	 * 
	 * @param id
	 *            ��ɫId
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "delRoleInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String delRoleInfo(@RequestParam("id") int id) {

		Map<String, String> resMap = new HashMap<String, String>();
		sysService.delRoleInfo(id);
		resMap.put("status", Constants.successStatus);
		return JSON.toJSONString(resMap);
	}

	/**
	 * У��Ⱥ�����Ƿ��ظ�
	 * 
	 * @param name
	 *            Ⱥ����
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
	 * �����û�Ⱥ����Ϣ
	 * 
	 * @param name
	 *            Ⱥ����
	 * @param description
	 *            Ⱥ������
	 * @param userId
	 *            ������Id
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "addUserGroup", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String addUserGroup(@RequestParam("name") String name, @RequestParam("description") String description,
			@RequestParam("userId") int userId) {

		Map<String, String> resMap = new HashMap<String, String>();
		sysService.addUserGroup(new UserGroup(name, description, userId, Utils.getNowDate("yyyy-MM-dd")));
		resMap.put("status", Constants.successStatus);
		return JSON.toJSONString(resMap);
	}

	/**
	 * ��ѯ�û�Ⱥ���б�
	 * 
	 * @param currentPage
	 *            ��ǰҳ��(��1��ʼ)
	 * @param name
	 *            Ⱥ����
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
	 * �༭�û�Ⱥ����Ϣ
	 * 
	 * @param id
	 *            Ⱥ��Id
	 * @param name
	 *            Ⱥ����
	 * @param description
	 *            ����
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "updateUserGroupInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String updateUserGroupInfo(@RequestParam("id") int id, @RequestParam("name") String name,
			@RequestParam("description") String description) {

		Map<String, String> resMap = new HashMap<String, String>();
		sysService.updateUserGroupInfo(id, name, description);
		resMap.put("status", Constants.successStatus);
		return JSON.toJSONString(resMap);
	}

	/**
	 * ɾ���û�Ⱥ����Ϣ
	 * 
	 * @param id
	 *            �û�Ⱥ��Id
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "delUserGroupInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String delUserGroupInfo(@RequestParam("id") int id) {

		Map<String, String> resMap = new HashMap<String, String>();
		sysService.delUserGroupInfo(id);
		resMap.put("status", Constants.successStatus);
		return JSON.toJSONString(resMap);
	}

	/**
	 * ��ȡ��ɫ�����б�
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
	 * ��ȡ�û�Ⱥ�������б�
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