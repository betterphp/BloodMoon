package uk.co.jacekk.bukkit.bloodmoon;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import uk.co.jacekk.bukkit.baseplugin.v5.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.entities.BloodMoonEntityCreeper;
import uk.co.jacekk.bukkit.bloodmoon.entities.BloodMoonEntityEnderman;
import uk.co.jacekk.bukkit.bloodmoon.entities.BloodMoonEntitySkeleton;
import uk.co.jacekk.bukkit.bloodmoon.entities.BloodMoonEntitySpider;
import uk.co.jacekk.bukkit.bloodmoon.entities.BloodMoonEntityZombie;

public class EntityReplaceListener extends BaseListener<BloodMoon> {
	
	public EntityReplaceListener(BloodMoon plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event){
		Location location = event.getLocation();
		Entity entity = event.getEntity();
		EntityType creatureType = event.getEntityType();
		World world = location.getWorld();
		
		net.minecraft.server.World mcWorld = ((CraftWorld) world).getHandle();
		net.minecraft.server.Entity mcEntity = ((CraftEntity) entity).getHandle();
		
		if (creatureType == EntityType.CREEPER && !(mcEntity instanceof BloodMoonEntityCreeper)){
			BloodMoonEntityCreeper bloodMoonEntityCreeper = new BloodMoonEntityCreeper(mcWorld);
			
			bloodMoonEntityCreeper.setPosition(location.getX(), location.getY(), location.getZ());
			bloodMoonEntityCreeper.bG();
			
			mcWorld.removeEntity(mcEntity);
			mcWorld.addEntity(bloodMoonEntityCreeper, SpawnReason.CUSTOM);
			
			return;
		}
		
		if (creatureType == EntityType.SKELETON && !(mcEntity instanceof BloodMoonEntitySkeleton)){
			BloodMoonEntitySkeleton bloodMoonEntitySkeleton = new BloodMoonEntitySkeleton(mcWorld);
			
			bloodMoonEntitySkeleton.setPosition(location.getX(), location.getY(), location.getZ());
			bloodMoonEntitySkeleton.bG();
			
			mcWorld.removeEntity(mcEntity);
			mcWorld.addEntity(bloodMoonEntitySkeleton, SpawnReason.CUSTOM);
			
			return;
		}
		
		if (creatureType == EntityType.SPIDER && !(mcEntity instanceof BloodMoonEntitySpider)){
			BloodMoonEntitySpider bloodMoonEntitySpider = new BloodMoonEntitySpider(mcWorld);
			
			bloodMoonEntitySpider.setPosition(location.getX(), location.getY(), location.getZ());
			bloodMoonEntitySpider.bG();
			
			mcWorld.removeEntity(mcEntity);
			mcWorld.addEntity(bloodMoonEntitySpider, SpawnReason.CUSTOM);
			
			return;
		}
		
		if (creatureType == EntityType.ZOMBIE && !(mcEntity instanceof BloodMoonEntityZombie)){
			BloodMoonEntityZombie bloodMoonEntityZombie = new BloodMoonEntityZombie(mcWorld);
			
			bloodMoonEntityZombie.setPosition(location.getX(), location.getY(), location.getZ());
			bloodMoonEntityZombie.bG();
			
			mcWorld.removeEntity(mcEntity);
			mcWorld.addEntity(bloodMoonEntityZombie, SpawnReason.CUSTOM);
			
			return;
		}
		
		if (creatureType == EntityType.ENDERMAN && !(mcEntity instanceof BloodMoonEntityEnderman)){
			BloodMoonEntityEnderman bloodMoonEntityEnderman = new BloodMoonEntityEnderman(mcWorld);
			
			bloodMoonEntityEnderman.setPosition(location.getX(), location.getY(), location.getZ());
			bloodMoonEntityEnderman.bG();
			
			mcWorld.removeEntity(mcEntity);
			mcWorld.addEntity(bloodMoonEntityEnderman, SpawnReason.CUSTOM);
			
			return;
		}
	}
	
}
