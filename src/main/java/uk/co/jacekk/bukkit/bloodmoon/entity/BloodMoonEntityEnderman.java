package uk.co.jacekk.bukkit.bloodmoon.entity;

import net.minecraft.server.v1_4_R1.Entity;
import net.minecraft.server.v1_4_R1.EntityEnderman;
import net.minecraft.server.v1_4_R1.EntityHuman;
import net.minecraft.server.v1_4_R1.EntityLiving;
import net.minecraft.server.v1_4_R1.Vec3D;
import net.minecraft.server.v1_4_R1.World;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_4_R1.CraftServer;
import org.bukkit.craftbukkit.v1_4_R1.entity.CraftEnderman;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import uk.co.jacekk.bukkit.baseplugin.v9_1.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v9_1.util.ReflectionUtils;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.nms.BloodMoonNavigation;

public class BloodMoonEntityEnderman extends EntityEnderman {
	
	private BloodMoon plugin;
	private int f = 0;
	
	public BloodMoonEntityEnderman(World world){
		super(world);
		
		Plugin plugin = Bukkit.getPluginManager().getPlugin("BloodMoon");
		
		if (plugin == null || !(plugin instanceof BloodMoon)){
			this.world.removeEntity(this);
			return;
		}
		
		this.plugin = (BloodMoon) plugin;
		
		this.bukkitEntity = new CraftEnderman((CraftServer) this.plugin.server, this);
		
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
			
			Block[] blocks = new Block[3];
			
			blocks[0] = this.world.getWorld().getBlockAt((int) Math.floor(this.locX + bdx), (int) Math.floor(this.locY), (int) Math.floor(this.locZ + bdz));
			blocks[1] = blocks[0].getRelative(BlockFace.DOWN);
			blocks[2] = blocks[1].getRelative(BlockFace.DOWN);
			
			for (Block block : blocks){
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
								
								if (worldConfig.getBoolean(Config.FEATURE_BREAK_BLOCKS_DROP_ITEMS)){
									this.world.getWorld().dropItemNaturally(location, new ItemStack(type, 1, block.getData()));
								}
							}
						}
					}
				}
			}
		}
		
		super.j_();
	}
	
	private boolean d(EntityHuman entityhuman){
		if (entityhuman.inventory.armor[3] != null && entityhuman.inventory.armor[3].id == Material.PUMPKIN.getId()){
			return false;
		}
		
		Vec3D vec3d = entityhuman.i(1.0F).a();
		Vec3D vec3d1 = this.world.getVec3DPool().create(this.locX - entityhuman.locX, this.boundingBox.b + (this.length / 2.0F) - (entityhuman.locY + entityhuman.getHeadHeight()), this.locZ - entityhuman.locZ);
		double d0 = vec3d1.b();
		
		vec3d1 = vec3d1.a();
		double d1 = vec3d.b(vec3d1);
		
		return d1 > 1.0D - 0.025D / d0 ? entityhuman.n(this) : false;
	}
	
	@Override
	protected Entity findTarget(){
		String worldName = this.world.worldData.getName();
		String entityName = this.getBukkitEntity().getType().name().toUpperCase();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		float distance = 64.0f;
		
		if (plugin.isActive(worldName) && worldConfig.getBoolean(Config.FEATURE_TARGET_DISTANCE_ENABLED) && worldConfig.getStringList(Config.FEATURE_TARGET_DISTANCE_MOBS).contains(entityName)){
			distance *= worldConfig.getInt(Config.FEATURE_TARGET_DISTANCE_MULTIPLIER);
		}
		
		EntityHuman entityhuman = this.world.findNearbyVulnerablePlayer(this, distance);
		
		if (entityhuman != null){
			if (this.d(entityhuman)){
				if (this.f == 0){
					this.world.makeSound(entityhuman, "mob.endermen.stare", 1.0F, 1.0F);
				}
				
				if (this.f++ == 5){
					this.f = 0;
					this.f(true);
					return entityhuman;
				}
			}else{
				this.f = 0;
			}
		}
		
		return null;
	}
	
}
