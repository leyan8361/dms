package dms.service;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface MessageService {

	public boolean addMessageGroup(int creator, String groupName, int groupType, int effectiveDays,
			JSONArray userArray);

	public JSONObject getChatList(int userId);

	public boolean updateMessageGroupInfo(int groupId, String groupName, int[] delArr, JSONArray userArray);

	public int delMessageGroup(int groupId);

	public boolean sendMessage(int fromId,String userName, int toId, int isGroupMessage, String content, int type,
			MultipartHttpServletRequest mReq);
}
