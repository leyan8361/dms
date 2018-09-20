package dms.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import dms.dao.MessageDao;
import dms.entity.Message;
import dms.entity.MessageGroup;
import dms.entity.MessageGroupDetail;
import dms.entity.UserInfo;
import dms.service.MessageService;
import dms.socket.WebSocket;
import dms.utils.Constants;
import dms.utils.FilePath;
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

	public boolean updateMessageGroupInfo(int groupId, String groupName, int[] delArr, JSONArray userArray) {

		messageDao.updateMessageGroupInfo(groupId, groupName);
		String delStr = "";
		if (delArr.length == 0) {
			delStr = " < 0";
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(" in (");
			for (int i = 0; i < delArr.length; i++) {
				if (i == 0) {
					sb.append(delArr[i]);
				} else {
					sb.append("," + delArr[i]);
				}
			}
			sb.append(")");
			delStr = sb.toString();
		}
		messageDao.delGroupUserInfo(groupId, delStr);
		List<MessageGroupDetail> lmgd = new ArrayList<MessageGroupDetail>();
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

	public int delMessageGroup(int groupId) {

		return messageDao.delMessageGroup(groupId);
	}

	public boolean sendMessage(int fromId, String userName, int toId, int isGroupMessage, String content, int type,
			MultipartHttpServletRequest mReq) {

		try {
			String trueFileSize = "";
			// 判断是文件还是消息
			if (type != 0) {
				MultipartFile file = mReq.getFileMap().get("file");
				Long fileSize = file.getSize();
				DecimalFormat df = new DecimalFormat("#.00");
				trueFileSize = df.format((double) fileSize / 1048576) + "M";
				content = System.currentTimeMillis() + "_" + file.getOriginalFilename();
				file.transferTo(new File(FilePath.messageAttachPath + content));
			}
			Message message = new Message(fromId, toId, isGroupMessage, content, type,
					Utils.getNowDate("yyyy-MM-dd hh:mm:ss"), trueFileSize);
			messageDao.sendMessage(message);
			if (isGroupMessage == 1) {
				JSONObject jo = new JSONObject();
				jo.put("info", "用户：" + userName + " 向你发送了一条新信息");
				jo.put("status", Constants.sendStatus);
				WebSocket.sendMessageToUser2(String.valueOf(toId), JSON.toJSONString(jo));
				JSONObject jo2 = new JSONObject();
				jo2.put("status", Constants.refreshStatus);
				jo2.put("type", "user");
				jo2.put("id", fromId);
				WebSocket.sendMessageToUser2(String.valueOf(toId), JSON.toJSONString(jo2));
			} else {
				if (toId != 0) {
					List<String> userIdList = messageDao.getGroupMembers(toId);
					JSONObject jo = new JSONObject();
					jo.put("status", Constants.refreshStatus);
					jo.put("type", "group");
					jo.put("id", toId);
					WebSocket.sendMessageToUser3(userIdList, JSON.toJSONString(jo));
				} else {
					List<String> userIdList = new ArrayList<String>();
					List<UserInfo> list = messageDao.getAllUserInfo();
					for (UserInfo userInfo : list) {
						userIdList.add(String.valueOf(userInfo.getId()));
					}
					JSONObject jo = new JSONObject();
					jo.put("status", Constants.refreshStatus);
					jo.put("type", "group");
					jo.put("id", 0);
					WebSocket.sendMessageToUser3(userIdList, JSON.toJSONString(jo));
				}
			}

			return true;
		} catch (IOException e) {

			e.printStackTrace();
			return false;
		}
	}
}
