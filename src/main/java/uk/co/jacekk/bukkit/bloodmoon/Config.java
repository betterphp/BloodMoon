package uk.co.jacekk.bukkit.bloodmoon;

import java.util.Arrays;

import uk.co.jacekk.bukkit.baseplugin.v8.config.PluginConfigKey;

public class Config {
	
	public static final PluginConfigKey ENABLED									= new PluginConfigKey("enabled", 								false);
	public static final PluginConfigKey ALWAYS_ON									= new PluginConfigKey("always-on",								false);
	public static final PluginConfigKey CHANCE									= new PluginConfigKey("chance",									14);
	
	public static final PluginConfigKey FEATURE_CHAT_MESSAGE_ENABLED				= new PluginConfigKey("features.chat-message.enabled",			true);
	public static final PluginConfigKey FEATURE_CHAT_MESSAGE_MESSAGE				= new PluginConfigKey("features.chat-message.message",			"&cThe bloodmoon is rising !");
	
	public static final PluginConfigKey FEATURE_ARROW_RATE_ENABLED				= new PluginConfigKey("features.arrow-rate.enabled",			true);
	public static final PluginConfigKey FEATURE_ARROW_RATE_MULTIPLIER				= new PluginConfigKey("features.arrow-rate.multiplier",			2);
	
	public static final PluginConfigKey FEATURE_FIRE_ARROWS_ENABLED				= new PluginConfigKey("features.fire-arrows.enabled",			true);
	public static final PluginConfigKey FEATURE_FIRE_ARROWS_IGNITE_TARGET			= new PluginConfigKey("features.fire-arrows.ignite-target",		true);
	
	public static final PluginConfigKey FEATURE_ZOMBIE_WEAPON_ENABLED				= new PluginConfigKey("features.zombie-weapon.enabled",			true);
	public static final PluginConfigKey FEATURE_ZOMBIE_WEAPON_CHANCE				= new PluginConfigKey("features.zombie-weapon.chance",			60);
	public static final PluginConfigKey FEATURE_ZOMBIE_WEAPON_DROP_CHANCE			= new PluginConfigKey("features.zombie-weapon.drop-chance",		25);
	public static final PluginConfigKey FEATURE_ZOMBIE_WEAPON_IGNORE_SPAWNERS		= new PluginConfigKey("features.zombie-weapon.ignore-spawners",	true);
	public static final PluginConfigKey FEATURE_ZOMBIE_WEAPON_WEAPONS				= new PluginConfigKey("features.zombie-weapon.weapons",			Arrays.asList("DIAMOND_SWORD", "GOLD_SWORD", "IRON_SWORD"));
	
	public static final PluginConfigKey FEATURE_ZOMBIE_ARMOR_ENABLED				= new PluginConfigKey("features.zombie-armor.enabled",			true);
	public static final PluginConfigKey FEATURE_ZOMBIE_ARMOR_CHANCE				= new PluginConfigKey("features.zombie-armor.chance",			60);
	public static final PluginConfigKey FEATURE_ZOMBIE_ARMOR_DROP_CHANCE			= new PluginConfigKey("features.zombie-armor.drop-chance",		7);
	public static final PluginConfigKey FEATURE_ZOMBIE_ARMOR_IGNORE_SPAWNERS		= new PluginConfigKey("features.zombie-armor.ignore-spawners",	true);
	public static final PluginConfigKey FEATURE_ZOMBIE_ARMOR_ARMOR				= new PluginConfigKey("features.zombie-armor.armor",			Arrays.asList("DIAMOND", "GOLD", "IRON"));
	
	public static final PluginConfigKey FEATURE_TARGET_DISTANCE_ENABLED			= new PluginConfigKey("features.target-distance.enabled",		true);
	public static final PluginConfigKey FEATURE_TARGET_DISTANCE_MULTIPLIER		= new PluginConfigKey("features.target-distance.multiplier",	3);
	public static final PluginConfigKey FEATURE_TARGET_DISTANCE_MOBS				= new PluginConfigKey("features.target-distance.mobs",			Arrays.asList("ZOMBIE", "SKELETON", "SPIDER", "CREEPER"));
	
