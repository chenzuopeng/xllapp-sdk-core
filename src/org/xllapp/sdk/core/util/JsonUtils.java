package org.xllapp.sdk.core.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * 此类提供进行JSON操作的工具方法.
 *
 * @author dylan.chen Sep 26, 2014
 *
 */
public abstract class JsonUtils {

	public static JSONObject toJSONObject(final Object object) {

		if (null == object) {
			return null;
		}

		final JSONObject jsonObject = new JSONObject();
		ReflectionUtils.doWithObjectFields(object.getClass(), true, new ReflectionUtils.FieldCallback() {
			@Override
			public void doWith(Field field) throws Exception {
				Object fieldValue = ReflectionUtils.getFieldValue(object, field);
				if (null != fieldValue) {
					jsonObject.put(ReflectionUtils.getFieldName(field, true), getJsonPropertyValue(fieldValue));
				}
			}
		});
		return jsonObject;
	}

	private static Object getJsonPropertyValue(Object fieldValue) {
		if (fieldValue.getClass().isArray()) {
			JSONArray jsonArray = new JSONArray();
			Object[] array = (Object[]) fieldValue;
			for (Object item : array) {
				jsonArray.put(getJsonPropertyValue(item));
			}
			return jsonArray;
		} else if (fieldValue instanceof String || fieldValue instanceof Integer || fieldValue instanceof Double || fieldValue instanceof Boolean) {
			return fieldValue;
		} else {
			return toJSONObject(fieldValue);
		}
	}

	public static String toJson(Object object) {
		return toJSONObject(object).toString();
	}

	public static <T> T toObject(final JSONObject jsonObject, Class<T> type) {
		final T object = newInstance(type);
		ReflectionUtils.doWithObjectFields(type, true, new ReflectionUtils.FieldCallback() {
			@Override
			public void doWith(Field field) throws Exception {
				String jsonPropertyName = ReflectionUtils.getFieldName(field, true);
				if (!jsonObject.isNull(jsonPropertyName)) {
					Object value = getObjectFieldValue(jsonObject, jsonPropertyName, field.getType());
					ReflectionUtils.setFieldValue(object, field, value);
				}
			}
		});
		return object;
	}

	private static Object getObjectFieldValue(JSONObject jsonObject, String jsonPropertyName, Class<?> fieldValueType) throws JSONException {

		if (Integer.class == fieldValueType || int.class == fieldValueType) {
			return jsonObject.getInt(jsonPropertyName);
		}

		if (Long.class == fieldValueType || long.class == fieldValueType) {
			return jsonObject.getLong(jsonPropertyName);
		}

		if (Double.class == fieldValueType || double.class == fieldValueType) {
			return jsonObject.getDouble(jsonPropertyName);
		}

		if (Boolean.class == fieldValueType || boolean.class == fieldValueType) {
			return jsonObject.getBoolean(jsonPropertyName);
		}

		if (String.class == fieldValueType) {
			return jsonObject.getString(jsonPropertyName);
		}

		if (fieldValueType.isArray()) {
			return toArray(jsonObject.getJSONArray(jsonPropertyName), fieldValueType.getComponentType());
		}

		return toObject(jsonObject.getJSONObject(jsonPropertyName), fieldValueType);
	}

	private static <T> T newInstance(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] toArray(JSONArray jsonArray, Class<T> itemType) {

		if (itemType.isPrimitive()) {
			throw new RuntimeException("item type can not be primitive");
		}else if(itemType.isArray()&&itemType.getComponentType().isPrimitive()){
			throw new RuntimeException("item type can not be primitive array");
		}

		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < jsonArray.length(); i++) {
			Object itemJsonValue = jsonArray.opt(i);
			if (null != itemJsonValue) {
				try {
					if (Integer.class == itemType) {
						list.add(jsonArray.getInt(i));
					} else if (Long.class == itemType) {
						list.add(jsonArray.getLong(i));
					} else if (Double.class == itemType) {
						list.add(jsonArray.getDouble(i));
					} else if (Boolean.class == itemType) {
						list.add(jsonArray.getBoolean(i));
					} else if (String.class == itemType) {
						list.add(jsonArray.getString(i));
					} else if (itemType.isArray()) {
						list.add(toArray(jsonArray.getJSONArray(i), itemType.getComponentType()));
					} else {
						list.add(toObject(jsonArray.getJSONObject(i), itemType));
					}
				} catch (Exception e) {
					Log.d(JsonUtils.class.getName(), e.getLocalizedMessage(), e);
				}
			}
		}
		return list.toArray((T[]) Array.newInstance(itemType, 0));
	}

}
