package spells;

import interfaces.Spell;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;

import tools.EntityTools;
import tools.IPlayer;
import tools.WorldTools;
import enums.SpellType;

public class Explode implements Spell {

	public static Map<Entity, Integer> explosions = new HashMap<Entity, Integer>();

	private int exp = 0;
	private int timer = 30;
	private IPlayer caster;

	public Explode(IPlayer player, Location location, int power) {
		caster = player;
		if (!caster.hasSpellPermission(this, location))
			return;
		float yield = .25F + ((float) power) / 4F;
		exp = EntityTools.getLivingEntitiesAroundPoint(location, 5).size();
		yield = WorldTools.getTNTYield(location.getWorld(), yield);
		Entity tnt = location.getWorld().spawn(location, TNTPrimed.class);
		((TNTPrimed) tnt).setFuseTicks(timer);
		((TNTPrimed) tnt).setYield(yield);
		explosions.put(tnt, power);
	}

	public static boolean wasCause(Entity entity) {
		return explosions.containsKey(entity);
	}

	public static int getDamage(Entity entity, int originaldamage) {
		double damage = originaldamage;
		if (explosions.containsKey(entity)) {
			damage = 2 * explosions.get(entity);
		}
		return (int) damage;
	}

	public static List<String> getAliases() {
		String[] aliases = { "praemium" };
		return Arrays.asList(aliases);
	}

	@Override
	public int getExp() {
		return exp;
	}

	@Override
	public SpellType getType() {
		return SpellType.EXPLODE;
	}

	@Override
	public IPlayer getCaster() {
		return caster;
	}

}
