package uk.co.jacekk.bukkit.bloodmoon.feature.world;

import net.minecraft.server.v1_5_R3.MobSpawnerAbstract;

import org.junit.Assert;
import org.junit.Test;

public class DungeonGeneratorTest {
	
	@Test
	public void testReflection(){
		try{
			for (String name : new String[]{"minSpawnDelay", "maxSpawnDelay", "spawnCount", "maxNearbyEntities", "spawnRange"}){
				MobSpawnerAbstract.class.getDeclaredField(name);
			}
		}catch (Exception e){
			Assert.fail(e.getMessage());
		}
	}
	
}
