package speiger.reactorplanner.planner.base;

import java.util.OptionalInt;

public interface IReactorProvider {
	public int width();
	public int height();
	
	public long time(long time);
	public int energyProduction();
	public float steamProduction();
	public int startingheat();
	public OptionalInt customDuration();
	public ReactorType type();
}
