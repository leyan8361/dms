package dms.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import dms.dao.TaskDao;
import dms.entity.Task;
import dms.entity.TaskAttach;
import dms.entity.TaskSave;
import dms.entity.TaskSaveAttach;
import dms.entity.TaskSaveUser;
import dms.entity.TaskUser;
import dms.entity.UserInfo;
import dms.service.TaskService;
import dms.utils.Constants;
import dms.utils.FilePath;
import dms.utils.Utils;

@Service("taskService")
public class TaskServiceImpl implements TaskService {

	@Autowired
	private TaskDao taskDao;

	private static Logger logger = LoggerFactory.getLogger(PlanServiceImpl.class);

	public List<UserInfo> getUserListBeforeAddTask() {

		return taskDao.getUserListBeforeAddTask();
	}

	public boolean saveTaskInfo(int userId, String content, String deadLine, String attention, String remark,
			String oriAttach, MultipartFile[] attachArr, JSONArray userInfo) {

		try {
			TaskSave ts = new TaskSave(userId, content, deadLine, attention, remark);
			taskDao.addTaskSaveInfo(ts);
			int taskSaveId = ts.getId();
			List<TaskSaveAttach> ltsa = new ArrayList<TaskSaveAttach>();
			List<TaskSaveUser> ltsu = new ArrayList<TaskSaveUser>();
			if (!"".equals(oriAttach)) {
				String[] oriAttachArr = oriAttach.split(",");
				for (String str : oriAttachArr) {
					ltsa.add(new TaskSaveAttach(taskSaveId, str));
				}
			}
			for (MultipartFile mf : attachArr) {
				String name = System.currentTimeMillis() + "_" + mf.getOriginalFilename();
				mf.transferTo(new File(FilePath.taskAttachPath + name));
				ltsa.add(new TaskSaveAttach(taskSaveId, name));
			}
			if (userInfo != null) {
				for (Object o : userInfo) {
					JSONObject jo = (JSONObject) o;
					ltsu.add(new TaskSaveUser(taskSaveId, Integer.valueOf(jo.getString("userId")),
							jo.getString("userName")));
				}
			}
			if (ltsa.size() != 0) {
				taskDao.addTaskSaveAttachInfo(ltsa);
			}
			if (ltsu.size() != 0) {
				taskDao.addTaskSaveUserInfo(ltsu);
			}
			taskDao.updateSaveAddStatus(userId, taskSaveId);
			return true;
		} catch (IOException e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	public String checkTaskSave(int userId) {

		return taskDao.checkTaskSave(userId);
	}
	
	public TaskSave getSaveTaskInfo(int taskSaveId) {
		
		return taskDao.getSaveTaskInfo(taskSaveId);
	}

	public boolean addTaskInfo(String content, String deadLine, String attention, String remark, int creator,
			String oriAttach, MultipartFile[] attachArr, JSONArray userInfo) {

		try {
			Task task = new Task(content, deadLine, 0, 0, attention, remark, creator, Utils.getNowDate("yyyy-MM-dd"));
			taskDao.addTaskInfo(task);
			int taskId = task.getId();
			List<TaskAttach> lta = new ArrayList<TaskAttach>();
			List<TaskUser> ltu = new ArrayList<TaskUser>();
			if (!"".equals(oriAttach)) {
				String[] oriAttachArr = oriAttach.split(",");
				for (String str : oriAttachArr) {
					lta.add(new TaskAttach(taskId, str));
				}
			}
			for (MultipartFile mf : attachArr) {
				String name = System.currentTimeMillis() + "_" + mf.getOriginalFilename();
				mf.transferTo(new File(FilePath.taskAttachPath + name));
				lta.add(new TaskAttach(taskId, name));
			}
			if (userInfo != null) {
				for (Object o : userInfo) {
					JSONObject jo = (JSONObject) o;
					ltu.add(new TaskUser(taskId, Integer.valueOf(jo.getString("userId")), jo.getString("userName")));
				}
			}
			if (lta.size() != 0) {
				taskDao.addTaskAttachInfo(lta);
			}
			if (ltu.size() != 0) {
				taskDao.addTaskUserInfo(ltu);
			}
			taskDao.updateSaveAddStatusAfterPublishTask(creator);
			return true;
		} catch (IOException e) {

			logger.error(e.getMessage());
			return false;
		}
	}

	public PageInfo<Map<String, String>> getMyUnDoTaskList(int userId, int currentPage) {

		PageHelper.startPage(currentPage, Constants.pageSize);
		List<Map<String, String>> list = taskDao.getMyUnDoTaskList(userId);
		PageInfo<Map<String, String>> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}
	
	public Task getTaskInfo(int taskId) {
		
		return taskDao.getTaskInfo(taskId);
	}
}
