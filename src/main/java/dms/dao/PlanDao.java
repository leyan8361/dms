package dms.dao;

import java.util.List;

import dms.entity.Line;
import dms.entity.LineStation;
import dms.entity.Plan;

public interface PlanDao {

	public List<Line> getLineList();
	
	public List<LineStation> getLineStationList(int lineId);
	
	public int addPlan(Plan plan);
}
