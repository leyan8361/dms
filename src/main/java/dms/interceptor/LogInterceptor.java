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
	 * ��¼��Ŀ����ʱ�Ļ�����־
	 * 
	 * @param jp
	 *            ���ӵ�
	 * @return
	 */
	@Around(value = "recorRunTimeLog()")
	public Object toRecorRunTimeLog(ProceedingJoinPoint jp) {
		Object result = null;
		// ��ȡ����ķ�����
		String methodName = jp.getTarget().getClass().getName() + "." + jp.getSignature().getName() + "()";
		try {
			logger.info("����" + methodName + "��ʼ");
			logger.info("����Ĳ���:" + Arrays.toString(jp.getArgs()));
			result = jp.proceed();
			logger.info("���ؽ��:" + result);
			logger.info("����" + methodName + "����");
		} catch (Throwable e) {
			StackTraceElement[] stackElements = e.getStackTrace();// ͨ��Throwable��ö�ջ��Ϣ
			if (stackElements != null) {
				logger.error(methodName + "�����쳣");
				logger.error("��:" + stackElements[0].getClassName());
				logger.error("����:" + stackElements[0].getMethodName());
				logger.error("�쳣����:" + e.getClass().getName());
				logger.error("�쳣��Ϣ:" + e.getMessage());
				logger.error("�к�:" + stackElements[0].getLineNumber());
			}

		}
		return result;
	}

}
