package dms.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import dms.utils.JwtManager;
import io.jsonwebtoken.Claims;

public class RouteInterceptor implements HandlerInterceptor {

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		try {
			String token = request.getParameter("token");
			Claims claims = JwtManager.parseToken(token);
			System.out.println(claims);
			request.setAttribute("jwtCheck", "true");
			return true;
		} catch (Exception e) {

			request.setAttribute("jwtCheck", "false");
			return true;
		}
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
