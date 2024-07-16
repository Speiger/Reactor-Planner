package speiger.reactorplanner.planner;

import com.google.gson.JsonObject;

import speiger.reactorplanner.utils.JsonUtils;

public class Prediction {
	
	public long totalHeatProduced;
	public int heatPerTick;
	public int heatPackHeatPerTick;
	public int coolingPerTick;
	public int reactorCoolingPerTick;
	public float heatEffectMod = 1F;
	
	public float efficiency;
	public float totalEfficiency;
	public boolean breeder;
	
	public int totalFuelRodPulses;
	public int totalIntenralFuelPulses;
	public int totalCellCount;
	public int totalBreedingPulses;
	
	public float totalExplosionPower;
	public float explosionPower = 10;
	public float explosionMod = 1F;
	
	public long totalEnergyProduced;
	public float energyPerTick;
	
	public long totalWaterConsumed;
	public long totalSteamProduced;
	public float waterPerTick;
	public float steamPerTick;
	
	public void reset() {
		totalHeatProduced = 0L;
		heatPerTick = 0;
		heatPackHeatPerTick = 0;
		coolingPerTick = 0;
		reactorCoolingPerTick = 0;
		heatEffectMod = 1F;
		
		efficiency = 0F;
		totalEfficiency = 0F;
		breeder = false;
		
		totalFuelRodPulses = 0;
		totalIntenralFuelPulses = 0;
		totalCellCount = 0;
		totalBreedingPulses = 0;
		
		totalExplosionPower = 0F;
		explosionPower = 10;
		explosionMod = 1F;
		
		totalEnergyProduced = 0L;
		energyPerTick = 0F;
		
		totalWaterConsumed = 0L;
		totalSteamProduced = 0L;
		waterPerTick = 0F;
		steamPerTick = 0F;
	}
	
	public JsonObject save(JsonObject obj) {
		obj.addProperty("totalHeat", totalHeatProduced);
		obj.addProperty("heat", heatPerTick);
		obj.addProperty("heatPack", heatPackHeatPerTick);
		obj.addProperty("cooling", coolingPerTick);
		obj.addProperty("reactorCooling", reactorCoolingPerTick);
		obj.addProperty("heatEffectMod", heatEffectMod);
		obj.addProperty("efficiency", efficiency);
		obj.addProperty("totalEfficiency", totalEfficiency);
		obj.addProperty("breeder", breeder);
		obj.addProperty("totalFuelPulses", totalFuelRodPulses);
		obj.addProperty("intenralFuelPulses", totalIntenralFuelPulses);
		obj.addProperty("cellCount", totalCellCount);
		obj.addProperty("breedingPulses", totalBreedingPulses);
		obj.addProperty("totalExplosionStrength", totalExplosionPower);
		obj.addProperty("explosionStrength", explosionPower);
		obj.addProperty("explosionMod", explosionMod);
		obj.addProperty("totalEnergy", totalEnergyProduced);
		obj.addProperty("energyPerTick", energyPerTick);
		obj.addProperty("totalWater", totalWaterConsumed);
		obj.addProperty("totalSteam", totalSteamProduced);
		obj.addProperty("waterPerTick", waterPerTick);
		obj.addProperty("steamPerTick", steamPerTick);
		return obj;
	}
	
	public void load(JsonObject obj) {
		totalHeatProduced = JsonUtils.get(obj, "totalHeat", 0L);
		heatPerTick = JsonUtils.get(obj, "heat", 0);
		heatPackHeatPerTick = JsonUtils.get(obj, "heatPack", 0);
		coolingPerTick = JsonUtils.get(obj, "cooling", 0);
		reactorCoolingPerTick = JsonUtils.get(obj, "reactorCooling", 0);
		heatEffectMod = JsonUtils.get(obj, "heatEffectMod", 1F);
		efficiency = JsonUtils.get(obj, "efficiency", 0F);
		totalEfficiency = JsonUtils.get(obj, "totalEfficiency", 0F);
		breeder = JsonUtils.get(obj, "breeder", false);
		totalFuelRodPulses = JsonUtils.get(obj, "totalFuelPulses", 0);
		totalIntenralFuelPulses = JsonUtils.get(obj, "intenralFuelPulses", 0);
		totalCellCount = JsonUtils.get(obj, "cellCount", 0);
		totalBreedingPulses = JsonUtils.get(obj, "breedingPulses", 0);
		totalExplosionPower = JsonUtils.get(obj, "totalExplosionStrength", 0F);
		explosionPower = JsonUtils.get(obj, "explosionPower", 0F);
		explosionMod = JsonUtils.get(obj, "explosionMod", 0F);
		totalEnergyProduced = JsonUtils.get(obj, "totalEnergy", 0L);
		energyPerTick = JsonUtils.get(obj, "energyPerTick", 0F);
		totalWaterConsumed = JsonUtils.get(obj, "totalWater", 0L);
		totalSteamProduced = JsonUtils.get(obj, "totalSteam", 0L);
		waterPerTick = JsonUtils.get(obj, "waterPerTick", 0F);
		steamPerTick = JsonUtils.get(obj, "steamPerTick", 0F);
	}
}
