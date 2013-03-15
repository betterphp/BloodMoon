package uk.co.jacekk.bukkit.bloodmoon.entity;

import org.bukkit.entity.EntityType;

import uk.co.jacekk.bukkit.baseplugin.v9_1.util.ReflectionUtils;

import net.minecraft.server.v1_5_R1.EntityLiving;
import net.minecraft.server.v1_5_R1.EntityTypes;
import net.minecraft.server.v1_5_R1.World;

public enum BloodMoonEntityType {
	
	CREEPER("Creeper", 50, EntityType.CREEPER, net.minecraft.server.v1_5_R1.EntityCreeper.class, uk.co.jacekk.bukkit.bloodmoon.nms.EntityCreeper.class),
	ENDERMAN("Enderman", 58, EntityType.ENDERMAN, net.minecraft.server.v1_5_R1.EntityEnderman.class, uk.co.jacekk.bukkit.bloodmoon.nms.EntityEnderman.class),
	SKELETON("Skeleton", 51, EntityType.SKELETON, net.minecraft.server.v1_5_R1.EntitySkeleton.class, uk.co.jacekk.bukkit.bloodmoon.nms.EntitySkeleton.class),
	SPIDER("Spider", 52, EntityType.SPIDER, net.minecraft.server.v1_5_R1.EntitySpider.class, uk.co.jacekk.bukkit.bloodmoon.nms.EntitySpider.class),
	ZOMBIE("Zombie", 54, EntityType.ZOMBIE, net.minecraft.server.v1_5_R1.EntityZombie.class, uk.co.jacekk.bukkit.bloodmoon.nms.EntityZombie.class);
	
	private String name;
	private int id;
	private EntityType entityType;
	private Class<? extends EntityLiving> nmsClass;
	private Class<? extends EntityLiving> bloodMoonClass;
	
	private BloodMoonEntityType(String name, int id, EntityType entityType, Class<? extends EntityLiving> nmsClass, Class<? extends EntityLiving> bloodMoonClass){
		this.name = name;
		this.id = id;
		this.entityType = entityType;
		this.nmsClass = nmsClass;
		this.bloodMoonClass = bloodMoonClass;
	}
	
	public static void registerEntities(){
		for (BloodMoonEntityType entity : values()){
			try{
				ReflectionUtils.invokeMethod(EntityTypes.class, "a", Void.class, null, new Class<?>[]{Class.class, String.class, int.class}, new Object[]{entity.getBloodMoonClass(), entity.getName(), entity.getID()});
			}catch (Exception e){
				e.printStackTrace();
			}
		}
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
	
	public Class<? extends EntityLiving> getNMSClass(){
		return this.nmsClass;
	}
	
	public Class<? extends EntityLiving> getBloodMoonClass(){
		return this.bloodMoonClass;
	}
	
	public EntityLiving createEntity(World world){
		try{
			return this.getBloodMoonClass().getConstructor(World.class).newInstance(world);
		}catch (Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
}