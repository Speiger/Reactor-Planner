package speiger.reactorplanner.planner.components.ic2c.v3;

import java.util.List;

import com.google.gson.JsonObject;

import speiger.reactorplanner.planner.base.ComponentType;
import speiger.reactorplanner.planner.base.IReactor;
import speiger.reactorplanner.planner.base.ReactorStat;
import speiger.reactorplanner.planner.components.base.BaseHeatComponent;
import speiger.reactorplanner.utils.JsonUtils;
import speiger.src.collections.objects.lists.ObjectArrayList;

public class Condensator extends BaseHeatComponent {
	protected int refuels;
	
	public Condensator(short id, String registryId, int maxHeat) {
		super(id, registryId, maxHeat);
	}
	
	@Override
	public void reset() {
		super.reset();
		refuels = 0;
	}
	
	@Override
	public void load(JsonObject obj) {
		super.load(obj);
		refuels = JsonUtils.get(obj, "refuels", 0);
	}
	
	@Override
	public JsonObject save() {
		JsonObject obj = super.save();
		obj.addProperty("refuels", refuels);
		return obj;
	}
	
	@Override
	public boolean canViewHeat(IReactor reactor, int x, int y) { return false; }
	@Override
	public int heat(IReactor reactor, int x, int y) { return 0; }
	@Override
	public int storeHeat(IReactor reactor, int x, int y, int heatChange) {
		int can = Math.min(maxHeat - (heat+1), heatChange);
		if(can < heatChange) {
			refuels++;
			heat = 0;
			can = Math.min(maxHeat - (heat+1), heatChange);
		}
		heat += can;
		changes.add(can);
		return heatChange - can;
	}
	@Override
	public List<ReactorStat> stats() { return ObjectArrayList.wrap(ReactorStat.HEAT_STORAGE); }
	@Override
	public ComponentType type() { return ComponentType.CONDENSATOR; }
	@Override
	public Number stat(ReactorStat stat) { return stat == ReactorStat.HEAT_STORAGE ? maxHeat : NULL_VALUE; }
}
