package dms.interceptor;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogInterceptor {
	private static Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

	@Pointcut("execution(* dms.controller.*.*(..))")
	public void recorRunTimeLog() {

	}

	@Pointcut("execution(* dms.serviceImpl.*.*(..))")
	public void recordServiceRunTimeException() {

	}

	/**
	 * 记录项目运行时的基本日志
	 * 
	 * @param jp
	 *            连接点
	 * @return
	 */
	@Around(value = "recorRunTimeLog()")
	public Object toRecorRunTimeLog(ProceedingJoinPoint jp) {
		Object result = null;
		// 获取请求的方法名
		String methodName = jp.getTarget().getClass().getName() + "." + jp.getSignature().getName() + "()";
		try {
			logger.info("访问" + methodName + "开始");
			logger.info("请求的参数:" + Arrays.toString(jp.getArgs()));
			result = jp.proceed();
			logger.info("返回结果:" + result);
			logger.info("访问" + methodName + "结束");
		} catch (Throwable e) {
			StackTraceElement[] stackElements = e.getStackTrace();// 通过Throwable获得堆栈信息
			if (stackElements != null) {
				logger.error(methodName + "出现异常");
				logger.error("类:" + stackElements[0].getClassName());
				logger.error("方法:" + stackElements[0].getMethodName());
				logger.error("异常代码:" + e.getClass().getName());
				logger.error("异常信息:" + e.getMessage());
				logger.error("行号:" + stackElements[0].getLineNumber());
			}

		}
		return result;
	}

}
