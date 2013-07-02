package uk.co.jacekk.bukkit.bloodmoon.entity;

import java.util.Random;

import net.minecraft.server.v1_6_R1.EntityLiving;

import org.bukkit.craftbukkit.v1_6_R1.entity.CraftLivingEntity;

import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;

public abstract class BloodMoonEntityLiving {
	
	protected BloodMoon plugin;
	protected EntityLiving nmsEntity;
	protected CraftLivingEntity bukkitEntity;
	protected BloodMoonEntityType type;
	
	protected Random rand;
	
	public BloodMoonEntityLiving(BloodMoon plugin, EntityLiving nmsEntity, CraftLivingEntity bukkitEntity, BloodMoonEntityType type){
		this.plugin = plugin;
		this.nmsEntity = nmsEntity;
		this.bukkitEntity = bukkitEntity;
		this.type = type;
		
		this.rand = new Random();
	}
	
	public abstract void onTick();
	
}
