package speiger.reactorplanner.planner.components.ic2c.v3;

import java.util.List;

import com.google.gson.JsonObject;

import speiger.reactorplanner.planner.base.ComponentType;
import speiger.reactorplanner.planner.base.IReactor;
import speiger.reactorplanner.planner.base.ReactorStat;
import speiger.reactorplanner.planner.base.ReactorType;
import speiger.reactorplanner.planner.components.base.BaseDurabilityComponent;
import speiger.reactorplanner.planner.components.base.IReactorComponent;
import speiger.reactorplanner.planner.utils.ReactorPosition;
import speiger.reactorplanner.planner.utils.UraniumType;
import speiger.reactorplanner.utils.JsonUtils;
import speiger.reactorplanner.utils.MathUtils;
import speiger.reactorplanner.utils.math.Vec2i;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.queues.ObjectArrayFIFOQueue;
import speiger.src.collections.objects.queues.ObjectPriorityQueue;

public class UraniumRod extends BaseDurabilityComponent {
	protected final UraniumType type;
	protected final int rodCount;
	protected int refilled;
	
	public UraniumRod(short id, String registryId, UraniumType type, int rodCount) {
		super(id, registryId, type.durability());
		this.type = type;
		this.rodCount = rodCount;
	}
	
	@Override
	public void reset() {
		super.reset();
		refilled = 0;
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
	public void simulate(IReactor reactor, int x, int y, boolean heatTick, boolean damageTick) {
		if(!reactor.isProducingEnergy()) return;
		int pulsesPerTick = type.pulses();
		List<Vec2i> pulseArea = type.pulseArea();
		if(heatTick) {
			ObjectPriorityQueue<ReactorPosition> queue = new ObjectArrayFIFOQueue<>();
			List<Vec2i> heatArea = type.heatArea();
			for(int iteration = 0;iteration<rodCount;iteration++) {
				int pulses = (1 + (rodCount >> 1)) * pulsesPerTick;
				for(int pulse = 0;pulse<pulsesPerTick;pulse++) {
					for(int i = 0,m=pulseArea.size();i<m;i++) {
						Vec2i offset = pulseArea.get(i);
						pulses += pulseNeighbor(reactor, x + offset.x(), y + offset.y(), this, x, y, true, damageTick);
					}
				}
				int heat = (int)(MathUtils.sumUp(pulses) * 4 * type.pulseHeatMod());
				for(int i = 0,m=heatArea.size();i<m;i++) {
					Vec2i offset = heatArea.get(i);
					testNeighbor(reactor, x + offset.x(), y + offset.y(), queue);
				}
				while(queue.size() > 0 && heat > 0) {
					int deltaHeat = heat / queue.size();
					heat -= deltaHeat;
					heat += queue.dequeue().store(reactor, deltaHeat);
				}
				if(heat > 0) {
					reactor.addHeat(heat);
				}
			}
		}
		else {
			int pulses = (1 + (rodCount >> 1)) * pulsesPerTick;
			for(int iteration = 0;iteration<rodCount;iteration++) {
				for(int pulse = 0;pulse<pulses;pulse++) {
					acceptUraniumPulse(reactor, x, y, this, x, y, false, damageTick);
				}
				for(int pulse = 0;pulse<pulsesPerTick;pulse++) {
					for(int i = 0,m=pulseArea.size();i<m;i++) {
						Vec2i offset = pulseArea.get(i);
						pulseNeighbor(reactor, x + offset.x(), y + offset.y(), this, x, y, false, damageTick);
					}
				}
			}
		}
		if(damageTick) {
			if(damage + 1 >= maxDamage) {
				refilled++;
				damage = 0;
			}
			else damage++;
		}
	}
	
	@Override
	public boolean acceptUraniumPulse(IReactor reactor, int x, int y, IReactorComponent source, int sourceX, int sourceY, boolean heatTick, boolean damageTick) {
		if(reactor.isSimulatingPulses()) reactor.fuelPulse();
		if(reactor.reactorType() == ReactorType.STEAM) return true;
		if(!heatTick) reactor.addOutput(type.output());
		return true;
	}
	
	@Override
	public float explosionInfluence(IReactor reactor, int x, int y) {
		return type.explosionMod() * rodCount;
	}
	
	protected int pulseNeighbor(IReactor reactor, int targetX, int targetY, IReactorComponent source, int x, int y, boolean heatTick, boolean damageTick) {
		IReactorComponent component = reactor.item(targetX, targetY);
		return component != null && component.acceptUraniumPulse(reactor, targetX, targetY, source, x, y, heatTick, damageTick) ? type.connectivityPulses() : 0;
	}
	
	protected void testNeighbor(IReactor reactor, int x, int y, ObjectPriorityQueue<ReactorPosition> positions) {
		IReactorComponent component = reactor.item(x, y);
		if(component != null && component.canStoreHeat(reactor, x, y)) {
			positions.enqueue(new ReactorPosition(component, x, y));
		}
	}
	
	@Override
	public List<ReactorStat> stats() { return ObjectArrayList.wrap(ReactorStat.ROD_COUNT, ReactorStat.MAX_COMPONENT_DURABILITY, ReactorStat.PULSE_COUNT, ReactorStat.HEAT_PRODUCTION, ReactorStat.ENERGY_PRODUCTION); }
	@Override
	public ComponentType type() { return ComponentType.FUEL_ROD; }
	@Override
	public Number stat(ReactorStat stat) {
		return switch(stat) {
			case ROD_COUNT -> rodCount;
			case MAX_COMPONENT_DURABILITY -> maxDamage;
			case PULSE_COUNT -> ((1 + (rodCount >> 1)) * type.pulses()) * rodCount;
			case HEAT_PRODUCTION -> (int)(MathUtils.sumUp((1 + rodCount >> 1) * type.pulses()) * 4 * type.pulseHeatMod()) * rodCount;
			case ENERGY_PRODUCTION -> (float)(((1 + (rodCount >> 1)) * type.pulses()) * type.output() * rodCount); //TODO implement Balance Config
			default -> IReactorComponent.NULL_VALUE;
		};
	}
	
	@Override
	public Number stat(ReactorStat stat, IReactor reactor, int x, int y) {
		return switch(stat) {
			case HEAT_PRODUCTION -> {
				int pulsesPerTick = type.pulses();
				List<Vec2i> pulseArea = type.pulseArea();
				int heat = 0;
				for(int iteration = 0;iteration<rodCount;iteration++) {
					int pulses = (1 + (rodCount >> 1)) * pulsesPerTick;
					for(int pulse = 0;pulse<pulsesPerTick;pulse++) {
						for(int i = 0,m=pulseArea.size();i<m;i++) {
							Vec2i offset = pulseArea.get(i);
							pulses += pulseNeighbor(reactor, x + offset.x(), y + offset.y(), this, x, y, true, false);
						}
					}
					heat += (int)(MathUtils.sumUp(pulses) * 4 * type.pulseHeatMod());
				}
				yield heat;
			}
			case ENERGY_PRODUCTION -> {
				int pulsesPerTick = type.pulses();
				List<Vec2i> pulseArea = type.pulseArea();
				int pulses = (1 + (rodCount >> 1)) * pulsesPerTick;
				for(int iteration = 0;iteration<rodCount;iteration++) {
					for(int pulse = 0;pulse<pulses;pulse++) {
						acceptUraniumPulse(reactor, x, y, this, x, y, false, false);
					}
					for(int pulse = 0;pulse<pulsesPerTick;pulse++) {
						for(int i = 0,m=pulseArea.size();i<m;i++) {
							Vec2i offset = pulseArea.get(i);
							pulseNeighbor(reactor, x + offset.x(), y + offset.y(), this, x, y, false, false);
						}
					}
				}
				yield reactor.getEnergyOutput();
			}
			default -> stat(stat);
		};
	}
	
}
