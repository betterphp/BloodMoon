package uk.co.jacekk.bukkit.bloodmoon;

import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R4.ChunkProviderServer;
import net.minecraft.server.v1_7_R4.IChunkLoader;

import org.junit.Assert;
import org.junit.Test;

public class WorldInitListenerTest {
	
	@Test
	public void testReflection(){
		try{
			Field field = ChunkProviderServer.class.getDeclaredField("f");
			
			if (!field.getType().equals(IChunkLoader.class)){
				Assert.fail("ChunkProviderServer.f not found");
			}
		}catch (Exception e){
			e.printStackTrace();
			Assert.fail("ChunkProviderServer.f not found");
		}
	}
	
}
