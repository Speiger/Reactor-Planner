package speiger.reactorplanner.planner.components.base;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.JsonObject;

import speiger.reactorplanner.planner.base.ComponentType;
import speiger.reactorplanner.planner.base.IReactor;
import speiger.reactorplanner.planner.base.ReactorStat;
import speiger.reactorplanner.planner.base.ReactorType;
import speiger.src.collections.ints.functions.consumer.IntIntConsumer;

public interface IReactorComponent {
	public static final Set<ReactorType> UNIVERSAL = EnumSet.allOf(ReactorType.class);
	public static final Number NULL_VALUE = new AtomicInteger(0);
	
	public JsonObject save();
	public void load(JsonObject obj);
	public void commit();
	public void reset();
	
	public short id();
	public String registryId();
	public List<ReactorStat> stats();
	public Set<ReactorType> validReactors();
	public ComponentType type();
	public Number stat(ReactorStat stat);
	public default Number stat(ReactorStat stat, IReactor reactor, int x, int y) { return stat(stat); }
	public void affectedSlots(int x, int y, IntIntConsumer slots);
	
	public void simulate(IReactor reactor, int x, int y, boolean heatTick, boolean damageTick);
	public boolean acceptUraniumPulse(IReactor reactor, int x, int y, IReactorComponent source, int sourceX, int sourceY, boolean heatTick, boolean damageTick);
	public int heat(IReactor reactor, int x, int y);
	public int maxHeat(IReactor reactor, int x, int y);
	public int storeHeat(IReactor reactor, int x, int y, int heatChange);
	public boolean canStoreHeat(IReactor reactor, int x, int y);
	public boolean canViewHeat(IReactor reactor, int x, int y);
	public float explosionInfluence(IReactor reactor, int x, int y);
}
