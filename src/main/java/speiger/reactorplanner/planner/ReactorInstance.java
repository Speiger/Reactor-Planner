package speiger.reactorplanner.planner;

import speiger.reactorplanner.planner.base.ComponentType;
import speiger.reactorplanner.planner.base.IReactor;
import speiger.reactorplanner.planner.base.IReactorProvider;
import speiger.reactorplanner.planner.base.ReactorStat;
import speiger.reactorplanner.planner.base.ReactorType;
import speiger.reactorplanner.planner.components.base.IReactorComponent;
import speiger.reactorplanner.utils.TrackedCounter;
import speiger.src.collections.ints.lists.IntArrayList;
import speiger.src.collections.ints.lists.IntList;

public class ReactorInstance implements IReactor {
	
	public Prediction prediction = new Prediction();
	//Providers
	IReactorProvider provider;
	int width;
	int height;
	
	//Inventory
	IReactorComponent[] items;
	
	//Visual Data
	public int totalTicks;
	public int startingHeat;
	
	//Simulation time data
	public int tick;
	public int ticksLeft;
	
	//Heat information
	public int heat;
	public int maxHeat;
	public float heatEffectMod = 1F;
	public TrackedCounter avgHeat = new TrackedCounter();
	
	//Production Information
	public float production;
	public int steamProduction;
	public long totalProducedEnergy;
	public long totalConsumedWater;
	public long totalProducedSteam;
	
	//State Information
	public boolean producing;
	public boolean isExploded;
	public IntList brokenSlots = new IntArrayList();
	public boolean valid;
	public long filledSlots;
	
	//Simulation type information
	public boolean simulating;
	public boolean breeding;
	public int fuelPulse;
	public int breedingPulse;
	
	public ReactorInstance(IReactorProvider provider) {
		this.provider = provider;
		onSizeChanged();
	}
	
	public void simulationTick() {
		tick++;
		maxHeat = 10000;
		heatEffectMod = 1F;
		production = 0;
		steamProduction = 0;
		boolean damageTick = tick % tickrate() == 0;
		for(int pass = 0;pass<2;pass++) {
			boolean heat = pass == 0;
			boolean damage = damageTick && pass == 1;
			for(int y = 0;y<height;y++) {
				for(int x = 0;x<width;x++) {
					IReactorComponent component = items[index(x, y)];
					if(component == null) continue;
					component.commit();
					component.simulate(this, x, y, heat, damage);
				}
			}
		}
		totalProducedEnergy += production * tickrate();
	}
	
	public int tickrate() {
		return provider.type().tickrate();
	}
	
	public void onSizeChanged() {
		if(items == null) {
			this.width = provider.width();
			this.height = provider.height();
			items = new IReactorComponent[width * height];
			return;
		}
		int newWidth = provider.width();
		int newHeight = provider.height();
		IReactorComponent[] newItems = new IReactorComponent[newWidth * newHeight];
		for(int i = 0,m=items.length;i<m;i++) {
			int x = i % width;
			int y = i / width;
			if(x >= newWidth || y >= newHeight) continue;
			newItems[y * newHeight + x] = items[i];
		}
		items = newItems;
		width = newWidth;
		height = newHeight;
	}
	
	public void validate() {
		reset();
		valid = true;
		
		int maxTick = 0;
		int maxCapacity = 0;
		for(int i = 0;i<items.length;i++) {
			IReactorComponent comp = items[i];
			if(comp == null) continue;
			int x = i % width;
			int y = i / width;
			
			ComponentType type = comp.type();
			float explosion = comp.explosionInfluence(this, x, y);
			if(explosion >= 0F && explosion < 1F) prediction.explosionMod *= explosion;
			else prediction.explosionPower += explosion;
			
			if(type == ComponentType.FUEL_ROD) {
				maxTick = Math.max(maxTick, comp.getStat(ReactorStat.MAX_COMPONENT_DURABILITY, this, x, y).intValue() * tickrate());
				prediction.heatPerTick += comp.getStat(ReactorStat.HEAT_PRODUCTION, this, x, y).intValue();
				breeding = true;
				prediction.energyPerTick += comp.getStat(ReactorStat.ENERGY_PRODUCTION, this, x, y).floatValue();
				breeding = false;
				prediction.totalFuelRodPulses += fuelPulse;
				prediction.totalBreedingPulses += breedingPulse;
				resetData();
				prediction.totalCellCount += comp.getStat(ReactorStat.ROD_COUNT, this, x, y).intValue();
				prediction.totalIntenralFuelPulses += comp.getStat(ReactorStat.PULSE_COUNT, this, x, y).intValue();
				resetData();
			}
			else if(type == ComponentType.HEAT_PACK) {
				int heat = comp.getStat(ReactorStat.HEAT_PRODUCTION, this, x, y).intValue();
				prediction.heatPerTick += heat;
				prediction.heatPackHeatPerTick += heat;
			}
			else if(type == ComponentType.HEAT_VENT || type == ComponentType.HEAT_SPREAD) {
				prediction.coolingPerTick += comp.getStat(ReactorStat.SELF_COOLING, this, x, y).intValue();
				prediction.reactorCoolingPerTick += comp.getStat(ReactorStat.REACTOR_COOLING, this, x, y).intValue();
			}
			else if(type == ComponentType.PLATING) {
				maxCapacity += comp.getStat(ReactorStat.MAX_HEAT_STORAGE, this, x, y).intValue();
				prediction.heatEffectMod *= comp.getStat(ReactorStat.REACTOR_HEAT_EFFECT_MULTIPLIER, this, x, y).floatValue(); //TODO fix in ic2c this isn't implemented.
			}
		}
		maxHeat = 10000 + maxCapacity;
		resetComponents();
		totalTicks = ticksLeft = provider.customDuration().orElse(maxTick);
		updatePrediction(totalTicks);
	}
	
