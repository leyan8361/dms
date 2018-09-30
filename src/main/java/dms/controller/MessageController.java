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
	 * ������ϢȺ
	 * 
	 * @param groupName
	 *            Ⱥ����
	 * @param groupType
	 *            Ⱥ������ 0����ϢȺ 1����ʱȺ
	 * @param effectiveDays
	 *            ��������(��Ϊ��ϢȺ �򷵻�0)
	 * @param userArray
	 *            ��ӵ���Ա��Ϣ [{"userId":"","userName":""},{..},..]
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
		sysService.addLog(new Log(Utils.getNowDate("yyyy-MM-dd HH:mm"), userId, userName, "������ϢȺ:" + groupName));
		resMap.put("status", Constants.successStatus);
		resMap.put("info", "�����ɹ�");
		return JSON.toJSONString(resMap);
	}

	/**
	 * ���������б�
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
	 *            Ⱥ��Id
	 * @param groupName
	 *            Ⱥ����
	 * @param delStr
	 *            ɾ������Աid��Ϣ ��"1,2,3,4,5"
	 * @param userArray
	 *            ��ӵ���Ա��Ϣ [{"userId":"","userName":""},{..},..]
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
				user.getString("userName"), "�޸�����ϢȺ�飺" + groupName + "����Ϣ"));
		resMap.put("status", Constants.successStatus);
		resMap.put("info", "success");
		return JSON.toJSONString(resMap);
	}

	/**
	 * ɾ����ϢȺ
	 * 
	 * @param groupId
	 *            ��ϢȺId
	 * @param groupName
	 *            ��ϢȺ����
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
				user.getString("userName"), "ɾ������ϢȺ�飺" + groupName));
		resMap.put("status", Constants.successStatus);
		resMap.put("info", "success");
		return JSON.toJSONString(resMap);
	}

	/**
	 * ������Ϣ
	 * 
	 * @param toId
	 *            ������Ϣ���û�Id��Ⱥ��Id(���Ƿ��������� ��Ϊ0)
	 * @param isGroupMessage
	 *            �Ƿ�ΪȺ����Ϣ 0�� �� 1��
	 * @param content
	 *            ��Ϣ����(��Ϊ�ļ�������Ϊ"")
	 * @param type
	 *            �Ƿ���������Ϣ 0�ǡ�1��
	 * @param flag
	 *            �ļ�����Ϣ(ʱ���_�ļ��� ������ͨ��Ϣ��Ϊ"")
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
	 * ��ȡ�����¼
	 * 
	 * @param aimId
	 *            Ŀ��Id
	 * @param isGroupMessage
	 *            �Ƿ���Ⱥ��Ϣ(0���� �� 1����)
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
