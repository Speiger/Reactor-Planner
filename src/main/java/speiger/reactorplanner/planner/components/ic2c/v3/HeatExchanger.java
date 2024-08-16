package speiger.reactorplanner.planner.components.ic2c.v3;

import java.util.List;

import speiger.reactorplanner.planner.base.ComponentType;
import speiger.reactorplanner.planner.base.IReactor;
import speiger.reactorplanner.planner.base.ReactorStat;
import speiger.reactorplanner.planner.components.base.BaseHeatComponent;
import speiger.reactorplanner.planner.components.base.IReactorComponent;
import speiger.reactorplanner.planner.utils.ExchangerStats;
import speiger.reactorplanner.planner.utils.ReactorPosition;
import speiger.reactorplanner.utils.math.Vec2i;
import speiger.src.collections.objects.lists.ObjectArrayList;

public class HeatExchanger extends BaseHeatComponent {
	protected final ExchangerStats stats;
	
	public HeatExchanger(ExchangerStats stats) {
		super(stats.id(), stats.registryId(), stats.capacity());
		this.stats = stats;
	}
	
	@Override
	public void simulate(IReactor reactor, int x, int y, boolean heatTick, boolean damageTick) {
//		if(!heatTick) return; This line is here to show ic2cs bug where heat vents transfer 2x the heat that they are supposed to. This bug stays like this until i decided how it should be done in the future. Because there are huge implications attached to this!
		int react = stats.reactor();
		int sides = stats.self();
		int myHeat = 0;
		List<ReactorPosition> positions = new ObjectArrayList<>();
		double medium = (double)heat / (double)maxHeat;
		int count = react > 0 ? 2 : 1;
		if(react > 0) {
			medium += (double)reactor.getHeat() / (double)reactor.getMaxHeat();
		}
		if(sides > 0) {
			List<Vec2i> offsets = stats.offsets();
			for(int i = 0,m=offsets.size();i<m;i++) {
				Vec2i pos = offsets.get(i);
				medium += checkHeatAcceptor(reactor, x + pos.x(), y + pos.y(), positions);
			}
		}
		medium /= count + positions.size();
		if(sides > 0) {
			for(int i = 0,m=positions.size();i<m;i++) {
				ReactorPosition position = positions.get(i);
				int add = Math.clamp(position.transferrate(reactor, medium), -sides, sides);
				myHeat -= add;
				myHeat += position.store(reactor, add);
			}
		}
		if(react > 0) {
			int add = Math.clamp((int)(medium * reactor.getMaxHeat()) - reactor.getHeat(), -react, react);
			myHeat -= add;
			reactor.setHeat(reactor.getHeat() + add);
		}
		if(myHeat != 0) return;
		storeHeat(reactor, x, y, myHeat);
	}
	
	@Override
	public List<ReactorStat> stats() { return ObjectArrayList.wrap(ReactorStat.SELF_COOLING, ReactorStat.REACTOR_COOLING); }
	@Override
	public ComponentType type() { return ComponentType.HEAT_EXCHANGER; }
	
	private double checkHeatAcceptor(IReactor reactor, int x, int y, List<ReactorPosition> results) {
		IReactorComponent component = reactor.item(x, y);
		if(component != null && component.canStoreHeat(reactor, x, y)) {
			results.add(new ReactorPosition(component, x, y));
			double max = component.maxHeat(reactor, x, y);
			if(max <= 0D) return 0D;
			return component.heat(reactor, x, y) / max;
		}
		return 0D;
	}
	
	@Override
	public Number stat(ReactorStat stat) {
		if(stat == ReactorStat.SELF_COOLING) return stats.self();
		if(stat == ReactorStat.REACTOR_COOLING) return stats.reactor();
		return NULL_VALUE;
	}
	
}
