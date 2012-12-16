package uk.co.jacekk.bukkit.bloodmoon;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.server.v1_4_5.EntityTypes;

import org.bukkit.World;

import uk.co.jacekk.bukkit.baseplugin.v6.BasePlugin;
import uk.co.jacekk.bukkit.baseplugin.v6.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v6.util.ReflectionUtils;
import uk.co.jacekk.bukkit.bloodmoon.commands.BloodMoonExecuter;
import uk.co.jacekk.bukkit.bloodmoon.entities.BloodMoonEntity;
import uk.co.jacekk.bukkit.bloodmoon.events.BloodMoonEndEvent;
import uk.co.jacekk.bukkit.bloodmoon.events.BloodMoonStartEvent;
import uk.co.jacekk.bukkit.bloodmoon.featurelisteners.ChatMessageListener;
import uk.co.jacekk.bukkit.bloodmoon.featurelisteners.DoubleHealthListener;
import uk.co.jacekk.bukkit.bloodmoon.featurelisteners.FireArrowsListener;
import uk.co.jacekk.bukkit.bloodmoon.featurelisteners.LockInWorldListener;
import uk.co.jacekk.bukkit.bloodmoon.featurelisteners.MoreDropsListener;
import uk.co.jacekk.bukkit.bloodmoon.featurelisteners.MoreExpListener;
import uk.co.jacekk.bukkit.bloodmoon.featurelisteners.MoreSpawningListener;
import uk.co.jacekk.bukkit.bloodmoon.featurelisteners.NetherMobsListener;
import uk.co.jacekk.bukkit.bloodmoon.featurelisteners.SpawnOnKillListener;
import uk.co.jacekk.bukkit.bloodmoon.featurelisteners.SpawnOnSleepListener;
import uk.co.jacekk.bukkit.bloodmoon.featurelisteners.SuperCreepersListener;
import uk.co.jacekk.bukkit.bloodmoon.featurelisteners.SwordDamageListener;
import uk.co.jacekk.bukkit.bloodmoon.featurelisteners.TexturePackListener;
import uk.co.jacekk.bukkit.bloodmoon.featurelisteners.ZombieArmorListener;
import uk.co.jacekk.bukkit.bloodmoon.featurelisteners.ZombieWeaponListener;

public class BloodMoon extends BasePlugin {
	
	private ArrayList<String> activeWorlds;
	protected ArrayList<String> forceWorlds;
	
	protected HashMap<String, PluginConfig> worldConfig;
	
	public void onEnable(){
		super.onEnable(true);
		
		this.activeWorlds = new ArrayList<String>();
		this.forceWorlds = new ArrayList<String>();
		
		this.worldConfig = new HashMap<String, PluginConfig>();
		
		try{
			for (BloodMoonEntity entity : BloodMoonEntity.values()){
				ReflectionUtils.invokeMethod(EntityTypes.class, "a", Void.class, null, new Class<?>[]{Class.class, String.class, int.class}, new Object[]{entity.getBloodMoonClass(), entity.getName(), entity.getID()});
			}
		}catch (Exception e){
			e.printStackTrace();
			
			this.setEnabled(false);
			return;
		}
		
		this.permissionManager.registerPermissions(Permission.class);
		this.commandManager.registerCommandExecutor(new BloodMoonExecuter(this));
		
		this.pluginManager.registerEvents(new EntityReplaceListener(this), this);
		this.pluginManager.registerEvents(new ConfigCreateListener(this), this);
		
		this.scheduler.scheduleSyncRepeatingTask(this, new TimeMonitorTask(this), 100L, 100L);
		
		// NOTE: arrow-rate is handled in BloodMoonPathfinderGoalArrowAttack and BloodMoonEntitySkeleton
		// NOTE: target-distance is handled in BloodMoonPathfinderGoalNearestAttackableTarget and all BloodMoonEntity*
		// NOTE: movement-speed is handled in all BloodMoonNavigation* and BloodMoonEntity*
		this.pluginManager.registerEvents(new ChatMessageListener(this), this);
		this.pluginManager.registerEvents(new FireArrowsListener(this), this);
		this.pluginManager.registerEvents(new ZombieWeaponListener(this), this);
		this.pluginManager.registerEvents(new ZombieArmorListener(this), this);
		// NOTE: break-blocks is handled in BloodMoonEntity*
		this.pluginManager.registerEvents(new DoubleHealthListener(this), this);
		this.pluginManager.registerEvents(new MoreSpawningListener(this), this);
		this.pluginManager.registerEvents(new MoreExpListener(this), this);
		this.pluginManager.registerEvents(new MoreDropsListener(this), this);
		this.pluginManager.registerEvents(new SwordDamageListener(this), this);
		this.pluginManager.registerEvents(new SuperCreepersListener(this), this);
		this.pluginManager.registerEvents(new SpawnOnKillListener(this), this);
		this.pluginManager.registerEvents(new SpawnOnSleepListener(this), this);
		this.pluginManager.registerEvents(new NetherMobsListener(this), this);
		this.pluginManager.registerEvents(new LockInWorldListener(this), this);
		this.pluginManager.registerEvents(new TexturePackListener(this), this);
	}
	
	/**
	 * Starts a bloodmoon in a specific world
	 * 
	 * @param worldName The name of the world
	 */
	public void activate(String worldName){
		World world = this.server.getWorld(worldName);
		
		if (world == null){
			return;
		}
		
		BloodMoonStartEvent event = new BloodMoonStartEvent(world);
		
		this.pluginManager.callEvent(event);
		
		if (!event.isCancelled()){
			this.activeWorlds.add(worldName);
		}
	}
	
	/**
	 * Starts a bloodmoon the next time night is reached in a specfic world
	 * 
	 * @param worldName The name of the world
	 */
	public void forceNextNight(String worldName){
		World world = this.server.getWorld(worldName);
		
		if (world == null){
			return;
		}
		
		this.forceWorlds.add(worldName);
	}
	
	/**
	 * Stops an existing bloodmoon in a world
	 * 
	 * @param worldName The name of the world
	 */
	public void deactivate(String worldName){
		World world = this.server.getWorld(worldName);
		
		if (world == null){
			return;
		}
		
		BloodMoonEndEvent event = new BloodMoonEndEvent(world);
		
		this.pluginManager.callEvent(event);
		
		if (!event.isCancelled()){
			this.activeWorlds.remove(worldName);
		}
	}
	
	/**
	 * Checks if a bloodmoon is currently active in a world
	 * 
	 * @param worldName The name of the world
	 * @return true if a bloodmoon is active false if not
	 */
	public boolean isActive(String worldName){
		return this.activeWorlds.contains(worldName);
	}
	
	/**
	 * Checks if the bloodmoon is enabled for a world
	 * 
	 * @param worldName The name of the world
	 * @return true if the bloodmoon is enabled false if not
	 */
	public boolean isEnabled(String worldName){
		if (!this.worldConfig.containsKey(worldName)){
			return false;
		}
		
		return this.worldConfig.get(worldName).getBoolean(Config.ENABLED);
	}
	
	/**
	 * Gets the config for a world
	 * 
	 * @param worldName The name of the world
	 * @return the {@link PluginConfig} for this world
	 */
	public PluginConfig getConfig(String worldName){
		return this.worldConfig.get(worldName);
	}
	
}