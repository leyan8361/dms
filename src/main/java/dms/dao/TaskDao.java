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
	
	public String judgeIfTaskTransfer(int taskId);
	
	public TaskTransferSaveStatus checkIfTaskTransferSave(int taskId);
	
	public int addTaskTransferSaveInfo(TaskTransferSave tts);
	
	public int addTaskTransferUserSave(List<TaskTransferUserSave> lttus);
	
	public int addTaskTransferAttachSave(List<TaskTransferAttachSave> lttas);
	
}
