package uk.co.jacekk.bukkit.bloodmoon.feature.spawning;

import java.util.Random;

import net.minecraft.server.v1_6_R1.EntityInsentient;
import net.minecraft.server.v1_6_R1.EntityLiving;
import net.minecraft.server.v1_6_R1.GroupDataEntity;
import net.minecraft.server.v1_6_R1.World;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_6_R1.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.Feature;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntityType;

public class MoreSpawningListener extends BaseListener<BloodMoon> {
	
	private Random random;
	
	public MoreSpawningListener(BloodMoon plugin){
		super(plugin);
		
		this.random = new Random();
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event){
		if (event.getSpawnReason() != SpawnReason.NATURAL) return;
		
		EntityType type = event.getEntityType();
		Location location = event.getLocation();
		World world = ((CraftWorld) location.getWorld()).getHandle();
		String worldName = world.getWorldData().getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (plugin.isActive(worldName) && plugin.isFeatureEnabled(worldName, Feature.MORE_SPAWNING) && worldConfig.getStringList(Config.FEATURE_MORE_SPAWNING_MOBS).contains(type.getName().toUpperCase())){
			for (int i = 0; i < Math.max(worldConfig.getInt(Config.FEATURE_MORE_SPAWNING_MULTIPLIER), 1); ++i){
				for (BloodMoonEntityType bloodMoonEntity : BloodMoonEntityType.values()){
					if (type == bloodMoonEntity.getEntityType()){
						try{
							EntityInsentient customEntity = (EntityInsentient) bloodMoonEntity.createEntity(world);
							
							if (customEntity != null){
								customEntity.setPositionRotation(location.getX() + (this.random.nextDouble() * 3) - 1.5, location.getY(), location.getZ() + (this.random.nextDouble() * 3) - 1.5, location.getYaw(), location.getPitch());
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
		}
	}
	
}
