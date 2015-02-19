package org.xllapp.sdk.core.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 反射工具类.
 *
 * @author dylan.chen Sep 25, 2014
 *
 */
public abstract class ReflectionUtils {

	public static void doWithObjectFields(Class<?> clazz, boolean enableFieldIgnore, FieldCallback fieldCallback) {
		Class<?> targetClass = clazz;
		do {
			Field[] fields = targetClass.getDeclaredFields();
			for (Field field : fields) {
				if (enableFieldIgnore && null != field.getAnnotation(FieldIgnore.class) || Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
					continue;
				}
				try {
					fieldCallback.doWith(field);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			targetClass = targetClass.getSuperclass();
		} while (targetClass != null && targetClass != Object.class);
	}

	public static String getFieldName(Field field,boolean isLowerCaseWithUnderscores) {
		FieldName fieldName = field.getAnnotation(FieldName.class);
		if(null != fieldName){
			return fieldName.value();
		}else {
			if(isLowerCaseWithUnderscores){
				return camelCaseToLowerCaseWithUnderscores(field.getName());
			} else {
				return field.getName();
			}
		}
	}

	public static Object getFieldValue(Object object, Field field) throws IllegalArgumentException, IllegalAccessException {
		field.setAccessible(true);
		return field.get(object);
	}

	public static void setFieldValue(Object object, Field field, Object value) throws IllegalArgumentException, IllegalAccessException {
		field.setAccessible(true);
		field.set(object, value);
	}

	/**
	 * 驼峰格式字符串 转换成 下划线格式字符串
	 */
	public static String camelCaseToLowerCaseWithUnderscores(String objectFieldName) {
		if (objectFieldName == null) {
			return objectFieldName;
		}
		int length = objectFieldName.length();
		StringBuilder result = new StringBuilder(length * 2);
		int resultLength = 0;
		boolean wasPrevTranslated = false;
		for (int i = 0; i < length; i++) {
			char c = objectFieldName.charAt(i);
			if (i > 0 || c != '_') {
				if (Character.isUpperCase(c)) {
					if (!wasPrevTranslated && resultLength > 0 && result.charAt(resultLength - 1) != '_') {
						result.append('_');
						resultLength++;
					}
					c = Character.toLowerCase(c);
					wasPrevTranslated = true;
				} else {
					wasPrevTranslated = false;
				}
				result.append(c);
				resultLength++;
			}
		}
		return resultLength > 0 ? result.toString() : objectFieldName;
	}

	@Target({ ElementType.FIELD })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface FieldIgnore {
	}

	@Target({ ElementType.FIELD })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface FieldName {
		String value();
	}

	public interface FieldCallback {
		public void doWith(Field field) throws Exception;
	}

}
