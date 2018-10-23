package dms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

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

public interface TaskDao {

	public List<UserInfo> getUserListBeforeAddTask();

	public int addTaskSaveInfo(TaskSave ts);

	public int addTaskSaveAttachInfo(List<TaskSaveAttach> ltsa);

	public int addTaskSaveUserInfo(List<TaskSaveUser> ltsu);

	public int updateSaveAddStatus(@Param("userId") int userId, @Param("addId") int addId);

	public String checkTaskSave(int userId);

	public TaskSave getSaveTaskInfo(int taskSaveId);

	public int addTaskInfo(Task task);

	public int addTaskAttachInfo(List<TaskAttach> lta);

	public int addTaskUserInfo(List<TaskUser> ltu);

	public int updateSaveAddStatusAfterPublishTask(int userId);

	public List<Map<String, String>> getMyUnDoTaskList(int userId);

	public Task getTaskInfo(int taskId);

	public String judgeIfTaskTransfer(@Param("taskId") int taskId, @Param("userId") int userId);

	public TaskTransferSaveStatus checkIfTaskTransferSave(@Param("taskId") int taskId, @Param("userId") int userId);

	public String getUserSaveTransferId(@Param("taskId") int taskId, @Param("userId") int userId);

	public int delTaskTransferSave(int transferSaveId);

	public int delTaskTransferUserSave(int transferSaveId);

	public int delTaskTransferAttachSave(int transferSaveId);

	public int delTaskTransferSaveStatus(int transferSaveId);

	public int addTaskTransferSaveInfo(TaskTransferSave tts);

	public int addTaskTransferUserSave(List<TaskTransferUserSave> lttus);

	public int addTaskTransferAttachSave(List<TaskTransferAttachSave> lttas);

	public int addTaskTransferSaveStatus(@Param("taskId") int taskId, @Param("transferSaveId") int transferSaveId,
			@Param("userId") int userId);

	public TaskTransferSave getTaskTransferSaveInfo(int transferSaveId);

	public int getTaskLevel(int taskId);

	public String checkIfTransferSaveRecordExist(@Param("taskId") int taskId, @Param("userId") int userId);

	public int recordTaskSonId(@Param("taskId") int taskId, @Param("userId") int userId, @Param("sonId") int sonId);

	public Integer getSonId(@Param("taskId") int taskId, @Param("userId") int userId);

	public List<TaskUser> getSonTaskInfo(int taskId);

	public int batchDelTaskInfo(@Param("idStr") String idStr);

	public int delTaskUserTransferInfo(@Param("taskId") int taskId, @Param("userId") int userId);

	public int userFinishTask(@Param("taskId") int taskId, @Param("userId") int userId);

	public List<TaskUser> checkIfAllUserFinish(int taskId);

	public int finishTask(int taskId);

	public TaskUser getTaskUserInfoBySonId(int sonId);

	public List<Task> getReleaseTaskList(@Param("userId") int userId, @Param("content") String content,
			@Param("deadLine") String deadLine, @Param("deadLine2") String deadLine2, @Param("isDone") String isDone,
			@Param("createDate") String createDate, @Param("createDate2") String createDate2);

	public int updateTaskInfo(@Param("taskId") int taskId, @Param("content") String content,
			@Param("deadLine") String deadLine, @Param("attention") String attention, @Param("remark") String remark);

	public int delTaskAttachInfo(@Param("delStr") String delStr);
	
	public int getParentId(int taskId);
	
	public List<Task> getSonTaskId(int taskId);
	
	public List<TaskUser> getUserAndIsDone(int taskId);
	
	public String getCreatorName(int taskId);
}
