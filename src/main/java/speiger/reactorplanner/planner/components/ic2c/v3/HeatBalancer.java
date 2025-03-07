package speiger.reactorplanner.planner.components.ic2c.v3;

import java.util.List;

import speiger.reactorplanner.planner.base.ComponentType;
import speiger.reactorplanner.planner.base.IReactor;
import speiger.reactorplanner.planner.base.ReactorStat;
import speiger.reactorplanner.planner.components.base.BaseHeatComponent;
import speiger.reactorplanner.planner.components.base.IReactorComponent;
import speiger.reactorplanner.planner.utils.ExchangerStats;
import speiger.reactorplanner.planner.utils.ReactorPosition.ReactorHeatPosition;
import speiger.reactorplanner.utils.math.Vec2i;
import speiger.src.collections.objects.lists.ObjectArrayList;

public class HeatBalancer extends BaseHeatComponent {
	protected final ExchangerStats stats;
	
	public HeatBalancer(ExchangerStats stats) {
		super(stats.id(), stats.registryId(), stats.capacity());
		this.stats = stats;
	}
	
	protected int processStep(IReactor reactor, int x, int y, int myHeat, boolean absorb, List<ReactorHeatPosition> process) {
		int sides = stats.self();
		int react = stats.reactor();
		double medium = ((double)heat / (double)maxHeat);
		int c = 1;
		boolean didReactor = false;
		if (react > 0) {
			double avg = reactor.getHeat() / (double) reactor.getMaxHeat();
			if(absorb ? avg >= 0.75 : avg <= 0.25) {
				c++;
				medium += avg;
				didReactor = true;
			}
		}
		
		if (sides > 0) {
			for(int i = 0,m=process.size();i<m;i++) {
				medium += process.get(i).average();
			}
		}
		medium /= c + process.size();
		if (sides > 0) {
			for (ReactorHeatPosition coord : process) {
				int add = Math.clamp(coord.transferrate(reactor, medium), -sides, sides);
				myHeat -= add;
				myHeat += coord.store(reactor, add);
			}
		}
		if (react > 0 && didReactor) {
			int add = Math.clamp((int)(medium * reactor.getMaxHeat()) - reactor.getHeat(), -react, react);
			myHeat -= add;
			reactor.setHeat(reactor.getHeat() + add);
		}
		return myHeat;
	}
	
	@Override
	public void simulate(IReactor reactor, int x, int y, boolean heatTick, boolean damageTick) {
//		if(!heatTick) return; This line is here to show ic2cs bug where heat vents transfer 2x the heat that they are supposed to. This bug stays like this until i decided how it should be done in the future. Because there are huge implications attached to this!
		List<ReactorHeatPosition> insert = new ObjectArrayList<>();
		List<ReactorHeatPosition> extract = new ObjectArrayList<>();
		List<Vec2i> offsets = stats.offsets();
		for(int i = 0,m=offsets.size();i<m;i++) {
			Vec2i pos = offsets.get(i);
			checkHeatComponent(reactor, x + pos.x(), y + pos.y(), insert, extract);
		}
		int myHeat = 0;
		myHeat = processStep(reactor, x, y, myHeat, false, insert);
		myHeat = processStep(reactor, x, y, myHeat, true, extract);
		storeHeat(reactor, x, y, myHeat);
	}
	
	@Override
	public List<ReactorStat> stats() { return ObjectArrayList.wrap(ReactorStat.SELF_COOLING, ReactorStat.REACTOR_COOLING); }
	@Override
	public ComponentType type() { return ComponentType.HEAT_EXCHANGER; }
	
	private void checkHeatComponent(IReactor reactor, int x, int y, List<ReactorHeatPosition> insert, List<ReactorHeatPosition> extract) {
		IReactorComponent component = reactor.item(x, y);
		if (component != null && component.canStoreHeat(reactor, x, y)) {
			double max = component.maxHeat(reactor, x, y);
			if (max <= 0.0D)
				return;
			double average = component.heat(reactor, x, y) / max;
			if(average <= 0.25D) insert.add(new ReactorHeatPosition(component, x, y, average));
			else if(average >= 0.75D) extract.add(new ReactorHeatPosition(component, x, y, average));
		}
	}
		
	@Override
	public Number stat(ReactorStat stat) {
		if(stat == ReactorStat.SELF_COOLING) return stats.self();
		if(stat == ReactorStat.REACTOR_COOLING) return stats.reactor();
		return NULL_VALUE;
	}
}
