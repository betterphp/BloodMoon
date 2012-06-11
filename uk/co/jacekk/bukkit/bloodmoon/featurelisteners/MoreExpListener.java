package uk.co.jacekk.bukkit.bloodmoon.featurelisteners;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;

import uk.co.jacekk.bukkit.baseplugin.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;

public class MoreExpListener extends BaseListener<BloodMoon> {
	
	private int multiplier;
	
	public MoreExpListener(BloodMoon plugin){
		super(plugin);
		
		this.multiplier = plugin.config.getInt(Config.FEATURE_MORE_EXP_MULTIPLIER);
		
		if (this.multiplier == 0 || this.multiplier > 100){
			this.multiplier = 1;
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event){
		Entity entity = event.getEntity();
		
		if (entity instanceof Creature && plugin.isActive(entity.getWorld())){
			event.setDroppedExp(event.getDroppedExp() * multiplier);
		}
	}
	
}
