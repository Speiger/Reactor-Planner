package speiger.reactorplanner.planner.components.ic2c.v3;

import java.util.List;
import java.util.Set;

import com.google.gson.JsonObject;

import speiger.reactorplanner.planner.base.ComponentType;
import speiger.reactorplanner.planner.base.IReactor;
import speiger.reactorplanner.planner.base.ReactorStat;
import speiger.reactorplanner.planner.base.ReactorType;
import speiger.reactorplanner.planner.components.base.IReactorComponent;
import speiger.src.collections.ints.functions.consumer.IntIntConsumer;
import speiger.src.collections.objects.lists.ObjectArrayList;

public class Spreader implements IReactorComponent {
	final short id;
	final int coolrate;
	
	public Spreader(short id, int coolrate) {
		this.id = id;
		this.coolrate = coolrate;
	}
	
	@Override
	public JsonObject save() { return new JsonObject(); }
	@Override
	public void load(JsonObject obj) {}
	@Override
	public void commit() {}
	@Override
	public void reset() {}
	@Override
	public short id() { return id; }
	@Override
	public List<ReactorStat> stats() { return ObjectArrayList.wrap(ReactorStat.PART_COOLING); }
	@Override
	public Set<ReactorType> validReactors() { return IReactorComponent.UNIVERSAL; }
	@Override
	public ComponentType type() { return ComponentType.HEAT_SPREAD; }
	@Override
	public Number stat(ReactorStat stat) { return stat == ReactorStat.PART_COOLING ? coolrate : NULL_VALUE; }
	@Override
	public void affectedSlots(int x, int y, IntIntConsumer slots) {
		slots.accept(x - 1, y); 
		slots.accept(x + 1, y);
		slots.accept(x, y - 1); 
		slots.accept(x, y + 1);
	}
	
	@Override
	public void simulate(IReactor reactor, int x, int y, boolean heatTick, boolean damageTick) {
		if(heatTick) {
			cool(reactor, x - 1, y);
			cool(reactor, x + 1, y);
			cool(reactor, x, y - 1);
			cool(reactor, x, y + 1);
		}
	}
	
	protected void cool(IReactor reactor, int x, int y) {
		IReactorComponent component = reactor.item(x, y);
		if(component != null && component.canStoreHeat(reactor, x, y)) {
			component.storeHeat(reactor, x, y, -coolrate);
		}
	}
	
	@Override
	public boolean acceptUraniumPulse(IReactor reactor, int x, int y, IReactorComponent source, int sourceX, int sourceY, boolean heatTick, boolean damageTick) { return false; }
	@Override
	public int heat(IReactor reactor, int x, int y) { return 0; }
	@Override
	public int maxHeat(IReactor reactor, int x, int y) { return 0; }
	@Override
	public int storeHeat(IReactor reactor, int x, int y, int heatChange) { return heatChange; }
	@Override
	public boolean canStoreHeat(IReactor reactor, int x, int y) { return false; }
	@Override
	public boolean canViewHeat(IReactor reactor, int x, int y) { return false; }
	@Override
	public float explosionInfluence(IReactor reactor, int x, int y) { return 0F; }
}