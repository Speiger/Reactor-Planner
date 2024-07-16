package speiger.reactorplanner.planner.base;

public enum ReactorType
{
	ELECTRIC(1),
	STEAM(20);
	
	int tickrate;

	private ReactorType(int tickrate) {
		this.tickrate = tickrate;
	}
	
	public int tickrate() { return tickrate; }
}