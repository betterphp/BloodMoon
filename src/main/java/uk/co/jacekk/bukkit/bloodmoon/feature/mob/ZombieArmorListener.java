package uk.co.jacekk.bukkit.bloodmoon.feature.mob;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.event.BaseListener;
import uk.co.jacekk.bukkit.baseplugin.util.ListUtils;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.Feature;
import uk.co.jacekk.bukkit.bloodmoon.event.BloodMoonEndEvent;
import uk.co.jacekk.bukkit.bloodmoon.event.BloodMoonStartEvent;

public class ZombieArmorListener extends BaseListener<BloodMoon> {

	private Random random;
	
	public ZombieArmorListener(BloodMoon plugin){
		super(plugin);
		
		this.random = new Random();
	}
	
	private void giveArmor(LivingEntity entity, PluginConfig worldConfig){
		String name = ListUtils.getRandom(worldConfig.getStringList(Config.FEATURE_ZOMBIE_ARMOR_ARMOR));
		
		if (Material.getMaterial(name + "_BOOTS") == null){
			plugin.log.warn(name + " is not a valid armor name");
			return;
		}
		
		EntityEquipment equipment = entity.getEquipment();
		
		equipment.setBoots(new ItemStack(Material.getMaterial(name + "_BOOTS")));
		equipment.setLeggings(new ItemStack(Material.getMaterial(name + "_LEGGINGS")));
		equipment.setChestplate(new ItemStack(Material.getMaterial(name + "_CHESTPLATE")));
		equipment.setHelmet(new ItemStack(Material.getMaterial(name + "_HELMET")));
		
		float dropChance = worldConfig.getInt(Config.FEATURE_ZOMBIE_ARMOR_DROP_CHANCE) / 100.0f;
		
		equipment.setBootsDropChance(dropChance);
		equipment.setLeggingsDropChance(dropChance);
		equipment.setChestplateDropChance(dropChance);
		equipment.setHelmetDropChance(dropChance);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onStart(BloodMoonStartEvent event){
		String worldName = event.getWorld().getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (plugin.isFeatureEnabled(worldName, Feature.ZOMBIE_ARMOR)){
			for (LivingEntity entity : event.getWorld().getLivingEntities()){
				if (!worldConfig.getBoolean(Config.FEATURE_ZOMBIE_ARMOR_IGNORE_SPAWNERS) || plugin.getSpawnReason(entity) != SpawnReason.SPAWNER){
					if (entity.getType() == EntityType.ZOMBIE && this.random.nextInt(100) < worldConfig.getInt(Config.FEATURE_ZOMBIE_WEAPON_CHANCE)){
						this.giveArmor(entity, worldConfig);
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event){
		String worldName = event.getLocation().getWorld().getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (plugin.isActive(worldName) && plugin.isFeatureEnabled(worldName, Feature.ZOMBIE_ARMOR)){
			LivingEntity entity = event.getEntity();
			
			if (!worldConfig.getBoolean(Config.FEATURE_ZOMBIE_ARMOR_IGNORE_SPAWNERS) || event.getSpawnReason() != SpawnReason.SPAWNER){
				if (entity.getType() == EntityType.ZOMBIE && this.random.nextInt(100) < worldConfig.getInt(Config.FEATURE_ZOMBIE_WEAPON_CHANCE)){
					this.giveArmor(entity, worldConfig);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onStop(BloodMoonEndEvent event){
		String worldName = event.getWorld().getName();
		
		if (plugin.isFeatureEnabled(worldName, Feature.ZOMBIE_ARMOR)){
			for (LivingEntity entity : event.getWorld().getLivingEntities()){
				if (entity.getType() == EntityType.ZOMBIE){
					EntityEquipment equipment = entity.getEquipment();
					
					equipment.setBoots(null);
					equipment.setLeggings(null);
					equipment.setChestplate(null);
					equipment.setHelmet(null);
					
					equipment.setBootsDropChance(0.0f);
					equipment.setLeggingsDropChance(0.0f);
					equipment.setChestplateDropChance(0.0f);
					equipment.setHelmetDropChance(0.0f);
				}
			}
		}
	}
	
}
