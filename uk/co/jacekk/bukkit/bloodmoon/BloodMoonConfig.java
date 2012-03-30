package uk.co.jacekk.bukkit.bloodmoon;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Creature;

public class BloodMoonConfig {
	
	private YamlConfiguration config;
	private LinkedHashMap<String, Object> configDefaults;
	
	public BloodMoonConfig(File configFile, BloodMoon plugin){
		this.configDefaults = new LinkedHashMap<String, Object>();
		this.config = new YamlConfiguration();
		
		this.configDefaults.put("affected-worlds", Arrays.asList());
		
		this.configDefaults.put("always-on", false);
		this.configDefaults.put("bloodmoon-chance", 14);
		
		this.configDefaults.put("features.arrow-rate.enabled", true);
		this.configDefaults.put("features.arrow-rate.multiplier", 2);
		
		this.configDefaults.put("features.fire-arrows.enabled", true);
		this.configDefaults.put("features.fire-arrows.ignight-target", true);
		
		this.configDefaults.put("features.break-blocks.enabled", true);
		this.configDefaults.put("features.break-blocks.realistic-drop", false);
		this.configDefaults.put("features.break-blocks.mobs", Arrays.asList("ZOMBIE", "SKELETON", "SPIDER", "CREEPER", "ENDERMAN"));
		this.configDefaults.put("features.break-blocks.blocks", Arrays.asList("WOOD"));
		
		this.configDefaults.put("features.double-health.enabled", true);
		this.configDefaults.put("features.double-health.mobs", Arrays.asList("ZOMBIE", "SKELETON", "SPIDER", "CREEPER", "ENDERMAN"));
		
		this.configDefaults.put("features.more-spawning.enabled", true);
		this.configDefaults.put("features.more-spawning.multiplier", 2);
		
		this.configDefaults.put("features.more-exp.enabled", true);
		this.configDefaults.put("features.more-exp.multiplier", 2);
		
		this.configDefaults.put("features.sword-damage.enabled", true);
		this.configDefaults.put("features.sword-damage.mobs", Arrays.asList("ZOMBIE", "SKELETON", "SPIDER", "CREEPER", "ENDERMAN"));
		this.configDefaults.put("features.sword-damage.chance", 10);
		this.configDefaults.put("features.sword-damage.damage.diamond", 100);
		this.configDefaults.put("features.sword-damage.damage.iron", 20);
		this.configDefaults.put("features.sword-damage.damage.gold", 10);
		this.configDefaults.put("features.sword-damage.damage.stone", 20);
		this.configDefaults.put("features.sword-damage.damage.wood", 10);
		
		this.configDefaults.put("features.super-creepers.enabled", true);
		this.configDefaults.put("features.super-creepers.power", 4.0D);
		
		this.configDefaults.put("features.spawn-on-kill.enabled", true);
		this.configDefaults.put("features.spawn-on-kill.mobs", Arrays.asList("ZOMBIE", "SKELETON", "SPIDER", "CREEPER", "ENDERMAN"));
		this.configDefaults.put("features.spawn-on-kill.chance", 10);
		this.configDefaults.put("features.spawn-on-kill.spawn", Arrays.asList("ZOMBIE", "SKELETON", "SPIDER", "CREEPER", "ENDERMAN"));
		
		this.configDefaults.put("features.spawn-on-sleep.enabled", true);
		this.configDefaults.put("features.spawn-on-sleep.spawn", Arrays.asList("ZOMBIE"));
				
		this.configDefaults.put("features.lock-in-world.enabled", true);
		
		if (configFile.exists()){
			try {
				this.config.load(configFile);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		
		boolean updateNeeded = false;
		
		for (String key : this.configDefaults.keySet()){
			if (this.config.contains(key) == false){
				this.config.set(key, this.configDefaults.get(key));
				
				updateNeeded = true;
			}
		}
		
		if (updateNeeded){
			try {
				this.config.save(configFile);
				plugin.log.info("The config.yml file has been updated.");
			} catch (IOException e){
				e.printStackTrace();
			}
		}
	}
	
	public boolean getBoolean(String key){
		if (this.configDefaults.containsKey(key) == false){
			return false;
		}
		
		return this.config.getBoolean(key, (Boolean) this.configDefaults.get(key));
	}
	
	public int getInt(String key){
		if (this.configDefaults.containsKey(key) == false){
			return 0;
		}
		
		return this.config.getInt(key, (Integer) this.configDefaults.get(key));
	}
	
	public double getDouble(String key){
		if (this.configDefaults.containsKey(key) == false){
			return 0.0;
		}
		
		return this.config.getDouble(key, (Double) this.configDefaults.get(key));
	}
	
	public List<String> getStringList(String key){
		if (this.configDefaults.containsKey(key) == false){
			return new ArrayList<String>();
		}
		
		return this.config.getStringList(key);
	}
	
	public String getRandomStringFromList(String key){
		List<String> list = this.getStringList(key);
		
		return list.get((int) (Math.random() * (list.size() - 1)));
	}
	
	public boolean isCreatureOnMobList(String key, Creature entity){
		String mobName = entity.toString().toUpperCase();
		
		if (mobName.startsWith("CRAFT")){
			mobName = mobName.substring(5);
		}
		
		return this.getStringList(key).contains(mobName);
	}
	
}
