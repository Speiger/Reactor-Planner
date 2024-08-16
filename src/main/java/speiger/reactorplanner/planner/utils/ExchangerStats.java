package speiger.reactorplanner.planner.utils;

import java.util.List;

import speiger.reactorplanner.utils.math.Vec2i;
import speiger.src.collections.objects.lists.ImmutableObjectList;

public record ExchangerStats(short id, String registryId, int self, int reactor, int capacity, List<Vec2i> offsets) {
	public static final List<Vec2i> DEFAULT_OFFSETS = new ImmutableObjectList<>(new Vec2i(-1, 0), new Vec2i(1, 0), new Vec2i(0, -1), new Vec2i(0, 1));
	
	public ExchangerStats(short id, String registryId, int self, int reactor, int capacity) {
		this(id, registryId, self, reactor, capacity, DEFAULT_OFFSETS);
	}
}
