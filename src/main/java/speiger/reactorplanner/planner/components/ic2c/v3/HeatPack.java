package speiger.reactorplanner.planner.components.ic2c.v3;

import java.util.List;
import java.util.Set;

import com.google.gson.JsonObject;

import speiger.reactorplanner.planner.base.ComponentType;
import speiger.reactorplanner.planner.base.IReactor;
import speiger.reactorplanner.planner.base.ReactorStat;
import speiger.reactorplanner.planner.base.ReactorType;
import speiger.reactorplanner.planner.components.base.IReactorComponent;
import speiger.reactorplanner.utils.JsonUtils;
import speiger.src.collections.ints.functions.consumer.IntIntConsumer;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.collections.objects.utils.ObjectSets;

public class HeatPack implements IReactorComponent {
	final short id;
	protected int size;
	
	public HeatPack(short id, int size) {
		this.id = id;
		this.size = size;
	}

	@Override
	public JsonObject save() {
		JsonObject obj = new JsonObject();
		obj.addProperty("size", size);
		return obj;
	}
	
	@Override
	public void load(JsonObject obj) {
		size = JsonUtils.get(obj, "size", 1);
	}
	
	@Override
	public void commit() {}
	@Override
	public void reset() {}
	@Override
	public short id() { return id; }
	@Override
	public List<ReactorStat> stats() { return ObjectLists.singleton(ReactorStat.HEAT_PRODUCTION); }
	@Override
	public Set<ReactorType> validReactors() { return ObjectSets.singleton(ReactorType.ELECTRIC); }
	@Override
	public ComponentType type() { return ComponentType.HEAT_PACK; }
	@Override
	public Number stat(ReactorStat stat) { return stat == ReactorStat.HEAT_PRODUCTION ? size : NULL_VALUE; }
	@Override
	public void affectedSlots(int x, int y, IntIntConsumer slots) { slots.accept(0, 0); }
	
	@Override
	public void simulate(IReactor reactor, int x, int y, boolean heatTick, boolean damageTick) {
		if(reactor.reactorType() == ReactorType.STEAM) return;
		if (reactor.getHeat() < 1000 * size) {
			reactor.addHeat(size);
		}
	}
	
	@Override
	public boolean acceptUraniumPulse(IReactor reactor, int x, int y, IReactorComponent source, int sourceX, int sourceY, boolean heatTick, boolean damageTick) { return false; }
	@Override
	public int heat(IReactor reactor, int x, int y) { return 0; }
	@Override
	public int maxHeat(IReactor reactor, int x, int y) { return 0; }
	@Override
	public int storeHeat(IReactor reactor, int x, int y, int heatChange) { return 0; }
	@Override
	public boolean canStoreHeat(IReactor reactor, int x, int y) { return false; }
	@Override
	public boolean canViewHeat(IReactor reactor, int x, int y) { return false; }
	@Override
	public float explosionInfluence(IReactor reactor, int x, int y) { return size / 10F; }
}
