package uk.co.jacekk.bukkit.bloodmoon.featurelisteners;

import java.util.Arrays;
import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;

public class SpawnOnKillListener implements Listener {
	
	private BloodMoon plugin;
	
	public SpawnOnKillListener(BloodMoon instance){
		this.plugin = instance;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDeath(EntityDeathEvent event){
		Entity entity = event.getEntity();
		World world = entity.getWorld();
		
		if (entity instanceof Creature && BloodMoon.bloodMoonWorlds.contains(world.getName())){
			Creature creature = (Creature) entity;
			LivingEntity target = creature.getTarget();
			
			List<DamageCause> causes = Arrays.asList(
											DamageCause.ENTITY_ATTACK,
											DamageCause.MAGIC,
											DamageCause.POISON,
											DamageCause.FIRE_TICK,
											DamageCause.PROJECTILE
										);
			
			if (target instanceof Player && causes.contains(entity.getLastDamageCause().getCause()) && BloodMoon.config.isCreatureOnMobList("features.spawn-on-kill.mobs", creature)){
				if (plugin.rand.nextInt(100) <= BloodMoon.config.getInt("features.spawn-on-kill.chance")){
					String mobName = BloodMoon.config.getRandomStringFromList("features.spawn-on-kill.spawn");
					EntityType creatureType = EntityType.fromName(Character.toUpperCase(mobName.charAt(0)) + mobName.toLowerCase().substring(1));
					
					if (creatureType != null){
						world.spawnCreature(creature.getLocation(), creatureType);
					}
				}
			}
		}
	}
	
}
