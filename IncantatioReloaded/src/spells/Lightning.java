package spells;

import interfaces.Spell;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;

import tools.EntityTools;
import tools.IPlayer;
import enums.SpellType;

public class Lightning implements Spell {

	private static ConcurrentHashMap<Integer, Lightning> instances = new ConcurrentHashMap<Integer, Lightning>();

	private static Map<Entity, Integer> strikes = new HashMap<Entity, Integer>();
	private static int ID = Integer.MIN_VALUE;

	private int exp = 0;
	private int id;
	private long time = 1500;
	private IPlayer caster;
	private Location location;
	private int power;

	public Lightning(IPlayer player, Location location, int power) {
		caster = player;
		if (!caster.hasSpellPermission(this, location))
			return;

		this.location = location;
		this.power = power;

		time += System.currentTimeMillis();

		id = ID++;
		if (ID >= Integer.MAX_VALUE)
			ID = Integer.MIN_VALUE;
		instances.put(id, this);

		exp = EntityTools.getLivingEntitiesAroundPoint(location, 5).size();
	}

	private void strike() {
		LightningStrike strike = location.getWorld().strikeLightning(location);
		strikes.put(strike, power);
	}

	public static boolean wasCause(Entity lighting) {
		return strikes.containsKey(lighting);
	}

	public static int getDamage(Entity lightning, int originaldamage) {
		double damage = originaldamage;
		if (strikes.containsKey(lightning)) {
			int power = strikes.get(lightning);
			damage = 2 * power;
			strikes.put(lightning, 0);
		}
		return (int) damage;
	}

	@Override
	public int getExp() {
		return exp;
	}

	@Override
	public SpellType getType() {
		return SpellType.LIGHTNING;
	}

	public static List<String> getAliases() {
		String[] aliases = { "fulmen" };
		return Arrays.asList(aliases);
	}

	@Override
	public IPlayer getCaster() {
		return caster;
	}

	private void progress() {
		if (System.currentTimeMillis() >= time) {
			strike();
			remove();
		} else {
			playEffect();
		}
	}

	private void playEffect() {
		location.getWorld().playEffect(location, Effect.SMOKE, 4, 20);
	}

	private void remove() {
		instances.remove(id);
	}

	public static void handle() {
		for (int id : instances.keySet()) {
			instances.get(id).progress();
		}
	}

}
