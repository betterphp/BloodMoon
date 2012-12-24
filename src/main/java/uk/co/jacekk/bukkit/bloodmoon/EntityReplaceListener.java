package uk.co.jacekk.bukkit.bloodmoon;

import net.minecraft.server.v1_4_6.Entity;
import net.minecraft.server.v1_4_6.EntityLiving;
import net.minecraft.server.v1_4_6.World;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_4_6.CraftWorld;
import org.bukkit.craftbukkit.v1_4_6.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import uk.co.jacekk.bukkit.baseplugin.v6.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntity;

public class EntityReplaceListener extends BaseListener<BloodMoon> {
	
	public EntityReplaceListener(BloodMoon plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event){
		Location location = event.getLocation();
		EntityType creatureType = event.getEntityType();
		
		Entity entity = ((CraftEntity) event.getEntity()).getHandle();
		World world = ((CraftWorld) location.getWorld()).getHandle();
		
		for (BloodMoonEntity bloodMoonEntity : BloodMoonEntity.values()){
			if (creatureType == bloodMoonEntity.getEntityType() && entity.getClass().equals(bloodMoonEntity.getNMSClass())){
				try{
					EntityLiving customEntity = bloodMoonEntity.createEntity(world);
					
					world.removeEntity(entity);
					
					if (customEntity != null){
						customEntity.setPosition(location.getX(), location.getY(), location.getZ());
						customEntity.bG();
						
						world.addEntity(customEntity, SpawnReason.CUSTOM);
					}
				}catch (Exception e){
					e.printStackTrace();
				}
				
				return;
			}
		}
	}
	
}
