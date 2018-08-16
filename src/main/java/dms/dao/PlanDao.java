package dms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import dms.entity.Line;
import dms.entity.LineStation;
import dms.entity.Plan;
import dms.entity.PlanAttach;

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
}
