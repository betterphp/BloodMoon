package uk.co.jacekk.bukkit.bloodmoon.listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import uk.co.jacekk.bukkit.bloodmoon.entities.BloodMoonEntityCreeper;
import uk.co.jacekk.bukkit.bloodmoon.entities.BloodMoonEntityEnderman;
import uk.co.jacekk.bukkit.bloodmoon.entities.BloodMoonEntitySkeleton;
import uk.co.jacekk.bukkit.bloodmoon.entities.BloodMoonEntitySpider;
import uk.co.jacekk.bukkit.bloodmoon.entities.BloodMoonEntityZombie;

public class EntityListener implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onCreatureSpawn(CreatureSpawnEvent event){
		if (event.isCancelled()) return;
		
		Location location = event.getLocation();
		Entity entity = event.getEntity();
		EntityType creatureType = event.getEntityType();
		World world = location.getWorld();
		
		net.minecraft.server.World mcWorld = ((CraftWorld) world).getHandle();
		net.minecraft.server.Entity mcEntity = (((CraftEntity) entity).getHandle());
		
		if (creatureType == EntityType.CREEPER && mcEntity instanceof BloodMoonEntityCreeper == false){
			BloodMoonEntityCreeper bloodMoonEntityCreeper = new BloodMoonEntityCreeper(mcWorld);
			
			bloodMoonEntityCreeper.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((net.minecraft.server.EntityCreeper) mcEntity);
			mcWorld.addEntity(bloodMoonEntityCreeper, SpawnReason.CUSTOM);
			
			return;
		}
		
		if (creatureType == EntityType.SKELETON && mcEntity instanceof BloodMoonEntitySkeleton == false){
			BloodMoonEntitySkeleton bloodMoonEntitySkeleton = new BloodMoonEntitySkeleton(mcWorld);
			
			bloodMoonEntitySkeleton.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((net.minecraft.server.EntitySkeleton) mcEntity);
			mcWorld.addEntity(bloodMoonEntitySkeleton, SpawnReason.CUSTOM);
			
			return;
		}
		
		if (creatureType == EntityType.SPIDER && mcEntity instanceof BloodMoonEntitySpider == false){
			BloodMoonEntitySpider bloodMoonEntitySpider = new BloodMoonEntitySpider(mcWorld);
			
			bloodMoonEntitySpider.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((net.minecraft.server.EntitySpider) mcEntity);
			mcWorld.addEntity(bloodMoonEntitySpider, SpawnReason.CUSTOM);
			
			return;
		}
		
		if (creatureType == EntityType.ZOMBIE && mcEntity instanceof BloodMoonEntityZombie == false){
			BloodMoonEntityZombie bloodMoonEntityZombie = new BloodMoonEntityZombie(mcWorld);
			
			bloodMoonEntityZombie.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((net.minecraft.server.EntityZombie) mcEntity);
			mcWorld.addEntity(bloodMoonEntityZombie, SpawnReason.CUSTOM);
			
			return;
		}
		
		if (creatureType == EntityType.ENDERMAN && mcEntity instanceof BloodMoonEntityEnderman == false){
			BloodMoonEntityEnderman bloodMoonEntityEnderman = new BloodMoonEntityEnderman(mcWorld);
			
			bloodMoonEntityEnderman.setPosition(location.getX(), location.getY(), location.getZ());
			
			mcWorld.removeEntity((net.minecraft.server.EntityEnderman) mcEntity);
			mcWorld.addEntity(bloodMoonEntityEnderman, SpawnReason.CUSTOM);
			
			return;
		}
	}
	
}
