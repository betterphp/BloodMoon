package uk.co.jacekk.bukkit.bloodmoon.entity;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_4_6.CraftServer;
import org.bukkit.craftbukkit.v1_4_6.entity.CraftSpider;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import uk.co.jacekk.bukkit.baseplugin.v7.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v7.util.ReflectionUtils;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.nms.BloodMoonNavigation;

import net.minecraft.server.v1_4_6.Entity;
import net.minecraft.server.v1_4_6.EntityHuman;
import net.minecraft.server.v1_4_6.EntityLiving;
import net.minecraft.server.v1_4_6.World;

public class BloodMoonEntitySpider extends net.minecraft.server.v1_4_6.EntitySpider {
	
	private BloodMoon plugin;
	
	public BloodMoonEntitySpider(World world){
		super(world);
		
		Plugin plugin = Bukkit.getPluginManager().getPlugin("BloodMoon");
		
		if (plugin == null || !(plugin instanceof BloodMoon)){
			this.world.removeEntity(this);
			return;
		}
		
		this.plugin = (BloodMoon) plugin;
		
		this.bukkitEntity = new CraftSpider((CraftServer) this.plugin.server, this);
		
		try{
			ReflectionUtils.setFieldValue(EntityLiving.class, "navigation", this, new BloodMoonNavigation(this.plugin, this, this.world, 16.0f));
		}catch (Exception e){
			e.printStackTrace();
			this.world.removeEntity(this);
		}
	}
	
	@Override
	public void j_(){
		String worldName = this.world.worldData.getName();
		String entityName = this.getBukkitEntity().getType().name().toUpperCase();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (this.target instanceof EntityHuman && plugin.isActive(worldName) && worldConfig.getBoolean(Config.FEATURE_BREAK_BLOCKS_ENABLED) && worldConfig.getStringList(Config.FEATURE_BREAK_BLOCKS_MOBS).contains(entityName) && world.getTime() % 20 == 0 && this.world.worldData.getName().equals(this.target.world.worldData.getName())){
			LivingEntity bukkitEntity = ((LivingEntity) this.getBukkitEntity());
			Location direction = this.target.getBukkitEntity().getLocation().subtract(bukkitEntity.getLocation());
			
			double dx = direction.getX();
			double dz = direction.getY();
			
			int bdx = 0;
			int bdz = 0;
			
			if (Math.abs(dx) > Math.abs(dz)){
				bdx = (dx > 0) ? 1 : -1;
			}else{
				bdz = (dx > 0) ? 1 : -1;
			}
			
			Block block = this.world.getWorld().getBlockAt((int) Math.floor(this.locX + bdx), (int) Math.floor(this.locY), (int) Math.floor(this.locZ + bdz));
			Material type = block.getType();
			
			if (type != Material.AIR && worldConfig.getStringList(Config.FEATURE_BREAK_BLOCKS_BLOCKS).contains(type.name())){
				Location location = block.getLocation();
				
				if (this.random.nextInt(100) < 80){
					this.world.getWorld().playEffect(location, Effect.ZOMBIE_CHEW_WOODEN_DOOR, 0);
				}else{
					EntityChangeBlockEvent event = new EntityChangeBlockEvent(bukkitEntity, block, Material.AIR, (byte) 0);
					plugin.pluginManager.callEvent(event);
					
					if (!event.isCancelled()){
						this.world.getWorld().playEffect(location, Effect.ZOMBIE_DESTROY_DOOR, 0);
						
						if (worldConfig.getBoolean(Config.FEATURE_BREAK_BLOCKS_REALISTIC_DROP)){
							block.breakNaturally();
						}else{
							block.setType(Material.AIR);
							this.world.getWorld().dropItemNaturally(location, new ItemStack(type, 1, block.getData()));
						}
					}
				}
			}
		}
		
		super.j_();
	}
	
	@Override
	protected Entity findTarget(){
		float f = this.c(1.0F);
		
		if (f < 0.5F){
			String worldName = this.world.worldData.getName();
			String entityName = this.getBukkitEntity().getType().name().toUpperCase();
			PluginConfig worldConfig = plugin.getConfig(worldName);
			
			float distance = 16.0f;
			
			if (plugin.isActive(worldName) && worldConfig.getBoolean(Config.FEATURE_TARGET_DISTANCE_ENABLED) && worldConfig.getStringList(Config.FEATURE_TARGET_DISTANCE_MOBS).contains(entityName)){
				distance *= worldConfig.getInt(Config.FEATURE_TARGET_DISTANCE_MULTIPLIER);
			}
			
			return this.world.findNearbyVulnerablePlayer(this, distance);
		}
		
		return null;
	}
	
}
