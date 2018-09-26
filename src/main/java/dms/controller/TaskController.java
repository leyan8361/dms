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
	@RequestMapping(value = "taskSaveId", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
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
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd HH:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "发布了条新任务"));
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
}
