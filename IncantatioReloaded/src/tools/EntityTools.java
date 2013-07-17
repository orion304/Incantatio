package tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class EntityTools {

	public static List<Entity> getEntitiesAroundPoint(Location location,
			double radius) {
		return getEntitiesAroundPoint(location, radius, new Entity[0]);
	}

	public static List<Entity> getEntitiesAroundPoint(Location location,
			double radius, Entity... ignore) {
		List<Entity> allEntities = location.getWorld().getEntities();
		List<Entity> entities = new ArrayList<Entity>();
		for (Entity entity : allEntities) {
			if (entity.getLocation().distance(location) <= radius)
				entities.add(entity);
		}
		return entities;
	}

	public static List<LivingEntity> getLivingEntitiesAroundPoint(
			Location location, double radius) {
		return getLivingEntitiesAroundPoint(location, radius, new Entity[0]);
	}

	public static List<LivingEntity> getLivingEntitiesAroundPoint(
			Location location, double radius, Entity... ignore) {
		List<Entity> allEntities = location.getWorld().getEntities();
		List<LivingEntity> entities = new ArrayList<LivingEntity>();
		for (Entity entity : allEntities) {
			if (entity.getLocation().distance(location) <= radius
					&& entity instanceof LivingEntity)
				entities.add((LivingEntity) entity);
		}
		return entities;
	}

	public static List<Player> getPlayersAroundPoint(Location location,
			double radius) {
		return getPlayersAroundPoint(location, radius, new Entity[0]);
	}

	public static List<Player> getPlayersAroundPoint(Location location,
			double radius, Entity... ignore) {
		List<Player> players = new ArrayList<Player>();
		List<Entity> allEntities = location.getWorld().getEntities();
		for (Entity entity : allEntities) {
			if (Arrays.asList(ignore).contains(entity))
				continue;
			if (!(entity instanceof Player))
				continue;
			if (entity.getLocation().distance(location) < radius)
				players.add((Player) entity);
		}
		return players;
	}

}
