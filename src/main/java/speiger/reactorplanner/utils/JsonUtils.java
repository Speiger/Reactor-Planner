package speiger.reactorplanner.utils;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
	
	public static String get(JsonObject obj, String tag, String defaultValue) {
		return obj.has(tag) ? obj.get(tag).getAsString() : defaultValue;
	}
	
	public static JsonArray getList(JsonObject obj, String tag) {
		return obj.has(tag) ? obj.getAsJsonArray(tag) : new JsonArray();
	}
	
	public static JsonObject getMap(JsonObject obj, String tag) {
		return obj.has(tag) ? obj.getAsJsonObject(tag) : new JsonObject();
	}
	
	public static <T> JsonArray toArray(List<T> list, Function<T, JsonElement> function) {
		JsonArray array = new JsonArray();
		for(int i = 0,m=list.size();i<m;i++) {
			array.add(function.apply(list.get(i)));
		}
		return array;
	}
	
	public static <T, E extends Collection<T>> E fromArray(JsonArray array, E input, Function<JsonElement, T> function) {
		for(int i = 0,m=array.size();i<m;i++) {
			T value = function.apply(array.get(i));
			if(value == null) continue;
			input.add(value);
		}
		return input;
	}
}
