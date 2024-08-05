package speiger.reactorplanner.planner.components.base;

import java.util.Set;

import com.google.gson.JsonObject;

import speiger.reactorplanner.planner.base.IReactor;
import speiger.reactorplanner.planner.base.ReactorType;
import speiger.reactorplanner.utils.JsonUtils;
import speiger.reactorplanner.utils.TrackedCounter;
import speiger.src.collections.ints.functions.consumer.IntIntConsumer;

public abstract class BaseHeatComponent implements IReactorComponent {
	protected final int maxHeat;
	protected final short id;
	protected int heat;
	protected TrackedCounter changes = new TrackedCounter();
	
	public BaseHeatComponent(int maxHeat, short id) {
		this.maxHeat = maxHeat;
		this.id = id;
	}
	
	@Override
	public JsonObject save() {
		JsonObject obj = new JsonObject();
		obj.addProperty("heat", heat);
		changes.save(obj);
		return obj;
	}
	
	@Override
	public void load(JsonObject obj) {
		heat = JsonUtils.get(obj, "heat", 0);
		changes.load(obj);
	}
	
	@Override
	public void commit() {
		changes.commit();
	}
	
	@Override
	public void reset() {
		changes.reset();
		heat = 0;
	}
	
	@Override
	public short id() { return id; }
	@Override
	public Set<ReactorType> validReactors() { return IReactorComponent.UNIVERSAL; }
	
	@Override
	public int storeHeat(IReactor reactor, int x, int y, int heatChange) {
		changes.add(heatChange);
		heat += heatChange;
		if (heat > maxHeat) {
			reactor.markBroken(x, y);
			heatChange = maxHeat - heat + 1;
		} 
		else {
			if(heat < 0)  {
				heatChange = heat;
				heat = 0;
			} 
			else heatChange = 0;
		}
		return heatChange;
	}
	
	@Override
	public void affectedSlots(int x, int y, IntIntConsumer slots) {}
	@Override
	public void simulate(IReactor reactor, int x, int y, boolean heatTick, boolean damageTick) {}
	@Override
	public boolean acceptUraniumPulse(IReactor reactor, int x, int y, IReactorComponent source, int sourceX, int sourceY, boolean heatTick, boolean damageTick) { return false; }
	@Override
	public int heat(IReactor reactor, int x, int y) { return heat; }
	@Override
	public int maxHeat(IReactor reactor, int x, int y) { return maxHeat; }
	@Override
	public boolean canStoreHeat(IReactor reactor, int x, int y) { return true; }
	@Override
	public boolean canViewHeat(IReactor reactor, int x, int y) { return true; }
	@Override
	public float explosionInfluence(IReactor reactor, int x, int y) { return 0; }
}
