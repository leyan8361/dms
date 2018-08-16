package dms.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;

import dms.entity.Line;
import dms.entity.LineStation;
import dms.entity.Plan;

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
}
