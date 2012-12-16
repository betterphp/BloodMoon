package uk.co.jacekk.bukkit.bloodmoon.feature;

import java.util.ArrayList;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import uk.co.jacekk.bukkit.baseplugin.v6.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v6.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;

public class DoubleHealthListener extends BaseListener<BloodMoon> {
	
	private ArrayList<DamageCause> playerCauses;
	
	public DoubleHealthListener(BloodMoon plugin){
		super(plugin);
		
		this.playerCauses = new ArrayList<DamageCause>();
		
		this.playerCauses.add(DamageCause.ENTITY_ATTACK);
		this.playerCauses.add(DamageCause.MAGIC);
		this.playerCauses.add(DamageCause.POISON);
		this.playerCauses.add(DamageCause.FIRE_TICK);
		this.playerCauses.add(DamageCause.PROJECTILE);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityDamage(EntityDamageEvent event){
		Entity entity = event.getEntity();
		String worldName = entity.getWorld().getName();
		
		if (entity instanceof Creature && this.playerCauses.contains(event.getCause()) && plugin.isActive(worldName)){
			Creature creature = (Creature) entity;
			PluginConfig worldConfig = plugin.getConfig(worldName);
			
			if (creature.getTarget() instanceof Player && worldConfig.getBoolean(Config.FEATURE_DOUBLE_HEALTH_ENABLED) && worldConfig.getStringList(Config.FEATURE_DOUBLE_HEALTH_MOBS).contains(entity.getType().name().toUpperCase())){
				event.setDamage((int) Math.floor(event.getDamage() / 2F));
				
				if (creature.getHealth() < 0){
					creature.setHealth(0);
				}
			}
		}
	}
	
}
