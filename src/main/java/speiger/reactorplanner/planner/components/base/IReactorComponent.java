package speiger.reactorplanner.planner.components.base;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.JsonObject;

import speiger.reactorplanner.planner.ComponentType;
import speiger.reactorplanner.planner.IReactor;
import speiger.reactorplanner.planner.ReactorStat;
import speiger.reactorplanner.planner.ReactorType;
import speiger.src.collections.ints.functions.consumer.IntIntConsumer;

public interface IReactorComponent {
	public static final Number NULL_VALUE = new AtomicInteger(0);
	
	public JsonObject save();
	public void load(JsonObject obj);
	public void commit();
	public void reset();
	
	public short id();
	public List<ReactorStat> stats();
	public ReactorType validReactors();
	public ComponentType type();
	public Number stat(ReactorStat stat);
	public default Number getStat(ReactorStat stat, IReactor reactor, int x, int y) { return stat(stat); }
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
