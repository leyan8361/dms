package dms.service;

import dms.entity.UserFunction;
import dms.entity.UserInfo;

public interface UserService {

	public UserInfo getUserInfo(String userName, String password);

	public int updateUserLastLoginTime(int userId);

	public UserFunction checkIfUserContainsFunction(int userId, String functionName);
}
