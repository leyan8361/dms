package dms.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import dms.dao.MessageDao;
import dms.entity.MessageGroup;
import dms.entity.MessageGroupDetail;
import dms.entity.UserInfo;
import dms.service.MessageService;
import dms.socket.WebSocket;
import dms.utils.Utils;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageDao messageDao;

	public boolean addMessageGroup(int creator, String groupName, int groupType, int effectiveDays,
			JSONArray userArray) {

		MessageGroup mg = new MessageGroup(groupName, creator, Utils.getNowDate("yyyy-MM-dd"), effectiveDays,
				groupType);
		List<MessageGroupDetail> lmgd = new ArrayList<MessageGroupDetail>();
		messageDao.addMessageGroup(mg);
		int groupId = mg.getId();
		if (userArray != null) {
			for (Object o : userArray) {
				JSONObject jo = (JSONObject) o;
				int userId = jo.getIntValue("userId");
				String userName = jo.getString("userName");
				lmgd.add(new MessageGroupDetail(groupId, userId, userName));
			}
		}
		if (!lmgd.isEmpty()) {
			messageDao.addMessageGroupDetail(lmgd);
		}
		return true;
	}

	public JSONObject getChatList(int userId2) {
		JSONObject resObj = new JSONObject();
		List<UserInfo> list = messageDao.getAllUserInfo();
		List<MessageGroup> list2 = messageDao.getAllMessageGroupInfo();
		resObj.put("totalNum", list.size() - 1);
		JSONArray messageGroupList = new JSONArray();
		JSONArray tempGroupList = new JSONArray();
		JSONArray userList = new JSONArray();
		JSONArray onlineUserList = new JSONArray();
		for (MessageGroup mg : list2) {
			if (mg.getType() == 0) {
				JSONObject jo = new JSONObject();
				jo.put("groupId", mg.getId());
				jo.put("groupName", mg.getName());
				jo.put("count", mg.getCount());
				JSONArray userArray = new JSONArray();
				List<MessageGroupDetail> lmgd = mg.getLmgd();
				if (!lmgd.isEmpty()) {
					for (MessageGroupDetail messageGroupDetail : lmgd) {
						JSONObject jo2 = new JSONObject();
						jo2.put("userId", messageGroupDetail.getUserId());
						jo2.put("userName", messageGroupDetail.getUserName());
						userArray.add(jo2);
					}
				}
				jo.put("userInfo", userArray);
				messageGroupList.add(jo);
			} else {
				if (Utils.judgeIsOverTime(mg.getCreateDate(), mg.getEffectiveDays())) {
					JSONObject jo = new JSONObject();
					jo.put("groupId", mg.getId());
					jo.put("groupName", mg.getName());
					jo.put("count", mg.getCount());
					JSONArray userArray = new JSONArray();
					List<MessageGroupDetail> lmgd = mg.getLmgd();
					if (!lmgd.isEmpty()) {
						for (MessageGroupDetail messageGroupDetail : lmgd) {
							JSONObject jo2 = new JSONObject();
							jo2.put("userId", messageGroupDetail.getUserId());
							jo2.put("userName", messageGroupDetail.getUserName());
							userArray.add(jo2);
						}
					}
					jo.put("userInfo", userArray);
					tempGroupList.add(jo);
				}
			}
		}
		for (UserInfo ui : list) {
			if (ui.getId() != userId2) {
				JSONObject jo = new JSONObject();
				jo.put("userId", ui.getId());
				jo.put("userName", ui.getUserName());
				userList.add(jo);
			}
		}
		for (String userId : WebSocket.userSocket.keySet()) {
			if (Integer.valueOf(userId) != userId2) {
				JSONObject jo = new JSONObject();
				jo.put("userId", userId);
				onlineUserList.add(jo);
			}
		}
		resObj.put("messageGroupList", messageGroupList);
		resObj.put("tempGroupList", tempGroupList);
		resObj.put("userList", userList);
		resObj.put("onlineUserList", onlineUserList);
		return resObj;
	}
}
