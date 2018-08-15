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

import dms.entity.Line;
import dms.entity.LineStation;
import dms.service.PlanService;
import dms.utils.Constants;

@CrossOrigin
@RestController
@RequestMapping("plan")
public class PlanController {

	@Resource(name = "planService")
	private PlanService planService;

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
}
