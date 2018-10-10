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
import dms.entity.TaskTransferSave;
import dms.entity.TaskTransferSaveStatus;
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
	 * 发布任务时获取员工列表
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
	 * 保存用户待发布的任务信息
	 * 
	 * @param content
	 *            任务内容
	 * @param deadLine
	 *            截止时间(yyyy-MM-dd HH:mm)
	 * @param attention
	 *            注意事项
	 * @param remark
	 *            备注
	 * @param oriAttach
	 *            已存在的附件的名称字符串 (用逗号分隔,如:aaaaaaa,xxxxxxxxx)
	 * @param attachArr
	 *            上传的附件数组
	 * @param userInfo
	 *            任务参与员工 如:[{"userId":"","userName":""},{..},..]
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
			resMap.put("info", "添加成功");
		} else {
			resMap.put("status", Constants.apiErrorStatus);
			resMap.put("info", "添加失败");
		}
		return JSON.toJSONString(resMap);
	}

	/**
	 * 检查用户是否存在已保存的任务信息
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
	 * 获取之前保存的任务发布信息
	 * 
	 * @param taskSaveId
	 *            保存的信息的Id
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "getSaveTaskInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getSaveTaskInfo(@RequestParam("taskSaveId") int taskSaveId) {

		Map<String, String> resMap = new HashMap<String, String>();
		TaskSave ts = taskService.getSaveTaskInfo(taskSaveId);
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(ts));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 发布任务
	 * 
	 * @param content
	 *            任务内容
	 * @param deadLine
	 *            截止时间(yyyy-MM-dd HH:mm)
	 * @param attention
	 *            注意事项
	 * @param remark
	 *            备注
	 * @param oriAttach
	 *            已存在的附件的名称字符串 (用逗号分隔,如:aaaaaaa,xxxxxxxxx)
	 * @param attachArr
	 *            上传的附件数组
	 * @param userInfo
	 *            任务参与员工 如:[{"userId":"","userName":""},{..},..]
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
			resMap.put("info", "发布成功");
		} else {
			resMap.put("status", Constants.apiErrorStatus);
			resMap.put("info", "发布失败");
		}
		sysService
				.addLog(new Log(Utils.getNowDate("yyyy-MM-dd HH:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
						String.valueOf(jo.get("userName")), "发布了条新任务:" + content));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 获取当前用户尚未完成的任务列表(任务状态在自己身上)
	 * 
	 * @param currentPage
	 *            当前页码(最小为1)
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
	 * 获取任务的详细信息
	 * 
	 * @param taskId
	 *            任务Id
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "getTaskInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getTaskInfo(@RequestParam("taskId") int taskId) {
		// TODO 需增加任务进度
		Map<String, String> resMap = new HashMap<String, String>();
		Task task = taskService.getTaskInfo(taskId);
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(task));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 判断任务是否有被移交
	 * 
	 * @param taskId
	 *            任务Id
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "judgeIfTaskTransfer", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String judgeIfTaskTransfer(@RequestParam("taskId") int taskId, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		JSONObject jo = (JSONObject) req.getAttribute("user");
		String isTransfer = taskService.judgeIfTaskTransfer(taskId, jo.getIntValue("userId"));
		resMap.put("status", Constants.successStatus);
		resMap.put("info", isTransfer);
		return JSON.toJSONString(resMap);
	}

	/**
	 * 检查是否有保存的任务移交信息
	 * 
	 * @param taskId
	 *            任务Id
	 * @return
	 */
	@RequestMapping(value = "checkIfTaskTransferSave", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String checkIfTaskTransferSave(@RequestParam("taskId") int taskId, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		JSONObject userInfo = (JSONObject) req.getAttribute("user");
		TaskTransferSaveStatus ttss = taskService.checkIfTaskTransferSave(taskId, userInfo.getIntValue("userId"));
		if (ttss == null) {
			resMap.put("status", Constants.apiErrorStatus);
			resMap.put("info", "没有保存信息");
		} else {
			resMap.put("status", Constants.successStatus);
			resMap.put("info", String.valueOf(ttss.getTransferSaveId()));
		}
		return JSON.toJSONString(resMap);
	}

	/**
	 * 保存任务的移交信息
	 * 
	 * @param taskId
	 *            任务Id
	 * @param content
	 *            内容
	 * @param deadLine
	 *            截止日期 yyyy-MM-dd HH:mm
	 * @param attention
	 *            注意事项
	 * @param remark
	 *            备注
	 * @param oriAttachStr
	 *            原先的附件名称字符串 "aaaa,bbbb"
	 * @param attachArr
	 *            附件数组
	 * @param userInfo
	 *            用户信息 [{"userId":"","userName":""},{...},...]
	 * @return
	 */
	@RequestMapping(value = "addTaskTransferSaveInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String addTaskTransferSaveInfo(@RequestParam("taskId") int taskId, @RequestParam("content") String content,
			@RequestParam("deadLine") String deadLine, @RequestParam("attention") String attention,
			@RequestParam("remark") String remark, @RequestParam("oriAttachStr") String oriAttachStr,
			@RequestParam("attachArr") MultipartFile[] attachArr, @RequestParam("userInfo") String userInfo,
			HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		JSONObject jo = (JSONObject) req.getAttribute("user");
		boolean res = taskService.addTaskTransferSaveInfo(jo.getIntValue("userId"), taskId, content, deadLine,
				attention, remark, oriAttachStr, attachArr, JSON.parseArray(userInfo));
		if (res == true) {
			resMap.put("status", Constants.successStatus);
			resMap.put("info", "保存成功");
		} else {
			resMap.put("status", Constants.apiErrorStatus);
			resMap.put("info", "保存失败");
		}
		return JSON.toJSONString(resMap);
	}

	/**
	 * 获取存储的任务移交信息
	 * 
	 * @param transferSaveId
	 *            任务移交Id
	 * @return
	 */
	@RequestMapping(value = "getTaskTransferSaveInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getTaskTransferSaveInfo(@RequestParam("transferSaveId") int transferSaveId, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		TaskTransferSave tts = taskService.getTaskTransferSaveInfo(transferSaveId);
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(tts));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 移交任务
	 * 
	 * @param taskId
	 *            任务Id
	 * @param content
	 *            内容
	 * @param deadLine
	 *            截止日期 yyyy-MM-dd HH:mm
	 * @param attention
	 *            注意事项
	 * @param remark
	 *            备注
	 * @param oriAttachStr
	 *            原先的附件名称字符串 "aaaa,bbbb"
	 * @param attachArr
	 *            附件数组
	 * @param userInfo
	 *            用户信息 [{"userId":"","userName":""},{...},...]
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "transferTask", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String transferTask(@RequestParam("taskId") int taskId, @RequestParam("content") String content,
			@RequestParam("deadLine") String deadLine, @RequestParam("attention") String attention,
			@RequestParam("remark") String remark, @RequestParam("oriAttachStr") String oriAttachStr,
			@RequestParam("attachArr") MultipartFile[] attachArr, @RequestParam("userInfo") String userInfo,
			HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		JSONObject jo = (JSONObject) req.getAttribute("user");
		taskService.addtransferTask(jo.getIntValue("userId"), taskId, content, deadLine, attention, remark,
				oriAttachStr, attachArr, JSONArray.parseArray(userInfo));
		resMap.put("status", Constants.successStatus);
		resMap.put("info", "移交成功");
		return JSON.toJSONString(resMap);
	}

	/**
	 * 获取移交的信息
	 * 
	 * @param taskId
	 *            任务Id
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "getTransferInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getTransferInfo(@RequestParam("taskId") int taskId, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		JSONObject jo = (JSONObject) req.getAttribute("user");
		Task task = taskService.getTransferInfo(taskId, jo.getIntValue("userId"));
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(task));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 取消任务移交
	 * 
	 * @param taskId
	 *            任务Id
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "cancelTransfer", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String cancelTransfer(@RequestParam("taskId") int taskId, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		JSONObject jo = (JSONObject) req.getAttribute("user");
		taskService.cancelTransfer(taskId, jo.getIntValue("userId"));
		resMap.put("status", Constants.successStatus);
		resMap.put("info", "移除数据");
		return JSON.toJSONString(resMap);
	}
}