	public void updatePrediction(int duration) {
		prediction.totalHeatProduced = prediction.heatPerTick * (long)duration;
		prediction.totalExplosionPower = prediction.explosionPower * (heatEffectMod * prediction.explosionMod);
		prediction.efficiency = prediction.energyPerTick != 0 ? ((float)prediction.totalFuelRodPulses / (float)prediction.totalCellCount) : 0F;
		prediction.totalEfficiency = prediction.energyPerTick != 0 ? ((float)(prediction.totalFuelRodPulses + prediction.totalBreedingPulses) / prediction.totalCellCount) : 0F;
		prediction.breeder = prediction.totalBreedingPulses > 0;
		prediction.totalEnergyProduced = (long)(prediction.energyPerTick * duration) * tickrate();
		prediction.waterPerTick = Math.min(prediction.heatPerTick / 40F, prediction.coolingPerTick) * provider.steamProduction();
		prediction.steamPerTick = prediction.waterPerTick * 160F;
		prediction.totalSteamProduced = (long)(prediction.steamPerTick * duration);
		prediction.totalWaterConsumed = prediction.totalSteamProduced / 160L;
	}
	
	//TODO implement saving/loading
	
	public void reset() {
		producing = true;
		isExploded = false;
		brokenSlots.clear();
		valid = false;
		resetComponents();
		resetData();
		totalTicks = 0;
		startingHeat = 0;
	}
	
	public void resetData() {
		tick = 0;
		ticksLeft = 0;
		maxHeat = 10000;
		heat = 0;
		heatEffectMod = 1F;
		production = 0F;
		totalProducedEnergy = 0L;
		steamProduction = 0;
		totalConsumedWater = 0L;
		totalProducedSteam = 0L;
		breedingPulse = 0;
		fuelPulse = 0;
		breeding = false;
	}
	
	public void resetComponents() {
		for(int i = 0,m=items.length;i<m;i++) {
			IReactorComponent comp = items[i];
			if(comp != null) comp.reset();
		}
	}
	
	public int index(int x, int y) { return y * width + x; }
	@Override
	public IReactorComponent item(int x, int y) { return items[index(x, y)]; }
	@Override
	public void markBroken(int x, int y) { brokenSlots.add(index(x, y)); }
	@Override
	public void breedingPulse() { breedingPulse++; }
	@Override
	public void fuelPulse() { fuelPulse++; }
	@Override
	public int getHeat() { return heat;	}
	@Override
	public void setHeat(int newHeat) { heat = newHeat; }
	@Override
	public void addHeat(int heat) { 
		this.heat += heat;
		this.avgHeat.add(heat);
	}
	
	@Override
	public int getMaxHeat() { return maxHeat; }
	@Override
	public void setMaxHeat(int heat) { this.maxHeat = heat; }
	@Override
	public float getHeatEffectModifier() { return heatEffectMod; }
	@Override
	public void setHeatEffectModifier(float hem) { heatEffectMod = hem; }
	@Override
	public void addOutput(float output) { production += output; }
	@Override
	public float getEnergyOutput() { return production * provider.energyProduction(); }
	
	@Override
	public void addSteam(int amount) {
		steamProduction += amount;
		totalProducedSteam += amount;
	}
	
	@Override
	public int consumeWater(int amount) {
		totalConsumedWater += amount;
		return amount;
	}
	
	@Override
	public boolean isProducingEnergy() { return producing; }
	@Override
	public ReactorType reactorType() { return provider.type(); }
	@Override
	public boolean isSimulatingPulses() { return breeding; }
	@Override
	public long gameTime() { return provider.time(tick); }
}
