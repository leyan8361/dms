package dms.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import dms.annotation.AuthAnnotation;
import dms.entity.UserFunction;
import dms.service.UserService;
import dms.utils.Constants;
import dms.utils.JwtManager;
import dms.utils.Utils;
import io.jsonwebtoken.Claims;

public class AuthInterceptor implements HandlerInterceptor {

	@Resource(name = "userService")
	private UserService userService;

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		HandlerMethod method = (HandlerMethod) handler;
		AuthAnnotation auth = method.getMethod().getAnnotation(AuthAnnotation.class);
		if (auth != null) {
			String token = request.getParameter("token");
			Claims claims = JwtManager.parseToken(token);
			int userId = Integer.valueOf(String.valueOf(claims.get("userId")));
			if ("addPlan".equals(auth.auth())) {
				UserFunction uf = userService.checkIfUserContainsFunction(userId, "����Ԥ��");
				if (uf == null) {
					Utils.returnErrorMessage(Constants.authError, "���û����޴�ģ��Ȩ��", response);
					return false;
				}
			} else if ("updatePlanInfo".equals(auth.auth())) {
				UserFunction uf = userService.checkIfUserContainsFunction(userId, "�޸�Ԥ��");
				if (uf == null) {
					Utils.returnErrorMessage(Constants.authError, "���û����޴�ģ��Ȩ��", response);
					return false;
				}
			} else if ("getPlanList".equals(auth.auth())) {
				UserFunction uf = userService.checkIfUserContainsFunction(userId, "�鿴Ԥ��");
				if (uf == null) {
					Utils.returnErrorMessage(Constants.authError, "���û����޴�ģ��Ȩ��", response);
					return false;
				}
			} else if ("delPlan".equals(auth.auth())) {
				UserFunction uf = userService.checkIfUserContainsFunction(userId, "ɾ��Ԥ��");
				if (uf == null) {
					Utils.returnErrorMessage(Constants.authError, "���û����޴�ģ��Ȩ��", response);
					return false;
				}
			} else if ("addProcess".equals(auth.auth())) {
				UserFunction uf = userService.checkIfUserContainsFunction(userId, "�������̿�");
				if (uf == null) {
					Utils.returnErrorMessage(Constants.authError, "���û����޴�ģ��Ȩ��", response);
					return false;
				}
			} else if ("getProcessList".equals(auth.auth())) {
				UserFunction uf = userService.checkIfUserContainsFunction(userId, "�鿴���̿�");
				if (uf == null) {
					Utils.returnErrorMessage(Constants.authError, "���û����޴�ģ��Ȩ��", response);
					return false;
				}
			} else if ("updateProcess".equals(auth.auth())) {
				UserFunction uf = userService.checkIfUserContainsFunction(userId, "�޸����̿�");
				if (uf == null) {
					Utils.returnErrorMessage(Constants.authError, "���û����޴�ģ��Ȩ��", response);
					return false;
				}
			} else if ("delProcess".equals(auth.auth())) {
				UserFunction uf = userService.checkIfUserContainsFunction(userId, "ɾ�����̿�");
				if (uf == null) {
					Utils.returnErrorMessage(Constants.authError, "���û����޴�ģ��Ȩ��", response);
					return false;
				}
			} else if ("addInfo".equals(auth.auth())) {
				UserFunction uf = userService.checkIfUserContainsFunction(userId, "�������Ͽ�");
				if (uf == null) {
					Utils.returnErrorMessage(Constants.authError, "���û����޴�ģ��Ȩ��", response);
					return false;
				}
			} else if ("getInfoList".equals(auth.auth())) {
				UserFunction uf = userService.checkIfUserContainsFunction(userId, "�鿴���Ͽ�");
				if (uf == null) {
					Utils.returnErrorMessage(Constants.authError, "���û����޴�ģ��Ȩ��", response);
					return false;
				}
			} else if ("updateInfo".equals(auth.auth())) {
				UserFunction uf = userService.checkIfUserContainsFunction(userId, "�޸����Ͽ�");
				if (uf == null) {
					Utils.returnErrorMessage(Constants.authError, "���û����޴�ģ��Ȩ��", response);
					return false;
				}
			} else if ("delInfo".equals(auth.auth())) {
				UserFunction uf = userService.checkIfUserContainsFunction(userId, "ɾ�����Ͽ�");
				if (uf == null) {
					Utils.returnErrorMessage(Constants.authError, "���û����޴�ģ��Ȩ��", response);
					return false;
				}
			} else if ("addReport".equals(auth.auth())) {
				UserFunction uf = userService.checkIfUserContainsFunction(userId, "��������");
				if (uf == null) {
					Utils.returnErrorMessage(Constants.authError, "���û����޴�ģ��Ȩ��", response);
					return false;
				}
			} else if ("getReport".equals(auth.auth())) {
				UserFunction uf = userService.checkIfUserContainsFunction(userId, "�鿴����");
				if (uf == null) {
					Utils.returnErrorMessage(Constants.authError, "���û����޴�ģ��Ȩ��", response);
					return false;
				}
			} else if ("updateReport".equals(auth.auth())) {
				UserFunction uf = userService.checkIfUserContainsFunction(userId, "�޸ı���");
				if (uf == null) {
					Utils.returnErrorMessage(Constants.authError, "���û����޴�ģ��Ȩ��", response);
					return false;
				}
			} else if ("delReport".equals(auth.auth())) {
				UserFunction uf = userService.checkIfUserContainsFunction(userId, "ɾ������");
				if (uf == null) {
					Utils.returnErrorMessage(Constants.authError, "���û����޴�ģ��Ȩ��", response);
					return false;
				}
			} else if ("getLogList".equals(auth.auth())) {
				UserFunction uf = userService.checkIfUserContainsFunction(userId, "�鿴��־");
				if (uf == null) {
					Utils.returnErrorMessage(Constants.authError, "���û����޴�ģ��Ȩ��", response);
					return false;
				}
			} else if ("delLog".equals(auth.auth())) {
				UserFunction uf = userService.checkIfUserContainsFunction(userId, "ɾ����־");
				if (uf == null) {
					Utils.returnErrorMessage(Constants.authError, "���û����޴�ģ��Ȩ��", response);
					return false;
				}
			} else if ("addRole".equals(auth.auth())) {
				UserFunction uf = userService.checkIfUserContainsFunction(userId, "������ɫ");
				if (uf == null) {
					Utils.returnErrorMessage(Constants.authError, "���û����޴�ģ��Ȩ��", response);
					return false;
				}
			} else if ("getRoleList".equals(auth.auth())) {
				UserFunction uf = userService.checkIfUserContainsFunction(userId, "�鿴��ɫ");
				if (uf == null) {
					Utils.returnErrorMessage(Constants.authError, "���û����޴�ģ��Ȩ��", response);
					return false;
				}
			} else if ("updateRole".equals(auth.auth())) {
				UserFunction uf = userService.checkIfUserContainsFunction(userId, "�޸Ľ�ɫ");
				if (uf == null) {
					Utils.returnErrorMessage(Constants.authError, "���û����޴�ģ��Ȩ��", response);
					return false;
				}
			} else if ("delRole".equals(auth.auth())) {
				UserFunction uf = userService.checkIfUserContainsFunction(userId, "ɾ����ɫ");
				if (uf == null) {
					Utils.returnErrorMessage(Constants.authError, "���û����޴�ģ��Ȩ��", response);
					return false;
				}
			} else if ("addGroup".equals(auth.auth())) {
				UserFunction uf = userService.checkIfUserContainsFunction(userId, "����Ⱥ��");
				if (uf == null) {
					Utils.returnErrorMessage(Constants.authError, "���û����޴�ģ��Ȩ��", response);
					return false;
				}
			} else if ("getGroupList".equals(auth.auth())) {
				UserFunction uf = userService.checkIfUserContainsFunction(userId, "�鿴Ⱥ��");
				if (uf == null) {
					Utils.returnErrorMessage(Constants.authError, "���û����޴�ģ��Ȩ��", response);
					return false;
				}
			} else if ("updateGroup".equals(auth.auth())) {
				UserFunction uf = userService.checkIfUserContainsFunction(userId, "�޸�Ⱥ��");
				if (uf == null) {
					Utils.returnErrorMessage(Constants.authError, "���û����޴�ģ��Ȩ��", response);
					return false;
				}
			} else if ("delGroup".equals(auth.auth())) {
				UserFunction uf = userService.checkIfUserContainsFunction(userId, "ɾ��Ⱥ��");
				if (uf == null) {
					Utils.returnErrorMessage(Constants.authError, "���û����޴�ģ��Ȩ��", response);
					return false;
				}
			} else if ("addUser".equals(auth.auth())) {
				UserFunction uf = userService.checkIfUserContainsFunction(userId, "�����û�");
				if (uf == null) {
					Utils.returnErrorMessage(Constants.authError, "���û����޴�ģ��Ȩ��", response);
					return false;
				}
			} else if ("getUserList".equals(auth.auth())) {
				UserFunction uf = userService.checkIfUserContainsFunction(userId, "�鿴�û�");
				if (uf == null) {
					Utils.returnErrorMessage(Constants.authError, "���û����޴�ģ��Ȩ��", response);
					return false;
				}
			} else if ("delUser".equals(auth.auth())) {
				UserFunction uf = userService.checkIfUserContainsFunction(userId, "ɾ���û�");
				if (uf == null) {
					Utils.returnErrorMessage(Constants.authError, "���û����޴�ģ��Ȩ��", response);
					return false;
				}
			}
		}
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
