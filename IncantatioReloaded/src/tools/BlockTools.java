package tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class BlockTools {

	private static final Material[] ignitable = { Material.BEDROCK,
			Material.BOOKSHELF, Material.BRICK, Material.CLAY,
			Material.CLAY_BRICK, Material.COAL_ORE, Material.COBBLESTONE,
			Material.DIAMOND_ORE, Material.DIAMOND_BLOCK, Material.DIRT,
			Material.ENDER_STONE, Material.GLOWING_REDSTONE_ORE,
			Material.GOLD_BLOCK, Material.GRAVEL, Material.GRASS,
			Material.HUGE_MUSHROOM_1, Material.HUGE_MUSHROOM_2,
			Material.LAPIS_BLOCK, Material.LAPIS_ORE, Material.LOG,
			Material.MOSSY_COBBLESTONE, Material.MYCEL, Material.NETHER_BRICK,
			Material.NETHERRACK, Material.OBSIDIAN, Material.REDSTONE_ORE,
			Material.SAND, Material.SANDSTONE, Material.SMOOTH_BRICK,
			Material.STONE, Material.SOUL_SAND, Material.SNOW_BLOCK,
			Material.WOOD, Material.WOOL, Material.LEAVES };

	private static final Material[] overwriteable = { Material.SAPLING,
			Material.LONG_GRASS, Material.DEAD_BUSH, Material.YELLOW_FLOWER,
			Material.RED_ROSE, Material.BROWN_MUSHROOM, Material.RED_MUSHROOM,
			Material.FIRE, Material.SNOW };

	public static List<Block> getBlocksAroundPoint(Location location,
			double radius) {
		return getBlocksAroundPoint(location, radius, null);
	}

	public static List<Block> getBlocksAroundPoint(Location location,
			double radius, List<Material> desired) {
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();

		int r = (int) radius;

		List<Block> blocks = new ArrayList<Block>();
		for (int dy = -r - 2; dy <= r + 2; dy++) {
			for (int dx = -r - 2; dx <= r + 2; dx++) {
				for (int dz = -r - 2; dz <= r + 2; dz++) {
					if ((double) dx * dx + (double) dy * dy + (double) dz * dz > (double) r
							* r)
						continue;
					Block block = location.getWorld().getBlockAt(x + dx,
							y + dy, z + dz);
					if (desired != null) {
						if (!desired.contains(block.getType()))
							continue;
					}
					blocks.add(block);
				}
			}
		}

		return blocks;
	}

	public static List<Block> getBlocksAroundPointCube(Location location,
			double length) {
		return getBlocksAroundPointCube(location, length, null);
	}

	public static List<Block> getBlocksAroundPointCube(Location location,
			double length, List<Material> desired) {
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();

		int r = (int) (length / 2.);

		List<Block> blocks = new ArrayList<Block>();
		for (int dy = -r - 2; dy <= r + 2; dy++) {
			for (int dx = -r - 2; dx <= r + 2; dx++) {
				for (int dz = -r - 2; dz <= r + 2; dz++) {
					Block block = location.getWorld().getBlockAt(x + dx,
							y + dy, z + dz);
					if (desired != null) {
						if (!desired.contains(block.getType()))
							continue;
					}
					blocks.add(block);
				}
			}
		}

		return blocks;
	}

	public static List<Block> getBlocksOnPlane(Location center, Vector normal,
			double length) {
		Vector ortho1 = VectorTools.getOrthogonalVector(normal, 0, 1);
		ortho1 = ortho1.normalize();
		Vector ortho2 = VectorTools.getOrthogonalVector(normal, 90, 1);
		ortho2 = ortho2.normalize();

		int r = (int) (length / 2.0);

		List<Block> blocks = new ArrayList<Block>();

		for (int r1 = -r; r1 <= r; r1++) {
			for (int r2 = -r; r2 <= r; r2++) {
				blocks.add(center.clone().add(ortho1.clone().multiply(r1))
						.add(ortho2.clone().multiply(r2)).getBlock());
			}
		}

		return blocks;
	}

	public static boolean isOverwriteable(Block block, Material desired,
			boolean force) {
		Material type = block.getType();
		if (type == Material.AIR)
			return true;
		if (Arrays.asList(overwriteable).contains(type))
			return true;
		if (isWater(block) && isWater(desired))
			return true;
		if (isLava(block) && isLava(desired))
			return true;
		if (force && type != Material.BEDROCK)
			return true;
		return false;
	}

	public static boolean isWater(Block block) {
		return isWater(block.getType());
	}

	public static boolean isWater(Material mat) {
		return (mat == Material.WATER || mat == Material.STATIONARY_WATER);
	}

	public static boolean isLava(Block block) {
		return isLava(block.getType());
	}

	public static boolean isLava(Material mat) {
		return (mat == Material.LAVA || mat == Material.STATIONARY_LAVA);
	}

	public static boolean isIgnitable(Material mat) {
		return (Arrays.asList(ignitable).contains(mat));
	}

	public static boolean isIgnitable(Block block) {
		Block below = block.getRelative(BlockFace.DOWN);
		return isIgnitable(below.getType());
	}

	public static boolean isObstructed(Location location1, Location location2) {
		Vector loc1 = location1.toVector();
		Vector loc2 = location2.toVector();

		Vector direction = loc2.subtract(loc1);
		direction.normalize();

		Location loc;

		double max = location1.distance(location2);

		for (double i = 0; i <= max; i++) {
			loc = location1.clone().add(direction.clone().multiply(i));
			Material type = loc.getBlock().getType();
			if (type != Material.AIR
					&& !Arrays.asList(overwriteable).contains(type))
				return true;
		}

		return false;
	}

}
