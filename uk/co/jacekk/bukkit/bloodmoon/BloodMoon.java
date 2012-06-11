package uk.co.jacekk.bukkit.bloodmoon;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

import uk.co.jacekk.bukkit.baseplugin.BasePlugin;
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
	
	public static BloodMoonConfig config;
	public static ArrayList<String> bloodMoonWorlds;
	
	protected BloodMoonTimeMonitor timeMonitor;
	
	public void onEnable(){
		this.rand = new Random();
		
		BloodMoon.config = new BloodMoonConfig(new File(this.baseDirPath + File.separator + "config.yml"), this);
		BloodMoon.bloodMoonWorlds = new ArrayList<String>();
		
		this.timeMonitor = new BloodMoonTimeMonitor(this);
		
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
		
		if (BloodMoon.config.getBoolean("always-on")){
			this.pluginManager.registerEvents(new WorldListener(this), this);
		}else{
			this.scheduler.scheduleAsyncRepeatingTask(this, this.timeMonitor, 100, 100);
		}
		
		this.getCommand("bloodmoon").setExecutor(new BloodMoonExecuter(this));
		
		// These events are always needed.
		this.pluginManager.registerEvents(new PlayerListener(this), this);
		this.pluginManager.registerEvents(new EntityListener(this), this);
		
		// These events are only needed by the certain features.
		if (BloodMoon.config.getBoolean("features.break-blocks.enabled")){
			this.pluginManager.registerEvents(new BreakBlocksListener(this), this);
		}
		
		if (BloodMoon.config.getBoolean("features.fire-arrows.enabled") && BloodMoon.config.getBoolean("features.fire-arrows.ignight-target")){
			this.pluginManager.registerEvents(new FireArrowsListener(this), this);
		}
		
		if (BloodMoon.config.getBoolean("features.double-health.enabled")){
			this.pluginManager.registerEvents(new DoubleHealthListener(this), this);
		}
		
		if (BloodMoon.config.getBoolean("features.more-spawning.enabled")){
			this.pluginManager.registerEvents(new MoreSpawningListener(this), this);
		}
		
		if (BloodMoon.config.getBoolean("features.more-exp.enabled")){
			this.pluginManager.registerEvents(new MoreExpListener(this), this);
		}
		
		if (BloodMoon.config.getBoolean("features.super-creepers.enabled")){
			this.pluginManager.registerEvents(new SuperCreepersListener(this), this);
		}
		
		if (BloodMoon.config.getBoolean("features.spawn-on-kill.enabled")){
			this.pluginManager.registerEvents(new SpawnOnKillListener(this), this);
		}
		
		// arrow-rate is handled in BloodMoonEntitySkeleton
		
		if (BloodMoon.config.getBoolean("features.sword-damage.enabled")){
			this.pluginManager.registerEvents(new SwordDamageListener(this), this);
		}
		
		if (BloodMoon.config.getBoolean("features.lock-in-world.enabled") && BloodMoon.config.getBoolean("always-on") == false){
			this.pluginManager.registerEvents(new LockInWorldListener(this), this);
		}
		
		if (BloodMoon.config.getBoolean("features.spawn-on-sleep.enabled")){
			this.pluginManager.registerEvents(new SpawnOnSleepListener(this), this);
		}
		
		this.log.info("Enabled.");
	}
	
	public void onDisable(){
		this.log.info("Disabled");
		
		BloodMoon.config = null;
		BloodMoon.bloodMoonWorlds = null;
	}
	
}
