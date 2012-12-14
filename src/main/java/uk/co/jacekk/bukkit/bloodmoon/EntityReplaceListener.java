package uk.co.jacekk.bukkit.bloodmoon;

import net.minecraft.server.v1_4_5.Entity;
import net.minecraft.server.v1_4_5.EntityLiving;
import net.minecraft.server.v1_4_5.World;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_4_5.CraftWorld;
import org.bukkit.craftbukkit.v1_4_5.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import uk.co.jacekk.bukkit.baseplugin.v5.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.entities.BloodMoonEntity;

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
					EntityLiving customEntity = bloodMoonEntity.getBloodMoonClass().getConstructor(World.class).newInstance(world);
					
					customEntity.setPosition(location.getX(), location.getY(), location.getZ());
					customEntity.bG();
					
					world.removeEntity(entity);
					world.addEntity(customEntity, SpawnReason.CUSTOM);
				}catch (Exception e){
					e.printStackTrace();
				}
				
				return;
			}
		}
	}
	
}
