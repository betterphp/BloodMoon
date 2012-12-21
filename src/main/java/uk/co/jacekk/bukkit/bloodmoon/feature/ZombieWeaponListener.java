package uk.co.jacekk.bukkit.bloodmoon.feature;

import java.util.Random;

import net.minecraft.server.v1_4_6.EntityLiving;
import net.minecraft.server.v1_4_6.Item;
import net.minecraft.server.v1_4_6.ItemStack;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_4_6.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;

import uk.co.jacekk.bukkit.baseplugin.v6.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v6.event.BaseListener;
import uk.co.jacekk.bukkit.baseplugin.v6.util.ListUtils;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntityZombie;
import uk.co.jacekk.bukkit.bloodmoon.event.BloodMoonEndEvent;
import uk.co.jacekk.bukkit.bloodmoon.event.BloodMoonStartEvent;

public class ZombieWeaponListener extends BaseListener<BloodMoon> {
	
	private Random random;
	
	public ZombieWeaponListener(BloodMoon plugin){
		super(plugin);
		
		this.random = new Random();
	}
	
	private void giveWeapon(BloodMoonEntityZombie entity, PluginConfig worldConfig){
		String name = ListUtils.getRandom(worldConfig.getStringList(Config.FEATURE_ZOMBIE_WEAPON_WEAPONS));
		Material type = Material.getMaterial(name);
		
		if (type == null || type.isBlock()){
			plugin.log.warn(name + " is not a valid item name");
			return;
		}
		
		entity.setEquipment(0, new ItemStack(Item.byId[type.getId()]));
		entity.setEquipmentDropChance(0, worldConfig.getInt(Config.FEATURE_ZOMBIE_WEAPON_DROP_CHANCE) / 100.0f);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onStart(BloodMoonStartEvent event){
		String worldName = event.getWorld().getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (worldConfig.getBoolean(Config.FEATURE_ZOMBIE_WEAPON_ENABLED)){
			for (LivingEntity entity : event.getWorld().getLivingEntities()){
				EntityLiving mcEntity = ((CraftLivingEntity) entity).getHandle();
				
				if (mcEntity instanceof BloodMoonEntityZombie && this.random.nextInt(100) < worldConfig.getInt(Config.FEATURE_ZOMBIE_WEAPON_CHANCE)){
					this.giveWeapon((BloodMoonEntityZombie) mcEntity, worldConfig);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event){
		String worldName = event.getLocation().getWorld().getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (plugin.isActive(worldName) && worldConfig.getBoolean(Config.FEATURE_ZOMBIE_WEAPON_ENABLED)){
			EntityLiving mcEntity = ((CraftLivingEntity) event.getEntity()).getHandle();
			
			if (mcEntity instanceof BloodMoonEntityZombie && this.random.nextInt(100) < worldConfig.getInt(Config.FEATURE_ZOMBIE_WEAPON_CHANCE)){
				this.giveWeapon((BloodMoonEntityZombie) mcEntity, worldConfig);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onStop(BloodMoonEndEvent event){
		String worldName = event.getWorld().getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (worldConfig.getBoolean(Config.FEATURE_ZOMBIE_WEAPON_ENABLED)){
			for (LivingEntity entity : event.getWorld().getLivingEntities()){
				EntityLiving mcEntity = ((CraftLivingEntity) entity).getHandle();
				
				if (mcEntity instanceof BloodMoonEntityZombie){
					BloodMoonEntityZombie bloodMoonEntity = (BloodMoonEntityZombie) mcEntity;
					
					bloodMoonEntity.setEquipment(0, null);
					bloodMoonEntity.setEquipmentDropChance(0, 0.0f);
				}
			}
		}
	}
	
}
