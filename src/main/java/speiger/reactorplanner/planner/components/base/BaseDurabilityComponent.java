package speiger.reactorplanner.planner.components.base;

import java.util.Set;

import com.google.gson.JsonObject;

import speiger.reactorplanner.planner.base.IReactor;
import speiger.reactorplanner.planner.base.ReactorType;
import speiger.reactorplanner.utils.JsonUtils;
import speiger.src.collections.ints.functions.consumer.IntIntConsumer;

public abstract class BaseDurabilityComponent implements IReactorComponent {
	final short id;
	protected final int maxDamage;
	protected int damage;
	
	public BaseDurabilityComponent(short id, int maxDamage) {
		this.id = id;
		this.maxDamage = maxDamage;
	}
	
	@Override
	public JsonObject save() {
		JsonObject obj = new JsonObject();
		obj.addProperty("damage", damage);
		return obj;
	}
	
	@Override
	public void load(JsonObject obj) {
		damage = JsonUtils.get(obj, "damage", 0);
	}
	
	@Override
	public void commit() {}
	
	@Override
	public void reset() {
		damage = 0;
	}
	
	@Override
	public short id() { return id; }
	@Override
	public Set<ReactorType> validReactors() { return IReactorComponent.UNIVERSAL; }
	@Override
	public void affectedSlots(int x, int y, IntIntConsumer slots) {}
	@Override
	public void simulate(IReactor reactor, int x, int y, boolean heatTick, boolean damageTick) {}
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
	public boolean canViewHeat(IReactor reactor, int x, int y) { return true; }
	@Override
	public float explosionInfluence(IReactor reactor, int x, int y) { return 0; }
}