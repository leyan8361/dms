package dms.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import dms.utils.Utils;

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
	 * @throws ParseException
	 */
	@RequestMapping(value = "checkLogin", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String checkLogin(@RequestParam("userName") String userName, @RequestParam("password") String password)
			throws ParseException {

		Map<String, String> resMap = new HashMap<String, String>();
		UserInfo ui = userService.getUserInfo(userName, password);
		if (ui == null) {
			resMap.put("status", Constants.apiErrorStatus);
			resMap.put("info", "用户名或密码错误");
		} else if (ui.getLastDate() != null) {
			// String nowDate = Utils.getNowDate("yyyy-MM-dd");
			String lastDate = ui.getLastDate();
			String nowDate = Utils.getNowDate("yyyy-MM-dd");
			Date nowDate_tmp = new SimpleDateFormat("yyyy-MM-dd").parse(nowDate);
			Date lastDate_tmp = new SimpleDateFormat("yyyy-MM-dd").parse(lastDate);
			if (nowDate_tmp.getTime() > lastDate_tmp.getTime()) {
				resMap.put("status", Constants.apiErrorStatus);
				resMap.put("info", "用户到期");
			} else {
				userService.updateUserLastLoginTime(ui.getId());
				String token = JwtManager.createToken(ui.getId(), ui.getUserName(), ui.getRoleId(), ui.getRoleName(),
						ui.getUserGroupId(), ui.getUserGroupName());
				resMap.put("status", Constants.successStatus);
				resMap.put("info", "登陆成功");
				resMap.put("token", token);
				resMap.put("userName", userName);
				resMap.put("userId", String.valueOf(ui.getId()));
				resMap.put("roleName", ui.getRoleName() == null ? "" : ui.getRoleName());
			}

		} else {
			userService.updateUserLastLoginTime(ui.getId());
			String token = JwtManager.createToken(ui.getId(), ui.getUserName(), ui.getRoleId(), ui.getRoleName(),
					ui.getUserGroupId(), ui.getUserGroupName());
			resMap.put("status", Constants.successStatus);
			resMap.put("info", "登陆成功");
			resMap.put("token", token);
			resMap.put("userName", userName);
			resMap.put("userId", String.valueOf(ui.getId()));
			resMap.put("roleName", ui.getRoleName() == null ? "" : ui.getRoleName());
		}
		return JSON.toJSONString(resMap);
	}

	// /**
	// * 获取版本号
	// *
	// * @return
	// */
	// @RequestMapping(value = "getVersion", method = RequestMethod.GET, produces =
	// "text/json;charset=UTF-8")
	// public String getVersion() {
	//
	// MavenXpp3Reader reader = new MavenXpp3Reader();
	// File f = new File(UserController.class.getResource("/").getPath());
	// String path = f.getParentFile().getParent();
	// System.out.println(path);
	// String myPom = path + File.separator + "pom.xml";
	// try {
	// Model model = reader.read(new FileReader(myPom));
	// return model.getVersion();
	// } catch (IOException | XmlPullParserException e) {
	// return e.getMessage();
	// }
	// }
}
