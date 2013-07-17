package tools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class TargetingTools {

	public static Integer[] transparent = { 0, 6, 8, 9, 10, 11, 31, 32, 37, 38,
			39, 40, 50, 51, 59, 78, 83, 106 };

	public static HashSet<Byte> getTransparent() {
		HashSet<Byte> set = new HashSet<Byte>();
		for (int i : transparent) {
			set.add((byte) i);
		}
		return set;
	}

	public static Location getTargetLocationNoTransparent(Player player) {
		return player.getLastTwoTargetBlocks(null, 20).get(0).getLocation();
	}

	public static Location getTargetLocation(Player player) {
		return getTargetLocation(player, 20, true);
	}

	public static Location getTargetLocation(Player player, int range) {
		return getTargetLocation(player, range, true);
	}

	public static Location getTargetLocation(Player player, int range,
			boolean targetentities) {
		Location playerLocation = player.getLocation();
		Location location = player
				.getLastTwoTargetBlocks(getTransparent(), range).get(0)
				.getLocation();
		if (targetentities) {
			Entity entity = getTargetedEntity(player, range);
			if (entity != null) {
				if (entity.getLocation().distance(playerLocation) <= location
						.distance(playerLocation))
					location = entity.getLocation();
			}
		}
		return location;
	}

	public static Entity getTargetedEntity(Player player, double range) {
		return getTargettedEntity(player, range, new ArrayList<Entity>());
	}

	public static Entity getTargettedEntity(Player player, double range,
			List<Entity> avoid) {
		double longestr = range + 1;
		Entity target = null;
		Location origin = player.getEyeLocation();
		Vector direction = player.getEyeLocation().getDirection().normalize();
		for (Entity entity : origin.getWorld().getEntities()) {
			if (avoid.contains(entity))
				continue;
			if (entity.getLocation().distance(origin) < longestr
					&& VectorTools.getDistanceFromLine(direction, origin,
							entity.getLocation()) < 2
					&& (entity instanceof LivingEntity)
					&& entity.getEntityId() != player.getEntityId()
					&& entity.getLocation().distance(
							origin.clone().add(direction)) < entity
							.getLocation().distance(
									origin.clone().add(
											direction.clone().multiply(-1)))) {
				target = entity;
				longestr = entity.getLocation().distance(origin);
			}
		}
		return target;
	}

	public static Block getTargetBlock(Player player) {
		return getTargetBlock(player, 20);
	}

	public static Block getTargetBlock(Player player, int range) {
		return player.getTargetBlock(getTransparent(), range);
	}

}
