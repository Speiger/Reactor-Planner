package speiger.reactorplanner.planner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import speiger.reactorplanner.planner.base.ComponentRegistry;
import speiger.reactorplanner.planner.base.ComponentType;
import speiger.reactorplanner.planner.base.IReactor;
import speiger.reactorplanner.planner.base.IReactorProvider;
import speiger.reactorplanner.planner.base.ReactorStat;
import speiger.reactorplanner.planner.base.ReactorType;
import speiger.reactorplanner.planner.components.base.IReactorComponent;
import speiger.reactorplanner.utils.JsonUtils;
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
				maxTick = Math.max(maxTick, comp.stat(ReactorStat.MAX_COMPONENT_DURABILITY, this, x, y).intValue() * tickrate());
				prediction.heatPerTick += comp.stat(ReactorStat.HEAT_PRODUCTION, this, x, y).intValue();
				breeding = true;
				prediction.energyPerTick += comp.stat(ReactorStat.ENERGY_PRODUCTION, this, x, y).floatValue();
				breeding = false;
				prediction.totalFuelRodPulses += fuelPulse;
				prediction.totalBreedingPulses += breedingPulse;
				resetData();
				prediction.totalCellCount += comp.stat(ReactorStat.ROD_COUNT, this, x, y).intValue();
				prediction.totalIntenralFuelPulses += comp.stat(ReactorStat.PULSE_COUNT, this, x, y).intValue();
				resetData();
			}
			else if(type == ComponentType.HEAT_PACK) {
				int heat = comp.stat(ReactorStat.HEAT_PRODUCTION, this, x, y).intValue();
				prediction.heatPerTick += heat;
				prediction.heatPackHeatPerTick += heat;
			}
			else if(type == ComponentType.HEAT_VENT || type == ComponentType.HEAT_SPREAD) {
				prediction.coolingPerTick += comp.stat(ReactorStat.SELF_COOLING, this, x, y).intValue();
				prediction.reactorCoolingPerTick += comp.stat(ReactorStat.REACTOR_COOLING, this, x, y).intValue();
			}
			else if(type == ComponentType.PLATING) {
				maxCapacity += comp.stat(ReactorStat.MAX_HEAT_STORAGE, this, x, y).intValue();
				prediction.heatEffectMod *= comp.stat(ReactorStat.REACTOR_HEAT_EFFECT_MULTIPLIER, this, x, y).floatValue(); //TODO fix in ic2c this isn't implemented.
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
	
	public JsonObject save(JsonObject obj) {
		obj.addProperty("total_ticks", totalTicks);
		obj.addProperty("starting_heat", startingHeat);
		
		obj.addProperty("tick", tick);
		obj.addProperty("ticks_left", ticksLeft);
		
		obj.addProperty("heat", heat);
		obj.addProperty("max_heat", maxHeat);
		obj.addProperty("hem", heatEffectMod);
		obj.add("average_heat", avgHeat.save(new JsonObject()));
		
		obj.addProperty("production", production);
		obj.addProperty("steam_production", steamProduction);
		obj.addProperty("total_production", totalProducedEnergy);
		obj.addProperty("total_steam", totalProducedSteam);
		obj.addProperty("total_water", totalConsumedWater);
		
		obj.addProperty("is_producing", producing);
		obj.addProperty("is_exploded", isExploded);
		obj.add("broken_slots", JsonUtils.toArray(brokenSlots, JsonPrimitive::new));
		obj.addProperty("is_valid", valid);
		obj.addProperty("filled_slots", filledSlots);
		
		obj.addProperty("simulating", simulating);
		obj.addProperty("breeding", breeding);
		obj.addProperty("fuel_pulse", fuelPulse);
		obj.addProperty("breeding_pulse", breedingPulse);
		
		obj.add("prediction", prediction.save(new JsonObject()));
		
		obj.addProperty("width", width);
		obj.addProperty("height", height);
		JsonArray inv = new JsonArray();
		for(int i = 0,m=items.length;i<m;i++) {
			IReactorComponent comp = items[i];
			if(comp == null) continue;
			JsonObject saved = comp.save();
			saved.addProperty("slot", i);
			saved.addProperty("id", ComponentRegistry.INSTANCE.save(comp));
			inv.add(saved);
		}
		obj.add("inv", inv);
		return obj;
	}
	
	public void load(JsonObject obj) {
		totalTicks = JsonUtils.get(obj, "total_ticks", 0);
		startingHeat = JsonUtils.get(obj, "starting_heat", 0);
		
		tick = JsonUtils.get(obj, "tick", 0);
		ticksLeft = JsonUtils.get(obj, "ticks_left", 0);
		
		heat = JsonUtils.get(obj, "heat", 0);
		maxHeat = JsonUtils.get(obj, "max_heat", 0);
		heatEffectMod = JsonUtils.get(obj, "hem", 1F);
		avgHeat.load(JsonUtils.getMap(obj, "average_heat"));
		
		production = JsonUtils.get(obj, "production", 0F);
		steamProduction = JsonUtils.get(obj, "steam_production", 0);
		totalProducedEnergy = JsonUtils.get(obj, "total_production", 0L);
		totalProducedSteam = JsonUtils.get(obj, "total_steam", 0L);
		totalConsumedWater = JsonUtils.get(obj, "total_water", 0L);
		
		producing = JsonUtils.get(obj, "is_producing", false);
		isExploded = JsonUtils.get(obj, "is_exploded", false);
		brokenSlots.clear();
		JsonUtils.fromArray(JsonUtils.getList(obj, "broken_slots"), brokenSlots, JsonElement::getAsInt);
		valid = JsonUtils.get(obj, "is_valid", false);
		filledSlots = JsonUtils.get(obj, "filled_slots", 0L);
		
		simulating = JsonUtils.get(obj, "simulating", false);
		breeding = JsonUtils.get(obj, "breeding", false);
		fuelPulse = JsonUtils.get(obj, "fuel_pulse", 0);
		breedingPulse = JsonUtils.get(obj, "breeding_pulse", 0);
		
		prediction.load(JsonUtils.getMap(obj, "prediction"));
		
		width = JsonUtils.get(obj, "width", 0);
		height = JsonUtils.get(obj, "height", 0);
		items = new IReactorComponent[width * height];
		for(JsonElement entry : JsonUtils.getList(obj, "inv")) {
			JsonObject item = entry.getAsJsonObject();
			IReactorComponent comp = ComponentRegistry.INSTANCE.load(JsonUtils.get(obj, "id", null));
			if(comp == null) continue;
			int slot = JsonUtils.get(item, "slot", -1);
			if(slot == -1) continue;
			items[slot] = comp;
		}
	}
}
