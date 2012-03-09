package uk.co.jacekk.bukkit.bloodmoon.featurelisteners;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;

public class SwordDamageListener implements Listener {
	
	private BloodMoon plugin;
	
	public SwordDamageListener(BloodMoon instance){
		this.plugin = instance;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamage(EntityDamageEvent event){
		if (event.isCancelled()) return;
		
		Entity entity = event.getEntity();
		
		if (event.getCause() == DamageCause.ENTITY_ATTACK && BloodMoon.bloodMoonWorlds.contains(entity.getWorld().getName())){
			if (entity instanceof Creature){
				Creature creature = (Creature) entity;
				
				LivingEntity target = creature.getTarget();
				
				if (target instanceof Player){
					Player player = (Player) target;
					
					ItemStack item = player.getItemInHand();
					
					// 190 character line :D
					if (BloodMoon.config.isCreatureOnMobList("features.sword-damage.mobs", creature) && plugin.rand.nextInt(100) <= BloodMoon.config.getInt("features.sword-damage.chance")){
						short damage = item.getDurability();
						
						switch (item.getType()){
							case DIAMOND_SWORD:
								if (damage > BloodMoon.config.getInt("features.sword-damage.damage.diamond")){
									item.setDurability((short) (item.getDurability() - BloodMoon.config.getInt("features.sword-damage.damage.diamond")));
								}
							case IRON_SWORD:
								if (damage > BloodMoon.config.getInt("features.sword-damage.damage.iron")){
									item.setDurability((short) (item.getDurability() - BloodMoon.config.getInt("features.sword-damage.damage.iron")));
								}
							case GOLD_SWORD:
								if (damage > BloodMoon.config.getInt("features.sword-damage.damage.gold")){
									item.setDurability((short) (item.getDurability() - BloodMoon.config.getInt("features.sword-damage.damage.gold")));
								}
							case STONE_SWORD:
								if (damage > BloodMoon.config.getInt("features.sword-damage.damage.stone")){
									item.setDurability((short) (item.getDurability() - BloodMoon.config.getInt("features.sword-damage.damage.stone")));
								}
							case WOOD_SWORD:
								if (damage > BloodMoon.config.getInt("features.sword-damage.damage.wood")){
									item.setDurability((short) (item.getDurability() - BloodMoon.config.getInt("features.sword-damage.damage.wood")));
								}
						}
					}
				}
			}
		}
	}
	
}
