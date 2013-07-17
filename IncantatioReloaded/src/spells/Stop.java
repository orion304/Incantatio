package spells;

import interfaces.Spell;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import tools.EntityTools;
import tools.IPlayer;
import enums.SpellType;

public class Stop implements Spell {

	private static ConcurrentHashMap<Integer, Stop> instances = new ConcurrentHashMap<Integer, Stop>();
	private static int ID = Integer.MIN_VALUE;

	private int id;
	private IPlayer caster;
	private Location origin;
	private double radius = 2;
	private long time;
	private long duration = 10000;

	private Map<Entity, Vector> entities = new HashMap<Entity, Vector>();
	private Map<Entity, Location> locations = new HashMap<Entity, Location>();

	public Stop(IPlayer player, Location origin, int power) {
		caster = player;
		this.origin = origin;
		radius *= power;
		duration *= Math.pow(2, power - 1);
		time = System.currentTimeMillis();

		for (Entity entity : EntityTools.getEntitiesAroundPoint(origin, radius)) {
			if (!(entity instanceof Player)) {
				entities.put(entity, entity.getVelocity().clone());
				locations.put(entity, entity.getLocation().clone());
				entity.getWorld().playEffect(entity.getLocation(),
						Effect.POTION_BREAK, 2, 20);
			}
		}

		if (!entities.isEmpty()) {
			id = ID++;
			if (ID >= Integer.MAX_VALUE) {
				ID = Integer.MIN_VALUE;
			}
			instances.put(id, this);
		}
	}

	@Override
	public int getExp() {
		return entities.size();
	}

	@Override
	public SpellType getType() {
		return SpellType.STOP;
	}

	@Override
	public IPlayer getCaster() {
		return caster;
	}

	public static boolean isFrozen(Block block) {
		return isFrozen(block.getLocation());
	}

	public static boolean isFrozen(Location location) {
		for (int id : instances.keySet()) {
			Stop stop = instances.get(id);
			if (location.getWorld() != stop.origin.getWorld())
				continue;
			if (location.distance(stop.origin) <= stop.radius)
				return true;
		}
		return false;
	}

	public static boolean isFrozen(Entity entity) {
		for (int id : instances.keySet()) {
			Stop stop = instances.get(id);
			if (stop.entities.containsKey(entity))
				return true;
		}
		return false;
	}

	private void cancel() {
		for (Entity entity : entities.keySet()) {
			entity.setVelocity(entities.get(entity));
		}

		entities.clear();
		locations.clear();
		instances.remove(id);
	}

	private void progress() {
		if (System.currentTimeMillis() > time + duration) {
			cancel();
		} else {
			for (Entity entity : entities.keySet()) {
				entity.setVelocity(new Vector(0, 0, 0));
				entity.teleport(locations.get(entity));
				if (entity instanceof Creature) {
					((Creature) entity).setTarget(null);
				}
			}
		}
	}

	public static void handle() {
		for (int id : instances.keySet()) {
			instances.get(id).progress();
		}
	}

	public static List<String> getAliases() {
		String[] aliases = { "subsisto" };
		return Arrays.asList(aliases);
	}

	public static void cancelAll() {
		for (int id : instances.keySet()) {
			instances.get(id).cancel();
		}
	}

}
