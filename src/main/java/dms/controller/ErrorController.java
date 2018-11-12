package dms.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

@CrossOrigin
@RestController
@RequestMapping("error")
public class ErrorController {
	
	/**
	 * 返回错误接口
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

	/**
	 * 返回项目版本号
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "getVersion", method = RequestMethod.GET, produces = "text/text;charset=UTF-8")
	public String getVersion() throws IOException {

		InputStream in = this.getClass().getResourceAsStream("/config.properties");
		Properties properties = new Properties();
		properties.load(in);
		String s = properties.getProperty("version");
		return s;
	}
}
