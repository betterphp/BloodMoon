package uk.co.jacekk.bukkit.bloodmoon;

import net.minecraft.server.v1_6_R1.Entity;
import net.minecraft.server.v1_6_R1.EntityInsentient;
import net.minecraft.server.v1_6_R1.EntityLiving;
import net.minecraft.server.v1_6_R1.GroupDataEntity;
import net.minecraft.server.v1_6_R1.World;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_6_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_6_R1.entity.CraftLivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.world.WorldInitEvent;

import uk.co.jacekk.bukkit.baseplugin.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntityType;

public class EntityReplaceListener extends BaseListener<BloodMoon> {
	
	public EntityReplaceListener(BloodMoon plugin){
		super(plugin);
	}
	
	private void replaceEntity(LivingEntity livingEntity){
		Location location = livingEntity.getLocation();
		EntityType creatureType = livingEntity.getType();
		
		Entity entity = ((CraftLivingEntity) livingEntity).getHandle();
		World world = ((CraftWorld) location.getWorld()).getHandle();
		
		for (BloodMoonEntityType bloodMoonEntity : BloodMoonEntityType.values()){
			if (creatureType == bloodMoonEntity.getEntityType() && entity.getClass().equals(bloodMoonEntity.getNMSClass())){
				try{
					EntityInsentient customEntity = (EntityInsentient) bloodMoonEntity.createEntity(world);
					
					world.removeEntity(entity);
					
					if (customEntity != null){
						customEntity.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
						customEntity.a((GroupDataEntity) null);
						world.addEntity(customEntity, SpawnReason.CUSTOM);
						customEntity.p();
					}
				}catch (Exception e){
					e.printStackTrace();
				}
				
				return;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event){
		if (event.getSpawnReason() == SpawnReason.NATURAL){
			this.replaceEntity(event.getEntity());
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onWorldInit(WorldInitEvent event){
		for (LivingEntity entity : event.getWorld().getLivingEntities()){
			this.replaceEntity(entity);
		}
	}
	
}
