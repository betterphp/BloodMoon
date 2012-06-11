package uk.co.jacekk.bukkit.bloodmoon;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

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
import uk.co.jacekk.bukkit.bloodmoon.listeners.EntityListener;
import uk.co.jacekk.bukkit.bloodmoon.listeners.PlayerListener;
import uk.co.jacekk.bukkit.bloodmoon.listeners.WorldListener;

public class BloodMoon extends BasePlugin {
	
	public Random rand;
	public ArrayList<String> bloodMoonActiveWorlds;
	
	public void onEnable(){
		this.rand = new Random();
		this.bloodMoonActiveWorlds = new ArrayList<String>();
		
		this.config = new PluginConfig(new File(this.baseDirPath + File.separator + "config.yml"), Config.values(), this.log);
		
		try{
			@SuppressWarnings({"rawtypes"})
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
		
		if (this.config.getBoolean(Config.ALWAYS_ON)){
			this.pluginManager.registerEvents(new WorldListener(this), this);
		}else{
			this.scheduler.scheduleAsyncRepeatingTask(this, new BloodMoonTimeMonitor(this), 100L, 100L);
		}
		
		this.getCommand("bloodmoon").setExecutor(new BloodMoonExecuter(this));
		
		// These events are always needed.
		this.pluginManager.registerEvents(new PlayerListener(this), this);
		this.pluginManager.registerEvents(new EntityListener(this), this);
		
		// These events are only needed by the certain features.
		if (this.config.getBoolean(Config.FEATURE_BREAK_BLOCKS_ENABLED)){
			this.pluginManager.registerEvents(new BreakBlocksListener(this), this);
		}
		
		if (this.config.getBoolean(Config.FEATURE_FIRE_ARROWS_ENABLED) && this.config.getBoolean(Config.FEATURE_FIRE_ARROWS_IGNITE_TARGET)){
			this.pluginManager.registerEvents(new FireArrowsListener(this), this);
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
		
		if (this.config.getBoolean(Config.FEATURE_SUPER_CREEPERS_ENABLED)){
			this.pluginManager.registerEvents(new SuperCreepersListener(this), this);
		}
		
		if (this.config.getBoolean(Config.FEATURE_SPAWN_ON_KILL_ENABLED)){
			this.pluginManager.registerEvents(new SpawnOnKillListener(this), this);
		}
		
		// arrow-rate is handled in BloodMoonEntitySkeleton
		
		if (this.config.getBoolean(Config.FEATURE_SWORD_DAMAGE_ENABLED)){
			this.pluginManager.registerEvents(new SwordDamageListener(this), this);
		}
		
		if (this.config.getBoolean(Config.FEATURE_LOCK_IN_WORLD_ENABLED) && !this.config.getBoolean(Config.ALWAYS_ON)){
			this.pluginManager.registerEvents(new LockInWorldListener(this), this);
		}
		
		if (this.config.getBoolean(Config.FEATURE_SPAWN_ON_SLEEP_ENABLED)){
			this.pluginManager.registerEvents(new SpawnOnSleepListener(this), this);
		}
	}
	
}
