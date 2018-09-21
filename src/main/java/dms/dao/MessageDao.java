package dms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import dms.entity.Message;
import dms.entity.MessageGroup;
import dms.entity.MessageGroupDetail;
import dms.entity.UserInfo;

public interface MessageDao {

	public int addMessageGroup(MessageGroup mg);

	public int addMessageGroupDetail(List<MessageGroupDetail> lmgd);

	public List<UserInfo> getAllUserInfo();

	public List<MessageGroup> getAllMessageGroupInfo();

	public int updateMessageGroupInfo(@Param("groupId") int groupId, @Param("groupName") String groupName);

	public int delGroupUserInfo(@Param("groupId") int groupId, @Param("delStr") String delStr);

	public int delMessageGroup(int groupId);

	public int sendMessage(Message message);

	public List<String> getGroupMembers(int groupId);
}
