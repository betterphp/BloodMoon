package uk.co.jacekk.bukkit.bloodmoon.featurelisteners;

import java.util.Random;

import net.minecraft.server.v1_4_5.EntityLiving;
import net.minecraft.server.v1_4_5.Item;
import net.minecraft.server.v1_4_5.ItemStack;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_4_5.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;

import uk.co.jacekk.bukkit.baseplugin.v6.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v6.event.BaseListener;
import uk.co.jacekk.bukkit.baseplugin.v6.util.ListUtils;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.entities.BloodMoonEntityZombie;
import uk.co.jacekk.bukkit.bloodmoon.events.BloodMoonEndEvent;
import uk.co.jacekk.bukkit.bloodmoon.events.BloodMoonStartEvent;

public class ZombieArmorListener extends BaseListener<BloodMoon> {

	private Random random;
	
	public ZombieArmorListener(BloodMoon plugin){
		super(plugin);
		
		this.random = new Random();
	}
	
	private void giveArmor(BloodMoonEntityZombie entity, PluginConfig worldConfig){
		String name = ListUtils.getRandom(worldConfig.getStringList(Config.FEATURE_ZOMBIE_ARMOR_ARMOR));
		
		if (Material.getMaterial(name + "_BOOTS") == null){
			plugin.log.warn(name + " is not a valid armor name");
			return;
		}
		
		entity.setEquipment(1, new ItemStack(Item.byId[Material.getMaterial(name + "_BOOTS").getId()]));
		entity.setEquipment(2, new ItemStack(Item.byId[Material.getMaterial(name + "_LEGGINGS").getId()]));
		entity.setEquipment(3, new ItemStack(Item.byId[Material.getMaterial(name + "_CHESTPLATE").getId()]));
		entity.setEquipment(4, new ItemStack(Item.byId[Material.getMaterial(name + "_HELMET").getId()]));
		
		for (int i = 1; i <= 4; ++i){
			entity.setEquipmentDropChance(i, worldConfig.getInt(Config.FEATURE_ZOMBIE_ARMOR_DROP_CHANCE) / 100.0f);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onStart(BloodMoonStartEvent event){
		String worldName = event.getWorld().getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (worldConfig.getBoolean(Config.FEATURE_ZOMBIE_ARMOR_ENABLED)){
			for (LivingEntity entity : event.getWorld().getLivingEntities()){
				EntityLiving mcEntity = ((CraftLivingEntity) entity).getHandle();
				
				if (mcEntity instanceof BloodMoonEntityZombie && this.random.nextInt(100) < worldConfig.getInt(Config.FEATURE_ZOMBIE_WEAPON_CHANCE)){
					this.giveArmor((BloodMoonEntityZombie) mcEntity, worldConfig);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event){
		String worldName = event.getLocation().getWorld().getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (plugin.isActive(worldName) && worldConfig.getBoolean(Config.FEATURE_ZOMBIE_ARMOR_ENABLED)){
			EntityLiving mcEntity = ((CraftLivingEntity) event.getEntity()).getHandle();
			
			if (mcEntity instanceof BloodMoonEntityZombie && this.random.nextInt(100) < worldConfig.getInt(Config.FEATURE_ZOMBIE_WEAPON_CHANCE)){
				this.giveArmor((BloodMoonEntityZombie) mcEntity, worldConfig);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onStop(BloodMoonEndEvent event){
		String worldName = event.getWorld().getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (worldConfig.getBoolean(Config.FEATURE_ZOMBIE_ARMOR_ENABLED)){
			for (LivingEntity entity : event.getWorld().getLivingEntities()){
				EntityLiving mcEntity = ((CraftLivingEntity) entity).getHandle();
				
				if (mcEntity instanceof BloodMoonEntityZombie){
					BloodMoonEntityZombie bloodMoonEntity = (BloodMoonEntityZombie) mcEntity;
					
					for (int i = 1; i <= 4; ++i){
						bloodMoonEntity.setEquipment(i, null);
						bloodMoonEntity.setEquipmentDropChance(i, 0.0f);
					}
				}
			}
		}
	}
	
}
