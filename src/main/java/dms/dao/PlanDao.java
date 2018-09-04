package dms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

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

public interface PlanDao {

	public List<Line> getLineList();

	public List<LineStation> getLineStationList(int lineId);

	public Plan checkPlanName(String name);

	public int addPlan(Plan plan);

	public int addPlanAttach(List<PlanAttach> lpa);

	public List<Plan> getPlanList(@Param("lineId") int lineId, @Param("stationId") int stationId,
			@Param("name") String name);

	public Plan getPlanInfo(int id);

	public int delPlanAttach(@Param("str") String str);

	public int updatePlanInfo(@Param("id") int id, @Param("lineId") int lineId, @Param("lineNo") int lineNo,
			@Param("stationId") int stationId, @Param("stationName") String stationName, @Param("name") String name);

	public int delPlan(int id);

	public List<String> getPlanAttach(int id);

	public int addProcess(Process process);

	public int addProcessColumn(List<ProcessColumn> lpc);

	public List<Process> getProcessList();

	public Process getProcessInfo(int id);

	public int updateProcessInfo(@Param("id") int id, @Param("name") String name);

	public int updateProcessColumnInfo(@Param("id") int id, @Param("name") String name);

	public int delProcess(int id);

	public List<ProcessColumn> getProcessColumnNameInfo(int id);

	public int addProcessContent(List<ProcessContent> lpc);

	public String JudgeColumnIdByProcess2IdAndFlag(@Param("processId") int processId, @Param("columnId") int columnId,
			@Param("flag") String flag);
	
	public int updateProcessContent(@Param("processId") int processId, @Param("columnId") int columnId,
			@Param("flag") String flag, @Param("content") String content);

	public int delProcessContent(@Param("processId") int processId, @Param("flag") String flag);

	public List<ProcessContent> getProcessContentList(int processId);

	public int addInfo(Info info);

	public int addInfoColumn(List<InfoColumn> lic);

	public List<Info> getInfoList();

	public Info getInfoInfo(int id);

	public int updateInfoInfo(@Param("id") int id, @Param("name") String name);

	public int updateInfoColumnInfo(@Param("id") int id, @Param("name") String name);

	public int delInfo(int id);

	public List<InfoColumn> getInfoColumnNameInfo(int id);

	public int addInfoContent(List<InfoContent> lic);

	public String JudgeColumnIdByProcessIdAndFlag(@Param("infoId") int infoId, @Param("columnId") int columnId,
			@Param("flag") String flag);

	public int updateInfoContent(@Param("infoId") int infoId, @Param("columnId") int columnId,
			@Param("flag") String flag, @Param("content") String content);

	public int delInfoContent(@Param("infoId") int infoId, @Param("flag") String flag);

	public List<InfoContent> getInfoContentList(int infoId);

	public String getAccidentReportNo();

	public int updateAccidentReportNo(String no);

	public int addAccidentReport(AccidentReport ar);

	public int addAccidentReportAttach(List<AccidentReportAttach> lara);

	public List<Map<String, String>> getAccidentReportList(@Param("eventName") String eventName,
			@Param("eventAddress") String eventAddress, @Param("rank") String rank,
			@Param("occurDate") String occurDate);

	public List<String> getAccidentReportAttachList(int id);

	public AccidentReport getAccidentReportInfo(int id);

	public int updateAccidentReportInfo(@Param("id") int id, @Param("eventName") String eventName,
			@Param("eventAddress") String eventAddress, @Param("occurDate") String occurDate,
			@Param("relatedPerson") String relatedPerson, @Param("rank") String rank,
			@Param("eventSummary") String eventSummary, @Param("affect") String affect,
			@Param("analysisAndReform") String analysisAndReform, @Param("opinion") String opinion,
			@Param("analysisMember") String analysisMember, @Param("otherExplain") String otherExplain,
			@Param("fillDepart") String fillDepart, @Param("fillDate") String fillDate,
			@Param("fillPerson") String fillPerson, @Param("responsiblePerson") String responsiblePerson);

	public int delAccidentReportAttachInfo(@Param("id") String id);

	public int delAccidentReportInfo(int id);
}
