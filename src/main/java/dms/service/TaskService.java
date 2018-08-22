package dms.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;

import dms.entity.UserInfo;

public interface TaskService {

	public List<UserInfo> getUserListBeforeAddTask();

	public boolean saveTaskInfo(int userId,  String content, String deadLine, String attention,
			String remark, String oriAttach, MultipartFile[] attachArr,JSONArray userInfo);
	
	public int checkTaskSave(int userId);
}
