package speiger.reactorplanner.planner;

import speiger.reactorplanner.planner.base.IReactor;
import speiger.reactorplanner.planner.base.IReactorProvider;
import speiger.reactorplanner.planner.base.ReactorType;
import speiger.reactorplanner.planner.components.base.IReactorComponent;
import speiger.reactorplanner.utils.TrackedCounter;
import speiger.src.collections.ints.lists.IntArrayList;
import speiger.src.collections.ints.lists.IntList;

public class ReactorInstance implements IReactor {
	
	//Providers
	IReactorProvider provider;
	int width;
	int height;
	
	//Inventory
	IReactorComponent[] items;
	
	//Visual Data
	public int totalTicks;
	public int heatCapacity;
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
	
	
	@Override
	public IReactorComponent item(int x, int y) {
		return items[y * width + x];
	}
	
	@Override
	public void markBroken(int x, int y) {
		brokenSlots.add(y * width + x);
	}
	
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
