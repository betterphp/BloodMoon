package uk.co.jacekk.bukkit.bloodmoon.entity;

import java.util.List;
import java.util.Map;

import net.minecraft.server.v1_8_R1.BiomeBase;
import net.minecraft.server.v1_8_R1.BiomeMeta;
import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.EntityTypes;
import net.minecraft.server.v1_8_R1.GroupDataEntity;
import net.minecraft.server.v1_8_R1.World;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import uk.co.jacekk.bukkit.baseplugin.util.ReflectionUtils;
import uk.co.jacekk.bukkit.bloodmoon.EntityRegistrationException;

public enum BloodMoonEntityType {
	
	CREEPER("Creeper", 50, EntityType.CREEPER, net.minecraft.server.v1_8_R1.EntityCreeper.class, uk.co.jacekk.bukkit.bloodmoon.nms.EntityCreeper.class),
	ENDERMAN("Enderman", 58, EntityType.ENDERMAN, net.minecraft.server.v1_8_R1.EntityEnderman.class, uk.co.jacekk.bukkit.bloodmoon.nms.EntityEnderman.class),
	SKELETON("Skeleton", 51, EntityType.SKELETON, net.minecraft.server.v1_8_R1.EntitySkeleton.class, uk.co.jacekk.bukkit.bloodmoon.nms.EntitySkeleton.class),
	SPIDER("Spider", 52, EntityType.SPIDER, net.minecraft.server.v1_8_R1.EntitySpider.class, uk.co.jacekk.bukkit.bloodmoon.nms.EntitySpider.class),
	ZOMBIE("Zombie", 54, EntityType.ZOMBIE, net.minecraft.server.v1_8_R1.EntityZombie.class, uk.co.jacekk.bukkit.bloodmoon.nms.EntityZombie.class),
	GHAST("Ghast", 56, EntityType.GHAST, net.minecraft.server.v1_8_R1.EntityGhast.class, uk.co.jacekk.bukkit.bloodmoon.nms.EntityGhast.class),
	BLAZE("Blaze", 61, EntityType.BLAZE, net.minecraft.server.v1_8_R1.EntityBlaze.class, uk.co.jacekk.bukkit.bloodmoon.nms.EntityBlaze.class),
	WITHER("WitherBoss", 64, EntityType.WITHER, net.minecraft.server.v1_8_R1.EntityWither.class, uk.co.jacekk.bukkit.bloodmoon.nms.EntityWither.class),
	WITCH("Witch", 66, EntityType.WITCH, net.minecraft.server.v1_8_R1.EntityWitch.class, uk.co.jacekk.bukkit.bloodmoon.nms.EntityWitch.class),
	GIANT_ZOMBIE("Giant", 53, EntityType.GIANT, net.minecraft.server.v1_8_R1.EntityGiantZombie.class, uk.co.jacekk.bukkit.bloodmoon.nms.EntityGiantZombie.class);
	
	private String name;
	private int id;
	private EntityType entityType;
	private Class<? extends EntityInsentient> nmsClass;
	private Class<? extends EntityInsentient> bloodMoonClass;
	
	private static boolean registered = false;
	
	private BloodMoonEntityType(String name, int id, EntityType entityType, Class<? extends EntityInsentient> nmsClass, Class<? extends EntityInsentient> bloodMoonClass){
		this.name = name;
		this.id = id;
		this.entityType = entityType;
		this.nmsClass = nmsClass;
		this.bloodMoonClass = bloodMoonClass;
	}
	
	public static void registerEntities() throws EntityRegistrationException {
		if (registered){
			throw new EntityRegistrationException("Already registered.");
		}
		
		Map<String, Class<?>> nameMap;
		Map<Integer, Class<?>> idMap;
		
		try{
			nameMap = ReflectionUtils.getFieldValue(EntityTypes.class, "c", Map.class, null);
			idMap = ReflectionUtils.getFieldValue(EntityTypes.class, "e", Map.class, null);
		}catch (Exception e){
			throw new EntityRegistrationException("Failed to get existing entity maps.", e);
		}
		
		for (BloodMoonEntityType entity : values()){
			try{
				nameMap.remove(entity.getName());
				idMap.remove(entity.getID());
				
				ReflectionUtils.invokeMethod(EntityTypes.class, "a", Void.class, null, new Class<?>[]{Class.class, String.class, int.class}, new Object[]{entity.getBloodMoonClass(), entity.getName(), entity.getID()});
			}catch (Exception e){
				throw new EntityRegistrationException("Failed to call EntityTypes.a() for " + entity.getName(), e);
			}
		}
		
		for (BiomeBase biomeBase : BiomeBase.getBiomes()){
			if (biomeBase == null){
				break;
			}
			
			for (String field : new String[]{"as", "at", "au", "av"}){
				try{
					@SuppressWarnings("unchecked")
					List<BiomeMeta> mobList = ReflectionUtils.getFieldValue(BiomeBase.class, field, List.class, biomeBase);
					
					for (BiomeMeta meta : mobList){
						for (BloodMoonEntityType entity : values()){
							if (entity.getNMSClass().equals(meta.b)){
								meta.b = entity.getBloodMoonClass();
							}
						}
					}
				}catch (Exception e){
					throw new EntityRegistrationException("Failed to modify biome data field " + field, e);
				}
			}
		}
		
		registered = true;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getID(){
		return this.id;
	}
	
	public EntityType getEntityType(){
		return this.entityType;
	}
	
	public Class<? extends EntityInsentient> getNMSClass(){
		return this.nmsClass;
	}
	
	public Class<? extends EntityInsentient> getBloodMoonClass(){
		return this.bloodMoonClass;
	}
	
	private EntityInsentient createEntity(World world){
		try{
			return this.getBloodMoonClass().getConstructor(World.class).newInstance(world);
		}catch (Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void spawnEntity(Location location){
		World world = ((CraftWorld) location.getWorld()).getHandle();
		
		EntityInsentient entity = this.createEntity(world);
		entity.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		entity.prepare((GroupDataEntity) null);
		world.addEntity(entity, SpawnReason.CUSTOM);
		entity.p();
	}
	
}
