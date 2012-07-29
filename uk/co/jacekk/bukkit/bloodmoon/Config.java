package uk.co.jacekk.bukkit.bloodmoon;

import java.util.Arrays;

import uk.co.jacekk.bukkit.baseplugin.config.PluginConfigKey;

public enum Config implements PluginConfigKey {
	
	AFFECTED_WORLDS(						"affected-worlds",							Arrays.asList("world")),
	ALWAYS_ON(								"always-on",								false),
	CHANCE(									"chance",									14),
	
	FEATURE_ARROW_RATE_ENABLED(				"features.arrow-rate.enabled",				true),
	FEATURE_ARROW_RATE_MULTIPLIER(			"features.arrow-rate.multiplier",			2),
	
	FEATURE_FIRE_ARROWS_ENABLED(			"features.fire-arrows.enabled",				true),
	FEATURE_FIRE_ARROWS_IGNITE_TARGET(		"features.fire-arrows.ignite-arget",		true),
	
	FEATURE_BREAK_BLOCKS_ENABLED(			"features.break-blocks.enabled",			true),
	FEATURE_BREAK_BLOCKS_REALISTIC_DROP(	"features.break-blocks.realistic-drop",		true),
	FEATURE_BREAK_BLOCKS_MOBS(				"features.break-blocks.mobs",				Arrays.asList("ZOMBIE", "SKELETON", "SPIDER", "CREEPER", "ENDERMAN")),
	FEATURE_BREAK_BLOCKS_BLOCKS(			"features.break-blocks.blocks",				Arrays.asList("WOOD")),
	
	FEATURE_DOUBLE_HEALTH_ENABLED(			"features.double-health.enabled",			true),
	FEATURE_DOUBLE_HEALTH_MOBS(				"features.double-health.mobs",				Arrays.asList("ZOMBIE", "SKELETON", "SPIDER", "CREEPER", "ENDERMAN")),
	
	FEATURE_MORE_SPAWNING_ENABLED(			"features.more-spawning.enabled",			true),
	FEATURE_MORE_SPAWNING_MULTIPLIER(		"features.more-spawning.multiplier",		2),
	FEATURE_MORE_SPAWNING_MOBS(				"features.more-spawning.mobs",				Arrays.asList("ZOMBIE", "SKELETON", "SPIDER", "CREEPER", "ENDERMAN")),
	
	FEATURE_MORE_EXP_ENABLED(				"features.more-exp.enabled",				true),
	FEATURE_MORE_EXP_IGNORE_SPAWNERS(		"features.more-exp.ignore-spawners",		false),
	FEATURE_MORE_EXP_MULTIPLIER(			"features.more-exp.multiplier",				2),
	
	FEATURE_SWORD_DAMAGE_ENABLED(			"features.sword-damage.enabled",			true),
	FEATURE_SWORD_DAMAGE_MOBS(				"features.sword-damage.mobs",				Arrays.asList("ZOMBIE", "SKELETON", "SPIDER", "CREEPER", "ENDERMAN")),
	FEATURE_SWORD_DAMAGE_CHANCE(			"features.sword-damage.chance",				10),
	
	FEATURE_SUPER_CREEPERS_ENABLED(			"features.super-creepers.enabled",			true),
	FEATURE_SUPER_CREEPERS_POWER(			"features.super-creepers.power",			4.0D),
	
	FEATURE_SPAWN_ON_KILL_ENABLED(			"features.spawn-on-kill.enabled",			true),
	FEATURE_SPAWN_ON_KILL_MOBS(				"features.spawn-on-kill.mobs",				Arrays.asList("ZOMBIE", "SKELETON", "SPIDER", "CREEPER", "ENDERMAN")),
	FEATURE_SPAWN_ON_KILL_CHANCE(			"features.spawn-on-kill.chance",			10),
	FEATURE_SPAWN_ON_KILL_SPAWN(			"features.spawn-on-kill.spawn",				Arrays.asList("ZOMBIE", "SKELETON", "SPIDER", "CREEPER", "ENDERMAN")),
	
	FEATURE_SPAWN_ON_SLEEP_ENABLED(			"features.spawn-on-sleep.enabled",			true),
	FEATURE_SPAWN_ON_SLEEP_SPAWN(			"features.spawn-on-sleep.spawn",			Arrays.asList("ZOMBIE")),
	
	FEATURE_LOCK_IN_WORLD_ENABLED(			"features.lock-in-world.enabled",			false);
	
	private String key;
	private Object defaultValue;
	
	private Config(String key, Object defaultValue){
		this.key = key;
		this.defaultValue = defaultValue;
	}
	
	public Object getDefault(){
		return this.defaultValue;
	}
	
	public String getKey(){
		return this.key;
	}
	
}
