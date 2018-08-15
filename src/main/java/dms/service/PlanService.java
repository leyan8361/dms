package dms.service;

import java.util.List;

import dms.entity.Line;
import dms.entity.LineStation;

public interface PlanService {

	public List<Line> getLineList();
	
	public List<LineStation> getLineStationList(int lineId);
}
