package dms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import dms.entity.TaskSave;
import dms.entity.TaskSaveAttach;
import dms.entity.TaskSaveUser;
import dms.entity.UserInfo;

public interface TaskDao {

	public List<UserInfo> getUserListBeforeAddTask();

	public int addTaskSaveInfo(TaskSave ts);

	public int addTaskSaveAttachInfo(List<TaskSaveAttach> ltsa);

	public int addTaskSaveUserInfo(List<TaskSaveUser> ltsu);

	public int updateSaveAddStatus(@Param("userId") int userId, @Param("addId") int addId);

	public int checkTaskSave(int userId);

	public TaskSave getSaveTaskInfo(int taskSavedId);
}
