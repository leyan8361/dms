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
	 * ��ȡ��·�����б�
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
	 * ��ȡ��·�³�վ�б�
	 * 
	 * @param lineId
	 *            ��·Id
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
	 * У��Ԥ�������Ƿ��ظ�
	 * 
	 * @param name
	 *            Ԥ����
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
	 * ����Ԥ��
	 * 
	 * @param userId
	 *            ��ǰ�û�Id
	 * @param name
	 *            Ԥ����
	 * @param lineId
	 *            ��·Id
	 * @param lineNo
	 *            ��·��
	 * @param stationId
	 *            ��վId
	 * @param stationName
	 *            ��վ��
	 * @param attachArr
	 *            �������� (����Ϊ��,���봫����)
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
			resMap.put("info", "��ӳɹ�");
		} else {
			resMap.put("status", Constants.apiErrorStatus);
			resMap.put("info", "���ʧ��");
		}
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "����Ԥ��:" + name));
		return JSON.toJSONString(resMap);
	}

	/**
	 * ��ѯԤ���б�
	 * 
	 * @param currentPage
	 *            ��ǰҳ��(��1��ʼ)
	 * @param lineId
	 *            ��·Id
	 * @param stationId
	 *            ��վId
	 * @param name
	 *            Ԥ������
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
	 * ��ȡԤ������
	 * 
	 * @param id
	 *            Ԥ��Id
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
	 * �༭Ԥ����Ϣ
	 * 
	 * @param id
	 *            Ԥ��Id
	 * @param name
	 *            Ԥ����
	 * @param lineId
	 *            ��·Id
	 * @param lineNo
	 *            ��·��
	 * @param stationId
	 *            ��վId
	 * @param stationName
	 *            ��վ��
	 * @param delStr
	 *            ɾ���ĸ���id��Ϣ ��"1,2,3,4,5"
	 * @param addAttach
	 *            �����ĸ���
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
			resMap.put("info", "�༭�ɹ�");
		} else {
			resMap.put("info", "�༭ʧ��");
		}
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "�༭Ԥ��:" + name));
		return JSON.toJSONString(resMap);
	}

	/**
	 * ɾ��Ԥ��
	 * 
	 * @param id
	 *            Ԥ��Id
	 * @param name
	 *            Ԥ������
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "delPlan", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String delPlan(@RequestParam("id") int id, @RequestParam("name") String name, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		planService.delPlan(id);
		resMap.put("status", Constants.successStatus);
		resMap.put("info", "ɾ���ɹ�");
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "ɾ��Ԥ��:" + name));
		return JSON.toJSONString(resMap);
	}
}
