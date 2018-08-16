package dms.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import dms.dao.PlanDao;
import dms.entity.Line;
import dms.entity.LineStation;
import dms.entity.Plan;
import dms.entity.PlanAttach;
import dms.service.PlanService;
import dms.utils.Constants;
import dms.utils.FilePath;

@Service("planService")
public class PlanServiceImpl implements PlanService {

	@Autowired
	private PlanDao planDao;

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
			int id = plan.getId(); // 获取新增的预案的id
			List<PlanAttach> lpa = new ArrayList<PlanAttach>();

			for (MultipartFile mf : attachArr) {
				String name = System.currentTimeMillis() + "_" + mf.getOriginalFilename();
				mf.transferTo(new File(FilePath.planAttachPath + name));
				lpa.add(new PlanAttach(id, name));
			}
			if(lpa.size()!=0) {
				planDao.addPlanAttach(lpa);
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public PageInfo<Plan> getPlanList(int currentPage, int lineId, int stationId, String name) {

		PageHelper.startPage(currentPage, Constants.pageSize);
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
			if(lpa.size()!=0) {
				planDao.addPlanAttach(lpa);	
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public int delPlan(int id) {
		
		return planDao.delPlan(id);
	}
}
