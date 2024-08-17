package speiger.reactorplanner.mods;

import speiger.reactorplanner.planner.base.ComponentRegistry;
import speiger.reactorplanner.planner.components.ic2c.v3.Condensator;
import speiger.reactorplanner.planner.components.ic2c.v3.HeatExchanger;
import speiger.reactorplanner.planner.components.ic2c.v3.HeatPack;
import speiger.reactorplanner.planner.components.ic2c.v3.HeatStorage;
import speiger.reactorplanner.planner.components.ic2c.v3.HeatVent;
import speiger.reactorplanner.planner.components.ic2c.v3.IsotopicCell;
import speiger.reactorplanner.planner.components.ic2c.v3.ReactorPlating;
import speiger.reactorplanner.planner.components.ic2c.v3.Reflector;
import speiger.reactorplanner.planner.components.ic2c.v3.Spreader;
import speiger.reactorplanner.planner.components.ic2c.v3.UraniumRod;
import speiger.reactorplanner.planner.utils.ExchangerStats;
import speiger.reactorplanner.planner.utils.UraniumType;
import speiger.reactorplanner.planner.utils.VentStats;
import speiger.reactorplanner.planner.utils.VentType;
import speiger.reactorplanner.utils.VersionedMod;

public class IC2Classic {
	public static final VersionedMod IC2_1710 = new VersionedMod("IC2Classic", "1.7.10");
	public static final VersionedMod IC2_1122 = new VersionedMod("IC2Classic", "1.12.2");
	public static final VersionedMod IC2_1192 = new VersionedMod("IC2Classic", "1.19.2");
	
	private static final VentStats HEAT_VENT = new VentStats((short)18, "heat_vent", VentType.NORMAL, 6, 0, 1000);
	private static final VentStats HEAT_VENT_CORE = new VentStats((short)19, "heat_vent_core", VentType.NORMAL, 5, 5, 1000);
	private static final VentStats HEAT_VENT_ADVANCED = new VentStats((short)20, "heat_vent_advanced", VentType.NORMAL, 12, 0, 1000);
	private static final VentStats HEAT_VENT_OVERCLOCKED = new VentStats((short)21, "heat_vent_overclocked", VentType.NORMAL, 20, 36, 1000);
	
	private static final VentStats STEAM_VENT = new VentStats((short)22, "steam_vent", VentType.STEAM, 12, 0, 1100);
	private static final VentStats STEAM_VENT_CORE = new VentStats((short)23, "steam_vent_core", VentType.STEAM, 10, 10, 1100);
	private static final VentStats STEAM_VENT_OVERCLOCKED = new VentStats((short)24, "steam_vent_overclocked", VentType.STEAM, 40, 72, 1100);
	private static final VentStats STEAM_VENT_ADVANCED = new VentStats((short)25, "steam_vent_advanced", VentType.STEAM, 24, 0, 1100);
	
	private static final VentStats ELECTRIC_VENT = new VentStats((short)48, "eletric_vent", VentType.ELECTRIC, 12, 0, 1000);
	private static final VentStats ELECTRIC_VENT_CORE = new VentStats((short)49, "eletric_vent_core", VentType.ELECTRIC, 10, 10, 1000);
	private static final VentStats ELECTRIC_VENT_OVERCLOCKED = new VentStats((short)50, "eletric_vent_overclocked", VentType.ELECTRIC, 40, 72, 1000);
	private static final VentStats ELECTRIC_VENT_ADVANCED = new VentStats((short)51, "eletric_vent_advanced", VentType.ELECTRIC, 24, 0, 1000);
	
	private static final ExchangerStats HEAT_EXCHANGER = new ExchangerStats((short)27, "heat_exchanger", 12, 4, 2500);
	private static final ExchangerStats HEAT_EXCHANGER_CORE = new ExchangerStats((short)28, "heat_exchanger_core", 0, 72, 2500);
	private static final ExchangerStats HEAT_EXCHANGER_SPREAD = new ExchangerStats((short)29, "heat_exchanger_spread", 36, 0, 2500);
	private static final ExchangerStats HEAT_EXCHANGER_ADVANCED = new ExchangerStats((short)30, "heat_exchanger_advanced", 24, 8, 2500);
	
