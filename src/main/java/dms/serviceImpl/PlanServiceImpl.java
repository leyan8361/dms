package dms.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
			int id = plan.getId(); // 获取新增的预案的id
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

	public List<Process> getProcessList() {

		return planDao.getProcessList();
	}

	public Process getProcessInfo(int id) {

		return planDao.getProcessInfo(id);
	}

	public int updateProcessInfo(int id, String name, JSONArray updateArr, JSONArray addArr) {

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
			for (String key : keySet) {
				String columnId = key.split("_")[1];
				String content = contentStr.getString(key);
				planDao.updateProcessContent(processId, Integer.valueOf(columnId), flag, content);
			}
			Set<String> keySet2 = attachMap.keySet();
			for (String key : keySet2) {
				String columnId = key.split("_")[1];
				MultipartFile mf = attachMap.get(key);
				String content = System.currentTimeMillis() + "_" + mf.getOriginalFilename();
				mf.transferTo(new File(FilePath.processAttachPath + content));
				planDao.updateProcessContent(processId, Integer.valueOf(columnId), flag, content);
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
}
