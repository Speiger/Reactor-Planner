package speiger.reactorplanner.planner.base;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import speiger.reactorplanner.mods.IC2Classic;
import speiger.reactorplanner.planner.components.base.IReactorComponent;
import speiger.reactorplanner.utils.VersionedMod;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.maps.interfaces.Object2ObjectMap;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.collections.shorts.maps.interfaces.Short2ObjectMap;

public class ComponentRegistry {
	public static final ComponentRegistry INSTANCE = new ComponentRegistry();
	private static final Object2ObjectMap<VersionedMod, List<RegistryComponent>> REGISTRY = Object2ObjectMap.builder().linkedMap();
	VersionedMod currentlyLoaded = null;
	Object2ObjectMap<String, Supplier<IReactorComponent>> idToComponent = Object2ObjectMap.builder().linkedMap();
	Short2ObjectMap<Supplier<IReactorComponent>> numericIdToComponent = Short2ObjectMap.builder().linkedMap();
	
	public static void loadComponents() {
		//Idea being. Instead of writing all components here, we simply link to mods
		IC2Classic.initComponents();
	}
	
	//TODO consider Addon selection?
	public static void registerComponent(String id, short numberId, Supplier<IReactorComponent> provider, VersionedMod...mods) {
		RegistryComponent component = new RegistryComponent(id, numberId, provider, mods);
		for(VersionedMod mod : mods) {
			REGISTRY.supplyIfAbsent(mod, ObjectArrayList::new).add(component);
		}
	}
	
	public void reload(VersionedMod mod) {
		if(Objects.equals(currentlyLoaded, mod)) return;
		currentlyLoaded = mod;
		idToComponent.clear();
		numericIdToComponent.clear();
		for(RegistryComponent entry : REGISTRY.getOrDefault(mod, ObjectLists.empty())) {
			String id = entry.id();
			idToComponent.putIfAbsent(id, entry.supplier());
			numericIdToComponent.putIfAbsent(entry.numberId(), entry.supplier());
		}
	}
	
	public IReactorComponent load(String id) {
		Supplier<IReactorComponent> provider = idToComponent.get(id);
		return provider == null ? null : provider.get();
	}
	
	public String save(IReactorComponent comp) {
		return comp == null ? null : comp.registryId();
	}
	
	public short toId(IReactorComponent comp) {
		return comp == null ? -1 : comp.id();
	}
	
	public IReactorComponent fromId(short id) {
		Supplier<IReactorComponent> provider = numericIdToComponent.get(id);
		return provider == null ? null : provider.get();
	}
	
	private static record RegistryComponent(String id, short numberId, Supplier<IReactorComponent> supplier, VersionedMod...mods) {}
}
