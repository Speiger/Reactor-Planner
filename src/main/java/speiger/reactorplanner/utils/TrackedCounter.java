package speiger.reactorplanner.utils;

import com.google.gson.JsonObject;

public class TrackedCounter {
	int total;
	int count;
	int change;
	
	public JsonObject save(JsonObject obj) {
		obj.addProperty("total", total);
		obj.addProperty("count", count);
		obj.addProperty("change", change);
		return obj;
	}
	
	public void load(JsonObject obj) {
		total = obj.get("total").getAsInt();
		count = obj.get("count").getAsInt();
		change = obj.get("change").getAsInt();
	}
	
	public void add(int value) {
		change += value;
	}
	
	public void commit() {
		total += change;
		change = 0;
		count++;
	}
	
	public void reset() {
		total = 0;
		change = 0;
		count = 0;
	}
	
	public float average() {
		return count == 0 ? 0F : (float)total / (float)count;
	}
	
	public int count() {
		return count;
	}
	
	public int total() {
		return total;
	}
}
