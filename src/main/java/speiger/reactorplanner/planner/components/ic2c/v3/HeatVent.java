package speiger.reactorplanner.planner.components.ic2c.v3;

import java.util.List;

import com.google.gson.JsonObject;

import speiger.reactorplanner.planner.base.ComponentType;
import speiger.reactorplanner.planner.base.IReactor;
import speiger.reactorplanner.planner.base.ReactorStat;
import speiger.reactorplanner.planner.base.ReactorType;
import speiger.reactorplanner.planner.components.base.BaseHeatComponent;
import speiger.reactorplanner.planner.utils.VentStats;
import speiger.reactorplanner.planner.utils.VentType;
import speiger.reactorplanner.utils.JsonUtils;
import speiger.src.collections.objects.lists.ObjectArrayList;

public class HeatVent extends BaseHeatComponent {
	protected final VentStats stats;
	protected double water;
	protected double storage;
	
	public HeatVent(VentStats stats) {
		super(stats.capacity(), stats.id());
		this.stats = stats;
	}
	
	@Override
	public JsonObject save() {
		JsonObject obj = new JsonObject();
		obj.addProperty("water", water);
		obj.addProperty("storage", storage);
		return obj;
	}
	
	@Override
	public void load(JsonObject obj) {
		water = JsonUtils.get(obj, "water", 0D);
		storage = JsonUtils.get(obj, "storage", 0D);
	}
	
	@Override
	public void reset() {
		super.reset();
		water = 0D;
		storage = 0D;
	}
	
	@Override
	public void simulate(IReactor reactor, int x, int y, boolean heatTick, boolean damageTick) {
		if(reactor.reactorType() == ReactorType.STEAM) {
			if(stats.type() == VentType.ELECTRIC) return;
			else if(stats.type() == VentType.NORMAL) simulateNormal(reactor, x, y, heatTick, damageTick);
			else if(stats.type() == VentType.STEAM) simulateSteam(reactor, x, y, heatTick, damageTick);
		}
		else simulateNormal(reactor, x, y, heatTick, damageTick);
	}
	
	protected void simulateSteam(IReactor reactor, int x, int y, boolean heatTick, boolean damageTick) {
		if(!heatTick) {
			if(water <= stats.self() - 1) {
				water += reactor.consumeWater(stats.self() - (int)water);
			}
			return;
		}
		int reactorVent = stats.reactor();
		int selfVent = stats.self();
		if(reactorVent > 0) {
			int reactorDrain;
			int rHeat = reactorDrain = reactor.getHeat();
			if(reactorDrain > reactorVent) {
				reactorDrain = reactorVent;
			}
			rHeat -= reactorDrain;
			if(storeHeat(reactor, x, y, reactorDrain) > 0) return;
			reactor.setHeat(rHeat);
		}
		if(heat < 100) {
			if(reactor.gameTime() % 40 == 0) {
				storeHeat(reactor, x, y, -1);
			}
			return;
		}
		if(water <= 0D) {
			if(reactor.gameTime() % 40 == 0) {
				storeHeat(reactor, x, y, -1);
			}
			return;
		}
		double heatLevel = heat / (stats.capacity() - 100D);
		double lvl = ((selfVent * heatLevel) / 40); //TODO add balance configs
		double consume = Math.min(water, lvl);
		if(consume <= 0D) return;
		int steam = (int)(160 * consume);
		if(steam <= 0) return;
		reactor.addSteam(steam);
		consume = steam / 160D;
		water -= consume;
		storage += (consume * 40D); //TODO add balance configs;
		int copy = -(int)storage;
		copy -= storeHeat(reactor, x, y, copy);
		storage += copy;
	}

	
	protected void simulateNormal(IReactor reactor, int x, int y, boolean heatTick, boolean damageTick) {
		if(!heatTick) return;
		boolean producing = reactor.isProducingEnergy();
		boolean electric = stats.type() == VentType.ELECTRIC;
		int reactorVent = electric && !producing ? stats.reactor() >> 1 : stats.reactor();
		int selfVent = stats.self();
		if(reactorVent > 0) {
			int reactorDrain;
			int rHeat = reactorDrain = reactor.getHeat();
			if(reactorDrain > reactorVent) {
				reactorDrain = reactorVent;
			}
			rHeat -= reactorDrain;
			if(storeHeat(reactor, x, y, reactorDrain) > 0) return;
			reactor.setHeat(rHeat);
		}
		if(electric) {
			if(producing) {
				reactor.addOutput(selfVent * -0.0005F);
			}
			storeHeat(reactor, x, y, producing ? -selfVent : selfVent >> 1);
			return;
		}
		storeHeat(reactor, x, y, -selfVent);
	}

	
	@Override
	public List<ReactorStat> stats() {
		return switch(stats.type()) {
			case NORMAL -> ObjectArrayList.wrap(ReactorStat.SELF_COOLING, ReactorStat.REACTOR_COOLING);
			case ELECTRIC -> ObjectArrayList.wrap(ReactorStat.SELF_COOLING, ReactorStat.REACTOR_COOLING, ReactorStat.ENERGY_USAGE);
			case STEAM -> ObjectArrayList.wrap(ReactorStat.SELF_COOLING, ReactorStat.REACTOR_COOLING, ReactorStat.WATER_CONSUMPTION, ReactorStat.WATER_STORAGE, ReactorStat.STEAM_PRODUCTION);
		};
	}
	
	@Override
	public ComponentType type() { return ComponentType.HEAT_VENT; }
	
	@Override
	public Number stat(ReactorStat stat) {
		return switch(stat) {
			case SELF_COOLING -> stats.self();
			case REACTOR_COOLING -> stats.reactor();
			case ENERGY_USAGE -> stats.self() * 0.005F;
			case WATER_CONSUMPTION -> stats.self() / 40F; //TODO implement CONFIGS!
			case WATER_STORAGE -> stats.self();
			case STEAM_PRODUCTION -> stats.self() * 4F;
			default -> NULL_VALUE;
		};
	}
	
}
