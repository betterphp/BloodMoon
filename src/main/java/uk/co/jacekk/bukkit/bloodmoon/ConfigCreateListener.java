package uk.co.jacekk.bukkit.bloodmoon;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldInitEvent;

import uk.co.jacekk.bukkit.baseplugin.v9_1.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v9_1.config.PluginConfigKey;
import uk.co.jacekk.bukkit.baseplugin.v9_1.event.BaseListener;

public class ConfigCreateListener extends BaseListener<BloodMoon> {
	
	private PluginConfigKey[] blockKeys;
	private PluginConfigKey[] itemKeys;
	private PluginConfigKey[] entityKeys;
	private PluginConfigKey[] soundKeys;
	
	public ConfigCreateListener(BloodMoon plugin){
		super(plugin);
		
		this.blockKeys = new PluginConfigKey[]{
			Config.FEATURE_BREAK_BLOCKS_BLOCKS,
		};
		
		this.itemKeys = new PluginConfigKey[]{
			Config.FEATURE_ZOMBIE_WEAPON_WEAPONS,
		};
		
		this.entityKeys = new PluginConfigKey[]{
			Config.FEATURE_MOVEMENT_SPEED_MOBS,
			Config.FEATURE_BREAK_BLOCKS_MOBS,
			Config.FEATURE_MAX_HEALTH_MOBS,
			Config.FEATURE_MORE_SPAWNING_MOBS,
			Config.FEATURE_SWORD_DAMAGE_MOBS,
			Config.FEATURE_SPAWN_ON_KILL_MOBS,
			Config.FEATURE_SPAWN_ON_KILL_SPAWN,
			Config.FEATURE_SPAWN_ON_SLEEP_SPAWN,
			Config.FEATURE_MORE_MOBS_SPAWN,
		};
		
		this.soundKeys = new PluginConfigKey[]{
			Config.FEATURE_PLAY_SOUND_SOUND,
		};
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onWorldInit(WorldInitEvent event){
		World world = event.getWorld();
		PluginConfig config = plugin.createConfig(world);
		
		for (PluginConfigKey key : this.blockKeys){
			for (String itemName : config.getStringList(key)){
				Material material = Material.getMaterial(itemName);
				
				if (material == null || !material.isBlock()){
					plugin.log.fatal(key.getKey() + " contained an invalid material '" + itemName + "' in " + world.getName() + ".yml");
					
					StringBuilder values = new StringBuilder();
					values.append("Valid Values:");
					
					for (Material type : Material.values()){
						if (type.isBlock()){
							values.append(" ");
							values.append(type.name());
						}
					}
					
					plugin.log.info(values.toString());
					
					plugin.pluginManager.disablePlugin(plugin);
					return;
				}
			}
		}
		
		for (PluginConfigKey key : this.itemKeys){
			for (String itemName : config.getStringList(key)){
				Material material = Material.getMaterial(itemName);
				
				if (material == null || material.isBlock()){
					plugin.log.fatal(key.getKey() + " contained an invalid material '" + itemName + "' in " + world.getName() + ".yml");
					
					StringBuilder values = new StringBuilder();
					values.append("Valid Values:");
					
					for (Material type : Material.values()){
						if (!type.isBlock()){
							values.append(" ");
							values.append(type.name());
						}
					}
					
					plugin.log.info(values.toString());
					
					plugin.pluginManager.disablePlugin(plugin);
					return;
				}
			}
		}
		
		for (PluginConfigKey key : this.entityKeys){
			for (String entityName : config.getStringList(key)){
				EntityType type = null;
				
				try{
					type = EntityType.valueOf(entityName);
				}catch (IllegalArgumentException e){  }
				
				if (type == null || !type.isSpawnable() || !type.isAlive()){
					plugin.log.fatal(key.getKey() + " contained an invalid entity type '" + entityName + "' in " + world.getName() + ".yml");
					
					StringBuilder values = new StringBuilder();
					values.append("Valid Values:");
					
					for (EntityType entityType : EntityType.values()){
						if (entityType.isSpawnable() && entityType.isAlive()){
							values.append(" ");
							values.append(entityType.name());
						}
					}
					
					plugin.log.info(values.toString());
					
					plugin.pluginManager.disablePlugin(plugin);
					return;
				}
			}
		}
		
		for (PluginConfigKey key : this.soundKeys){
			for (String soundName : config.getStringList(key)){
				try{
					Sound type = Sound.valueOf(soundName);
				}catch (IllegalArgumentException e){
					plugin.log.fatal(key.getKey() + " contained an invalid sound type '" + soundName + "' in " + world.getName() + ".yml");
					
					StringBuilder values = new StringBuilder();
					values.append("Valid Values:");
					
					for (Sound sound : Sound.values()){
						values.append(" ");
						values.append(sound.name());
					}
					
					plugin.log.info(values.toString());
					
					plugin.pluginManager.disablePlugin(plugin);
					return;
				}
			}
		}
	}
	
}
