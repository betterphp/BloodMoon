package uk.co.jacekk.bukkit.bloodmoon;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.bukkit.World;

import uk.co.jacekk.bukkit.baseplugin.BasePlugin;
import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.bloodmoon.commands.BloodMoonExecuter;
import uk.co.jacekk.bukkit.bloodmoon.entities.BloodMoonEntityCreeper;
import uk.co.jacekk.bukkit.bloodmoon.entities.BloodMoonEntityEnderman;
import uk.co.jacekk.bukkit.bloodmoon.entities.BloodMoonEntitySkeleton;
import uk.co.jacekk.bukkit.bloodmoon.entities.BloodMoonEntitySpider;
import uk.co.jacekk.bukkit.bloodmoon.entities.BloodMoonEntityZombie;
import uk.co.jacekk.bukkit.bloodmoon.featurelisteners.BreakBlocksListener;
import uk.co.jacekk.bukkit.bloodmoon.featurelisteners.DoubleHealthListener;
import uk.co.jacekk.bukkit.bloodmoon.featurelisteners.FireArrowsListener;
import uk.co.jacekk.bukkit.bloodmoon.featurelisteners.LockInWorldListener;
import uk.co.jacekk.bukkit.bloodmoon.featurelisteners.MoreExpListener;
import uk.co.jacekk.bukkit.bloodmoon.featurelisteners.MoreSpawningListener;
import uk.co.jacekk.bukkit.bloodmoon.featurelisteners.SpawnOnKillListener;
import uk.co.jacekk.bukkit.bloodmoon.featurelisteners.SpawnOnSleepListener;
import uk.co.jacekk.bukkit.bloodmoon.featurelisteners.SuperCreepersListener;
import uk.co.jacekk.bukkit.bloodmoon.featurelisteners.SwordDamageListener;

public class BloodMoon extends BasePlugin {
	
	private ArrayList<String> activeWorlds;
	
	public void onEnable(){
		super.onEnable(true);
		
		this.activeWorlds = new ArrayList<String>();
		
		this.config = new PluginConfig(new File(this.baseDirPath + File.separator + "config.yml"), Config.values(), this.log);
		
		try{
			@SuppressWarnings("rawtypes")
			Class[] args = new Class[3];
			args[0] = Class.class;
			args[1] = String.class;
			args[2] = int.class;
			
			Method a = net.minecraft.server.EntityTypes.class.getDeclaredMethod("a", args);
			
			a.setAccessible(true);
			
			a.invoke(a, BloodMoonEntityCreeper.class, "Creeper", 50);
			a.invoke(a, BloodMoonEntitySkeleton.class, "Skeleton", 51);
			a.invoke(a, BloodMoonEntitySpider.class, "Spider", 52);
			a.invoke(a, BloodMoonEntityZombie.class, "Zombie", 54);
			a.invoke(a, BloodMoonEntityEnderman.class, "Enderman", 58);
		}catch (Exception e){
			e.printStackTrace();
			
			this.setEnabled(false);
			return;
		}
		
		this.getCommand("bloodmoon").setExecutor(new BloodMoonExecuter(this));
		
		for (Permission permission : Permission.values()){
			this.pluginManager.addPermission(new org.bukkit.permissions.Permission(permission.getNode(), permission.getDescription(), permission.getDefault()));
		}
		
		if (this.config.getBoolean(Config.ALWAYS_ON)){
			this.pluginManager.registerEvents(new ActivateWorldListener(this), this);
		}else{
			this.scheduler.scheduleAsyncRepeatingTask(this, new TimeMonitorTask(this), 100L, 100L);
		}
		
		// These events are always needed.
		this.pluginManager.registerEvents(new PlayerEnterWorldListener(this), this);
		this.pluginManager.registerEvents(new EntityReplaceListener(this), this);
		
		// NOTE: arrow-rate is handled in BloodMoonEntitySkeleton
		
		if (this.config.getBoolean(Config.FEATURE_FIRE_ARROWS_ENABLED) && this.config.getBoolean(Config.FEATURE_FIRE_ARROWS_IGNITE_TARGET)){
			this.pluginManager.registerEvents(new FireArrowsListener(this), this);
		}
		
		if (this.config.getBoolean(Config.FEATURE_BREAK_BLOCKS_ENABLED)){
			this.pluginManager.registerEvents(new BreakBlocksListener(this), this);
		}
		
		if (this.config.getBoolean(Config.FEATURE_DOUBLE_HEALTH_ENABLED)){
			this.pluginManager.registerEvents(new DoubleHealthListener(this), this);
		}
		
		if (this.config.getBoolean(Config.FEATURE_MORE_SPAWNING_ENABLED)){
			this.pluginManager.registerEvents(new MoreSpawningListener(this), this);
		}
		
		if (this.config.getBoolean(Config.FEATURE_MORE_EXP_ENABLED)){
			this.pluginManager.registerEvents(new MoreExpListener(this), this);
		}
		
		if (this.config.getBoolean(Config.FEATURE_SWORD_DAMAGE_ENABLED)){
			this.pluginManager.registerEvents(new SwordDamageListener(this), this);
		}
		
		if (this.config.getBoolean(Config.FEATURE_SUPER_CREEPERS_ENABLED)){
			this.pluginManager.registerEvents(new SuperCreepersListener(this), this);
		}
		
		if (this.config.getBoolean(Config.FEATURE_SPAWN_ON_KILL_ENABLED)){
			this.pluginManager.registerEvents(new SpawnOnKillListener(this), this);
		}
		
		if (this.config.getBoolean(Config.FEATURE_SPAWN_ON_SLEEP_ENABLED)){
			this.pluginManager.registerEvents(new SpawnOnSleepListener(this), this);
		}
		
		if (this.config.getBoolean(Config.FEATURE_LOCK_IN_WORLD_ENABLED) && !this.config.getBoolean(Config.ALWAYS_ON)){
			this.pluginManager.registerEvents(new LockInWorldListener(this), this);
		}
	}
	
	public void activate(String worldName){
		this.activeWorlds.add(worldName);
	}
	
	public void activate(World world){
		this.activate(world.getName());
	}
	
	public void deactivate(String worldName){
		this.activeWorlds.remove(worldName);
	}
	
	public void deactivate(World world){
		this.activate(world.getName());
	}
	
	public boolean isActive(String worldName){
		return this.activeWorlds.contains(worldName);
	}
	
	public boolean isActive(World world){
		return this.isActive(world.getName());
	}
	
}
