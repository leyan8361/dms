package dms.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface MessageService {

	public boolean addMessageGroup(int creator, String groupName, int groupType, int effectiveDays,
			JSONArray userArray);
	
	public JSONObject getChatList(int userId);
	
	public boolean updateMessageGroupInfo(int groupId,String groupName,int[] delArr,JSONArray userArray);
	
	public int delMessageGroup(int groupId);
}
