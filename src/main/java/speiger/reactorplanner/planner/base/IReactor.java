package speiger.reactorplanner.planner.base;

import speiger.reactorplanner.planner.components.base.IReactorComponent;

public interface IReactor {
	public IReactorComponent item(int x, int y);
	public void markBroken(int x, int y);
	public void breedingPulse();
	public void fuelPulse();
	
	public int getHeat();
	public void setHeat(int newHeat);
	public void addHeat(int heat);
	
	public int getMaxHeat();
	public void setMaxHeat(int heat);
	
	public float getHeatEffectModifier();
	public void setHeatEffectModifier(float hem);
	
	public void addOutput(float output);
	public float getEnergyOutput();
	public void addSteam(int amount);
	public int consumeWater(int amount);
	
	public boolean isProducingEnergy();
	public ReactorType reactorType();
	public boolean isSimulatingPulses();
	
	public long gameTime();
}
