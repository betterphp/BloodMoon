package uk.co.jacekk.bukkit.bloodmoon;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;

import net.minecraft.server.v1_4_5.EntityTypes;

import org.bukkit.World;

import uk.co.jacekk.bukkit.baseplugin.v5.BasePlugin;
import uk.co.jacekk.bukkit.baseplugin.v5.config.PluginConfig;
import uk.co.jacekk.bukkit.bloodmoon.commands.BloodMoonExecuter;
import uk.co.jacekk.bukkit.bloodmoon.entities.BloodMoonEntity;
import uk.co.jacekk.bukkit.bloodmoon.events.BloodMoonEndEvent;
import uk.co.jacekk.bukkit.bloodmoon.events.BloodMoonStartEvent;
import uk.co.jacekk.bukkit.bloodmoon.featurelisteners.BreakBlocksListener;
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
	
	public void onEnable(){
		super.onEnable(true);
		
		this.activeWorlds = new ArrayList<String>();
		this.forceWorlds = new ArrayList<String>();
		
		this.config = new PluginConfig(new File(this.baseDirPath + File.separator + "config.yml"), Config.class, this.log);
		
		try{
			Method a = EntityTypes.class.getDeclaredMethod("a", new Class<?>[]{Class.class, String.class, int.class});
			
			a.setAccessible(true);
			
			for (BloodMoonEntity entity : BloodMoonEntity.values()){
				a.invoke(a, entity.getBloodMoonClass(), entity.getName(), entity.getID());
			}
		}catch (Exception e){
			e.printStackTrace();
			
			this.setEnabled(false);
			return;
		}
		
		this.permissionManager.registerPermissions(Permission.class);
		
		this.commandManager.registerCommandExecutor(new BloodMoonExecuter(this));
		
		if (this.config.getBoolean(Config.ALWAYS_ON)){
			this.pluginManager.registerEvents(new ActivateWorldListener(this), this);
		}else{
			this.scheduler.scheduleSyncRepeatingTask(this, new TimeMonitorTask(this), 100L, 100L);
		}
		
		// These events are always needed.
		this.pluginManager.registerEvents(new EntityReplaceListener(this), this);
		
		// NOTE: arrow-rate is handled in BloodMoonPathfinderGoalArrowAttack and BloodMoonEntitySkeleton
		
		// NOTE: target-distance is handled in BloodMoonPathfinderGoalNearestAttackableTarget and all BloodMoonEntity*
		
		// NOTE: movement-speed is handled in all BloodMoonNavigation* and BloodMoonEntity*
		
		if (this.config.getBoolean(Config.FEATURE_CHAT_MESSAGE_ENABLED)){
			this.pluginManager.registerEvents(new ChatMessageListener(this), this);
		}
		
		if (this.config.getBoolean(Config.FEATURE_FIRE_ARROWS_ENABLED) && this.config.getBoolean(Config.FEATURE_FIRE_ARROWS_IGNITE_TARGET)){
			this.pluginManager.registerEvents(new FireArrowsListener(this), this);
		}
		
		if (this.config.getBoolean(Config.FEATURE_ZOMBIE_WEAPON_ENABLED)){
			this.pluginManager.registerEvents(new ZombieWeaponListener(this), this);
		}
		
		if (this.config.getBoolean(Config.FEATURE_ZOMBIE_ARMOR_ENABLED)){
			this.pluginManager.registerEvents(new ZombieArmorListener(this), this);
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
		
		if (this.config.getBoolean(Config.FEATURE_MORE_DROPS_ENABLED)){
			this.pluginManager.registerEvents(new MoreDropsListener(this), this);
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
		
		if (this.config.getBoolean(Config.FEATURE_NETHER_MOBS_ENABLED)){
			this.pluginManager.registerEvents(new NetherMobsListener(this), this);
		}
		
		if (this.config.getBoolean(Config.FEATURE_LOCK_IN_WORLD_ENABLED) && !this.config.getBoolean(Config.ALWAYS_ON)){
			this.pluginManager.registerEvents(new LockInWorldListener(this), this);
		}
		
		if (this.config.getBoolean(Config.FEATURE_TEXTURE_PACK_ENABLED)){
			this.pluginManager.registerEvents(new TexturePackListener(this), this);
		}
	}
	
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
	
	public void forceNextNight(String worldName){
		World world = this.server.getWorld(worldName);
		
		if (world == null){
			return;
		}
		
		this.forceWorlds.add(worldName);
	}
	
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
	
	public boolean isActive(String worldName){
		return this.activeWorlds.contains(worldName);
	}
	
}