package dms.dao;

import java.util.List;

import dms.entity.MessageGroup;
import dms.entity.MessageGroupDetail;
import dms.entity.UserInfo;

public interface MessageDao {

	public int addMessageGroup(MessageGroup mg);
	
	public int addMessageGroupDetail(List<MessageGroupDetail> lmgd);
	
	public List<UserInfo> getAllUserInfo();
	
	public List<MessageGroup> getAllMessageGroupInfo();
}
