package dms.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import dms.entity.UserInfo;
import dms.service.UserService;
import dms.utils.Constants;
import dms.utils.JwtManager;

@CrossOrigin
@RestController
@RequestMapping("user")
public class UserController {

	@Resource(name = "userService")
	private UserService userService;

	/**
	 * 验证用户登录
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @return
	 */
	@RequestMapping(value = "checkLogin", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String checkLogin(@RequestParam("userName") String userName, @RequestParam("password") String password) {

		Map<String, String> resMap = new HashMap<String, String>();
		UserInfo ui = userService.getUserInfo(userName, password);
		if (ui == null) {
			resMap.put("status", Constants.apiErrorStatus);
			resMap.put("info", "用户名或密码错误");
		} else {
			userService.updateUserLastLoginTime(ui.getId());
			String token = JwtManager.createToken(ui.getId(), ui.getUserName(), ui.getRoleId(), ui.getRoleName(),
					ui.getUserGroupId(), ui.getUserGroupName());
			resMap.put("status", Constants.successStatus);
			resMap.put("info", "登陆成功");
			resMap.put("token", token);
			resMap.put("userName", userName);
			resMap.put("userId", String.valueOf(ui.getId()));
		}
		return JSON.toJSONString(resMap);
	}

}
