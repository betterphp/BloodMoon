package uk.co.jacekk.bukkit.bloodmoon.featurelisteners;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;

public class MoreExpListener implements Listener {
	
private int multiplier;
	
	public MoreExpListener(){
		this.multiplier = BloodMoon.config.getInt("features.more-exp.multiplier");
		
		if (this.multiplier == 0 || this.multiplier > 100){
			this.multiplier = 1;
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event){
		Entity entity = event.getEntity();
		
		if (entity instanceof Creature && BloodMoon.bloodMoonWorlds.contains(entity.getWorld().getName())){
			event.setDroppedExp(event.getDroppedExp() * multiplier);
		}
	}
	
}
