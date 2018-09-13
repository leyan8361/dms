package dms.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// ��ʾ�����ڷ�����
@Target({ ElementType.METHOD })
// ��ʾ����ʱ��Ч
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthAnnotation {
	
	String auth() default "";
}
