package speiger.reactorplanner.planner.components.ic2c.v3;

import java.util.List;

import com.google.gson.JsonObject;

import speiger.reactorplanner.planner.base.ComponentType;
import speiger.reactorplanner.planner.base.IReactor;
import speiger.reactorplanner.planner.base.ReactorStat;
import speiger.reactorplanner.planner.base.ReactorType;
import speiger.reactorplanner.planner.components.base.BaseDurabilityComponent;
import speiger.reactorplanner.planner.components.base.IReactorComponent;
import speiger.reactorplanner.utils.JsonUtils;
import speiger.src.collections.objects.lists.ObjectArrayList;

public class Reflector extends BaseDurabilityComponent {
	protected final boolean unbreakable;
	protected int refilled = 0;
	
	public Reflector(short id, String registryId, int maxDamage, boolean unrbeakable) {
		super(id, registryId, maxDamage);
		this.unbreakable = unrbeakable;
	}
	
	@Override
	public JsonObject save() {
		JsonObject obj = super.save();
		obj.addProperty("refilled", refilled);
		return obj;
	}
	
	@Override
	public void load(JsonObject obj) {
		super.load(obj);
		refilled = JsonUtils.get(obj, "refilled", 0);
	}
	
	@Override
	public void reset() {
		super.reset();
		refilled = 0;
	}
	
	@Override
	public boolean acceptUraniumPulse(IReactor reactor, int x, int y, IReactorComponent source, int sourceX, int sourceY, boolean heatTick, boolean damageTick) {
		if(unbreakable && damage >= maxDamage) return false;
		if(reactor.isSimulatingPulses()) {
			reactor.fuelPulse();
		}
		if(reactor.reactorType() == ReactorType.STEAM) {
			if(damageTick) {
				if(damage >= maxDamage) {
					damage = 0;
					refilled++;
				}
				else damage++;
			}
			return true;
		}
		if(!heatTick) {
			reactor.addOutput(1F);
		}
		if(damageTick) {
			if(damage >= maxDamage) {
				damage = 0;
				refilled++;
			}
			else damage++;
		}
		return true;
	}
	
	@Override
	public List<ReactorStat> stats() { return ObjectArrayList.wrap(ReactorStat.MAX_COMPONENT_DURABILITY); }
	@Override
	public ComponentType type() { return ComponentType.REFLECTORS; }
	@Override
	public Number stat(ReactorStat stat) { return stat == ReactorStat.MAX_COMPONENT_DURABILITY ? maxDamage : NULL_VALUE; }
}
