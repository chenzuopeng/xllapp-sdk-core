package org.xllapp.sdk.core.util;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import org.xllapp.sdk.core.util.ReflectionUtils.FieldName;

/**
 * 此类用于将对象转换成string.
 *
 * @author dylan.chen Sep 23, 2014
 *
 */
public abstract class ToStringBuilder {

	public static String toString(Object object) {
		if (null == object) {
			return "";
		}
		if (object.getClass().isArray()) {
			try {
				return objectToString(object);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			return toMap(object).toString();
		}
	}

	public static Map<String, String> toMap(final Object object) {
		final Map<String, String> map = new LinkedHashMap<String, String>();
		try {
			ReflectionUtils.doWithObjectFields(object.getClass(), true, new ReflectionUtils.FieldCallback() {
				@Override
				public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
					map.put(getFieldName(field), objectToString(ReflectionUtils.getFieldValue(object, field)));
				}
			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return map;
	}

	public static String getFieldName(Field field){
		FieldName fieldName = field.getAnnotation(FieldName.class);
		if(null != fieldName){
			return field.getName()+"["+fieldName.value()+"]";
		}else {
			return field.getName();
		}
	}

	private static String objectToString(Object input) throws IllegalArgumentException, IllegalAccessException {

		if (null == input || input instanceof String) {
			return (String) input;
		}

		if (input instanceof Integer || input instanceof Long || input instanceof Double || input instanceof Float || input instanceof Short || input instanceof Byte || input instanceof Character || input instanceof Boolean) {
			return String.valueOf(input);
		}

		if(input instanceof Enum){
			return input+"";
		}

		if (input.getClass().isArray()) {
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			Object[] array = (Object[]) input;
			for (int i = 0; i < array.length; i++) {
				sb.append(objectToString(array[i]));
				if (i <= array.length - 2) {
					sb.append(",");
				}
			}
			sb.append("]");
			return sb.toString();
		}

		return toString(input);
	}

}
