package dms.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import dms.utils.Constants;
import dms.utils.JwtManager;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.MalformedJwtException;

/**
 * 解析前台返回的token信息
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
			System.out.println(claims);
			return true;
		} catch (InvalidClaimException e) {
			response.sendRedirect("/dms/error/?status=" + Constants.tokenErrorStatus + "&info=" + e.getMessage());
			return false;
		} catch (IllegalArgumentException e) {
			response.sendRedirect("/dms/error/?status=" + Constants.tokenErrorStatus + "&info=" + e.getMessage());
			return false;
		} catch (MalformedJwtException e) {
			response.sendRedirect("/dms/error/?status=" + Constants.tokenErrorStatus + "&info=" + e.getMessage());
			return false;
		} catch (ExpiredJwtException e) {
			response.sendRedirect("/dms/error/?status=" + Constants.tokenOverTimeStatus + "&info=" + e.getMessage());
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
