package uk.co.jacekk.bukkit.bloodmoon.nms;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_8_R1.BiomeMeta;
import net.minecraft.server.v1_8_R1.EntityTypes;
import net.minecraft.server.v1_8_R1.EnumCreatureType;
import net.minecraft.server.v1_8_R1.IChunkLoader;
import net.minecraft.server.v1_8_R1.IChunkProvider;
import net.minecraft.server.v1_8_R1.WorldServer;

import org.bukkit.entity.EntityType;

import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;

public class ChunkProviderServer extends net.minecraft.server.v1_8_R1.ChunkProviderServer {
	
	private BloodMoon plugin;
	private List<BiomeMeta> bloodMoonMobs;
	
	public ChunkProviderServer(BloodMoon plugin, WorldServer worldserver, IChunkLoader ichunkloader, IChunkProvider ichunkprovider){
		super(worldserver, ichunkloader, ichunkprovider);
		
		this.plugin = plugin;
		this.bloodMoonMobs = new ArrayList<BiomeMeta>();
		
		PluginConfig worldConfig = this.plugin.getConfig(this.world.worldData.getName());
		
		for (String name : worldConfig.getStringList(Config.FEATURE_SPAWN_CONTROL_SPAWN)){
			this.bloodMoonMobs.add(new BiomeMeta(EntityTypes.a(EntityType.valueOf(name).getTypeId()), 10, 4, 4)); // Entity class, weight, minGroupSize, maxGroupSize
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<BiomeMeta> getMobsFor(EnumCreatureType creatureType, int x, int y, int z){
		return (this.plugin.isActive(this.world.worldData.getName())) ? this.bloodMoonMobs : super.getMobsFor(creatureType, x, y, z);
	}
	
}
