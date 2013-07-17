package spells;

import interfaces.Spell;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import tools.IPlayer;
import tools.TargetingTools;
import enums.SpellType;

public class Hop implements Spell {

	double range = 5;
	double distancetraveled;
	private IPlayer caster;

	public Hop(IPlayer caster, int power) {
		Player player = caster.getPlayer();
		this.caster = caster;
		range *= Math.pow(2, power - 1);
		player.getWorld().playEffect(player.getLocation(), Effect.SMOKE, 4, 20);
		Location location = TargetingTools.getTargetLocation(player,
				(int) range, false);
		if (!caster.hasSpellPermission(this, location))
			return;
		Location playerLocation = player.getLocation().clone();
		distancetraveled = playerLocation.distance(location);
		location.setPitch(playerLocation.getPitch());
		location.setYaw(playerLocation.getYaw());
		player.teleport(location, TeleportCause.PLUGIN);
		player.setFallDistance(0);
		player.getWorld().playEffect(location, Effect.SMOKE, 4, 20);
	}

	public static List<String> getAliases() {
		String[] aliases = { "salio" };
		return Arrays.asList(aliases);

	}

	@Override
	public int getExp() {
		return (int) distancetraveled;
	}

	@Override
	public SpellType getType() {
		return SpellType.HOP;
	}

	@Override
	public IPlayer getCaster() {
		return caster;
	}

}
