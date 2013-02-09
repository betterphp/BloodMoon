package uk.co.jacekk.bukkit.bloodmoon.feature;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import uk.co.jacekk.bukkit.baseplugin.v9_1.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v9_1.event.BaseListener;
import uk.co.jacekk.bukkit.baseplugin.v9_1.util.ListUtils;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;

public class SpawnOnKillListener extends BaseListener<BloodMoon> {
	
	private Random random;
	private ArrayList<DamageCause> playerCauses;
	
	public SpawnOnKillListener(BloodMoon plugin){
		super(plugin);
		
		this.random = new Random();
		this.playerCauses = new ArrayList<DamageCause>();
		
		this.playerCauses.add(DamageCause.ENTITY_ATTACK);
		this.playerCauses.add(DamageCause.MAGIC);
		this.playerCauses.add(DamageCause.POISON);
		this.playerCauses.add(DamageCause.FIRE_TICK);
		this.playerCauses.add(DamageCause.PROJECTILE);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event){
		Entity entity = event.getEntity();
		World world = entity.getWorld();
		String worldName = world.getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (entity instanceof Creature && plugin.isActive(worldName) && worldConfig.getBoolean(Config.FEATURE_SPAWN_ON_KILL_ENABLED)){
			Creature creature = (Creature) entity;
			LivingEntity target = creature.getTarget();
			EntityDamageEvent lastDamage = entity.getLastDamageCause();
			
			if (lastDamage != null && target instanceof Player && this.playerCauses.contains(lastDamage.getCause()) && worldConfig.getStringList(Config.FEATURE_SPAWN_ON_KILL_MOBS).contains(creature.getType().name().toUpperCase())){
				if (this.random.nextInt(100) < worldConfig.getInt(Config.FEATURE_SPAWN_ON_KILL_CHANCE)){
					String mobName = ListUtils.getRandom(worldConfig.getStringList(Config.FEATURE_SPAWN_ON_KILL_SPAWN));
					EntityType creatureType = EntityType.fromName(mobName.toUpperCase());
					
					if (creatureType != null){
						world.spawnEntity(creature.getLocation(), creatureType);
					}
				}
			}
		}
	}
	
}
