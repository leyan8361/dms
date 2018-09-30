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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import dms.entity.Log;
import dms.service.MessageService;
import dms.service.SysService;
import dms.socket.WebSocket;
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
		messageService.addMessageGroup(userId, userName, groupName, groupType, effectiveDays,
				JSONArray.parseArray(userArray));
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd HH:mm"), userId, userName, "新增消息群:" + groupName));
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

	/**
	 * 
	 * @param groupId
	 *            群组Id
	 * @param groupName
	 *            群组名
	 * @param delStr
	 *            删除的人员id信息 如"1,2,3,4,5"
	 * @param userArray
	 *            添加的人员信息 [{"userId":"","userName":""},{..},..]
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "updateMessageGroupInfo", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String updateMessageGroupInfo(@RequestParam("groupId") int groupId,
			@RequestParam("groupName") String groupName, @RequestParam("delStr") String delStr,
			@RequestParam("userArray") String userArray, HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		int[] delArr = {};
		if (!"".equals(delStr)) {
			delArr = Utils.transStrToIntArr(delStr);
		}
		JSONObject user = (JSONObject) req.getAttribute("user");
		messageService.updateMessageGroupInfo(user.getString("userName"), groupId, groupName, delArr,
				JSONArray.parseArray(userArray));
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd HH:mm"), user.getIntValue("userId"),
				user.getString("userName"), "修改了消息群组：" + groupName + "的信息"));
		resMap.put("status", Constants.successStatus);
		resMap.put("info", "success");
		return JSON.toJSONString(resMap);
	}

	/**
	 * 删除消息群
	 * 
	 * @param groupId
	 *            消息群Id
	 * @param groupName
	 *            消息群名称
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "delMessageGroup", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String delMessageGroup(@RequestParam("groupId") int groupId, @RequestParam("groupName") String groupName,
			HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		JSONObject user = (JSONObject) req.getAttribute("user");
		messageService.delMessageGroup(user.getString("userId"), user.getString("userName"), groupId, groupName);
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd HH:mm"), user.getIntValue("userId"),
				user.getString("userName"), "删除了消息群组：" + groupName));
		resMap.put("status", Constants.successStatus);
		resMap.put("info", "success");
		return JSON.toJSONString(resMap);
	}

	/**
	 * 发送消息
	 * 
	 * @param toId
	 *            接受消息的用户Id或群组Id(若是发给所有人 则为0)
	 * @param isGroupMessage
	 *            是否为群组消息 0是 、 1否
	 * @param content
	 *            消息内容(若为文件类型则为"")
	 * @param type
	 *            是否是文字消息 0是、1否
	 * @param flag
	 *            文件名信息(时间戳_文件名 若是普通信息则为"")
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "sendMessage", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String sendMessage(@RequestParam("toId") int toId, @RequestParam("isGroupMessage") int isGroupMessage,
			@RequestParam("content") String content, @RequestParam("type") int type, @RequestParam("flag") String flag,
			HttpServletRequest req, MultipartHttpServletRequest mReq) {

		Map<String, String> resMap = new HashMap<String, String>();
		JSONObject user = (JSONObject) req.getAttribute("user");
		messageService.sendMessage(user.getIntValue("userId"), user.getString("userName"), toId, isGroupMessage,
				content, type, mReq);
		JSONObject jo2 = new JSONObject();
		jo2.put("status", Constants.noticeFIleUploadStatus);
		jo2.put("info", flag);
		WebSocket.sendMessageToUser2(user.getString("userId"), JSON.toJSONString(jo2));
		resMap.put("status", Constants.successStatus);
		resMap.put("info", flag);
		return JSON.toJSONString(resMap);
	}

	/**
	 * 获取聊天记录
	 * 
	 * @param aimId
	 *            目标Id
	 * @param isGroupMessage
	 *            是否是群消息(0：是 、 1：否)
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "getChatRecord", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
	public String getChatRecord(@RequestParam("aimId") int aimId, @RequestParam("isGroupMessage") int isGroupMessage,
			HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		JSONObject user = (JSONObject) req.getAttribute("user");
		JSONArray ja = messageService.getChatRecord(user.getIntValue("userId"), aimId, isGroupMessage);
		resMap.put("status", Constants.successStatus);
		resMap.put("info", JSON.toJSONString(ja));
		return JSON.toJSONString(resMap);
	}
}
