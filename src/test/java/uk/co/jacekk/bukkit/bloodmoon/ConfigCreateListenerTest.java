package uk.co.jacekk.bukkit.bloodmoon;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.World;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.MockGateway;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import uk.co.jacekk.bukkit.baseplugin.v6.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.v6.logging.PluginLogger;

@RunWith(PowerMockRunner.class)
@PrepareForTest({BloodMoon.class, PluginDescriptionFile.class, World.class, WorldInitEvent.class})
public class ConfigCreateListenerTest {
	
	private File testDir;
	private String testDirPath;
	
	@Before
	public void setUp(){
		this.testDir = new File("bin/test");
		this.testDir.mkdirs();
		
		Assert.assertTrue(this.testDir.exists());
		Assert.assertTrue(this.testDir.canWrite());
		
		this.testDirPath = this.testDir.getAbsolutePath();
	}
	
	@After
	public void tearDown(){
		for (File file : this.testDir.listFiles()){
			file.delete();
		}
		
		this.testDir.delete();
	}
	
	@Test
	public void testConfigCreate(){
		MockGateway.MOCK_STANDARD_METHODS = false;
		
		BloodMoon plugin = PowerMockito.spy(new BloodMoon());
		PluginDescriptionFile pdf = PowerMockito.spy(new PluginDescriptionFile("BloodMoon", "0.21.0-TEST", "uk.co.jacekk.bukkit.bloodmoon.BloodMoon"));
		World world = PowerMockito.mock(World.class);
		WorldInitEvent event = PowerMockito.mock(WorldInitEvent.class);
		
		PowerMockito.when(pdf.getAuthors()).thenReturn(new ArrayList<String>());
		PowerMockito.when(plugin.getBaseDirPath()).thenReturn(this.testDirPath);
		PowerMockito.when(plugin.getDescription()).thenReturn(pdf);
		
		PowerMockito.when(world.getName()).thenReturn("test_world");
		PowerMockito.when(event.getWorld()).thenReturn(world);
		
		plugin.log = new PluginLogger(plugin);
		plugin.worldConfig = new HashMap<String, PluginConfig>();
		
		ConfigCreateListener listener = new ConfigCreateListener(plugin);
		listener.onWorldInit(event);
		
		Assert.assertFalse("World config not created", plugin.worldConfig.isEmpty());
	}
	
}
