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

public class Pull implements Spell {

	private int exp = 0;
	private IPlayer caster;
	private double radius = 1;
	private Player source;
	int power = 1;
	static double factor = 2;

	public Pull(IPlayer player, Location location, int power) {
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
		Vector tovector = source.getEyeLocation().toVector();
		Vector fromvector = entity.getLocation().toVector();
		Vector vector = tovector.clone().subtract(fromvector);
		vector.setY(0);
		vector = vector.normalize();
		vector = vector.multiply(power * factor);
		entity.setVelocity(vector);
	}

	@Override
	public int getExp() {
		return exp;
	}

	@Override
	public SpellType getType() {
		return SpellType.PULL;
	}

	@Override
	public IPlayer getCaster() {
		return caster;
	}

	public static List<String> getAliases() {
		String[] aliases = { "vello" };
		return Arrays.asList(aliases);
	}

}
