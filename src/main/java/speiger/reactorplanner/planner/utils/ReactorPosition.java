package speiger.reactorplanner.planner.utils;

import speiger.reactorplanner.planner.base.IReactor;
import speiger.reactorplanner.planner.components.base.IReactorComponent;

public record ReactorPosition(IReactorComponent component, int x, int y) {
	public int store(IReactor reactor, int heat) {
		return component().storeHeat(reactor, x(), y(), heat);
	}
	
	public int transferrate(IReactor reactor, double medium) {
		return (int)((medium * component().maxHeat(reactor, x(), y())) - component().heat(reactor, x(), y()));
	}
	
	public static record ReactorHeatPosition(IReactorComponent component, int x, int y, double average) {
		public int store(IReactor reactor, int heat) {
			return component().storeHeat(reactor, x(), y(), heat);
		}
		
		public int transferrate(IReactor reactor, double medium) {
			return (int)((medium * component().maxHeat(reactor, x(), y())) - component().heat(reactor, x(), y()));
		}
	}
}
