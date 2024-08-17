package speiger.reactorplanner.planner.utils;

import java.util.List;

import speiger.reactorplanner.utils.math.Vec2i;
import speiger.src.collections.objects.lists.ImmutableObjectList;
import speiger.src.collections.objects.lists.ObjectArrayList;

public record UraniumType(int durability, float output, int pulses, int connectivityPulses, float pulseHeatMod, float explosionMod, boolean enriched, int breedingHeat, List<Vec2i> pulseArea, List<Vec2i> heatArea) {
	public static final List<Vec2i> DEFAULT_AREA = new ImmutableObjectList<>(new Vec2i(-1, 0), new Vec2i(1, 0), new Vec2i(0, -1), new Vec2i(0, 1));
	public static final List<Vec2i> ENDER_PULSES_AREA = new ImmutableObjectList<>(new Vec2i(-1, 0), new Vec2i(1, 0), new Vec2i(0, -1), new Vec2i(0, 1), new Vec2i(-1, -1), new Vec2i(1, -1), new Vec2i(-1, 1), new Vec2i(1, 1));
	public static final List<Vec2i> ENDER_HEAT_AREA = createEnderPulses();
	
	public static final UraniumType STANDARD = new UraniumType(10000, 1F, 1, 1, 1F, 2F, false);
	public static final UraniumType BLAZE = new UraniumType(10000, 1F, 1, 1, 4F, 6F, true);
	public static final UraniumType CHARCOAL = new UraniumType(20000, 0.6F, 1, 1, 0.6F, 2F, true);
	public static final UraniumType ENDER = new UraniumType(5000, 2, 1, 1, 0.25F, 2F, true, 3000, ENDER_PULSES_AREA, ENDER_HEAT_AREA);
	public static final UraniumType NETHER_STAR = new UraniumType(20000, 5, 2, 3, 1.2F, 2F, true);
	public static final UraniumType REDSTONE = new UraniumType(8000, 1, 2, 1, 0.5F, 2, true);
	
	public UraniumType(int durability, float output, int pulses, int connectivityPulses, float pulseHeatMod, float explosionMod, boolean enriched) {
		this(durability, output, pulses, connectivityPulses, pulseHeatMod, explosionMod, enriched, 3000, DEFAULT_AREA, DEFAULT_AREA);
	}
	
	private static List<Vec2i> createEnderPulses() {
		List<Vec2i> builder = new ObjectArrayList<>();
		for(int x = 1;x<10;x++) {
			builder.add(new Vec2i(x, 0));
			builder.add(new Vec2i(-x, 0));
		}
		for(int y = 1;y<7;y++) {
			builder.add(new Vec2i(0, y));
			builder.add(new Vec2i(0, -y));
		}
		return new ImmutableObjectList<>(builder);
	}
}
