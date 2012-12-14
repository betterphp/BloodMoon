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

public class ZombieWeaponListener extends BaseListener<BloodMoon> {
	
	private Random rand;
	
	public ZombieWeaponListener(BloodMoon plugin){
		super(plugin);
		
		this.rand = new Random();
	}
	
	private void giveWeapon(BloodMoonEntityZombie entity){
		String name = ListUtils.getRandom(plugin.config.getStringList(Config.FEATURE_ZOMBIE_WEAPON_WEAPONS));
		Material type = Material.getMaterial(name);
		
		if (type == null || type.isBlock()){
			plugin.log.warn(name + " is not a valid item name");
			return;
		}
		
		entity.setEquipment(0, new ItemStack(Item.byId[type.getId()]));
		entity.setEquipmentDropChance(0, plugin.config.getInt(Config.FEATURE_ZOMBIE_WEAPON_DROP_CHANCE) / 100.0f);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onStart(BloodMoonStartEvent event){
		for (LivingEntity entity : event.getWorld().getLivingEntities()){
			EntityLiving mcEntity = ((CraftLivingEntity) entity).getHandle();
			
			if (mcEntity instanceof BloodMoonEntityZombie && this.rand.nextInt(100) < plugin.config.getInt(Config.FEATURE_ZOMBIE_WEAPON_CHANCE)){
				this.giveWeapon((BloodMoonEntityZombie) mcEntity);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event){
		if (plugin.isActive(event.getLocation().getWorld().getName())){
			EntityLiving mcEntity = ((CraftLivingEntity) event.getEntity()).getHandle();
			
			if (mcEntity instanceof BloodMoonEntityZombie && this.rand.nextInt(100) < plugin.config.getInt(Config.FEATURE_ZOMBIE_WEAPON_CHANCE)){
				this.giveWeapon((BloodMoonEntityZombie) mcEntity);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onStop(BloodMoonEndEvent event){
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
