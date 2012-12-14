package uk.co.jacekk.bukkit.bloodmoon.entities;

import org.bukkit.entity.EntityType;

import net.minecraft.server.v1_4_5.EntityCreeper;
import net.minecraft.server.v1_4_5.EntityEnderman;
import net.minecraft.server.v1_4_5.EntityLiving;
import net.minecraft.server.v1_4_5.EntitySkeleton;
import net.minecraft.server.v1_4_5.EntitySpider;
import net.minecraft.server.v1_4_5.EntityZombie;

public enum BloodMoonEntity {
	
	CREEPER("Creeper", 50, EntityType.CREEPER, EntityCreeper.class, BloodMoonEntityCreeper.class),
	ENDERMAN("Enderman", 58, EntityType.ENDERMAN, EntityEnderman.class, BloodMoonEntityEnderman.class),
	SKELETON("Skeleton", 51, EntityType.SKELETON, EntitySkeleton.class, BloodMoonEntitySkeleton.class),
	SPIDER("Spider", 52, EntityType.SPIDER, EntitySpider.class, BloodMoonEntitySpider.class),
	ZOMBIE("Zombie", 54, EntityType.ZOMBIE, EntityZombie.class, BloodMoonEntityZombie.class);
	
	private String name;
	private int id;
	private EntityType entityType;
	private Class<? extends EntityLiving> nmsClass;
	private Class<? extends EntityLiving> bloodMoonClass;
	
	private BloodMoonEntity(String name, int id, EntityType entityType, Class<? extends EntityLiving> nmsClass, Class<? extends EntityLiving> bloodMoonClass){
		this.name = name;
		this.id = id;
		this.entityType = entityType;
		this.nmsClass = nmsClass;
		this.bloodMoonClass = bloodMoonClass;
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
	
}