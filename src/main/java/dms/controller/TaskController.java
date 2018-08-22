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
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import dms.entity.UserInfo;
import dms.service.SysService;
import dms.service.TaskService;
import dms.utils.Constants;

@CrossOrigin
@RestController
@RequestMapping("task")
public class TaskController {

	@Resource(name = "taskService")
	private TaskService taskService;

	@Resource(name = "sysService")
	private SysService sysService;

	/**
	 * ��������ʱ��ȡԱ���б�
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "getUserListBeforeAddTask", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getUserListBeforeAddTask() {

		Map<String, String> resMap = new HashMap<String, String>();
		List<UserInfo> lui = taskService.getUserListBeforeAddTask();
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(lui));
		return JSON.toJSONString(resMap);
	}

	/**
	 * �����û���������������Ϣ
	 * 
	 * @param content
	 *            ��������
	 * @param deadLine
	 *            ��ֹʱ��(yyyy-MM-dd hh:mm)
	 * @param attention
	 *            ע������
	 * @param remark
	 *            ��ע
	 * @param oriAttach
	 *            �Ѵ��ڵĸ����������ַ��� (�ö��ŷָ�,��:aaaaaaa,xxxxxxxxx)
	 * @param attachArr
	 *            �ϴ��ĸ�������
	 * @param userInfo
	 *            �������Ա�� ��:[{"userId":"","userName":""},{..},..]
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "saveTaskInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String saveTaskInfo(@RequestParam("content") String content, @RequestParam("deadLine") String deadLine,
			@RequestParam("attention") String attention, @RequestParam("remark") String remark,
			@RequestParam("oriAttach") String oriAttach, @RequestParam("attachArr") MultipartFile[] attachArr,
			String userInfo, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		JSONObject jo = (JSONObject) req.getAttribute("user");
		boolean res = taskService.saveTaskInfo(Integer.valueOf(jo.getString("userId")), content, deadLine, attention,
				remark, oriAttach, attachArr, JSONArray.parseArray(userInfo));
		if (res == true) {
			resMap.put("status", Constants.successStatus);
			resMap.put("info", "��ӳɹ�");
		} else {
			resMap.put("status", Constants.apiErrorStatus);
			resMap.put("info", "���ʧ��");
		}
		return JSON.toJSONString(resMap);
	}

	/**
	 * ����û��Ƿ�����ѱ����������Ϣ
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "checkTaskSave", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String checkTaskSave(HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		JSONObject jo = (JSONObject) req.getAttribute("user");
		int res = taskService.checkTaskSave(Integer.valueOf(jo.getString("userId")));
		resMap.put("status", Constants.successStatus);
		if (res != 0) {
			resMap.put("info", "success");
			resMap.put("taskSavedId", String.valueOf(res));
		} else {
			resMap.put("info", "error");
		}
		return JSON.toJSONString(resMap);
	}
}
