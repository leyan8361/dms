package dms.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

			// 1. ���������t_task���У�������level�͸�����Id
			int parentLevel = taskDao.getTaskLevel(taskId);
			int level = parentLevel + 1;
			List<TaskAttach> lta = new ArrayList<TaskAttach>();
			List<TaskUser> ltu = new ArrayList<TaskUser>();
			Task task = new Task(content, deadLine, level, taskId, attention, remark, userId,
					Utils.getNowDate("yyyy-MM-dd HH:mm"));
			taskDao.addTaskInfo(task);
			int sonId = task.getId(); // ��ȡ ������Id
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
			// 2. ɾ�������м���м�¼�ƽ�������Ϣ������
			String transferSaveId = taskDao.checkIfTransferSaveRecordExist(taskId, userId);
			if (transferSaveId != null) {
				taskDao.delTaskTransferSave(Integer.valueOf(transferSaveId));
				taskDao.delTaskTransferUserSave(Integer.valueOf(transferSaveId));
				taskDao.delTaskTransferAttachSave(Integer.valueOf(transferSaveId));
				taskDao.delTaskTransferSaveStatus(Integer.valueOf(transferSaveId));
			}
			// 3. ��t_task_user���� ��¼ �����sonId
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

		// 1. ��ȡ���е�������Id
		Integer sonId = taskDao.getSonId(taskId, userId);
		Set<Integer> set = new HashSet<Integer>();
		set.add(sonId);
		recursionSonTaskInfo(set, sonId);
		// 1. ɾ��t_task���ƽ�����Ϣ�ļ�¼
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
		// 2. ɾ��t_task_user��isTransfer��sonId�����ֶ�
		taskDao.delTaskUserTransferInfo(taskId, userId);
		return true;
	}

	/**
	 * �ݹ��ȡ��������Ϣ
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
	 * �ݹ��������
	 */
	public void recursionFinishTask(int taskId, int userId) {

		// ��t_task_user������ɶ�ӦtaskId��userId����������
		taskDao.userFinishTask(taskId, userId);
		// ��ȡ��taskId��Ӧ�������û���������
		List<TaskUser> list = taskDao.checkIfAllUserFinish(taskId);
		if (list.isEmpty()) {
			taskDao.finishTask(taskId);
			// ��listΪ�գ���˵����taskId��Ӧ������user������ɣ���t_task_user����sonIdΪtaskId�����ݵ�isDone�޸�Ϊyes
			TaskUser tu = taskDao.getTaskUserInfoBySonId(taskId);
			if (tu != null) {
				recursionFinishTask(tu.getTaskId(), tu.getUserId());
			}
		}
	}

	public PageInfo<Task> getReleaseTaskList(int currentPage, int userId, String content, String deadLine,
			String deadLine2, String isDone, String createDate, String createDate2) {

		PageHelper.startPage(currentPage, 10000);
		List<Task> list = taskDao.getReleaseTaskList(userId, content, deadLine, deadLine2, isDone, createDate,
				createDate2);
		PageInfo<Task> planList = new PageInfo<>(list);
		return planList;
	}

	public boolean updateTaskInfo(int taskId, String content, String deadLine, String attention, String remark,
			String delAttachStr, MultipartFile[] attachArr) {

		try {
			List<TaskAttach> lta = new ArrayList<TaskAttach>();
			taskDao.updateTaskInfo(taskId, content, deadLine, attention, remark);
			String delStr = " < 0";
			if (!"".equals(delAttachStr)) {
				String[] delAttachArr = delAttachStr.split(",");
				StringBuilder sb = new StringBuilder();
				sb.append(" in (");
				for (int i = 0; i < delAttachArr.length; i++) {
					if (i != (delAttachArr.length - 1)) {
						sb.append(delAttachArr[i] + " ,");
					} else {
						sb.append(delAttachArr[i]);
					}
				}
				sb.append(")");
				delStr = sb.toString();
			}
			taskDao.delTaskAttachInfo(delStr);
			for (MultipartFile mf : attachArr) {
				String name = System.currentTimeMillis() + "_" + mf.getOriginalFilename();
				mf.transferTo(new File(FilePath.taskAttachPath + name));
				lta.add(new TaskAttach(taskId, name));
			}
			if (!lta.isEmpty()) {
				taskDao.addTaskAttachInfo(lta);
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public JSONArray getTaskProcess(int taskId) {

		JSONArray res = new JSONArray();
		// ��¼����Id�Լ�����level
		JSONObject taskInfo = new JSONObject();
		// ��ȡ��ǰ����ļ���
		int level = taskDao.getTaskLevel(taskId);
		// String creatorName = taskDao.getCreatorName(taskId);

		List<Integer> list2 = new ArrayList<Integer>();

		// int oriId = taskId;
		recursionGetParentInfo(list2, taskId, level);
		int oriId_temp;
		if (!list2.isEmpty()) {
			oriId_temp = list2.get(list2.size() - 1);
		} else {
			oriId_temp = taskId;
		}

		int level2 = taskDao.getTaskLevel(oriId_temp);
		String creatorName2 = taskDao.getCreatorName(oriId_temp);
		taskInfo.put("level", level2);
		taskInfo.put("id", oriId_temp);
		taskInfo.put("creatorName", creatorName2);
		res.add(taskInfo);

		recursionGetSonInfo(res, oriId_temp);
		// ��ʼ�����ص�jsonarray
		JSONArray res2 = new JSONArray();
		for (Object object : res) {
			JSONObject jo = (JSONObject) object;
			JSONObject jo2 = new JSONObject();
			jo2.put("level", jo.getIntValue("level"));
			jo2.put("taskId", jo.getIntValue("id"));
			jo2.put("creator", jo.getString("creatorName"));
			jo2.put("userInfo", new JSONArray());
			int taskId_tmp = jo.getIntValue("id");
			List<TaskUser> list_tmp = taskDao.getUserAndIsDone(taskId_tmp);
			for (TaskUser tu : list_tmp) {
				JSONObject jo3 = new JSONObject();
				jo3.put("userName", tu.getUserName());
				jo3.put("isDone", tu.getIsDone());
				jo2.getJSONArray("userInfo").add(jo3);
			}
			res2.add(jo2);
		}
		// ��������jsonArray
		List<JSONObject> list = JSONArray.parseArray(res2.toJSONString(), JSONObject.class);
		Collections.sort(list, new Comparator<JSONObject>() {

			public int compare(JSONObject o1, JSONObject o2) {
				int a = o1.getIntValue("level");
				int b = o2.getIntValue("level");
				if (a > b) {
					return 1;
				} else if (a == b) {
					return 0;
				} else
					return -1;

			}
		});
		JSONArray jsonArray = JSONArray.parseArray(list.toString());
		return jsonArray;
	}

	// �ݹ��ѯ��������Ϣ
	public void recursionGetParentInfo(List<Integer> list, int taskId, int level) {

		if (level != 0) {
			int parentId = taskDao.getParentId(taskId);
			int parentLevel = taskDao.getTaskLevel(parentId);
			// String parentCreatorName = taskDao.getCreatorName(parentId);
			list.add(parentId);
			// JSONObject taskInfo = new JSONObject();
			// taskInfo.put("level", parentLevel);
			// taskInfo.put("id", parentId);
			// taskInfo.put("creatorName", parentCreatorName);
			// res.add(taskInfo);
			recursionGetParentInfo(list, parentId, parentLevel);
		}
	}

	// �ݹ��ѯ��������Ϣ
	public void recursionGetSonInfo(JSONArray res, int taskId) {

		List<Task> ltask = taskDao.getSonTaskId(taskId);
		if (!ltask.isEmpty()) {
			for (Task task : ltask) {
				int taskId_tmp = task.getId();
				int level_tmp = task.getLevel();
				String parentCreatorName = taskDao.getCreatorName(taskId_tmp);
				JSONObject taskInfo = new JSONObject();
				taskInfo.put("level", level_tmp);
				taskInfo.put("id", taskId_tmp);
				taskInfo.put("creatorName", parentCreatorName);
				res.add(taskInfo);
				recursionGetSonInfo(res, taskId_tmp);
			}
		}
	}
}
