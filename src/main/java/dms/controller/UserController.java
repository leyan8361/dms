package dms.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import dms.entity.UserInfo;
import dms.service.UserService;
import dms.utils.JwtManager;

@CrossOrigin
@RestController
@RequestMapping("user")
public class UserController {

	@Resource(name = "userService")
	private UserService userService;

	/**
	 * ��֤�û���¼
	 * 
	 * @param userName
	 *            �û���
	 * @param password
	 *            ����
	 * @return
	 */
	@RequestMapping(value = "checkLogin", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String checkLogin(@RequestParam("userName") String userName, @RequestParam("password") String password) {

		Map<String, String> resMap = new HashMap<String, String>();
		UserInfo ui = userService.getUserInfo(userName, password);
		if (ui == null) {
			resMap.put("status", "error");
			resMap.put("info", "�û������������");
		} else {
			userService.updateUserLastLoginTime(ui.getId());
			String token = JwtManager.createToken(ui.getId(), ui.getUserName(), ui.getRoleId(), ui.getRoleName(),
					ui.getUserGroupId(), ui.getUserGroupName());
			resMap.put("status", "success");
			resMap.put("info", "��½�ɹ�");
			resMap.put("token", token);
		}
		return JSON.toJSONString(resMap);
	}

	@RequestMapping(value = "test", method = RequestMethod.POST, produces = "text/text;charset=UTF-8")
	public String test(HttpServletRequest req) {

		System.out.println(req.getAttribute("jwtCheck"));
		return "1";
	}
}
