package dms.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import dms.dao.PlanDao;
import dms.entity.AccidentReport;
import dms.entity.AccidentReportAttach;
import dms.entity.Info;
import dms.entity.InfoColumn;
import dms.entity.InfoContent;
import dms.entity.Line;
import dms.entity.LineStation;
import dms.entity.Plan;
import dms.entity.PlanAttach;
import dms.entity.Process;
import dms.entity.ProcessColumn;
import dms.entity.ProcessContent;
import dms.service.PlanService;
import dms.utils.Constants;
import dms.utils.FilePath;
import dms.utils.Utils;

@Service("planService")
public class PlanServiceImpl implements PlanService {

	@Autowired
	private PlanDao planDao;

	private static Logger logger = LoggerFactory.getLogger(PlanServiceImpl.class);

	public List<Line> getLineList() {

		return planDao.getLineList();
	}

	public List<LineStation> getLineStationList(int lineId) {

		return planDao.getLineStationList(lineId);
	}

	public Plan checkPlanName(String name) {

		return planDao.checkPlanName(name);
	}

	public boolean addPlan(Plan plan, MultipartFile[] attachArr) {

		try {
			planDao.addPlan(plan);
			int id = plan.getId(); // ��ȡ������Ԥ����id
			List<PlanAttach> lpa = new ArrayList<PlanAttach>();

			for (MultipartFile mf : attachArr) {
				String name = System.currentTimeMillis() + "_" + mf.getOriginalFilename();
				mf.transferTo(new File(FilePath.planAttachPath + name));
				lpa.add(new PlanAttach(id, name));
			}
			if (lpa.size() != 0) {
				planDao.addPlanAttach(lpa);
			}
			return true;
		} catch (IOException e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	public PageInfo<Plan> getPlanList(int currentPage, int lineId, int stationId, String name) {

		PageHelper.startPage(currentPage, 10000);
		List<Plan> list = planDao.getPlanList(lineId, stationId, name);
		PageInfo<Plan> planList = new PageInfo<>(list);
		return planList;
	}

	public Plan getPlanInfo(int id) {

		return planDao.getPlanInfo(id);
	}

	public boolean updatePlanInfo(int id, String name, int lineId, int lineNo, int stationId, String stationName,
			int[] delArr, MultipartFile[] addAttach) {

		try {
			String delStr = "";
			if (delArr.length == 0) {
				delStr = " < 0";
			} else {
				StringBuilder sb = new StringBuilder();
				sb.append(" in (");
				for (int i = 0; i < delArr.length; i++) {
					if (i == 0) {
						sb.append(delArr[i]);
					} else {
						sb.append("," + delArr[i]);
					}
				}
				sb.append(")");
				delStr = sb.toString();
			}
			planDao.delPlanAttach(delStr);
			planDao.updatePlanInfo(id, lineId, lineNo, stationId, stationName, name);
			List<PlanAttach> lpa = new ArrayList<PlanAttach>();
			for (MultipartFile mf : addAttach) {
				String fileName = System.currentTimeMillis() + "_" + mf.getOriginalFilename();
				mf.transferTo(new File(FilePath.planAttachPath + fileName));
				lpa.add(new PlanAttach(id, fileName));
			}
			if (lpa.size() != 0) {
				planDao.addPlanAttach(lpa);
			}
			return true;
		} catch (IOException e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	public int delPlan(int id) {

		return planDao.delPlan(id);
	}

	public JSONArray getPlanAttach(int id) {

		List<String> list = planDao.getPlanAttach(id);
		JSONArray ja = new JSONArray();
		for (String s : list) {
			JSONObject jo = new JSONObject();
			String path = FilePath.planAttachPath + s;
			jo.put("path", s);
			jo.put("createTime", Utils.getCreateTime(path));
			jo.put("modifyTime",
					new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(new File(path).lastModified())));
			ja.add(jo);
		}
		return ja;
	}

	public int addProcess(String name, int userId, JSONArray columnArr) {

		Process process = new Process(name, userId, Utils.getNowDate("yyyy-MM-dd"));
		planDao.addProcess(process);
		int processId = process.getId();
		List<ProcessColumn> lpc = new ArrayList<ProcessColumn>();
		for (Object object : columnArr) {
			JSONObject jo = (JSONObject) object;
			lpc.add(new ProcessColumn(processId, jo.getString("name"), jo.getString("type")));
		}
		planDao.addProcessColumn(lpc);
		return 1;
	}

	public PageInfo<Process> getProcessList(int currentPage) {

		PageHelper.startPage(currentPage, Constants.pageSize);
		List<Process> list = planDao.getProcessList();
		PageInfo<Process> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}

	public Process getProcessInfo(int id) {

		return planDao.getProcessInfo(id);
	}

	public int updateProcessInfo(int id, String name, JSONArray updateArr, JSONArray addArr, int[] delArr) {

		String delStr = "";
		if (delArr.length == 0) {
			delStr = " <0";
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(" in (");
			for (int i = 0; i < delArr.length; i++) {
				if (i == 0) {
					sb.append(delArr[i]);
				} else {
					sb.append("," + delArr[i]);
				}
			}
			sb.append(")");
			delStr = sb.toString();
		}
		planDao.delProcessColumn(delStr);
		planDao.updateProcessInfo(id, name);
		if (updateArr != null) {
			for (Object o : updateArr) {
				JSONObject jo = (JSONObject) o;
				planDao.updateProcessColumnInfo(Integer.valueOf(jo.getString("id")), jo.getString("name"));
			}
		}
		if (addArr != null) {
			List<ProcessColumn> lpc = new ArrayList<ProcessColumn>();
			for (Object o : addArr) {
				JSONObject jo = (JSONObject) o;
				lpc.add(new ProcessColumn(id, jo.getString("name"), jo.getString("type")));
			}
			planDao.addProcessColumn(lpc);
		}
		return 1;
	}

	public int delProcess(int id) {

		return planDao.delProcess(id);
	}

	public List<ProcessColumn> getProcessColumnNameInfo(int id) {

		return planDao.getProcessColumnNameInfo(id);
	}

	public boolean addProcessContent(int processId, String processName, JSONObject contentStr,
			Map<String, MultipartFile> attachMap) {

		try {
			Set<String> keySet = contentStr.keySet();
			String flag = String.valueOf(System.currentTimeMillis());
			List<ProcessContent> lpc = new ArrayList<ProcessContent>();
			for (String key : keySet) {
				String columnId = key.split("_")[1];
				String content = contentStr.getString(key);
				lpc.add(new ProcessContent(processId, Integer.valueOf(columnId), flag, content));
			}
			Set<String> keySet2 = attachMap.keySet();
			for (String key : keySet2) {
				String columnId = key.split("_")[1];
				MultipartFile mf = attachMap.get(key);
				String content = System.currentTimeMillis() + "_" + mf.getOriginalFilename();
				lpc.add(new ProcessContent(processId, Integer.valueOf(columnId), flag, content));
				mf.transferTo(new File(FilePath.processAttachPath + content));
			}
			if (lpc.size() != 0) {
				planDao.addProcessContent(lpc);
			}
			return true;
		} catch (IOException e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	public boolean updateProcessContent(int processId, String flag, JSONObject contentStr,
			Map<String, MultipartFile> attachMap) {

		try {
			Set<String> keySet = contentStr.keySet();
			List<ProcessContent> lic = new ArrayList<ProcessContent>();
			for (String key : keySet) {
				String columnId = key.split("_")[1];
				String content = contentStr.getString(key);
				if (planDao.JudgeColumnIdByProcess2IdAndFlag(processId, Integer.valueOf(columnId), flag) == null) {
					lic.add(new ProcessContent(processId, Integer.valueOf(columnId), flag, content));
				} else {
					planDao.updateProcessContent(processId, Integer.valueOf(columnId), flag, content);
				}
			}
			Set<String> keySet2 = attachMap.keySet();
			for (String key : keySet2) {
				String columnId = key.split("_")[1];
				MultipartFile mf = attachMap.get(key);
				String content = System.currentTimeMillis() + "_" + mf.getOriginalFilename();
				mf.transferTo(new File(FilePath.processAttachPath + content));
				if (planDao.JudgeColumnIdByProcess2IdAndFlag(processId, Integer.valueOf(columnId), flag) == null) {
					lic.add(new ProcessContent(processId, Integer.valueOf(columnId), flag, content));
				} else {
					planDao.updateProcessContent(processId, Integer.valueOf(columnId), flag, content);
				}
			}
			if (lic.size() != 0) {
				planDao.addProcessContent(lic);
			}
			return true;
		} catch (IOException e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	public int delProcessContent(int processId, String flag) {

		return planDao.delProcessContent(processId, flag);
	}

	public JSONObject getProcessContent(int processId, String columnId, String content) {

		List<ProcessContent> lpc = planDao.getProcessContentList(processId);
		List<ProcessColumn> lic2 = planDao.getProcessColumnNameInfo(processId);
		Set<Integer> set = new HashSet<Integer>();
		Set<Integer> set2 = new HashSet<Integer>();
		for (ProcessColumn ic : lic2) {
			set.add(ic.getId());
		}
		JSONObject jo = new JSONObject();
		for (ProcessContent pc : lpc) {
			if (!"".equals(columnId)) {
				if (pc.getColumnId() == Integer.valueOf(columnId) && pc.getContent().indexOf(content) != -1) {
					jo.put(pc.getFlag(), new JSONArray());
				}
			} else {
				jo.put(pc.getFlag(), new JSONArray());
			}
		}
		// System.out.println(jo);
		for (ProcessContent pc : lpc) {
			JSONArray ja = (JSONArray) jo.get(pc.getFlag());
			if (ja != null) {
				JSONObject jo2 = new JSONObject();
				jo2.put("columnId", pc.getColumnId());
				jo2.put("content", pc.getContent() == null ? "" : pc.getContent());
				ja.add(jo2);
			}
		}
		Set<String> key = jo.keySet();
		for (String s : key) {
			JSONArray jsa = jo.getJSONArray(s);
			for (Object o : jsa) {
				JSONObject jso = (JSONObject) o;
				set2.add(Integer.valueOf(String.valueOf(jso.get("columnId"))));
			}
		}
		set.removeAll(set2);
		for (String s : key) {
			JSONArray jsa = jo.getJSONArray(s);
			for (int i : set) {
				JSONObject jo2 = new JSONObject();
				jo2.put("columnId", i);
				jo2.put("content", "");
				jsa.add(jo2);
			}
			jsa = Utils.sortJSONArray(jsa);
			jo.put(s, jsa);
		}
		return jo;
	}

	public int addInfo(String name, int userId, JSONArray columnArr) {

		Info info = new Info(name, userId, Utils.getNowDate("yyyy-MM-dd"));
		planDao.addInfo(info);
		int infoId = info.getId(); // ��ȡ����ӵ����Ͽ��Id
		List<InfoColumn> lic = new ArrayList<InfoColumn>();
		for (Object o : columnArr) {
			JSONObject jo = (JSONObject) o;
			lic.add(new InfoColumn(infoId, jo.getString("name"), jo.getString("type")));
		}
		planDao.addInfoColumn(lic);
		return 1;
	}

	public PageInfo<Info> getInfoList(int currentPage) {

		PageHelper.startPage(currentPage, Constants.pageSize);
		List<Info> list = planDao.getInfoList();
		PageInfo<Info> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}

	public Info getInfoInfo(int id) {

		return planDao.getInfoInfo(id);
	}

	public int updateInfoInfo(int id, String name, JSONArray updateArr, JSONArray addArr, int[] delArr) {

		String delStr = "";
		if (delArr.length == 0) {
			delStr = " < 0";
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(" in (");
			for (int i = 0; i < delArr.length; i++) {
				if (i == 0) {
					sb.append(delArr[i]);
				} else {
					sb.append("," + delArr[i]);
				}
			}
			sb.append(")");
			delStr = sb.toString();
		}
		planDao.delInfoColumn(delStr);
		planDao.updateInfoInfo(id, name);
		if (updateArr != null) {
			for (Object o : updateArr) {
				JSONObject jo = (JSONObject) o;
				planDao.updateInfoColumnInfo(Integer.valueOf(jo.getString("id")), jo.getString("name"));
			}
		}
		if (addArr != null) {
			List<InfoColumn> lic = new ArrayList<InfoColumn>();
			for (Object o : addArr) {
				JSONObject jo = (JSONObject) o;
				lic.add(new InfoColumn(id, jo.getString("name"), jo.getString("type")));
			}
			planDao.addInfoColumn(lic);
		}
		return 1;
	}

	public int delInfo(int id) {

		return planDao.delInfo(id);
	}

	public List<InfoColumn> getInfoColumnNameInfo(int id) {

		return planDao.getInfoColumnNameInfo(id);
	}

	public boolean addInfoContent(int infoId, String infoName, JSONObject contentStr,
			Map<String, MultipartFile> attachMap) {

		try {
			Set<String> keySet = contentStr.keySet();
			String flag = String.valueOf(System.currentTimeMillis());
			List<InfoContent> lic = new ArrayList<InfoContent>();
			for (String key : keySet) {
				String columnId = key.split("_")[1];
				String content = contentStr.getString(key);
				lic.add(new InfoContent(infoId, Integer.valueOf(columnId), flag, content));
			}
			Set<String> keySet2 = attachMap.keySet();
			for (String key : keySet2) {
				String columnId = key.split("_")[1];
				MultipartFile mf = attachMap.get(key);
				String content = System.currentTimeMillis() + "_" + mf.getOriginalFilename();
				lic.add(new InfoContent(infoId, Integer.valueOf(columnId), flag, content));
				mf.transferTo(new File(FilePath.infoAttachPath + content));
			}
			if (lic.size() != 0) {
				planDao.addInfoContent(lic);
			}
			return true;
		} catch (IOException e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	public boolean updateInfoContent(int infoId, String flag, JSONObject contentStr,
			Map<String, MultipartFile> attachMap) {

		try {
			Set<String> keySet = contentStr.keySet();
			List<InfoContent> lic = new ArrayList<InfoContent>();
			for (String key : keySet) {
				String columnId = key.split("_")[1];
				String content = contentStr.getString(key);
				if (planDao.JudgeColumnIdByProcessIdAndFlag(infoId, Integer.valueOf(columnId), flag) == null) {
					lic.add(new InfoContent(infoId, Integer.valueOf(columnId), flag, content));
				} else {
					planDao.updateInfoContent(infoId, Integer.valueOf(columnId), flag, content);
				}
			}
			Set<String> keySet2 = attachMap.keySet();
			for (String key : keySet2) {
				String columnId = key.split("_")[1];
				MultipartFile mf = attachMap.get(key);
				String content = System.currentTimeMillis() + "_" + mf.getOriginalFilename();
				mf.transferTo(new File(FilePath.infoAttachPath + content));
				if (planDao.JudgeColumnIdByProcessIdAndFlag(infoId, Integer.valueOf(columnId), flag) == null) {
					lic.add(new InfoContent(infoId, Integer.valueOf(columnId), flag, content));
				} else {
					planDao.updateInfoContent(infoId, Integer.valueOf(columnId), flag, content);
				}
			}
			if (lic.size() != 0) {
				planDao.addInfoContent(lic);
			}
			return true;
		} catch (IOException e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	public int delInfoContent(int infoId, String flag) {

		return planDao.delInfoContent(infoId, flag);
	}

	public JSONObject getInfoContent(int infoId, String columnId, String content) {

		List<InfoContent> lic = planDao.getInfoContentList(infoId);
		List<InfoColumn> lic2 = planDao.getInfoColumnNameInfo(infoId); // ��ȡ���ϱ����е�����Ϣ
		Set<Integer> set = new HashSet<Integer>();
		Set<Integer> set2 = new HashSet<Integer>();
		for (InfoColumn ic : lic2) {
			set.add(ic.getId());
		}
		JSONObject jo = new JSONObject();
		for (InfoContent ic : lic) {
			if (!"".equals(columnId)) {
				if (ic.getColumnId() == Integer.valueOf(columnId) && ic.getContent().indexOf(content) != -1) {
					jo.put(ic.getFlag(), new JSONArray());
				}
			} else {
				jo.put(ic.getFlag(), new JSONArray());
			}
		}
		for (InfoContent ic : lic) {
			JSONArray ja = (JSONArray) jo.get(ic.getFlag());
			if (ja != null) {
				JSONObject jo2 = new JSONObject();
				jo2.put("columnId", ic.getColumnId());
				jo2.put("content", ic.getContent() == null ? "" : ic.getContent());
				ja.add(jo2);
			}
		}
		Set<String> key = jo.keySet();
		for (String s : key) {
			JSONArray jsa = jo.getJSONArray(s);
			for (Object o : jsa) {
				JSONObject jso = (JSONObject) o;
				set2.add(Integer.valueOf(String.valueOf(jso.get("columnId"))));
			}
		}
		set.removeAll(set2);
		for (String s : key) {
			JSONArray jsa = jo.getJSONArray(s);
			for (int i : set) {
				JSONObject jo2 = new JSONObject();
				jo2.put("columnId", i);
				jo2.put("content", "");
				jsa.add(jo2);
			}
			jsa = Utils.sortJSONArray(jsa);
			jo.put(s, jsa);
		}
		return jo;
	}

	public String getAccidentReportNo() {

		return planDao.getAccidentReportNo();
	}

	public int updateAccidentReportNo(String no) {

		return planDao.updateAccidentReportNo(no);
	}

	public boolean addAccidentReport(AccidentReport ar, MultipartFile[] attachArr) {

		try {
			planDao.addAccidentReport(ar);
			int reportId = ar.getId();
			List<AccidentReportAttach> lara = new ArrayList<AccidentReportAttach>();
			for (MultipartFile mf : attachArr) {
				String name = System.currentTimeMillis() + "_" + mf.getOriginalFilename();
				mf.transferTo(new File(FilePath.accidentReportAttachPath + name));
				lara.add(new AccidentReportAttach(reportId, name));
			}
			if (lara.size() != 0) {
				planDao.addAccidentReportAttach(lara);
			}
			return true;
		} catch (IOException e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	public List<Map<String, String>> getAccidentReportList(String eventName, String eventAddress, String rank,
			String occurDate) {

		return planDao.getAccidentReportList(eventName, eventAddress, rank, occurDate);
	}

	public JSONArray getAccidentReportAttachList(int id) {

		List<String> list = planDao.getAccidentReportAttachList(id);
		JSONArray ja = new JSONArray();
		for (String s : list) {
			JSONObject jo = new JSONObject();
			String path = FilePath.accidentReportAttachPath + s;
			jo.put("path", s);
			jo.put("createTime", Utils.getCreateTime(path));
			jo.put("modifyTime",
					new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(new File(path).lastModified())));
			ja.add(jo);
		}
		return ja;
	}

	public AccidentReport getAccidentReportInfo(int id) {

		return planDao.getAccidentReportInfo(id);
	}

	public boolean updateAccidentReportInfo(int id, String eventName, String eventAddress, String occurDate,
			String relatedPerson, String rank, String eventSummary, String affect, String analysisAndReform,
			String opinion, String analysisMember, String otherExplain, String fillDepart, String fillDate,
			String fillPerson, String responsiblePerson, int[] delArr, MultipartFile[] attachArr) {

		try {
			String delStr = "";
			if (delArr.length == 0) {
				delStr = " < 0";
			} else {
				StringBuilder sb = new StringBuilder();
				sb.append(" in (");
				for (int i = 0; i < delArr.length; i++) {
					if (i == 0) {
						sb.append(delArr[i]);
					} else {
						sb.append("," + delArr[i]);
					}
				}
				sb.append(")");
				delStr = sb.toString();
			}
			planDao.delAccidentReportAttachInfo(delStr);
			planDao.updateAccidentReportInfo(id, eventName, eventAddress, occurDate, relatedPerson, rank, eventSummary,
					affect, analysisAndReform, opinion, analysisMember, otherExplain, fillDepart, fillDate, fillPerson,
					responsiblePerson);
			List<AccidentReportAttach> lara = new ArrayList<AccidentReportAttach>();
			for (MultipartFile mf : attachArr) {
				String name = System.currentTimeMillis() + "_" + mf.getOriginalFilename();
				lara.add(new AccidentReportAttach(id, name));
				mf.transferTo(new File(FilePath.accidentReportAttachPath + name));
			}
			if (lara.size() != 0) {
				planDao.addAccidentReportAttach(lara);
			}
			return true;
		} catch (IOException e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	public int delAccidentReportInfo(int id) {

		return planDao.delAccidentReportInfo(id);
	}
}
