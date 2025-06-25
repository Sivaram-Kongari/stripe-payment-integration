package com.hulkhiretech.payments.util;

import org.springframework.stereotype.Component;
import com.google.gson.Gson;

@Component
public class GsonUtils {

	private static final Gson gson = new Gson();

	private GsonUtils() {
	}

	public static <T> T fromJson(String json, Class<T> clazz) {

		return gson.fromJson(json, clazz);
	}
}
