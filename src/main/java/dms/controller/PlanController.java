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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;

import dms.entity.AccidentReport;
import dms.entity.Info;
import dms.entity.InfoColumn;
import dms.entity.Line;
import dms.entity.LineStation;
import dms.entity.Log;
import dms.entity.Plan;
import dms.entity.Process;
import dms.entity.ProcessColumn;
import dms.service.PlanService;
import dms.service.SysService;
import dms.utils.Constants;
import dms.utils.Utils;

@CrossOrigin
@RestController
@RequestMapping("plan")
public class PlanController {

	@Resource(name = "planService")
	private PlanService planService;

	@Resource(name = "sysService")
	private SysService sysService;

	/**
	 * 获取线路下拉列表
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "getLineList", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getLineList() {

		Map<String, String> resMap = new HashMap<String, String>();
		List<Line> list = planService.getLineList();
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(list));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 获取线路下车站列表
	 * 
	 * @param lineId
	 *            线路Id
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "getLineStation", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getLineStation(@RequestParam("lineId") int lineId) {

		Map<String, String> resMap = new HashMap<String, String>();
		List<LineStation> list = planService.getLineStationList(lineId);
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(list));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 校验预案名称是否重复
	 * 
	 * @param name
	 *            预案名
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "checkPlanName", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String checkPlanName(@RequestParam("name") String name) {

		Map<String, String> resMap = new HashMap<String, String>();
		Plan plan = planService.checkPlanName(name);
		resMap.put("status", Constants.successStatus);
		if (plan == null) {
			resMap.put("info", "yes");
		} else {
			resMap.put("info", "no");
		}
		return JSON.toJSONString(resMap);
	}

	/**
	 * 新增预案
	 * 
	 * @param userId
	 *            当前用户Id
	 * @param name
	 *            预案名
	 * @param lineId
	 *            线路Id
	 * @param lineNo
	 *            线路名
	 * @param stationId
	 *            车站Id
	 * @param stationName
	 *            车站名
	 * @param attachArr
	 *            附件数组 (不可为空,必须传附件)
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "addPlan", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String addPlan(@RequestParam("userId") int userId, @RequestParam("name") String name,
			@RequestParam("lineId") int lineId, @RequestParam("lineNo") int lineNo,
			@RequestParam("stationId") int stationId, @RequestParam("stationName") String stationName,
			@RequestParam("attachArr") MultipartFile[] attachArr, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		boolean res = planService.addPlan(new Plan(name, lineId, lineNo, stationId, stationName), attachArr);
		if (res == true) {
			resMap.put("status", Constants.successStatus);
			resMap.put("info", "添加成功");
		} else {
			resMap.put("status", Constants.apiErrorStatus);
			resMap.put("info", "添加失败");
		}
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "创建预案:" + name));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 查询预案列表
	 * 
	 * @param currentPage
	 *            当前页码(从1开始)
	 * @param lineId
	 *            线路Id
	 * @param stationId
	 *            车站Id
	 * @param name
	 *            预案名称
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "getPLanList", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getPLanList(@RequestParam("currentPage") int currentPage, @RequestParam("lineId") String lineId,
			@RequestParam("stationId") String stationId, @RequestParam("name") String name, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		PageInfo<Plan> pageInfo = planService.getPlanList(currentPage, "".equals(lineId) ? 0 : Integer.valueOf(lineId),
				"".equals(stationId) ? 0 : Integer.valueOf(stationId), name);
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(pageInfo.getList()));
		resMap.put("totalNum", String.valueOf(pageInfo.getTotal()));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 获取预案详情
	 * 
	 * @param id
	 *            预案Id
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "getPlanInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getPlanInfo(@RequestParam("id") int id, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		Plan plan = planService.getPlanInfo(id);
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(plan));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 编辑预案信息
	 * 
	 * @param id
	 *            预案Id
	 * @param name
	 *            预案名
	 * @param lineId
	 *            线路Id
	 * @param lineNo
	 *            线路号
	 * @param stationId
	 *            车站Id
	 * @param stationName
	 *            车站名
	 * @param delStr
	 *            删除的附件id信息 如"1,2,3,4,5"
	 * @param addAttach
	 *            新增的附件
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "updatePlanInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String updatePlanInfo(@RequestParam("id") int id, @RequestParam("name") String name,
			@RequestParam("lineId") int lineId, @RequestParam("lineNo") int lineNo,
			@RequestParam("stationId") int stationId, @RequestParam("stationName") String stationName,
			@RequestParam("delStr") String delStr, @RequestParam("addAttach") MultipartFile[] addAttach,
			HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		int[] delArr = {};
		if (!"".equals(delStr)) {
			delArr = Utils.transStrToIntArr(delStr);
		}
		boolean res = planService.updatePlanInfo(id, name, lineId, lineNo, stationId, stationName, delArr, addAttach);
		resMap.put("status", Constants.successStatus);
		if (res == true) {
			resMap.put("info", "编辑成功");
		} else {
			resMap.put("info", "编辑失败");
		}
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "编辑预案:" + name));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 删除预案
	 * 
	 * @param id
	 *            预案Id
	 * @param name
	 *            预案名称
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "delPlan", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String delPlan(@RequestParam("id") int id, @RequestParam("name") String name, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		planService.delPlan(id);
		resMap.put("status", Constants.successStatus);
		resMap.put("info", "删除成功");
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "删除预案:" + name));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 新增流程库表
	 * 
	 * @param name
	 *            表名
	 * @param columnArr
	 *            列信息 [{"name":"","type":""},{...},...] (不可为空!!)
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "addProcess", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String addProcess(@RequestParam("name") String name, @RequestParam("columnArr") String columnArr,
			HttpServletRequest req) {

		JSONObject jo = (JSONObject) req.getAttribute("user");
		Map<String, String> resMap = new HashMap<String, String>();
		planService.addProcess(name, Integer.valueOf(String.valueOf(jo.get("userId"))),
				JSONArray.parseArray(columnArr));
		resMap.put("status", Constants.successStatus);
		resMap.put("info", "新增成功");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "新增流程库表:" + name));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 查询流程库表列表
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "getProcessList", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getProcessList() {

		Map<String, String> resMap = new HashMap<String, String>();
		List<Process> list = planService.getProcessList();
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(list));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 获取流程库表详情
	 * 
	 * @param id
	 *            流程库表Id
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "getProcessInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getProcessInfo(@RequestParam("id") int id) {

		Map<String, String> resMap = new HashMap<String, String>();
		Process process = planService.getProcessInfo(id);
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(process));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 修改流程库表信息
	 * 
	 * @param id
	 *            流程库表Id
	 * @param name
	 *            流程库表名
	 * @param updateArr
	 *            需修改的列信息 [{"id":"","name":""},{...},..] id:列Id,name:列名
	 * @param addArr
	 *            新增的列信息 [{"name":"","type":""},{..},..] name:列名,type:列类型
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "updateProcessInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String updateProcessInfo(@RequestParam("id") int id, @RequestParam("name") String name,
			@RequestParam("updateArr") String updateArr, @RequestParam("addArr") String addArr,
			HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		planService.updateProcessInfo(id, name, JSONArray.parseArray(updateArr), JSONArray.parseArray(addArr));
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "编辑流程库表:" + name));
		resMap.put("status", Constants.successStatus);
		resMap.put("info", "编辑成功");
		return JSON.toJSONString(resMap);
	}

	/**
	 * 删除流程库表信息
	 * 
	 * @param id
	 *            流程库表Id
	 * @param name
	 *            流程库表名
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "delProcess", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String delProcess(@RequestParam("id") int id, @RequestParam("name") String name, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		planService.delProcess(id);
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "删除流程库表:" + name));
		resMap.put("status", Constants.successStatus);
		resMap.put("info", "删除成功");
		return JSON.toJSONString(resMap);
	}

	/**
	 * 获取预案的.pdf附件信息
	 * 
	 * @param id
	 *            预案Id
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "getPlanAttach", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getPlanAttach(@RequestParam("id") int id) {

		Map<String, String> resMap = new HashMap<String, String>();
		JSONArray ja = planService.getPlanAttach(id);
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(ja));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 加载页面时获取流程库表的列名信息
	 * 
	 * @param id
	 *            流程库表Id
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "getProcessColumnNameInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getProcessColumnNameInfo(@RequestParam("id") int id) {

		Map<String, String> resMap = new HashMap<String, String>();
		List<ProcessColumn> list = planService.getProcessColumnNameInfo(id);
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(list));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 添加流程库表的内容
	 * 
	 * @param processId
	 *            流程库表Id
	 * @param processName
	 *            流程库表名
	 * @param contentStr
	 *            字符串型的内容 {"col_1":"","col_3":"",..} 格式:col_加上该列的Id
	 * @param 上传的附件内容
	 *            格式: 文件名 col_+列Id 如col_X
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "addProcessContent", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String addProcessContent(@RequestParam("processId") int processId,
			@RequestParam("processName") String processName, @RequestParam("contentStr") String contentStr,
			MultipartHttpServletRequest multiReq, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		boolean res = planService.addProcessContent(processId, processName, JSONObject.parseObject(contentStr),
				multiReq.getFileMap());
		if (res == true) {
			resMap.put("status", Constants.successStatus);
			resMap.put("info", "添加成功");
		} else {
			resMap.put("status", Constants.apiErrorStatus);
			resMap.put("info", "添加失败");
		}
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "添加流程库表:" + processName + "内容"));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 修改流程库表的内容
	 * 
	 * @param processId
	 *            流程库表Id
	 * @param processName
	 *            流程库表名
	 * @param flag
	 *            行标识
	 * @param contentStr
	 *            字符串型的内容 {"col_1":"","col_3":"",..} 格式:col_加上该列的Id
	 * @param 上传的附件内容
	 *            格式: 文件名 col_+列Id 如col_X
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "updateProcessContent", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String updateProcessContent(@RequestParam("processId") int processId,
			@RequestParam("processName") String processName, @RequestParam("flag") String flag,
			@RequestParam("contentStr") String contentStr, MultipartHttpServletRequest multiReq,
			HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		boolean res = planService.updateProcessContent(processId, flag, JSONObject.parseObject(contentStr),
				multiReq.getFileMap());
		if (res == true) {
			resMap.put("status", Constants.successStatus);
			resMap.put("info", "修改成功");
		} else {
			resMap.put("status", Constants.apiErrorStatus);
			resMap.put("info", "修改失败");
		}
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "修改流程库表:" + processName + "内容"));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 删除流程库表的内容
	 * 
	 * @param processId
	 *            流程库表Id
	 * @param processName
	 *            流程库表名
	 * @param flag
	 *            行标识
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "delProcessContent", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String delProcessContent(@RequestParam("processId") int processId,
			@RequestParam("processName") String processName, @RequestParam("flag") String flag,
			HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		resMap.put("status", Constants.successStatus);
		planService.delProcessContent(processId, flag);
		resMap.put("info", "删除成功");
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "删除流程库表:" + processName + "内容"));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 查询流程库表内容
	 * 
	 * @param processId
	 *            流程库表Id
	 * @param columnId
	 *            列Id
	 * @param content
	 *            列的具体内容
	 * @param token
	 *            // 此接口需要前台进行分页
	 * @return
	 */
	@RequestMapping(value = "getProcessContent", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getProcessContent(@RequestParam("processId") int processId, @RequestParam("columnId") String columnId,
			@RequestParam("content") String content, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		JSONObject jo = planService.getProcessContent(processId, columnId, content);
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(jo));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 新增资料库
	 * 
	 * @param name
	 *            表名
	 * @param columnArr
	 *            列信息 [{"name":"","type":""},{...},...] (不可为空!!)
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "addInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String addInfo(@RequestParam("name") String name, @RequestParam("columnArr") String columnArr,
			HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		JSONObject jo = (JSONObject) req.getAttribute("user");
		planService.addInfo(name, Integer.valueOf(jo.getString("userId")), JSONArray.parseArray(columnArr));
		resMap.put("status", Constants.successStatus);
		resMap.put("info", "新增成功");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "新增资料库:" + name));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 查询资料库列表
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "getInfoList", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getInfoList() {

		Map<String, String> resMap = new HashMap<String, String>();
		List<Info> list = planService.getInfoList();
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(list));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 获取资料库详情
	 * 
	 * @param id
	 *            资料库Id
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "getInfoInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getInfoInfo(@RequestParam("id") int id) {

		Map<String, String> resMap = new HashMap<String, String>();
		Info info = planService.getInfoInfo(id);
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(info));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 修改资料库信息
	 * 
	 * @param id
	 *            资料库Id
	 * @param name
	 *            资料库名
	 * @param updateArr
	 *            需修改的列信息 [{"id":"","name":""},{...},..] id:列Id,name:列名
	 * @param addArr
	 *            新增的列信息 [{"name":"","type":""},{..},..] name:列名,type:列类型
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "updateInfoInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String updateInfoInfo(@RequestParam("id") int id, @RequestParam("name") String name,
			@RequestParam("updateArr") String updateArr, @RequestParam("addArr") String addArr,
			HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		planService.updateInfoInfo(id, name, JSONArray.parseArray(updateArr), JSONArray.parseArray(addArr));
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "编辑资料库:" + name));
		resMap.put("status", Constants.successStatus);
		resMap.put("info", "编辑成功");
		return JSON.toJSONString(resMap);
	}

	/**
	 * 删除资料库信息
	 * 
	 * @param id
	 *            资料库Id
	 * @param name
	 *            资料库名
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "delInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String delInfo(@RequestParam("id") int id, @RequestParam("name") String name, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		planService.delInfo(id);
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "删除资料库:" + name));
		resMap.put("status", Constants.successStatus);
		resMap.put("info", "删除成功");
		return JSON.toJSONString(resMap);
	}

	/**
	 * 加载页面时获取资料库的列名信息
	 * 
	 * @param id
	 *            资料库Id
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "getInfoColumnNameInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getInfoColumnNameInfo(@RequestParam("id") int id) {

		Map<String, String> resMap = new HashMap<String, String>();
		List<InfoColumn> list = planService.getInfoColumnNameInfo(id);
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(list));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 添加资料库的内容
	 * 
	 * @param infoId
	 *            资料库Id
	 * @param infoName
	 *            资料库名
	 * @param contentStr
	 *            字符串型的内容 {"col_1":"","col_3":"",..} 格式:col_加上该列的Id
	 * @param 上传的附件内容
	 *            格式: 文件名 col_+列Id 如col_X
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "addInfoContent", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String addInfoContent(@RequestParam("infoId") int infoId, @RequestParam("infoName") String infoName,
			@RequestParam("contentStr") String contentStr, MultipartHttpServletRequest multiReq,
			HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		boolean res = planService.addInfoContent(infoId, infoName, JSONObject.parseObject(contentStr),
				multiReq.getFileMap());
		if (res == true) {
			resMap.put("status", Constants.successStatus);
			resMap.put("info", "添加成功");
		} else {
			resMap.put("status", Constants.apiErrorStatus);
			resMap.put("info", "添加失败");
		}
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "添加资料库:" + infoName + "内容"));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 修改资料库的内容
	 * 
	 * @param infoId
	 *            流程库表Id
	 * @param infoName
	 *            流程库表名
	 * @param flag
	 *            行标识
	 * @param contentStr
	 *            字符串型的内容 {"col_1":"","col_3":"",..} 格式:col_加上该列的Id
	 * @param 上传的附件内容
	 *            格式: 文件名 col_+列Id 如col_X
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "updateInfoContent", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String updateInfoContent(@RequestParam("infoId") int infoId, @RequestParam("infoName") String infoName,
			@RequestParam("flag") String flag, @RequestParam("contentStr") String contentStr,
			MultipartHttpServletRequest multiReq, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		boolean res = planService.updateInfoContent(infoId, flag, JSONObject.parseObject(contentStr),
				multiReq.getFileMap());
		if (res == true) {
			resMap.put("status", Constants.successStatus);
			resMap.put("info", "修改成功");
		} else {
			resMap.put("status", Constants.apiErrorStatus);
			resMap.put("info", "修改失败");
		}
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "修改资料库:" + infoName + "内容"));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 删除资料库的内容
	 * 
	 * @param infoId
	 *            资料库Id
	 * @param infoName
	 *            资料库名
	 * @param flag
	 *            行标识
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "delInfoContent", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String delInfoContent(@RequestParam("infoId") int infoId, @RequestParam("infoName") String infoName,
			@RequestParam("flag") String flag, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		resMap.put("status", Constants.successStatus);
		planService.delInfoContent(infoId, flag);
		resMap.put("info", "删除成功");
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "删除流程库表:" + infoName + "内容"));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 查询流程库表内容
	 * 
	 * @param infoId
	 *            流程库表Id
	 * @param columnId
	 *            列Id
	 * @param content
	 *            列的具体内容
	 * @param token
	 *            // 此接口需要前台进行分页
	 * @return
	 */
	@RequestMapping(value = "getInfoContent", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getInfoContent(@RequestParam("infoId") int infoId, @RequestParam("columnId") String columnId,
			@RequestParam("content") String content, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		JSONObject jo = planService.getInfoContent(infoId, columnId, content);
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(jo));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 获取运营报告编号
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "getAccidentReportNo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getAccidentReportNo() {

		Map<String, String> resMap = new HashMap<String, String>();
		String no = planService.getAccidentReportNo();
		resMap.put("status", Constants.successStatus);
		resMap.put("info", no);
		return JSON.toJSONString(resMap);
	}

	/**
	 * 修改运营报告编号
	 * 
	 * @param no
	 *            编号
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "updateAccidentReportNo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String updateAccidentReportNo(@RequestParam("no") String no, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		planService.updateAccidentReportNo(no);
		resMap.put("status", Constants.successStatus);
		resMap.put("info", "修改成功");
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "修改了运营报告编号:" + no));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 新增运营事故报告
	 * 
	 * @param no
	 *            报告编号
	 * @param eventName
	 *            事件名称
	 * @param eventAddress
	 *            时间地址
	 * @param occurDate
	 *            发生时间(yyyy-MM-dd hh:mm)
	 * @param relatedPerson
	 *            相关人员
	 * @param rank
	 *            初判等级
	 * @param eventSummary
	 *            事件概况
	 * @param affect
	 *            影响情况
	 * @param analysisAndReform
	 *            过程分析及整改要求
	 * @param opinion
	 *            处理意见
	 * @param analysisMember
	 *            专题分析会成员用单
	 * @param otherExplain
	 *            其他需要说明
	 * @param fillDepart
	 *            填报部门
	 * @param fillDate
	 *            填报日期(yyyy-MM-dd)
	 * @param fillPerson
	 *            填报人
	 * @param responsiblePerson
	 *            负责人
	 * @param attachArr
	 *            附件数组
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "addAccidentReport", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String addAccidentReport(@RequestParam("no") String no, @RequestParam("eventName") String eventName,
			@RequestParam("eventAddress") String eventAddress, @RequestParam("occurDate") String occurDate,
			@RequestParam("relatedPerson") String relatedPerson, @RequestParam("rank") String rank,
			@RequestParam("eventSummary") String eventSummary, @RequestParam("affect") String affect,
			@RequestParam("analysisAndReform") String analysisAndReform, @RequestParam("opinion") String opinion,
			@RequestParam("analysisMember") String analysisMember, @RequestParam("otherExplain") String otherExplain,
			@RequestParam("fillDepart") String fillDepart, @RequestParam("fillDate") String fillDate,
			@RequestParam("fillPerson") String fillPerson, @RequestParam("responsiblePerson") String responsiblePerson,
			@RequestParam("attachArr") MultipartFile[] attachArr, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		boolean res = planService.addAccidentReport(new AccidentReport(no, eventName, eventAddress, occurDate,
				relatedPerson, rank, eventSummary, affect, analysisAndReform, opinion, analysisMember, otherExplain,
				fillDepart, fillDate, fillPerson, responsiblePerson), attachArr);
		if (res == true) {
			resMap.put("status", Constants.successStatus);
			resMap.put("info", "新增成功");
		} else {
			resMap.put("status", Constants.apiErrorStatus);
			resMap.put("info", "新增失败");
		}
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "运营事故报告:" + eventName));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 获取运营报告列表
	 * 
	 * @param eventName
	 *            事件名称
	 * @param eventAddress
	 *            事件地址
	 * @param rank
	 *            初判等级
	 * @param occurDate
	 *            发生时间(yyyy-MM-dd hh:mm)
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "getAccidentReportList", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getAccidentReportList(@RequestParam("eventName") String eventName,
			@RequestParam("eventAddress") String eventAddress, @RequestParam("rank") String rank,
			@RequestParam("occurDate") String occurDate) {

		Map<String, String> resMap = new HashMap<String, String>();
		List<Map<String, String>> list = planService.getAccidentReportList(eventName, eventAddress, rank, occurDate);
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(list));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 获取事故报告的附件列表
	 * 
	 * @param id
	 *            事故报告Id
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "getAccidentReportAttachList", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getAccidentReportAttachList(@RequestParam("id") int id) {

		Map<String, String> resMap = new HashMap<String, String>();
		JSONArray ja = planService.getAccidentReportAttachList(id);
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(ja));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 获取事故报告详细信息
	 * 
	 * @param id
	 *            事故报告Id
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "getAccidentReportInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getAccidentReportInfo(@RequestParam("id") int id) {

		Map<String, String> resMap = new HashMap<String, String>();
		AccidentReport ar = planService.getAccidentReportInfo(id);
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(ar));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 修改事故报告的内容
	 * 
	 * @param id
	 *            事故报告的Id
	 * @param eventName
	 *            事件名称
	 * @param eventAddress
	 *            时间地址
	 * @param occurDate
	 *            发生时间(yyyy-MM-dd hh:mm)
	 * @param relatedPerson
	 *            相关人员
	 * @param rank
	 *            初判等级
	 * @param eventSummary
	 *            事件概况
	 * @param affect
	 *            影响情况
	 * @param analysisAndReform
	 *            过程分析及整改要求
	 * @param opinion
	 *            处理意见
	 * @param analysisMember
	 *            专题分析会成员用单
	 * @param otherExplain
	 *            其他需要说明
	 * @param fillDepart
	 *            填报部门
	 * @param fillDate
	 *            填报日期(yyyy-MM-dd)
	 * @param fillPerson
	 *            填报人
	 * @param responsiblePerson
	 *            负责人
	 * @param delArr
	 *            删除的附件id信息 如"1,2,3,4,5"
	 * @param attachArr
	 *            上传的附件数组
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "updateAccidentReportInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String updateAccidentReportInfo(@RequestParam("id") int id, @RequestParam("eventName") String eventName,
			@RequestParam("eventAddress") String eventAddress, @RequestParam("occurDate") String occurDate,
			@RequestParam("relatedPerson") String relatedPerson, @RequestParam("rank") String rank,
			@RequestParam("eventSummary") String eventSummary, @RequestParam("affect") String affect,
			@RequestParam("analysisAndReform") String analysisAndReform, @RequestParam("opinion") String opinion,
			@RequestParam("analysisMember") String analysisMember, @RequestParam("otherExplain") String otherExplain,
			@RequestParam("fillDepart") String fillDepart, @RequestParam("fillDate") String fillDate,
			@RequestParam("fillPerson") String fillPerson, @RequestParam("responsiblePerson") String responsiblePerson,
			@RequestParam("delArr") String delArr, @RequestParam("attachArr") MultipartFile[] attachArr,
			HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		int[] delArr1 = {};
		if (!"".equals(delArr)) {
			delArr1 = Utils.transStrToIntArr(delArr);
		}
		boolean res = planService.updateAccidentReportInfo(id, eventName, eventAddress, occurDate, relatedPerson, rank,
				eventSummary, affect, analysisAndReform, opinion, analysisMember, otherExplain, fillDepart, fillDate,
				fillPerson, responsiblePerson, delArr1, attachArr);
		if (res == true) {
			resMap.put("status", Constants.successStatus);
			resMap.put("info", "修改成功");
		} else {
			resMap.put("status", Constants.apiErrorStatus);
			resMap.put("info", "修改失败");
		}
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "修改事故报告" + eventName + "的内容"));
		return JSON.toJSONString(resMap);
	}

	/**
	 * 删除运营事故报告
	 * 
	 * @param id
	 *            报告Id
	 * @param eventName
	 *            事件名称
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "delAccidentReportInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String delAccidentReportInfo(@RequestParam("id") int id, @RequestParam("eventName") String eventName,
			HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		planService.delAccidentReportInfo(id);
		resMap.put("status", Constants.successStatus);
		resMap.put("info", "删除成功");
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "删除事故报告" + eventName));
		return JSON.toJSONString(resMap);
	}
}
