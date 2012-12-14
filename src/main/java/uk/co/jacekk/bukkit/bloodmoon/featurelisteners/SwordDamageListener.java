package uk.co.jacekk.bukkit.bloodmoon.featurelisteners;

import java.util.Random;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import uk.co.jacekk.bukkit.baseplugin.v5.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;

public class SwordDamageListener extends BaseListener<BloodMoon> {
	
	private Random rand;
	
	public SwordDamageListener(BloodMoon plugin){
		super(plugin);
		
		this.rand = new Random();
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityDamage(EntityDamageEvent event){
		Entity entity = event.getEntity();
		
		if (event.getCause() == DamageCause.ENTITY_ATTACK && plugin.isActive(entity.getWorld().getName())){
			if (entity instanceof Creature){
				Creature creature = (Creature) entity;
				String creatureName = creature.getType().name().toUpperCase();
				LivingEntity target = creature.getTarget();
				
				if (target instanceof Player){
					Player player = (Player) target;
					ItemStack item = player.getItemInHand();
					String itemName = item.getType().name().toUpperCase();
					
					if (plugin.config.getStringList(Config.FEATURE_SPAWN_ON_KILL_MOBS).contains(creatureName) && itemName.endsWith("_SWORD") && this.rand.nextInt(100) <= plugin.config.getInt(Config.FEATURE_SWORD_DAMAGE_CHANCE)){
						short damage = item.getDurability();
						short remove = (short) (item.getType().getMaxDurability() / 50);
						
						item.setDurability((short) ((damage > remove) ? damage - remove : 1));
					}
				}
			}
		}
	}
	
}