package spells;

import interfaces.CreatedEntities;
import interfaces.Spell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import tools.EntityTools;
import tools.IPlayer;
import enums.SpellType;

public class Illusion implements Spell, CreatedEntities {

	private static ConcurrentHashMap<Integer, Illusion> instances = new ConcurrentHashMap<Integer, Illusion>();

	private static EntityType[] creatures = { EntityType.SPIDER,
			EntityType.ZOMBIE, EntityType.CAVE_SPIDER, EntityType.CREEPER,
			EntityType.SKELETON };
	private static int ID = Integer.MIN_VALUE;

	private int id;
	private Location location;
	private int exp = 0;
	private List<LivingEntity> ignore;
	private int numberOfMonsters = 1;
	private double radius = 3;
	private long time;
	private long duration = 10000;
	private ArrayList<Entity> created = new ArrayList<Entity>();
	private IPlayer caster;

	public Illusion(IPlayer player, Location location, int power,
			List<LivingEntity> ignore) {
		caster = player;
		this.location = location;
		this.ignore = ignore;

		numberOfMonsters *= Math.pow(2, power - 1);
		duration *= Math.pow(2, power - 1);
		time = System.currentTimeMillis();
		radius *= power;

		createCreatures();

		exp = EntityTools.getPlayersAroundPoint(location, 10,
				ignore.toArray(new Entity[ignore.size()])).size();

		id = ID++;

		instances.put(id, this);

		if (ID >= Integer.MAX_VALUE)
			ID = Integer.MIN_VALUE;

	}

	public static List<Entity> getIllusions() {
		List<Entity> entities = new ArrayList<Entity>();
		for (int id : instances.keySet()) {
			entities.addAll(instances.get(id).created);
		}
		return entities;
	}

	public static Entity[] getIllusionsArray() {
		List<Entity> entities = getIllusions();
		return entities.toArray(new Entity[entities.size()]);
	}

	public static Illusion getCreated(Entity entity) {
		for (int id : instances.keySet()) {
			Illusion illusion = instances.get(id);
			if (illusion.created.contains(entity))
				return illusion;
		}
		return null;
	}

	private void createCreatures() {
		World world = location.getWorld();
		Random random = new Random();
		int length = creatures.length;
		for (int i = 0; i < numberOfMonsters; i++) {
			if (!caster.hasSpellPermission(this, location))
				continue;
			EntityType type = creatures[random.nextInt(length)];
			Entity entity = world.spawnEntity(location, type);
			created.add(entity);
		}
	}

	public List<Entity> getEntities() {
		return created;
	}

	public void removeEntity(Entity entity) {
		if (created.contains(entity)) {
			created.remove(entity);
			entity.remove();
		}
	}

	public boolean isIngored(Entity entity) {
		if (entity instanceof LivingEntity) {
			if (ignore.contains(entity))
				return true;
		}
		return false;
	}

	private void remove() {
		removeAllEntities();
		instances.remove(id);
	}

	private void removeAllEntities() {
		ArrayList<Entity> entities = new ArrayList<Entity>(created);
		for (Entity entity : entities) {
			removeEntity(entity);
		}
	}

	public static List<String> getAliases() {
		String[] aliases = { "inlusio" };
		return Arrays.asList(aliases);
	}

	public static void stopAll() {
		for (int id : instances.keySet()) {
			Illusion illusion = instances.get(id);
			illusion.removeAllEntities();
		}
		instances.clear();
	}

	public static void handle() {
		for (int id : instances.keySet()) {
			Illusion illusion = instances.get(id);
			if (System.currentTimeMillis() > illusion.time + illusion.duration)
				illusion.remove();
		}
	}

	@Override
	public int getExp() {
		return exp;
	}

	@Override
	public SpellType getType() {
		return SpellType.ILLUSION;
	}

	@Override
	public IPlayer getCaster() {
		return caster;
	}

}