	public static void initComponents() {
		registerVent(HEAT_VENT, IC2_1710, IC2_1122, IC2_1192);
		registerVent(HEAT_VENT_CORE, IC2_1710, IC2_1122, IC2_1192);
		registerVent(HEAT_VENT_ADVANCED, IC2_1710, IC2_1122, IC2_1192);
		registerVent(HEAT_VENT_OVERCLOCKED, IC2_1710, IC2_1122, IC2_1192);
		
		//TODO check how many versions have these with the exact same stats
		registerVent(STEAM_VENT, IC2_1192);
		registerVent(STEAM_VENT_CORE, IC2_1192);
		registerVent(STEAM_VENT_ADVANCED, IC2_1192);
		registerVent(STEAM_VENT_OVERCLOCKED, IC2_1192);
		
		//TODO check how many versions have these with the exact same stats
		registerVent(ELECTRIC_VENT, IC2_1192);
		registerVent(ELECTRIC_VENT_CORE, IC2_1192);
		registerVent(ELECTRIC_VENT_ADVANCED, IC2_1192);
		registerVent(ELECTRIC_VENT_OVERCLOCKED, IC2_1192);
		
		registerExchanger(HEAT_EXCHANGER, IC2_1710, IC2_1122, IC2_1192);
		registerExchanger(HEAT_EXCHANGER_CORE, IC2_1710, IC2_1122, IC2_1192);
		registerExchanger(HEAT_EXCHANGER_SPREAD, IC2_1710, IC2_1122, IC2_1192);
		registerExchanger(HEAT_EXCHANGER_ADVANCED, IC2_1710, IC2_1122, IC2_1192);
		
		registerCoolant((short)12, "heat_storage_single", 10000, IC2_1710, IC2_1122, IC2_1192);
		registerCoolant((short)13, "heat_storage_triple", 30000, IC2_1710, IC2_1122, IC2_1192);
		registerCoolant((short)14, "heat_storage_six", 60000, IC2_1710, IC2_1122, IC2_1192);
		
		registerCondensator((short)15, "condensator", 20000, IC2_1710, IC2_1122, IC2_1192);
		registerCondensator((short)16, "condensator_lap", 100000, IC2_1710, IC2_1122, IC2_1192);
		
		registerHeatPack((short)17, "heat_pack", IC2_1192);
		
		registerPlating((short)31, "plating", 1000, 0.95F, IC2_1710, IC2_1122, IC2_1192);
		registerPlating((short)32, "plating_containment", 500, 0.9F, IC2_1710, IC2_1122, IC2_1192);
		registerPlating((short)33, "plating_heat_capacity", 2000, 0.99F, IC2_1710, IC2_1122, IC2_1192);
		
		registerSpreader((short)26, "heat_spreader", 4, IC2_1710, IC2_1122, IC2_1192);
		
		registerReflector((short)34, "reflector", 10000, false, IC2_1710, IC2_1122, IC2_1192);
		registerReflector((short)35, "reflector_thick", 40000, false, IC2_1710, IC2_1122, IC2_1192);
		registerReflector((short)53, "reflector_iridium", 320000, true, IC2_1710, IC2_1122, IC2_1192);
		
		//TODO implement fuel cell
		
		registerIsotopic((short)36, "uranium_rod_isotopic", UraniumType.STANDARD, IC2_1710, IC2_1122, IC2_1192);
		registerIsotopic((short)43, "uranium_rod_isotopic_redstone", UraniumType.REDSTONE, IC2_1710, IC2_1122, IC2_1192);
		registerIsotopic((short)44, "uranium_rod_isotopic_blaze", UraniumType.BLAZE, IC2_1710, IC2_1122, IC2_1192);
		registerIsotopic((short)45, "uranium_rod_isotopic_ender_pearl", UraniumType.ENDER, IC2_1710, IC2_1122, IC2_1192);
		registerIsotopic((short)46, "uranium_rod_isotopic_nether_star", UraniumType.NETHER_STAR, IC2_1710, IC2_1122, IC2_1192);
		registerIsotopic((short)47, "uranium_rod_isotopic_charcoal", UraniumType.CHARCOAL, IC2_1710, IC2_1122, IC2_1192);
		
		registerRod((short)0, "uranium_rod_single", UraniumType.STANDARD, 1, IC2_1710, IC2_1122, IC2_1192);
		registerRod((short)1, "uranium_rod_dual", UraniumType.STANDARD, 2, IC2_1710, IC2_1122, IC2_1192);
		registerRod((short)2, "uranium_rod_quad", UraniumType.STANDARD, 4, IC2_1710, IC2_1122, IC2_1192);

		registerRod((short)3, "uranium_rod_redstone_single", UraniumType.REDSTONE, 1, IC2_1710, IC2_1122, IC2_1192);
		registerRod((short)4, "uranium_rod_redstone_dual", UraniumType.REDSTONE, 2, IC2_1710, IC2_1122, IC2_1192);
		registerRod((short)5, "uranium_rod_redstone_quad", UraniumType.REDSTONE, 4, IC2_1710, IC2_1122, IC2_1192);
		
		registerRod((short)6, "uranium_rod_blaze_single", UraniumType.BLAZE, 1, IC2_1710, IC2_1122, IC2_1192);
		registerRod((short)7, "uranium_rod_blaze_dual", UraniumType.BLAZE, 2, IC2_1710, IC2_1122, IC2_1192);
		registerRod((short)8, "uranium_rod_blaze_quad", UraniumType.BLAZE, 4, IC2_1710, IC2_1122, IC2_1192);
		
		registerRod((short)9, "uranium_rod_ender_pearl_single", UraniumType.ENDER, 1, IC2_1192);
		registerRod((short)10, "uranium_rod_ender_pearl_dual", UraniumType.ENDER, 2, IC2_1192);
		registerRod((short)11, "uranium_rod_ender_pearl_quad", UraniumType.ENDER, 4, IC2_1192);
		
		registerRod((short)37, "uranium_rod_nether_star_single", UraniumType.NETHER_STAR, 1, IC2_1710, IC2_1122, IC2_1192);
		registerRod((short)38, "uranium_rod_nether_star_dual", UraniumType.NETHER_STAR, 2, IC2_1710, IC2_1122, IC2_1192);
		registerRod((short)39, "uranium_rod_nether_star_quad", UraniumType.NETHER_STAR, 4, IC2_1710, IC2_1122, IC2_1192);
		
		registerRod((short)40, "uranium_rod_charcoal_single", UraniumType.ENDER, 1, IC2_1192);
		registerRod((short)41, "uranium_rod_charcoal_dual", UraniumType.ENDER, 2, IC2_1192);
		registerRod((short)42, "uranium_rod_charcoal_quad", UraniumType.ENDER, 4, IC2_1192);
	}
	