	public static final PluginConfigKey FEATURE_MOVEMENT_SPEED_ENABLED			= new PluginConfigKey("features.movement-speed.enabled",		true);
	public static final PluginConfigKey FEATURE_MOVEMENT_SPEED_MULTIPLIER			= new PluginConfigKey("features.movement-speed.multiplier",		1.30d);
	public static final PluginConfigKey FEATURE_MOVEMENT_SPEED_FAST_CHANCE		= new PluginConfigKey("features.movement-speed.multiplier",		15);
	public static final PluginConfigKey FEATURE_MOVEMENT_SPEED_FAST_MULTIPLIER	= new PluginConfigKey("features.movement-speed.multiplier",		1.5d);
	public static final PluginConfigKey FEATURE_MOVEMENT_SPEED_MOBS				= new PluginConfigKey("features.movement-speed.mobs",			Arrays.asList("ZOMBIE", "SKELETON", "CREEPER"));
	
	public static final PluginConfigKey FEATURE_BREAK_BLOCKS_ENABLED				= new PluginConfigKey("features.break-blocks.enabled",			true);
	public static final PluginConfigKey FEATURE_BREAK_BLOCKS_REALISTIC_DROP		= new PluginConfigKey("features.break-blocks.realistic-drop",	true);
	public static final PluginConfigKey FEATURE_BREAK_BLOCKS_MOBS					= new PluginConfigKey("features.break-blocks.mobs",				Arrays.asList("ZOMBIE", "SKELETON", "SPIDER", "CREEPER", "ENDERMAN"));
	public static final PluginConfigKey FEATURE_BREAK_BLOCKS_BLOCKS				= new PluginConfigKey("features.break-blocks.blocks",			Arrays.asList("WOOD", "LOG", "GLASS"));
	
	public static final PluginConfigKey FEATURE_DOUBLE_HEALTH_ENABLED				= new PluginConfigKey("features.double-health.enabled",			true);
	public static final PluginConfigKey FEATURE_DOUBLE_HEALTH_MOBS				= new PluginConfigKey("features.double-health.mobs",			Arrays.asList("ZOMBIE", "SKELETON", "SPIDER", "CREEPER", "ENDERMAN"));
	
	public static final PluginConfigKey FEATURE_MORE_SPAWNING_ENABLED				= new PluginConfigKey("features.more-spawning.enabled",			true);
	public static final PluginConfigKey FEATURE_MORE_SPAWNING_MULTIPLIER			= new PluginConfigKey("features.more-spawning.multiplier",		2);
	public static final PluginConfigKey FEATURE_MORE_SPAWNING_MOBS				= new PluginConfigKey("features.more-spawning.mobs",			Arrays.asList("ZOMBIE", "SKELETON", "SPIDER", "CREEPER", "ENDERMAN"));
	
	public static final PluginConfigKey FEATURE_MORE_EXP_ENABLED					= new PluginConfigKey("features.more-exp.enabled",				true);
	public static final PluginConfigKey FEATURE_MORE_EXP_IGNORE_SPAWNERS			= new PluginConfigKey("features.more-exp.ignore-spawners",		false);
	public static final PluginConfigKey FEATURE_MORE_EXP_MULTIPLIER				= new PluginConfigKey("features.more-exp.multiplier",			2);
	
	public static final PluginConfigKey FEATURE_MORE_DROPS_ENABLED				= new PluginConfigKey("features.more-drops.enabled",			true);
	public static final PluginConfigKey FEATURE_MORE_DROPS_IGNORE_SPAWNERS		= new PluginConfigKey("features.more-drops.ignore-spawners",	false);
	public static final PluginConfigKey FEATURE_MORE_DROPS_MULTIPLIER				= new PluginConfigKey("features.more-drops.multiplier",			2);
	
	public static final PluginConfigKey FEATURE_SWORD_DAMAGE_ENABLED				= new PluginConfigKey("features.sword-damage.enabled",			true);
	public static final PluginConfigKey FEATURE_SWORD_DAMAGE_MOBS					= new PluginConfigKey("features.sword-damage.mobs",				Arrays.asList("ZOMBIE", "SKELETON", "SPIDER", "CREEPER", "ENDERMAN"));
	public static final PluginConfigKey FEATURE_SWORD_DAMAGE_CHANCE				= new PluginConfigKey("features.sword-damage.chance",			10);
	
