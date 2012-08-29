package uk.co.jacekk.bukkit.bloodmoon.featurelisteners;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import uk.co.jacekk.bukkit.baseplugin.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;

public class DoubleHealthListener extends BaseListener<BloodMoon> {
	
	public DoubleHealthListener(BloodMoon plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityDamage(EntityDamageEvent event){
		Entity entity = event.getEntity();
		List<DamageCause> causes = Arrays.asList(
										DamageCause.ENTITY_ATTACK,
										DamageCause.MAGIC,
										DamageCause.POISON,
										DamageCause.FIRE_TICK,
										DamageCause.PROJECTILE
									);
		
		if (entity instanceof Creature && causes.contains(event.getCause()) && plugin.isActive(entity.getWorld().getName())){
			Creature creature = (Creature) entity;
			
			if (creature.getTarget() instanceof Player){
				event.setDamage((int) Math.floor(event.getDamage() / 2F));
				
				if (creature.getHealth() < 0){
					creature.setHealth(0);
				}
			}
		}
	}
	
}
