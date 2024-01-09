package com.fw.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public final class JsonUtil {
	@SuppressWarnings("unchecked")
    public static <T> T fromJson(String json, Type type) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			Class<T> cls = (Class<T>) typeToClass(type);
			return mapper.readValue(json, cls);
		} catch (IOException var4) {
			var4.printStackTrace();
			return null;
		}
	}

	public static <T> T stringToJsonObject(String jsonString, Class<T> aclass) {
		Gson gson = new Gson();
		return gson.fromJson(jsonString, aclass);
	}

	// Chuyển đổi chuỗi thành danh sách đối tượng
	public static <T> List<T> stringToJsonList(String jsonArrayString, Class<T> objectClass) {
		Gson gson = new Gson();
		Type listType = TypeToken.getParameterized(List.class,objectClass).getType();
		return gson.fromJson(jsonArrayString, listType);
	}

	public static Class<?> typeToClass(Type type) {
		if (type instanceof Class) {
			return (Class<?>) type;
		} else if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			return (Class<?>) parameterizedType.getRawType();
		} else {
			return null;
		}
	}

	private JsonUtil() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}
}
