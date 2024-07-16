package speiger.reactorplanner.utils;

import com.google.gson.JsonObject;

public class JsonUtils {
	
	public static long get(JsonObject obj, String tag, long defaultValue) {
		return obj.has(tag) ? obj.get(tag).getAsLong() : defaultValue;
	}
	
	public static int get(JsonObject obj, String tag, int defaultValue) {
		return obj.has(tag) ? obj.get(tag).getAsInt() : defaultValue;
	}
	
	public static float get(JsonObject obj, String tag, float defaultValue) {
		return obj.has(tag) ? obj.get(tag).getAsFloat() : defaultValue;
	}
	
	public static double get(JsonObject obj, String tag, double defaultValue) {
		return obj.has(tag) ? obj.get(tag).getAsDouble() : defaultValue;
	}
	
	public static boolean get(JsonObject obj, String tag, boolean defaultValue) {
		return obj.has(tag) ? obj.get(tag).getAsBoolean() : defaultValue;
	}
}
