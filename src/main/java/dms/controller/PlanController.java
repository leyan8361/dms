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
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
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
		int[] delArr = {};
		if (!"".equals(delStr)) {
			delArr = Utils.transStrToIntArr(delStr);
		}
		boolean res = planService.updatePlanInfo(id, name, lineId, lineNo, stationId, stationName, delArr, addAttach);
		resMap.put("status", Constants.successStatus);
		if (res == true) {
			resMap.put("info", "�༭�ɹ�");
		} else {
			resMap.put("info", "�༭ʧ��");
		}
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
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
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "ɾ��Ԥ��:" + name));
		return JSON.toJSONString(resMap);
	}

	/**
	 * �������̿��
	 * 
	 * @param name
	 *            ����
	 * @param columnArr
	 *            ����Ϣ [{"name":"","type":""},{...},...] (����Ϊ��!!)
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
		resMap.put("info", "�����ɹ�");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "�������̿��:" + name));
		return JSON.toJSONString(resMap);
	}

	/**
	 * ��ѯ���̿���б�
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
	 * ��ȡ���̿������
	 * 
	 * @param id
	 *            ���̿��Id
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
	 * �޸����̿����Ϣ
	 * 
	 * @param id
	 *            ���̿��Id
	 * @param name
	 *            ���̿����
	 * @param updateArr
	 *            ���޸ĵ�����Ϣ [{"id":"","name":""},{...},..] id:��Id,name:����
	 * @param addArr
	 *            ����������Ϣ [{"name":"","type":""},{..},..] name:����,type:������
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
				String.valueOf(jo.get("userName")), "�༭���̿��:" + name));
		resMap.put("status", Constants.successStatus);
		resMap.put("info", "�༭�ɹ�");
		return JSON.toJSONString(resMap);
	}

	/**
	 * ɾ�����̿����Ϣ
	 * 
	 * @param id
	 *            ���̿��Id
	 * @param name
	 *            ���̿����
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "delProcess", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String delProcess(@RequestParam("id") int id, @RequestParam("name") String name, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		planService.delProcess(id);
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "ɾ�����̿��:" + name));
		resMap.put("status", Constants.successStatus);
		resMap.put("info", "ɾ���ɹ�");
		return JSON.toJSONString(resMap);
	}

	/**
	 * ��ȡԤ����.pdf������Ϣ
	 * 
	 * @param id
	 *            Ԥ��Id
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
	 * ����ҳ��ʱ��ȡ���̿���������Ϣ
	 * 
	 * @param id
	 *            ���̿��Id
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
	 * ������̿�������
	 * 
	 * @param processId
	 *            ���̿��Id
	 * @param processName
	 *            ���̿����
	 * @param contentStr
	 *            �ַ����͵����� {"col_1":"","col_3":"",..} ��ʽ:col_���ϸ��е�Id
	 * @param �ϴ��ĸ�������
	 *            ��ʽ: �ļ��� col_+��Id ��col_X
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
			resMap.put("info", "��ӳɹ�");
		} else {
			resMap.put("status", Constants.apiErrorStatus);
			resMap.put("info", "���ʧ��");
		}
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "������̿��:" + processName + "����"));
		return JSON.toJSONString(resMap);
	}

	/**
	 * �޸����̿�������
	 * 
	 * @param processId
	 *            ���̿��Id
	 * @param processName
	 *            ���̿����
	 * @param flag
	 *            �б�ʶ
	 * @param contentStr
	 *            �ַ����͵����� {"col_1":"","col_3":"",..} ��ʽ:col_���ϸ��е�Id
	 * @param �ϴ��ĸ�������
	 *            ��ʽ: �ļ��� col_+��Id ��col_X
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
			resMap.put("info", "�޸ĳɹ�");
		} else {
			resMap.put("status", Constants.apiErrorStatus);
			resMap.put("info", "�޸�ʧ��");
		}
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "�޸����̿��:" + processName + "����"));
		return JSON.toJSONString(resMap);
	}

	/**
	 * ɾ�����̿�������
	 * 
	 * @param processId
	 *            ���̿��Id
	 * @param processName
	 *            ���̿����
	 * @param flag
	 *            �б�ʶ
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
		resMap.put("info", "ɾ���ɹ�");
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "ɾ�����̿��:" + processName + "����"));
		return JSON.toJSONString(resMap);
	}

	/**
	 * ��ѯ���̿������
	 * 
	 * @param processId
	 *            ���̿��Id
	 * @param columnId
	 *            ��Id
	 * @param content
	 *            �еľ�������
	 * @param token
	 *            // �˽ӿ���Ҫǰ̨���з�ҳ
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
	 * �������Ͽ�
	 * 
	 * @param name
	 *            ����
	 * @param columnArr
	 *            ����Ϣ [{"name":"","type":""},{...},...] (����Ϊ��!!)
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
		resMap.put("info", "�����ɹ�");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "�������Ͽ�:" + name));
		return JSON.toJSONString(resMap);
	}

	/**
	 * ��ѯ���Ͽ��б�
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
	 * ��ȡ���Ͽ�����
	 * 
	 * @param id
	 *            ���Ͽ�Id
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
	 * �޸����Ͽ���Ϣ
	 * 
	 * @param id
	 *            ���Ͽ�Id
	 * @param name
	 *            ���Ͽ���
	 * @param updateArr
	 *            ���޸ĵ�����Ϣ [{"id":"","name":""},{...},..] id:��Id,name:����
	 * @param addArr
	 *            ����������Ϣ [{"name":"","type":""},{..},..] name:����,type:������
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
				String.valueOf(jo.get("userName")), "�༭���Ͽ�:" + name));
		resMap.put("status", Constants.successStatus);
		resMap.put("info", "�༭�ɹ�");
		return JSON.toJSONString(resMap);
	}

	/**
	 * ɾ�����Ͽ���Ϣ
	 * 
	 * @param id
	 *            ���Ͽ�Id
	 * @param name
	 *            ���Ͽ���
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "delInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String delInfo(@RequestParam("id") int id, @RequestParam("name") String name, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		planService.delInfo(id);
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "ɾ�����Ͽ�:" + name));
		resMap.put("status", Constants.successStatus);
		resMap.put("info", "ɾ���ɹ�");
		return JSON.toJSONString(resMap);
	}

	/**
	 * ����ҳ��ʱ��ȡ���Ͽ��������Ϣ
	 * 
	 * @param id
	 *            ���Ͽ�Id
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
	 * ������Ͽ������
	 * 
	 * @param infoId
	 *            ���Ͽ�Id
	 * @param infoName
	 *            ���Ͽ���
	 * @param contentStr
	 *            �ַ����͵����� {"col_1":"","col_3":"",..} ��ʽ:col_���ϸ��е�Id
	 * @param �ϴ��ĸ�������
	 *            ��ʽ: �ļ��� col_+��Id ��col_X
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
			resMap.put("info", "��ӳɹ�");
		} else {
			resMap.put("status", Constants.apiErrorStatus);
			resMap.put("info", "���ʧ��");
		}
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "������Ͽ�:" + infoName + "����"));
		return JSON.toJSONString(resMap);
	}

	/**
	 * �޸����Ͽ������
	 * 
	 * @param infoId
	 *            ���̿��Id
	 * @param infoName
	 *            ���̿����
	 * @param flag
	 *            �б�ʶ
	 * @param contentStr
	 *            �ַ����͵����� {"col_1":"","col_3":"",..} ��ʽ:col_���ϸ��е�Id
	 * @param �ϴ��ĸ�������
	 *            ��ʽ: �ļ��� col_+��Id ��col_X
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
			resMap.put("info", "�޸ĳɹ�");
		} else {
			resMap.put("status", Constants.apiErrorStatus);
			resMap.put("info", "�޸�ʧ��");
		}
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "�޸����Ͽ�:" + infoName + "����"));
		return JSON.toJSONString(resMap);
	}

	/**
	 * ɾ�����Ͽ������
	 * 
	 * @param infoId
	 *            ���Ͽ�Id
	 * @param infoName
	 *            ���Ͽ���
	 * @param flag
	 *            �б�ʶ
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "delInfoContent", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String delInfoContent(@RequestParam("infoId") int infoId, @RequestParam("infoName") String infoName,
			@RequestParam("flag") String flag, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		resMap.put("status", Constants.successStatus);
		planService.delInfoContent(infoId, flag);
		resMap.put("info", "ɾ���ɹ�");
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "ɾ�����̿��:" + infoName + "����"));
		return JSON.toJSONString(resMap);
	}

	/**
	 * ��ѯ���̿������
	 * 
	 * @param infoId
	 *            ���̿��Id
	 * @param columnId
	 *            ��Id
	 * @param content
	 *            �еľ�������
	 * @param token
	 *            // �˽ӿ���Ҫǰ̨���з�ҳ
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
	 * ��ȡ��Ӫ������
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
	 * �޸���Ӫ������
	 * 
	 * @param no
	 *            ���
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "updateAccidentReportNo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String updateAccidentReportNo(@RequestParam("no") String no, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		planService.updateAccidentReportNo(no);
		resMap.put("status", Constants.successStatus);
		resMap.put("info", "�޸ĳɹ�");
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "�޸�����Ӫ������:" + no));
		return JSON.toJSONString(resMap);
	}

	/**
	 * ������Ӫ�¹ʱ���
	 * 
	 * @param no
	 *            ������
	 * @param eventName
	 *            �¼�����
	 * @param eventAddress
	 *            ʱ���ַ
	 * @param occurDate
	 *            ����ʱ��(yyyy-MM-dd hh:mm)
	 * @param relatedPerson
	 *            �����Ա
	 * @param rank
	 *            ���еȼ�
	 * @param eventSummary
	 *            �¼��ſ�
	 * @param affect
	 *            Ӱ�����
	 * @param analysisAndReform
	 *            ���̷���������Ҫ��
	 * @param opinion
	 *            �������
	 * @param analysisMember
	 *            ר��������Ա�õ�
	 * @param otherExplain
	 *            ������Ҫ˵��
	 * @param fillDepart
	 *            �����
	 * @param fillDate
	 *            �����(yyyy-MM-dd)
	 * @param fillPerson
	 *            ���
	 * @param responsiblePerson
	 *            ������
	 * @param attachArr
	 *            ��������
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
			resMap.put("info", "�����ɹ�");
		} else {
			resMap.put("status", Constants.apiErrorStatus);
			resMap.put("info", "����ʧ��");
		}
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "��Ӫ�¹ʱ���:" + eventName));
		return JSON.toJSONString(resMap);
	}

	/**
	 * ��ȡ��Ӫ�����б�
	 * 
	 * @param eventName
	 *            �¼�����
	 * @param eventAddress
	 *            �¼���ַ
	 * @param rank
	 *            ���еȼ�
	 * @param occurDate
	 *            ����ʱ��(yyyy-MM-dd hh:mm)
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
	 * ��ȡ�¹ʱ���ĸ����б�
	 * 
	 * @param id
	 *            �¹ʱ���Id
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
	 * ��ȡ�¹ʱ�����ϸ��Ϣ
	 * 
	 * @param id
	 *            �¹ʱ���Id
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
	 * �޸��¹ʱ��������
	 * 
	 * @param id
	 *            �¹ʱ����Id
	 * @param eventName
	 *            �¼�����
	 * @param eventAddress
	 *            ʱ���ַ
	 * @param occurDate
	 *            ����ʱ��(yyyy-MM-dd hh:mm)
	 * @param relatedPerson
	 *            �����Ա
	 * @param rank
	 *            ���еȼ�
	 * @param eventSummary
	 *            �¼��ſ�
	 * @param affect
	 *            Ӱ�����
	 * @param analysisAndReform
	 *            ���̷���������Ҫ��
	 * @param opinion
	 *            �������
	 * @param analysisMember
	 *            ר��������Ա�õ�
	 * @param otherExplain
	 *            ������Ҫ˵��
	 * @param fillDepart
	 *            �����
	 * @param fillDate
	 *            �����(yyyy-MM-dd)
	 * @param fillPerson
	 *            ���
	 * @param responsiblePerson
	 *            ������
	 * @param delArr
	 *            ɾ���ĸ���id��Ϣ ��"1,2,3,4,5"
	 * @param attachArr
	 *            �ϴ��ĸ�������
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
			resMap.put("info", "�޸ĳɹ�");
		} else {
			resMap.put("status", Constants.apiErrorStatus);
			resMap.put("info", "�޸�ʧ��");
		}
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "�޸��¹ʱ���" + eventName + "������"));
		return JSON.toJSONString(resMap);
	}

	/**
	 * ɾ����Ӫ�¹ʱ���
	 * 
	 * @param id
	 *            ����Id
	 * @param eventName
	 *            �¼�����
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "delAccidentReportInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String delAccidentReportInfo(@RequestParam("id") int id, @RequestParam("eventName") String eventName,
			HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		planService.delAccidentReportInfo(id);
		resMap.put("status", Constants.successStatus);
		resMap.put("info", "ɾ���ɹ�");
		JSONObject jo = (JSONObject) req.getAttribute("user");
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), Integer.valueOf(String.valueOf(jo.get("userId"))),
				String.valueOf(jo.get("userName")), "ɾ���¹ʱ���" + eventName));
		return JSON.toJSONString(resMap);
	}
}
