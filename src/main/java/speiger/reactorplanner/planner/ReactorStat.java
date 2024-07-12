package speiger.reactorplanner.planner;

public enum ReactorStat
{
	//Rods
	HEAT_PRODUCTION(false),
	ENERGY_PRODUCTION(true),
	STEAM_PRODUCTION(true),
	ROD_COUNT(false),
	PULSE_COUNT(false),
	
	//Vents
	SELF_COOLING(false),
	REACTOR_COOLING(false),
	PART_COOLING(false),
	
	//Exchangers
	PART_BALANCING(false),
	REACTOR_BALANCING(false),
	
	//Storage
	HEAT_STORAGE(false),
	MAX_HEAT_STORAGE(false),
	
	//Misc
	REACTOR_HEAT_EFFECT_MODIFIER(true),
	REACTOR_HEAT_EFFECT_MULTIPLIER(true),
	MAX_COMPONENT_DURABILITY(false),
	RECHARGEABLE(false),
	
	//SpecialStuff
	ENERGY_USAGE(true),
	WATER_CONSUMPTION(true),
	WATER_STORAGE(true);
	
	boolean isFloat;
	
	ReactorStat(boolean isFloat) {
		this.isFloat = isFloat;
	}
	
	public Number createStat(Number nr) {
		return isFloat ? Float.valueOf(nr.floatValue()) : Integer.valueOf(nr.intValue());
	}
	
	public boolean isFloat() { 
		return isFloat;
	}
}