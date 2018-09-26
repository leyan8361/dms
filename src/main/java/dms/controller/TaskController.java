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
import com.github.pagehelper.PageInfo;

import dms.entity.Log;
import dms.entity.Task;
import dms.entity.TaskSave;
import dms.entity.UserInfo;
import dms.service.SysService;
import dms.service.TaskService;
import dms.utils.Constants;
import dms.utils.Utils;

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
	 *            ��ֹʱ��(yyyy-MM-dd HH:mm)
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
		String res = taskService.checkTaskSave(Integer.valueOf(jo.getString("userId")));
		resMap.put("status", Constants.successStatus);
		if (res != null && !"0".equals(res)) {
			resMap.put("info", "success");
			resMap.put("taskSaveId", res);
		} else {
			resMap.put("info", "error");
		}
		return JSON.toJSONString(resMap);
	}

	/**
	 * ��ȡ֮ǰ��������񷢲���Ϣ
	 * 
	 * @param taskSaveId
	 *            �������Ϣ��Id
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "taskSaveId", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getSaveTaskInfo(@RequestParam("taskSaveId") int taskSaveId) {

		Map<String, String> resMap = new HashMap<String, String>();
		TaskSave ts = taskService.getSaveTaskInfo(taskSaveId);
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(ts));
		return JSON.toJSONString(resMap);
	}

	/**
	 * ��������
	 * 
	 * @param content
	 *            ��������
	 * @param deadLine
	 *            ��ֹʱ��(yyyy-MM-dd HH:mm)
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
	@RequestMapping(value = "addTaskInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String addTaskInfo(@RequestParam("content") String content, @RequestParam("deadLine") String deadLine,
			@RequestParam("attention") String attention, @RequestParam("remark") String remark,
			@RequestParam("oriAttach") String oriAttach, @RequestParam("attachArr") MultipartFile[] attachArr,
			@RequestParam("userInfo") String userInfo, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		JSONObject jo = (JSONObject) req.getAttribute("user");
		boolean res = taskService.addTaskInfo(content, deadLine, attention, remark,
				Integer.valueOf(jo.getString("userId")), oriAttach, attachArr, JSONArray.parseArray(userInfo));
		if (res == true) {
			resMap.put("status", Constants.successStatus);
			resMap.put("info", "�����ɹ�");
		} else {
			resMap.put("status", Constants.apiErrorStatus);
			resMap.put("info", "����ʧ��");
		}
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd HH:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "��������������"));
		return JSON.toJSONString(resMap);
	}

	/**
	 * ��ȡ��ǰ�û���δ��ɵ������б�(����״̬���Լ�����)
	 * 
	 * @param currentPage
	 *            ��ǰҳ��(��СΪ1)
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "getMyUnDoTaskList", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getMyUnDoTaskList(@RequestParam("currentPage") int currentPage, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		JSONObject jo = (JSONObject) req.getAttribute("user");
		PageInfo<Map<String, String>> pageInfo = taskService.getMyUnDoTaskList(Integer.valueOf(jo.getString("userId")),
				currentPage);
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(pageInfo.getList()));
		resMap.put("totalNum", String.valueOf(pageInfo.getTotal()));
		return JSON.toJSONString(resMap);
	}

	/**
	 * ��ȡ�������ϸ��Ϣ
	 * 
	 * @param taskId
	 *            ����Id
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "getTaskInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getTaskInfo(@RequestParam("taskId") int taskId) {
		// TODO �������������
		Map<String, String> resMap = new HashMap<String, String>();
		Task task = taskService.getTaskInfo(taskId);
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(task));
		return JSON.toJSONString(resMap);
	}
}
