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
}
