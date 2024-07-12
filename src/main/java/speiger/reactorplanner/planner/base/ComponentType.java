package speiger.reactorplanner.planner.base;

import java.util.List;

import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;

public record ComponentType(int index) {
	private static final ObjectList<ComponentType> KNOWN_TYPES = new ObjectArrayList<>();
	public static final ComponentType FUEL_ROD = new ComponentType();
	public static final ComponentType COOLANT_CELL = new ComponentType();
	public static final ComponentType CONDENSATOR = new ComponentType();
	public static final ComponentType HEAT_PACK = new ComponentType();
	public static final ComponentType HEAT_VENT = new ComponentType();
	public static final ComponentType HEAT_SPREAD = new ComponentType();
	public static final ComponentType HEAT_EXCHANGER = new ComponentType();
	public static final ComponentType HEAT_PUMP = new ComponentType();
	public static final ComponentType PLATING = new ComponentType();
	public static final ComponentType REFLECTORS = new ComponentType();
	public static final ComponentType ISOTOPE_CELL = new ComponentType();
	public static final ComponentType UNDEFINED = new ComponentType();
	
	public ComponentType() {
		this(KNOWN_TYPES.size());
		KNOWN_TYPES.add(this);
	}
	
	public static List<ComponentType> knownTypes() {
		return KNOWN_TYPES.unmodifiable();
	}
	
	public static int size() {
		return KNOWN_TYPES.size(); 
	}
	
	public static ComponentType byId(int index) {
		return KNOWN_TYPES.get(index % KNOWN_TYPES.size());
	}
}
