package uk.co.jacekk.bukkit.bloodmoon.featurelisteners;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import uk.co.jacekk.bukkit.baseplugin.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;

public class SwordDamageListener extends BaseListener<BloodMoon> {
	
	public SwordDamageListener(BloodMoon plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamage(EntityDamageEvent event){
		if (event.isCancelled()) return;
		
		Entity entity = event.getEntity();
		
		if (event.getCause() == DamageCause.ENTITY_ATTACK && plugin.bloodMoonActiveWorlds.contains(entity.getWorld().getName())){
			if (entity instanceof Creature){
				Creature creature = (Creature) entity;
				
				LivingEntity target = creature.getTarget();
				
				if (target instanceof Player){
					Player player = (Player) target;
					
					ItemStack item = player.getItemInHand();
					
					// 190 character line :D
					if (BloodMoon.config.isCreatureOnMobList("features.sword-damage.mobs", creature) && plugin.rand.nextInt(100) <= plugin.config.getInt(Config.FEATURE_SWORD_DAMAGE_CHANCE)){
						short damage = item.getDurability();
						
						// TODO: Take a percentage of the max uses
					}
				}
			}
		}
	}
	
}
