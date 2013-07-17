package spells;

import interfaces.Spell;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import tools.EntityTools;
import tools.IPlayer;
import enums.SpellType;

public class Push implements Spell {

	private int exp = 0;
	private IPlayer caster;
	private double radius = 1;
	private Player source;
	int power = 1;
	private static double factor = Pull.factor;

	public Push(IPlayer player, Location location, int power) {
		caster = player;

		this.power = power;
		source = caster.getPlayer();

		radius *= power;

		for (Entity entity : EntityTools.getEntitiesAroundPoint(location,
				radius, source)) {
			if (caster.hasSpellPermission(this, entity.getLocation())) {
				pull(entity);
				exp++;
			}
		}
	}

	private void pull(Entity entity) {
		Vector fromvector = source.getEyeLocation().toVector();
		Vector tovector = entity.getLocation().toVector();
		Vector vector = tovector.subtract(fromvector);
		vector.setY(0);
		vector = vector.normalize().multiply(factor * power);
		entity.setVelocity(vector);
	}

	@Override
	public int getExp() {
		return exp;
	}

	@Override
	public SpellType getType() {
		return SpellType.PUSH;
	}

	@Override
	public IPlayer getCaster() {
		return caster;
	}

	public static List<String> getAliases() {
		String[] aliases = { "urgeo" };
		return Arrays.asList(aliases);
	}

}
