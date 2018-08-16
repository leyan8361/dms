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
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;

import dms.entity.Line;
import dms.entity.LineStation;
import dms.entity.Log;
import dms.entity.Plan;
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
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd"), Integer.valueOf(String.valueOf(jo.get("userId"))),
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
		int[] delArr = Utils.transStrToIntArr(delStr);
		boolean res = planService.updatePlanInfo(id, name, lineId, lineNo, stationId, stationName, delArr, addAttach);
		resMap.put("status", Constants.successStatus);
		if (res == true) {
			resMap.put("info", "编辑成功");
		} else {
			resMap.put("info", "编辑失败");
		}
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd"), Integer.valueOf(String.valueOf(jo.get("userId"))),
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
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "删除预案:" + name));
		return JSON.toJSONString(resMap);
	}
}