	private static void registerRod(short id, String registryId, UraniumType type, int count, VersionedMod... mods) {
		ComponentRegistry.registerComponent(registryId, id, () -> new UraniumRod(id, registryId, type, count), mods);
	}
	
	private static void registerIsotopic(short id, String registryId, UraniumType type, VersionedMod... mods) {
		ComponentRegistry.registerComponent(registryId, id, () -> new IsotopicCell(id, registryId, type.durability(), type.breedingHeat()), mods);
	}
	
	private static void registerReflector(short id, String registryId, int durability, boolean unbreakable, VersionedMod... mods) {
		ComponentRegistry.registerComponent(registryId, id, () -> new Reflector(id, registryId, durability, unbreakable), mods);
	}
	
	private static void registerSpreader(short id, String registryId, int cooling, VersionedMod... mods) {
		ComponentRegistry.registerComponent(registryId, id, () -> new Spreader(id, registryId, cooling), mods);
	}
	
	private static void registerPlating(short id, String registryId, int heatModifier, float effectModifier, VersionedMod... mods) {
		ComponentRegistry.registerComponent(registryId, id, () -> new ReactorPlating(id, registryId, heatModifier, effectModifier), mods);
	}
	
	private static void registerHeatPack(short id, String registryId, VersionedMod... mods) {
		ComponentRegistry.registerComponent(registryId, id, () -> new HeatPack(id, registryId), mods);
	}
	
	private static void registerCondensator(short id, String registryId, int capacity, VersionedMod... mods) {
		ComponentRegistry.registerComponent(registryId, id, () -> new Condensator(id, registryId, capacity), mods);
	}
	
	private static void registerCoolant(short id, String registryId, int capacity, VersionedMod... mods) {
		ComponentRegistry.registerComponent(registryId, id, () -> new HeatStorage(id, registryId, capacity), mods);
	}
	
	private static void registerVent(VentStats stats, VersionedMod... mods) {
		ComponentRegistry.registerComponent(stats.registryId(), stats.id(), () -> new HeatVent(stats), mods);
	}
	
	private static void registerExchanger(ExchangerStats stats, VersionedMod... mods) {
		ComponentRegistry.registerComponent(stats.registryId(), stats.id(), () -> new HeatExchanger(stats), mods);
	}
}