package uk.co.jacekk.bukkit.bloodmoon.feature;

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

import uk.co.jacekk.bukkit.baseplugin.v7.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v7.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;

public class SwordDamageListener extends BaseListener<BloodMoon> {
	
	private Random random;
	
	public SwordDamageListener(BloodMoon plugin){
		super(plugin);
		
		this.random = new Random();
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityDamage(EntityDamageEvent event){
		Entity entity = event.getEntity();
		String worldName = entity.getWorld().getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (event.getCause() == DamageCause.ENTITY_ATTACK && plugin.isActive(worldName) && worldConfig.getBoolean(Config.FEATURE_SWORD_DAMAGE_ENABLED)){
			if (entity instanceof Creature){
				Creature creature = (Creature) entity;
				String creatureName = creature.getType().name().toUpperCase();
				LivingEntity target = creature.getTarget();
				
				if (target instanceof Player){
					Player player = (Player) target;
					ItemStack item = player.getItemInHand();
					String itemName = item.getType().name().toUpperCase();
					
					if (worldConfig.getStringList(Config.FEATURE_SPAWN_ON_KILL_MOBS).contains(creatureName) && itemName.endsWith("_SWORD") && this.random.nextInt(100) <= worldConfig.getInt(Config.FEATURE_SWORD_DAMAGE_CHANCE)){
						short damage = item.getDurability();
						short remove = (short) (item.getType().getMaxDurability() / 50);
						
						item.setDurability((short) ((damage > remove) ? damage - remove : 1));
					}
				}
			}
		}
	}
	
}