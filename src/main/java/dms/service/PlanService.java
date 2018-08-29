package dms.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;

import dms.entity.AccidentReport;
import dms.entity.Info;
import dms.entity.InfoColumn;
import dms.entity.Line;
import dms.entity.LineStation;
import dms.entity.Plan;
import dms.entity.Process;
import dms.entity.ProcessColumn;

public interface PlanService {

	public List<Line> getLineList();

	public List<LineStation> getLineStationList(int lineId);

	public Plan checkPlanName(String name);

	public boolean addPlan(Plan plan, MultipartFile[] attachArr);

	public PageInfo<Plan> getPlanList(int currentPage, int lineId, int stationId, String name);

	public Plan getPlanInfo(int id);

	public boolean updatePlanInfo(int id, String name, int lineId, int lineNo, int stationId, String stationName,
			int[] delArr, MultipartFile[] addAttach);

	public int delPlan(int id);
	
	public JSONArray getPlanAttach(int id);

	public int addProcess(String name, int userId, JSONArray columnArr);

	public List<Process> getProcessList();

	public Process getProcessInfo(int id);

	public int updateProcessInfo(int id, String name, JSONArray updateArr, JSONArray addArr);

	public int delProcess(int id);

	public List<ProcessColumn> getProcessColumnNameInfo(int id);

	public boolean addProcessContent(int processId, String processName, JSONObject contentStr,
			Map<String, MultipartFile> attachMap);

	public boolean updateProcessContent(int processId, String flag, JSONObject contentStr,
			Map<String, MultipartFile> attachMap);

	public int delProcessContent(int processId, String flag);

	public JSONObject getProcessContent(int processId, String columnId, String content);

	public int addInfo(String name, int userId, JSONArray columnArr);

	public List<Info> getInfoList();

	public Info getInfoInfo(int id);

	public int updateInfoInfo(int id, String name, JSONArray updateArr, JSONArray addArr);

	public int delInfo(int id);

	public List<InfoColumn> getInfoColumnNameInfo(int id);

	public boolean addInfoContent(int infoId, String InfoName, JSONObject contentStr,
			Map<String, MultipartFile> attachMap);

	public boolean updateInfoContent(int infoId, String flag, JSONObject contentStr,
			Map<String, MultipartFile> attachMap);

	public int delInfoContent(int infoId, String flag);

	public JSONObject getInfoContent(int infoId, String columnId, String content);

	public String getAccidentReportNo();

	public int updateAccidentReportNo(String no);

	public boolean addAccidentReport(AccidentReport ar, MultipartFile[] attachArr);

	public List<Map<String, String>> getAccidentReportList(String eventName, String eventAddress, String rank,
			String occurDate);

	public JSONArray getAccidentReportAttachList(int id);

	public AccidentReport getAccidentReportInfo(int id);

	public boolean updateAccidentReportInfo(int id, String eventName, String eventAddress, String occurDate,
			String relatedPerson, String rank, String eventSummary, String affect, String analysisAndReform,
			String opinion, String analysisMember, String otherExplain, String fillDepart, String fillDate,
			String fillPerson, String responsiblePerson, int[] delArr, MultipartFile[] attachArr);
	
	public int delAccidentReportInfo(int id);
}
