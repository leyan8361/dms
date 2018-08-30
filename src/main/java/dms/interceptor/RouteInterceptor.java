package dms.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

import dms.utils.Constants;
import dms.utils.JwtManager;
import dms.utils.Utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.MalformedJwtException;

/**
 * ����ǰ̨���ص�token��Ϣ
 * 
 * @author ACER
 *
 */
public class RouteInterceptor implements HandlerInterceptor {

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		try {
			String token = request.getParameter("token");
			Claims claims = JwtManager.parseToken(token);
			JSONObject user = new JSONObject();
			user.put("userId", claims.get("userId"));
			user.put("userName", claims.get("userName"));
			request.setAttribute("user", user);
			System.out.println(claims);
			return true;
		} catch (InvalidClaimException e) {
			Utils.returnErrorMessage(Constants.tokenErrorStatus, e.getMessage(), response);
//			response.sendRedirect("/dms/error/?status=" + Constants.tokenErrorStatus + "&info=" + e.getMessage());
			return false;
		} catch (IllegalArgumentException e) {
			Utils.returnErrorMessage(Constants.tokenErrorStatus, e.getMessage(), response);
//			response.sendRedirect("/dms/error/?status=" + Constants.tokenErrorStatus + "&info=" + e.getMessage());
			return false;
		} catch (MalformedJwtException e) {
			Utils.returnErrorMessage(Constants.tokenErrorStatus, e.getMessage(), response);
//			response.sendRedirect("/dms/error/?status=" + Constants.tokenErrorStatus + "&info=" + e.getMessage());
			return false;
		} catch (ExpiredJwtException e) {
			Utils.returnErrorMessage(Constants.tokenOverTimeStatus, e.getMessage(), response);
//			response.sendRedirect("/dms/error/?status=" + Constants.tokenOverTimeStatus + "&info=" + e.getMessage());
			return false;
		}
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
