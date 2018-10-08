package dms.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;

import dms.entity.Task;
import dms.entity.TaskSave;
import dms.entity.TaskTransferSave;
import dms.entity.TaskTransferSaveStatus;
import dms.entity.UserInfo;

public interface TaskService {

	public List<UserInfo> getUserListBeforeAddTask();

	public boolean saveTaskInfo(int userId, String content, String deadLine, String attention, String remark,
			String oriAttach, MultipartFile[] attachArr, JSONArray userInfo);

	public String checkTaskSave(int userId);

	public TaskSave getSaveTaskInfo(int taskSaveId);

	public boolean addTaskInfo(String content, String deadLine, String attention, String remark, int creator,
			String oriAttach, MultipartFile[] attachArr, JSONArray userInfo);

	public PageInfo<Map<String, String>> getMyUnDoTaskList(int userId, int currentPage);

	public Task getTaskInfo(int taskId);

	public String judgeIfTaskTransfer(int taskId);

	public TaskTransferSaveStatus checkIfTaskTransferSave(int taskId);

	public boolean addTaskTransferSaveInfo(int taskId, String content, String deadLine, String attention, String remark,
			String oriAttachStr, MultipartFile[] attachArr, JSONArray userInfo);
	
	public TaskTransferSave getTaskTransferSaveInfo(int transferSaveId);
}
