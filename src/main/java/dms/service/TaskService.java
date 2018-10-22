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

	public String judgeIfTaskTransfer(int taskId, int userId);

	public TaskTransferSaveStatus checkIfTaskTransferSave(int taskId, int userId);

	public boolean addTaskTransferSaveInfo(int userId, int taskId, String content, String deadLine, String attention,
			String remark, String oriAttachStr, MultipartFile[] attachArr, JSONArray userInfo);

	public TaskTransferSave getTaskTransferSaveInfo(int transferSaveId);

	public boolean addtransferTask(int userId, int taskId, String content, String deadLine, String attention,
			String remark, String oriAttachStr, MultipartFile[] attachArr, JSONArray userInfo);

	public Task getTransferInfo(int taskId, int userId);

	public boolean cancelTransfer(int taskId, int userId);

	public boolean finishTask(int taskId, int userId);

	public PageInfo<Task> getReleaseTaskList(int currentPage, int userId, String content, String deadLine,
			String deadLine2, String isDone, String createDate, String createDate2);
}
