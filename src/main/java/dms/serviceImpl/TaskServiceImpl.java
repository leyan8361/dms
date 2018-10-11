package dms.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
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
import dms.entity.TaskTransferAttachSave;
import dms.entity.TaskTransferSave;
import dms.entity.TaskTransferSaveStatus;
import dms.entity.TaskTransferUserSave;
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
			Task task = new Task(content, deadLine, 0, 0, attention, remark, creator,
					Utils.getNowDate("yyyy-MM-dd HH:mm"));
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
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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

	public String judgeIfTaskTransfer(int taskId, int userId) {

		return taskDao.judgeIfTaskTransfer(taskId, userId);
	}

	public TaskTransferSaveStatus checkIfTaskTransferSave(int taskId, int userId) {

		return taskDao.checkIfTaskTransferSave(taskId, userId);
	}

	public boolean addTaskTransferSaveInfo(int userId, int taskId, String content, String deadLine, String attention,
			String remark, String oriAttachStr, MultipartFile[] attachArr, JSONArray userInfo) {

		try {
			String transferSaveIdTemp = taskDao.getUserSaveTransferId(taskId, userId);
			if (transferSaveIdTemp != null) {
				taskDao.delTaskTransferSave(Integer.valueOf(transferSaveIdTemp));
				taskDao.delTaskTransferUserSave(Integer.valueOf(transferSaveIdTemp));
				taskDao.delTaskTransferAttachSave(Integer.valueOf(transferSaveIdTemp));
				taskDao.delTaskTransferSaveStatus(Integer.valueOf(transferSaveIdTemp));
			}
			TaskTransferSave tts = new TaskTransferSave(userId, taskId, content, deadLine, attention, remark);
			taskDao.addTaskTransferSaveInfo(tts);
			int transferSaveId = tts.getId();
			List<TaskTransferUserSave> lttus = new ArrayList<TaskTransferUserSave>();
			List<TaskTransferAttachSave> lttas = new ArrayList<TaskTransferAttachSave>();
			if (userInfo != null) {
				for (Object o : userInfo) {
					JSONObject jo = (JSONObject) o;
					lttus.add(new TaskTransferUserSave(transferSaveId, jo.getIntValue("userId"),
							jo.getString("userName")));
				}
			}
			if (!"".equals(oriAttachStr)) {
				String[] oriAttachArr = oriAttachStr.split(",");
				for (String s : oriAttachArr) {
					lttas.add(new TaskTransferAttachSave(transferSaveId, s));
				}
			}
			for (MultipartFile mf : attachArr) {
				String name = mf.getOriginalFilename();
				mf.transferTo(new File(FilePath.taskAttachPath + name));
				lttas.add(new TaskTransferAttachSave(transferSaveId, name));
			}
			if (!lttus.isEmpty()) {
				taskDao.addTaskTransferUserSave(lttus);
			}
			if (!lttas.isEmpty()) {
				taskDao.addTaskTransferAttachSave(lttas);
			}
			taskDao.addTaskTransferSaveStatus(taskId, transferSaveId, userId);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public TaskTransferSave getTaskTransferSaveInfo(int transferSaveId) {

		TaskTransferSave tts = taskDao.getTaskTransferSaveInfo(transferSaveId);
		return tts;
	}

	public boolean addtransferTask(int userId, int taskId, String content, String deadLine, String attention,
			String remark, String oriAttachStr, MultipartFile[] attachArr, JSONArray userInfo) {

		try {

			// 1. 添加任务至t_task表中，并标明level和父任务Id
			int parentLevel = taskDao.getTaskLevel(taskId);
			int level = parentLevel + 1;
			List<TaskAttach> lta = new ArrayList<TaskAttach>();
			List<TaskUser> ltu = new ArrayList<TaskUser>();
			Task task = new Task(content, deadLine, level, taskId, attention, remark, userId,
					Utils.getNowDate("yyyy-MM-dd HH:mm"));
			taskDao.addTaskInfo(task);
			int sonId = task.getId(); // 获取 子任务Id
			if (!"".equals(oriAttachStr)) {
				String[] oriAttachArr = oriAttachStr.split(",");
				for (String s : oriAttachArr) {
					lta.add(new TaskAttach(sonId, s));
				}
			}
			for (MultipartFile mp : attachArr) {
				String name = System.currentTimeMillis() + "_" + mp.getOriginalFilename();
				mp.transferTo(new File(FilePath.taskAttachPath + name));
				lta.add(new TaskAttach(sonId, name));
			}
			if (userInfo != null) {
				for (Object o : userInfo) {
					JSONObject jo = (JSONObject) o;
					ltu.add(new TaskUser(sonId, jo.getIntValue("userId"), jo.getString("userName")));
				}
			}
			if (!lta.isEmpty()) {
				taskDao.addTaskAttachInfo(lta);
			}
			if (!ltu.isEmpty()) {
				taskDao.addTaskUserInfo(ltu);
			}
			// 2. 删除几张中间表中记录移交保存信息的数据
			String transferSaveId = taskDao.checkIfTransferSaveRecordExist(taskId, userId);
			if (transferSaveId != null) {
				taskDao.delTaskTransferSave(Integer.valueOf(transferSaveId));
				taskDao.delTaskTransferUserSave(Integer.valueOf(transferSaveId));
				taskDao.delTaskTransferAttachSave(Integer.valueOf(transferSaveId));
				taskDao.delTaskTransferSaveStatus(Integer.valueOf(transferSaveId));
			}
			// 3. 在t_task_user表中 记录 任务的sonId
			taskDao.recordTaskSonId(taskId, userId, sonId);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Task getTransferInfo(int taskId, int userId) {

		Integer sonId = taskDao.getSonId(taskId, userId);
		Task task = taskDao.getTaskInfo(sonId);
		return task;
	}

	public boolean cancelTransfer(int taskId, int userId) {

		// 1. 获取所有的子任务Id
		Integer sonId = taskDao.getSonId(taskId, userId);
		Set<Integer> set = new HashSet<Integer>();
		set.add(sonId);
		recursionSonTaskInfo(set, sonId);
		// 1. 删除t_task中移交的信息的记录
		List<Integer> list = new ArrayList<Integer>();
		for (Integer integer : set) {
			list.add(integer);
		}
		StringBuilder sb = new StringBuilder();
		sb.append(" in (");
		for (int i = 0; i < list.size(); i++) {
			if (i != list.size() - 1) {
				sb.append(list.get(i) + ",");
			} else {
				sb.append(list.get(i));
			}
		}
		sb.append(")");
		taskDao.batchDelTaskInfo(sb.toString());
		// 2. 删除t_task_user中isTransfer和sonId两个字段
		taskDao.delTaskUserTransferInfo(taskId, userId);
		return true;
	}

	/**
	 * 递归获取子任务信息
	 */
	public void recursionSonTaskInfo(Set<Integer> set, int taskId) {

		List<TaskUser> list = taskDao.getSonTaskInfo(taskId);
		for (TaskUser tu : list) {
			if (tu.getSonId() != 0) {
				set.add(tu.getTaskId());
				recursionSonTaskInfo(set, tu.getSonId());
			} else {
				set.add(tu.getTaskId());
			}
		}
	}

	public boolean finishTask(int taskId, int userId) {

		recursionFinishTask(taskId, userId);
		return true;
	}

	/**
	 * 递归完成任务
	 */
	public void recursionFinishTask(int taskId, int userId) {

		// 在t_task_user表中完成对应taskId和userId的这条任务
		taskDao.userFinishTask(taskId, userId);
		// 获取该taskId对应的所有用户的完成情况
		List<TaskUser> list = taskDao.checkIfAllUserFinish(taskId);
		if (list.isEmpty()) {
			// 若list为空，则说明该taskId对应的所有user都已完成，则t_task_user表中sonId为taskId的数据的isDone修改为yes
			TaskUser tu = taskDao.getTaskUserInfoBySonId(taskId);
			if (tu != null) {
				recursionFinishTask(tu.getTaskId(), tu.getUserId());
			}
		}
	}
}
