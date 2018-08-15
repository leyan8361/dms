package dms.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

@CrossOrigin
@RestController
@RequestMapping("error")
public class ErrorController {

	/**
	 * ·µ»Ø´íÎó½Ó¿Ú
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping("/")
	public String returnErrorMsg(HttpServletRequest req) {

		Map<String, String> resMap = new HashMap<String, String>();
		resMap.put("status", req.getParameter("status"));
		resMap.put("info", req.getParameter("info"));
		return JSON.toJSONString(resMap);
	}
}
