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

import uk.co.jacekk.bukkit.baseplugin.v5.event.BaseListener;
import uk.co.jacekk.bukkit.baseplugin.v5.util.ListUtils;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.entities.BloodMoonEntityZombie;
import uk.co.jacekk.bukkit.bloodmoon.events.BloodMoonEndEvent;
import uk.co.jacekk.bukkit.bloodmoon.events.BloodMoonStartEvent;

public class ZombieArmorListener extends BaseListener<BloodMoon> {

	private Random rand;
	
	public ZombieArmorListener(BloodMoon plugin){
		super(plugin);
		
		this.rand = new Random();
	}
	
	private void giveArmor(BloodMoonEntityZombie entity){
		String name = ListUtils.getRandom(plugin.config.getStringList(Config.FEATURE_ZOMBIE_ARMOR_ARMOR));
		
		if (Material.getMaterial(name + "_BOOTS") == null){
			plugin.log.warn(name + " is not a valid armor name");
			return;
		}
		
		entity.setEquipment(1, new ItemStack(Item.byId[Material.getMaterial(name + "_BOOTS").getId()]));
		entity.setEquipment(2, new ItemStack(Item.byId[Material.getMaterial(name + "_LEGGINGS").getId()]));
		entity.setEquipment(3, new ItemStack(Item.byId[Material.getMaterial(name + "_CHESTPLATE").getId()]));
		entity.setEquipment(4, new ItemStack(Item.byId[Material.getMaterial(name + "_HELMET").getId()]));
		
		entity.setEquipmentDropChance(1, plugin.config.getInt(Config.FEATURE_ZOMBIE_ARMOR_DROP_CHANCE) / 100.0f);
		entity.setEquipmentDropChance(2, plugin.config.getInt(Config.FEATURE_ZOMBIE_ARMOR_DROP_CHANCE) / 100.0f);
		entity.setEquipmentDropChance(3, plugin.config.getInt(Config.FEATURE_ZOMBIE_ARMOR_DROP_CHANCE) / 100.0f);
		entity.setEquipmentDropChance(4, plugin.config.getInt(Config.FEATURE_ZOMBIE_ARMOR_DROP_CHANCE) / 100.0f);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onStart(BloodMoonStartEvent event){
		for (LivingEntity entity : event.getWorld().getLivingEntities()){
			EntityLiving mcEntity = ((CraftLivingEntity) entity).getHandle();
			
			if (mcEntity instanceof BloodMoonEntityZombie && this.rand.nextInt(100) < plugin.config.getInt(Config.FEATURE_ZOMBIE_WEAPON_CHANCE)){
				this.giveArmor((BloodMoonEntityZombie) mcEntity);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event){
		if (plugin.isActive(event.getLocation().getWorld().getName())){
			EntityLiving mcEntity = ((CraftLivingEntity) event.getEntity()).getHandle();
			
			if (mcEntity instanceof BloodMoonEntityZombie && this.rand.nextInt(100) < plugin.config.getInt(Config.FEATURE_ZOMBIE_WEAPON_CHANCE)){
				this.giveArmor((BloodMoonEntityZombie) mcEntity);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onStop(BloodMoonEndEvent event){
		for (LivingEntity entity : event.getWorld().getLivingEntities()){
			EntityLiving mcEntity = ((CraftLivingEntity) entity).getHandle();
			
			if (mcEntity instanceof BloodMoonEntityZombie){
				BloodMoonEntityZombie bloodMoonEntity = (BloodMoonEntityZombie) mcEntity;
				
				bloodMoonEntity.setEquipment(1, null);
				bloodMoonEntity.setEquipment(2, null);
				bloodMoonEntity.setEquipment(3, null);
				bloodMoonEntity.setEquipment(4, null);
				
				bloodMoonEntity.setEquipmentDropChance(1, 0.0f);
				bloodMoonEntity.setEquipmentDropChance(2, 0.0f);
				bloodMoonEntity.setEquipmentDropChance(3, 0.0f);
				bloodMoonEntity.setEquipmentDropChance(4, 0.0f);
			}
		}
	}
	
}
