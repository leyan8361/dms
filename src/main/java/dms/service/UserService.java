package dms.service;

import dms.entity.UserInfo;

public interface UserService {

	public UserInfo getUserInfo(String userName,String password);
	
	public int updateUserLastLoginTime(int userId);
}
