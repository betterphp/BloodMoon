package uk.co.jacekk.bukkit.bloodmoon.entity;

import net.minecraft.server.v1_7_R3.BiomeBase;
import net.minecraft.server.v1_7_R3.EntityTypes;

import org.junit.Assert;
import org.junit.Test;

public class BloodMoonEntityTypeTest {
	
	@Test
	public void testReflection(){
		try{
			EntityTypes.class.getDeclaredField("c");
			EntityTypes.class.getDeclaredField("e");
		}catch (Exception e){
			e.printStackTrace();
			Assert.fail("EntityTypes name and id map fields not found");
		}
		
		try{
			EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
		}catch (Exception e){
			e.printStackTrace();
			Assert.fail("EntityTypes.a(Class, String, int) not found");
		}
		
		try{
			for (String field : new String[]{"as", "at", "au", "av"}){
				BiomeBase.class.getDeclaredField(field);
			}
		}catch (Exception e){
			e.printStackTrace();
			Assert.fail("BiomeBase field not found");
		}
	}
	
}