	public static final PluginConfigKey FEATURE_SUPER_CREEPERS_ENABLED			= new PluginConfigKey("features.super-creepers.enabled",		true);
	public static final PluginConfigKey FEATURE_SUPER_CREEPERS_POWER				= new PluginConfigKey("features.super-creepers.power",			4.0D);
	public static final PluginConfigKey FEATURE_SUPER_CREEPERS_FIRE				= new PluginConfigKey("features.super-creepers.fire",			true);
	
	public static final PluginConfigKey FEATURE_SPAWN_ON_KILL_ENABLED				= new PluginConfigKey("features.spawn-on-kill.enabled",			true);
	public static final PluginConfigKey FEATURE_SPAWN_ON_KILL_MOBS				= new PluginConfigKey("features.spawn-on-kill.mobs",			Arrays.asList("ZOMBIE", "SKELETON", "SPIDER", "CREEPER", "ENDERMAN"));
	public static final PluginConfigKey FEATURE_SPAWN_ON_KILL_CHANCE				= new PluginConfigKey("features.spawn-on-kill.chance",			10);
	public static final PluginConfigKey FEATURE_SPAWN_ON_KILL_SPAWN				= new PluginConfigKey("features.spawn-on-kill.spawn",			Arrays.asList("ZOMBIE", "SKELETON", "SPIDER", "CREEPER", "ENDERMAN"));
	
	public static final PluginConfigKey FEATURE_SPAWN_ON_SLEEP_ENABLED			= new PluginConfigKey("features.spawn-on-sleep.enabled",		true);
	public static final PluginConfigKey FEATURE_SPAWN_ON_SLEEP_SPAWN				= new PluginConfigKey("features.spawn-on-sleep.spawn",			Arrays.asList("ZOMBIE"));
	
	public static final PluginConfigKey FEATURE_MORE_MOBS_ENABLED					= new PluginConfigKey("features.more-mobs.enabled",				true);
	public static final PluginConfigKey FEATURE_MORE_MOBS_CHANCE					= new PluginConfigKey("features.more-mobs.chance",				10);
	public static final PluginConfigKey FEATURE_MORE_MOBS_GROUP_SIZE				= new PluginConfigKey("features.more-mobs.group-size",			4);
	public static final PluginConfigKey FEATURE_MORE_MOBS_GROUP_VARIATION			= new PluginConfigKey("features.more-mobs.group-variation",		2);
	public static final PluginConfigKey FEATURE_MORE_MOBS_SPAWN					= new PluginConfigKey("features.more-mobs.spawn",				Arrays.asList("PIG_ZOMBIE", "BLAZE", "MAGMA_CUBE"));
	
	public static final PluginConfigKey FEATURE_LOCK_IN_WORLD_ENABLED				= new PluginConfigKey("features.lock-in-world.enabled",			false);
	
	public static final PluginConfigKey FEATURE_TEXTURE_PACK_ENABLED				= new PluginConfigKey("features.texture-pack.enabled",			false);
	public static final PluginConfigKey FEATURE_TEXTURE_PACK_NORMAL				= new PluginConfigKey("features.texture-pack.normal",			"http://bukkit.jacekk.co.uk/bloodmoon_tps/normal.zip");
	public static final PluginConfigKey FEATURE_TEXTURE_PACK_BLOODMOON			= new PluginConfigKey("features.texture-pack.bloodmoon",		"http://bukkit.jacekk.co.uk/bloodmoon_tps/bloodmoon.zip");
	
	public static final PluginConfigKey FEATURE_EXTENDED_NIGHT_ENABLED			= new PluginConfigKey("features.extended-night.enabled",		true);
	public static final PluginConfigKey FEATURE_EXTENDED_NIGHT_MIN_KILLS			= new PluginConfigKey("features.extended-night.min-kills",		16);
	public static final PluginConfigKey FEATURE_EXTENDED_NIGHT_MAX_RESETS			= new PluginConfigKey("features.extended-night.max-resets",		6);
	
	public static final PluginConfigKey FEATURE_WEATHER_ENABLED					= new PluginConfigKey("features.weather.enabled",				true);
	public static final PluginConfigKey FEATURE_WEATHER_THUNDER					= new PluginConfigKey("features.weather.thunder",				true);
	public static final PluginConfigKey FEATURE_WEATHER_RAIN						= new PluginConfigKey("features.weather.rain",					true);
	
	public static final PluginConfigKey FEATURE_DAYLIGHT_PROOF_MOBS_ENABLED		= new PluginConfigKey("features.daylist-proof-mobs.enabled",	true);
	
}
