package uk.co.jacekk.bukkit.bloodmoon.feature;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import uk.co.jacekk.bukkit.baseplugin.v6.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v6.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;

public class MoreDropsListener extends BaseListener<BloodMoon> {
	
	public MoreDropsListener(BloodMoon plugin){
		super(plugin);
	}
	
	private void setSpawnReason(Entity entity, SpawnReason reason){
		entity.setMetadata("spawn-reason", new FixedMetadataValue(plugin, reason));
	}
	
	private SpawnReason getSpawnReason(Entity entity){
		for (MetadataValue value : entity.getMetadata("spawn-reason")){
			if (value.getOwningPlugin() instanceof BloodMoon){
				return (SpawnReason) value.value();
			}
		}
		
		return SpawnReason.DEFAULT;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntitySpawn(CreatureSpawnEvent event){
		this.setSpawnReason(event.getEntity(), event.getSpawnReason());
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent event){
		Entity entity = event.getEntity();
		String worldName = entity.getWorld().getName();
		PluginConfig worldConfig = plugin.getConfig(worldName);
		
		if (entity instanceof Creature && plugin.isActive(worldName) && worldConfig.getBoolean(Config.FEATURE_MORE_DROPS_ENABLED)){
			if (!worldConfig.getBoolean(Config.FEATURE_MORE_EXP_IGNORE_SPAWNERS) || this.getSpawnReason(entity) != SpawnReason.SPAWNER){
				for (ItemStack drop : event.getDrops()){
					drop.setAmount(drop.getAmount() * Math.max(worldConfig.getInt(Config.FEATURE_MORE_DROPS_MULTIPLIER), 1));
				}
			}
		}
	}
	
}
