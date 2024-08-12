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

public class ReactorPlating implements IReactorComponent {
	final short id;
	protected final int heatModifier;
	protected final float effectModifier;
	
	public ReactorPlating(short id, int heatModifier, float effectModifier) {
		this.id = id;
		this.heatModifier = heatModifier;
		this.effectModifier = effectModifier;
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
	public List<ReactorStat> stats() { return ObjectArrayList.wrap(ReactorStat.MAX_HEAT_STORAGE, ReactorStat.REACTOR_HEAT_EFFECT_MULTIPLIER); }
	@Override
	public Set<ReactorType> validReactors() { return IReactorComponent.UNIVERSAL; }
	@Override
	public ComponentType type() { return ComponentType.PLATING; }
	@Override
	public Number stat(ReactorStat stat) {
		if(stat == ReactorStat.MAX_HEAT_STORAGE) return heatModifier;
		if(stat == ReactorStat.REACTOR_HEAT_EFFECT_MULTIPLIER) return heatModifier;
		return NULL_VALUE;
	}
	
	@Override
	public void affectedSlots(int x, int y, IntIntConsumer slots) { slots.accept(x, y); }
	
	@Override
	public void simulate(IReactor reactor, int x, int y, boolean heatTick, boolean damageTick) {
		if(heatTick) {
			reactor.setMaxHeat(reactor.getMaxHeat() + heatModifier);
			reactor.setHeatEffectModifier(reactor.getHeatEffectModifier() * effectModifier);
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
	public float explosionInfluence(IReactor reactor, int x, int y) { return effectModifier >= 1.0F ? 0F : effectModifier; }
}
