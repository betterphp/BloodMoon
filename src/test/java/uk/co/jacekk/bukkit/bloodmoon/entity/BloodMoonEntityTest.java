package uk.co.jacekk.bukkit.bloodmoon.entity;

import org.junit.Assert;
import org.junit.Test;

public class BloodMoonEntityTest {
	
	@Test
	public void testMapping(){
		for (BloodMoonEntityType entity : BloodMoonEntityType.values()){
			Assert.assertTrue(entity.getName().equals(entity.getEntityType().getName()));
			Assert.assertTrue(entity.getID() == entity.getEntityType().getTypeId());
		}
	}
	
}
