package speiger.reactorplanner.planner.base;

public interface IReactorProvider {
	public int width();
	public int height();
	
	public long time(long time);
	public int energyProduction();
	public ReactorType type();
}
