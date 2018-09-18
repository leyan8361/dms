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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import dms.entity.Log;
import dms.service.MessageService;
import dms.service.SysService;
import dms.utils.Constants;
import dms.utils.Utils;

@RestController
@CrossOrigin
@RequestMapping("message")
public class MessageController {

	@Resource(name = "messageService")
	private MessageService messageService;

	@Resource(name = "sysService")
	private SysService sysService;

	/**
	 * 新增消息群
	 * 
	 * @param groupName
	 *            群组名
	 * @param groupType
	 *            群组类型 0：消息群 1：临时群
	 * @param effectiveDays
	 *            保留天数(若为消息群 则返回0)
	 * @param userArray
	 *            添加的人员信息 [{"userId":"","userName":""},{..},..]
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "addMessageGroup", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String addMessageGroup(@RequestParam("groupName") String groupName, @RequestParam("groupType") int groupType,
			@RequestParam("effectiveDays") int effectiveDays, String userArray, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		JSONObject jo = (JSONObject) req.getAttribute("user");
		int userId = jo.getIntValue("userId");
		String userName = jo.getString("userName");
		messageService.addMessageGroup(userId, groupName, groupType, effectiveDays, JSONArray.parseArray(userArray));
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd hh:mm"), userId, userName, "新增消息群:" + groupName));
		resMap.put("status", Constants.successStatus);
		resMap.put("info", "新增成功");
		return JSON.toJSONString(resMap);
	}

	/**
	 * 加载聊天列表
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "getChatList", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getChatList(HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		JSONObject user = (JSONObject) req.getAttribute("user");
		int userId = user.getIntValue("userId");
		JSONObject jo = messageService.getChatList(userId);
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(jo));
		return JSON.toJSONString(resMap);
	}
}
