package uk.co.jacekk.bukkit.bloodmoon.featurelisteners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerBedEnterEvent;

import uk.co.jacekk.bukkit.baseplugin.BaseListener;
import uk.co.jacekk.bukkit.baseplugin.util.ListUtils;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;

public class SpawnOnSleepListener extends BaseListener<BloodMoon> {
	
	public SpawnOnSleepListener(BloodMoon plugin){
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerBedEnter(PlayerBedEnterEvent event){
		Player player = event.getPlayer();
		Location location = player.getLocation();
		World world = location.getWorld();
		
		if (plugin.isActive(world)){
			String mobName = (String) ListUtils.getRandom(plugin.config.getStringList(Config.FEATURE_SPAWN_ON_SLEEP_SPAWN));
			EntityType creatureType = EntityType.fromName(mobName.toUpperCase());
			
			if (creatureType != null){
				world.spawnEntity(location, creatureType);
			}
		}
	}
	
}
