package dms.dao;

import org.apache.ibatis.annotations.Param;

import dms.entity.UserInfo;

public interface UserDao {

	public UserInfo getUserInfo(@Param("userName") String userName, @Param("password") String password);

	public int updateUserLastLoginTime(@Param("userId") int userId, @Param("nowDate") String nowDate);
}
