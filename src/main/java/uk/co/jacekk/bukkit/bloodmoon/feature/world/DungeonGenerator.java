package uk.co.jacekk.bukkit.bloodmoon.feature.world;

import java.util.Random;

import net.minecraft.server.v1_5_R2.MobSpawnerAbstract;
import net.minecraft.server.v1_5_R2.TileEntityMobSpawner;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_5_R2.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Step;
import org.bukkit.material.WoodenStep;

import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.util.ListUtils;
import uk.co.jacekk.bukkit.baseplugin.util.ReflectionUtils;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;

public class DungeonGenerator extends BlockPopulator {
	
	private BloodMoon plugin;
	private Random random;
	
	public DungeonGenerator(BloodMoon plugin, Random random){
		this.plugin = plugin;
		this.random = random;
	}
	
	@Override
	public void populate(World world, Random random, Chunk chunk){
		int chunkX = chunk.getX() * 16;
		int chunkZ = chunk.getZ() * 16;
		
		PluginConfig worldConfig = plugin.getConfig(world.getName());
		Biome biome = world.getBiome(chunkX + 8, chunkZ + 8);
		
		if (!worldConfig.getStringList(Config.FEATURE_DUNGEONS_BIOMES).contains(biome.name()) || this.random.nextInt(10000) > worldConfig.getInt(Config.FEATURE_DUNGEONS_CHANCE)){
			return;
		}
		
		plugin.log.info("Generated BloodMoon dungeon at " + chunkX + "," + chunkZ);
		
		int yMax = world.getHighestBlockYAt(chunkX + 8, chunkZ) - 1;
		int minLayers = worldConfig.getInt(Config.FEATURE_DUNGEONS_MIN_LAYERS);
		int maxLayers = worldConfig.getInt(Config.FEATURE_DUNGEONS_MAX_LAYERS);
		int layers = this.random.nextInt(maxLayers - minLayers) + minLayers;
		int yMin = yMax - (layers * 6);
		
		// Walls
		for (int y = yMax + 3; y > yMin; --y){
			for (int x = 1; x < 15; ++x){
				for (int z = 1; z < 15; ++z){
					chunk.getBlock(x, y, z).setTypeId(Material.AIR.getId());
				}
			}
			
			for (int i = 0; i < 16; ++i){
				chunk.getBlock(i, y, 0).setTypeIdAndData(Material.SMOOTH_BRICK.getId(), (byte) this.random.nextInt(3), false);
				chunk.getBlock(i, y, 15).setTypeIdAndData(Material.SMOOTH_BRICK.getId(), (byte) this.random.nextInt(3), false);
				
				chunk.getBlock(0, y, i).setTypeIdAndData(Material.SMOOTH_BRICK.getId(), (byte) this.random.nextInt(3), false);
				chunk.getBlock(15, y, i).setTypeIdAndData(Material.SMOOTH_BRICK.getId(), (byte) this.random.nextInt(3), false);
			}
		}
		
		// Gate
		chunk.getBlock(7, yMax + 1, 0).setTypeId(Material.IRON_FENCE.getId());
		chunk.getBlock(8, yMax + 1, 0).setTypeId(Material.IRON_FENCE.getId());
		chunk.getBlock(7, yMax + 2, 0).setTypeId(Material.IRON_FENCE.getId());
		chunk.getBlock(8, yMax + 2, 0).setTypeId(Material.IRON_FENCE.getId());
		
		// Roof
		for (int i = 1; i < 15; ++i){
			chunk.getBlock(i, yMax + 4, 1).setTypeIdAndData(Material.SMOOTH_BRICK.getId(), (byte) this.random.nextInt(3), false);
			chunk.getBlock(i, yMax + 4, 14).setTypeIdAndData(Material.SMOOTH_BRICK.getId(), (byte) this.random.nextInt(3), false);
			
			chunk.getBlock(1, yMax + 4, i).setTypeIdAndData(Material.SMOOTH_BRICK.getId(), (byte) this.random.nextInt(3), false);
			chunk.getBlock(14, yMax + 4, i).setTypeIdAndData(Material.SMOOTH_BRICK.getId(), (byte) this.random.nextInt(3), false);
		}
		
		for (int x = 2; x < 14; ++x){
			for (int z = 2; z < 14; ++z){
				chunk.getBlock(x, yMax + 5, z).setTypeIdAndData(Material.SMOOTH_BRICK.getId(), (byte) this.random.nextInt(3), false);
			}
		}
		
		WoodenStep dataWood = new WoodenStep();
		dataWood.setSpecies(TreeSpecies.REDWOOD);
		dataWood.setInverted(true);
		
		Step dataBrick = new Step();
		dataBrick.setMaterial(Material.SMOOTH_BRICK);
		dataBrick.setInverted(true);
		
		for (int layer = 0; layer <= layers; ++layer){
			int yBase = yMax - (layer * 6);
			
			// Floors
			for (int x = 1; x < 7; ++x){
				for (int z = 1; z < 7; ++z){
					chunk.getBlock(x, yBase, z).setTypeIdAndData(Material.WOOD_STEP.getId(), dataWood.getData(), false);
					chunk.getBlock(x + 8, yBase, z).setTypeIdAndData(Material.WOOD_STEP.getId(), dataWood.getData(), false);
					
					chunk.getBlock(x, yBase, z + 8).setTypeIdAndData(Material.WOOD_STEP.getId(), dataWood.getData(), false);
					chunk.getBlock(x + 8, yBase, z + 8).setTypeIdAndData(Material.WOOD_STEP.getId(), dataWood.getData(), false);
				}
			}
			
			for (int x = 5; x < 11; ++x){
				for (int z = 5; z < 11; ++z){
					chunk.getBlock(x, yBase, z).setTypeId(Material.AIR.getId());
				}
			}
			
			// Paths
			for (int i = 1; i < 5; ++i){
				chunk.getBlock(i, yBase, 7).setTypeIdAndData(Material.STEP.getId(), dataBrick.getData(), false);
				chunk.getBlock(i, yBase, 8).setTypeIdAndData(Material.STEP.getId(), dataBrick.getData(), false);
				chunk.getBlock(i + 10, yBase, 7).setTypeIdAndData(Material.STEP.getId(), dataBrick.getData(), false);
				chunk.getBlock(i + 10, yBase, 8).setTypeIdAndData(Material.STEP.getId(), dataBrick.getData(), false);
				
				chunk.getBlock(7, yBase, i).setTypeIdAndData(Material.STEP.getId(), dataBrick.getData(), false);
				chunk.getBlock(8, yBase, i).setTypeIdAndData(Material.STEP.getId(), dataBrick.getData(), false);
				chunk.getBlock(7, yBase, i + 10).setTypeIdAndData(Material.STEP.getId(), dataBrick.getData(), false);
				chunk.getBlock(8, yBase, i + 10).setTypeIdAndData(Material.STEP.getId(), dataBrick.getData(), false);
			}
			
			// Columns
			for (int y = 0; y < 6; ++y){
				chunk.getBlock(5, yBase + y, 5).setTypeIdAndData(Material.SMOOTH_BRICK.getId(), (byte) this.random.nextInt(3), false);
				chunk.getBlock(5, yBase + y, 10).setTypeIdAndData(Material.SMOOTH_BRICK.getId(), (byte) this.random.nextInt(3), false);
				chunk.getBlock(10, yBase + y, 5).setTypeIdAndData(Material.SMOOTH_BRICK.getId(), (byte) this.random.nextInt(3), false);
				chunk.getBlock(10, yBase + y, 10).setTypeIdAndData(Material.SMOOTH_BRICK.getId(), (byte) this.random.nextInt(3), false);
			}
			
			// Spawners
			Block[] spawners = new Block[]{
				chunk.getBlock(14, yBase + 1, 1),
				chunk.getBlock(1, yBase + 1, 14)
			};
			
			for (Block block : spawners){
				EntityType type = EntityType.valueOf(ListUtils.getRandom(worldConfig.getStringList(Config.FEATURE_DUNGEONS_SPAWNER_TYPES)));
				
				if (type != null){
					block.setTypeIdAndData(Material.MOB_SPAWNER.getId(), (byte) 0, false);
					MobSpawnerAbstract spawner = ((TileEntityMobSpawner) ((CraftWorld) world).getTileEntityAt(block.getX(), block.getY(), block.getZ())).a();
					
					spawner.a(type.getName());
					
					try{
						ReflectionUtils.setFieldValue(MobSpawnerAbstract.class, "minSpawnDelay", spawner, worldConfig.getInt(Config.FEATURE_DUNGEONS_SPAWNER_DELAY));
						ReflectionUtils.setFieldValue(MobSpawnerAbstract.class, "maxSpawnDelay", spawner, worldConfig.getInt(Config.FEATURE_DUNGEONS_SPAWNER_DELAY));
						ReflectionUtils.setFieldValue(MobSpawnerAbstract.class, "spawnCount", spawner, worldConfig.getInt(Config.FEATURE_DUNGEONS_SPAWNER_COUNT));
						ReflectionUtils.setFieldValue(MobSpawnerAbstract.class, "maxNearbyEntities", spawner, worldConfig.getInt(Config.FEATURE_DUNGEONS_SPAWNER_MAX_MOBS));
						ReflectionUtils.setFieldValue(MobSpawnerAbstract.class, "spawnRange", spawner, 4);
					}catch (Exception e){
						e.printStackTrace();
					}
				}
			}
		}
		
		// Steps
		for (int layer = 0; layer < layers; ++layer){
			int yBase = yMax - (layer * 6);
			
			Step topStep = new Step();
			topStep.setMaterial(Material.SMOOTH_BRICK);
			topStep.setInverted(true);
			
			Step bottomStep = new Step();
			bottomStep.setMaterial(Material.SMOOTH_BRICK);
			bottomStep.setInverted(false);
			
			if (layer % 2 == 0){
				for (int x = 9; x < 15; ++x){
					chunk.getBlock(x, yBase, 14).setTypeId(Material.AIR.getId());
				}
				
				for (int y = 1; y < 4; ++y){
					int l = y * 2;
					
					chunk.getBlock(7 + l, yBase - y + 1, 14).setTypeIdAndData(Material.STEP.getId(), bottomStep.getData(), false);
					chunk.getBlock(8 + l, yBase - y, 14).setTypeIdAndData(Material.STEP.getId(), topStep.getData(), false);
					
					chunk.getBlock(14, yBase - y - 2, 15 - l).setTypeIdAndData(Material.STEP.getId(), bottomStep.getData(), false);
					chunk.getBlock(14, yBase - y - 2, 16 - l).setTypeIdAndData(Material.STEP.getId(), topStep.getData(), false);
				}
			}else{
				for (int x = 1; x < 7; ++x){
					chunk.getBlock(x, yBase, 1).setTypeId(Material.AIR.getId());
				}
				
				for (int y = 1; y < 4; ++y){
					int l = y * 2;
					
					chunk.getBlock(8 - l, yBase - y + 1, 1).setTypeIdAndData(Material.STEP.getId(), bottomStep.getData(), false);
					chunk.getBlock(7 - l, yBase - y, 1).setTypeIdAndData(Material.STEP.getId(), topStep.getData(), false);
					
					chunk.getBlock(1, yBase - y - 2, l).setTypeIdAndData(Material.STEP.getId(), bottomStep.getData(), false);
					chunk.getBlock(1, yBase - y - 2, l - 1).setTypeIdAndData(Material.STEP.getId(), topStep.getData(), false);
				}
			}
		}
		
		// Loot room
		for (int y = 3; y >= 0; --y){
			for (int i = 0; i < 4; ++i){
				chunk.getBlock(i + 6, yMin - y, 5).setTypeIdAndData(Material.SMOOTH_BRICK.getId(), (byte) this.random.nextInt(3), false);
				chunk.getBlock(i + 6, yMin - y, 10).setTypeIdAndData(Material.SMOOTH_BRICK.getId(), (byte) this.random.nextInt(3), false);
				
				chunk.getBlock(5, yMin - y, i + 6).setTypeIdAndData(Material.SMOOTH_BRICK.getId(), (byte) this.random.nextInt(3), false);
				chunk.getBlock(10, yMin - y, i + 6).setTypeIdAndData(Material.SMOOTH_BRICK.getId(), (byte) this.random.nextInt(3), false);
				
				for (int z = 0; z < 4; ++z){
					chunk.getBlock(i + 6, yMin - y, z + 6).setTypeId(Material.AIR.getId());
				}
			}
		}
		
		for (int x = 0; x < 6; ++x){
			for (int z = 0; z < 6; ++z){
				chunk.getBlock(x + 5, yMin - 4, z + 5).setTypeIdAndData(Material.SMOOTH_BRICK.getId(), (byte) this.random.nextInt(3), false);
			}
		}
		
		chunk.getBlock(7, yMin - 3, 6).setTypeIdAndData(Material.CHEST.getId(), (byte) 2, false);
		chunk.getBlock(8, yMin - 3, 6).setTypeIdAndData(Material.CHEST.getId(), (byte) 2, false);
		
		chunk.getBlock(7, yMin - 3, 9).setTypeIdAndData(Material.CHEST.getId(), (byte) 3, false);
		chunk.getBlock(8, yMin - 3, 9).setTypeIdAndData(Material.CHEST.getId(), (byte) 3, false);
		
		chunk.getBlock(6, yMin - 3, 7).setTypeIdAndData(Material.CHEST.getId(), (byte) 4, false);
		chunk.getBlock(6, yMin - 3, 8).setTypeIdAndData(Material.CHEST.getId(), (byte) 4, false);
		
		chunk.getBlock(9, yMin - 3, 7).setTypeIdAndData(Material.CHEST.getId(), (byte) 5, false);
		chunk.getBlock(9, yMin - 3, 8).setTypeIdAndData(Material.CHEST.getId(), (byte) 5, false);
		
		// Add loot
		Chest[] chests = new Chest[]{
			(Chest) chunk.getBlock(7, yMin - 3, 6).getState(),
			(Chest) chunk.getBlock(7, yMin - 3, 9).getState(),
			(Chest) chunk.getBlock(6, yMin - 3, 7).getState(),
			(Chest) chunk.getBlock(9, yMin - 3, 7).getState()
		};
		
		for (Chest chest : chests){
			Inventory inv = chest.getInventory();
			
			for (int i = 0; i < worldConfig.getInt(Config.FEATURE_DUNGEONS_ITEMS_PER_CHEST); ++i){
				Material type = Material.getMaterial(ListUtils.getRandom(worldConfig.getStringList(Config.FEATURE_DUNGEONS_CHEST_ITEMS)));
				
				if (type != null){
					ItemStack item = new ItemStack(type);
					item.setAmount(Math.min(type.getMaxStackSize(), this.random.nextInt(worldConfig.getInt(Config.FEATURE_DUNGEONS_MAX_STACK_SIZE))));
					
					inv.setItem(this.random.nextInt(inv.getSize()), item);
				}
			}
		}
	}
	
}
