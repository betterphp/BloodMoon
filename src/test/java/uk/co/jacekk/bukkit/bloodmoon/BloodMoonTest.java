package uk.co.jacekk.bukkit.bloodmoon;

import net.minecraft.server.v1_4_R1.EntityTypes;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class BloodMoonTest {
	
	@Test
	public void testReflection(){
		try{
			EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
		}catch (Exception e){
			e.printStackTrace();
			Assert.fail("EntityTypes.a(Class, String, int) not found");
		}
	}
	
}
