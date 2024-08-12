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
import speiger.src.collections.objects.utils.ObjectLists;

public class IsotopicCell extends BaseDurabilityComponent {
	protected final int heatRequired;
	protected int finished = 0;
	
	public IsotopicCell(short id, int maxDamage, int heatRequired) {
		super(id, maxDamage);
		damage = maxDamage;
		this.heatRequired = heatRequired;
	}
	
	@Override
	public JsonObject save() {
		JsonObject obj = super.save();
		obj.addProperty("finished", finished);
		return obj;
	}
	
	@Override
	public void load(JsonObject obj) {
		super.load(obj);
		finished = JsonUtils.get(obj, "finished", 0);
	}
	
	@Override
	public void reset() {
		damage = maxDamage;
		finished = 0;
	}
	
	@Override
	public boolean acceptUraniumPulse(IReactor reactor, int x, int y, IReactorComponent source, int sourceX, int sourceY, boolean heatTick, boolean damageTick) {
		if(reactor.isSimulatingPulses()) {
			reactor.breedingPulse();
		}
		if(reactor.reactorType() == ReactorType.STEAM) {
			if(damageTick) {
				int level = damage - 1 - reactor.getHeat() / heatRequired;
				if(level <= 0) {
					damage = maxDamage;
					finished++;
				}
				else damage = level;
			}
			return true;
		}
		//TODO maybe evaluate if damage tick should be maybe done here too?
		int level = damage - 1 - reactor.getHeat() / heatRequired;
		if(level <= 0) {
			damage = maxDamage;
			finished++;
		}
		else damage = level;
		return true;
	}
	
	@Override
	public List<ReactorStat> stats() { return ObjectLists.singleton(ReactorStat.MAX_COMPONENT_DURABILITY); }
	@Override
	public ComponentType type() { return ComponentType.ISOTOPE_CELL; }
	@Override
	public Number stat(ReactorStat stat) { return stat == ReactorStat.MAX_COMPONENT_DURABILITY ? maxDamage : NULL_VALUE; }
}
