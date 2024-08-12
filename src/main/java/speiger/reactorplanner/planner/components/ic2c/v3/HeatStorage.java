package speiger.reactorplanner.planner.components.ic2c.v3;

import java.util.List;

import speiger.reactorplanner.planner.base.ComponentType;
import speiger.reactorplanner.planner.base.ReactorStat;
import speiger.reactorplanner.planner.components.base.BaseHeatComponent;
import speiger.src.collections.objects.utils.ObjectLists;

public class HeatStorage extends BaseHeatComponent {
	
	public HeatStorage(int maxHeat, short id) {
		super(maxHeat, id);
	}
	
	@Override
	public List<ReactorStat> stats() { return ObjectLists.singleton(ReactorStat.HEAT_STORAGE); }
	@Override
	public ComponentType type() { return ComponentType.COOLANT_CELL; }
	
	@Override
	public Number stat(ReactorStat stat) {
		return stat == ReactorStat.HEAT_STORAGE ? maxHeat : NULL_VALUE;
	}
	
}
