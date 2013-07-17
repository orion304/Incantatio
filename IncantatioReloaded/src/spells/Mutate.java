package spells;

import interfaces.Spell;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import tools.EntityTools;
import tools.IPlayer;
import enums.SpellType;

public class Mutate implements Spell {

	private static ConcurrentHashMap<Integer, Mutate> instances = new ConcurrentHashMap<Integer, Mutate>();
	private static int ID = Integer.MIN_VALUE;

	private EntityType[] mutationTargets = { EntityType.CREEPER,
			EntityType.ZOMBIE, EntityType.SKELETON, EntityType.BLAZE,
			EntityType.CAVE_SPIDER, EntityType.ENDERMAN, EntityType.MAGMA_CUBE,
			EntityType.PIG_ZOMBIE, EntityType.SPIDER, EntityType.SPIDER,
			EntityType.SLIME, EntityType.SILVERFISH };
	private EntityType[] mutations = { EntityType.COW, EntityType.CHICKEN,
			EntityType.SHEEP, EntityType.PIG };
	private int radius = 2;
	private long duration = 10000;
	private long time;
	private int id;
	private int exp = 0;
	private IPlayer caster;

	private ConcurrentHashMap<Entity, EntityType> creatures = new ConcurrentHashMap<Entity, EntityType>();

	public Mutate(IPlayer player, Location origin, int power, boolean perpetual) {
		caster = player;
		radius *= power;
		duration *= Math.pow(2, power - 1);

		if (perpetual)
			duration = 0;

		List<EntityType> types = Arrays.asList(mutationTargets);

		for (LivingEntity entity : EntityTools.getLivingEntitiesAroundPoint(
				origin, radius)) {
			if (types.contains(entity.getType())
					&& Illusion.getCreated(entity) == null
					&& caster.hasSpellPermission(this, entity.getLocation())) {
				polymorph(entity);
				exp++;
			}
		}
		time = System.currentTimeMillis();

		if (!perpetual) {
			id = ID++;
			if (ID >= Integer.MAX_VALUE)
				ID = Integer.MIN_VALUE;

			instances.put(id, this);
		}

	}

	private void polymorph(LivingEntity entity) {
		int choice = (new Random()).nextInt(mutations.length);
		EntityType type = mutations[choice];
		World world = entity.getWorld();
		EntityType oldtype = entity.getType();
		Location location = entity.getLocation();
		double hp = (double) entity.getHealth()
				/ (double) entity.getMaxHealth();
		entity.remove();
		Entity polymorphed = world.spawnEntity(location, type);
		LivingEntity lPoly = (LivingEntity) polymorphed;
		int newhp = (int) (hp * lPoly.getMaxHealth());
		lPoly.setHealth(newhp);
		playEffect(world, location);
		creatures.put(polymorphed, oldtype);
	}

	private void playEffect(World world, Location location) {
		world.playEffect(location, Effect.SMOKE, 4, 20);
	}

	public static void handle() {
		for (int id : instances.keySet()) {
			instances.get(id).progress();
		}
	}

	public static void stopAll() {
		for (int id : instances.keySet()) {
			instances.get(id).removeAll();
		}
	}

	private void removeAll() {
		for (Entity entity : creatures.keySet()) {
			revert(entity);
		}
		instances.remove(id);
	}

	public void revert(Entity entity) {
		if (creatures.containsKey(entity)) {
			LivingEntity lEntity = (LivingEntity) entity;
			double hp = (double) lEntity.getHealth()
					/ (double) lEntity.getMaxHealth();
			Location location = entity.getLocation();
			EntityType type = creatures.get(entity);
			entity.remove();
			Entity old = location.getWorld().spawnEntity(location, type);
			LivingEntity lOld = (LivingEntity) old;
			int newhp = (int) (hp * lOld.getMaxHealth());
			lOld.setHealth(newhp);
			creatures.remove(entity);
		}
	}

	private void progress() {
		if (System.currentTimeMillis() > time + duration) {
			removeAll();
		}
	}

	public static Mutate getCreated(Entity entity) {
		for (int id : instances.keySet()) {
			Mutate mutate = instances.get(id);
			if (mutate.creatures.containsKey(entity))
				return mutate;
		}
		return null;
	}

	public static List<String> getAliases() {
		String[] aliases = { "mutatio" };
		return Arrays.asList(aliases);
	}

	@Override
	public int getExp() {
		return exp;
	}

	@Override
	public SpellType getType() {
		return SpellType.MUTATE;
	}

	@Override
	public IPlayer getCaster() {
		return caster;
	}
}
