package dms.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dms.dao.UserDao;
import dms.entity.UserInfo;
import dms.service.UserService;
import dms.utils.Utils;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	public UserInfo getUserInfo(String userName, String password) {

		return userDao.getUserInfo(userName, password);
	}

	public int updateUserLastLoginTime(int userId) {

		return userDao.updateUserLastLoginTime(userId, Utils.getNowDate("yyyy-MM-dd hh:mm:ss"));
	}
}
