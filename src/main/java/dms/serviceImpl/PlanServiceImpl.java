package dms.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dms.dao.PlanDao;
import dms.entity.Line;
import dms.entity.LineStation;
import dms.service.PlanService;

@Service("planService")
public class PlanServiceImpl implements PlanService {

	@Autowired
	private PlanDao planDao;

	public List<Line> getLineList() {

		return planDao.getLineList();
	}
	
	public List<LineStation> getLineStationList(int lineId){
		
		return planDao.getLineStationList(lineId);
	}
}
