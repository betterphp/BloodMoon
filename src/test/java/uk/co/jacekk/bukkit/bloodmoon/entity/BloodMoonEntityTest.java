package uk.co.jacekk.bukkit.bloodmoon.entity;

import org.junit.Assert;
import org.junit.Test;

public class BloodMoonEntityTest {
	
	@Test
	public void testMapping(){
		for (BloodMoonEntity entity : BloodMoonEntity.values()){
			Assert.assertTrue(entity.getName().equals(entity.getEntityType().getName()));
			Assert.assertTrue(entity.getID() == entity.getEntityType().getTypeId());
		}
	}
	
}
