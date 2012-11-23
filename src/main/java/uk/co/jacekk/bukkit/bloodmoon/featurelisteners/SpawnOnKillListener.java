package uk.co.jacekk.bukkit.bloodmoon.featurelisteners;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import uk.co.jacekk.bukkit.baseplugin.v5.event.BaseListener;
import uk.co.jacekk.bukkit.baseplugin.v5.util.ListUtils;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;

public class SpawnOnKillListener extends BaseListener<BloodMoon> {
	
	private Random rand;
	
	public SpawnOnKillListener(BloodMoon plugin){
		super(plugin);
		
		this.rand = new Random();
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event){
		Entity entity = event.getEntity();
		World world = entity.getWorld();
		
		if (entity instanceof Creature && plugin.isActive(world.getName())){
			Creature creature = (Creature) entity;
			LivingEntity target = creature.getTarget();
			
			List<DamageCause> causes = Arrays.asList(
											DamageCause.ENTITY_ATTACK,
											DamageCause.MAGIC,
											DamageCause.POISON,
											DamageCause.FIRE_TICK,
											DamageCause.PROJECTILE
										);
			
			if (target instanceof Player && causes.contains(entity.getLastDamageCause().getCause()) && plugin.config.getStringList(Config.FEATURE_SPAWN_ON_KILL_MOBS).contains(creature.getType().name().toUpperCase())){
				if (this.rand.nextInt(100) < plugin.config.getInt(Config.FEATURE_SPAWN_ON_KILL_CHANCE)){
					String mobName = ListUtils.getRandom(plugin.config.getStringList(Config.FEATURE_SPAWN_ON_KILL_SPAWN));
					EntityType creatureType = EntityType.fromName(mobName.toUpperCase());
					
					if (creatureType != null){
						world.spawnEntity(creature.getLocation(), creatureType);
					}
				}
			}
		}
	}
	
}
