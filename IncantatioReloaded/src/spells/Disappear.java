package spells;

import interfaces.Spell;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import tools.EntityTools;
import tools.IPlayer;
import enums.SpellType;

public class Disappear implements Spell {

	private static ConcurrentHashMap<Player, Disappear> instances = new ConcurrentHashMap<Player, Disappear>();

	private IPlayer caster;
	private int exp = 0;
	private Player target;

	public Disappear(IPlayer player, int power) {
		caster = player;
		if (!caster.hasSpellPermission(this, player.getPlayer().getLocation()))
			return;
		target = caster.getPlayer();

		if (hasInvisibility()) {
			removeInvisibility();
			remove();
			return;
		} else {
			List<Entity> entities = Illusion.getIllusions();
			entities.add(target);
			exp = EntityTools.getLivingEntitiesAroundPoint(
					target.getLocation(), 15,
					entities.toArray(new Entity[entities.size()])).size();
		}
		int duration = (int) (20 * (10 * Math.pow(2, power - 1)));
		PotionEffect effect = new PotionEffect(PotionEffectType.INVISIBILITY,
				duration, 0);
		target.addPotionEffect(effect);
		instances.put(target, this);
	}

	public static List<String> getAliases() {
		String[] aliases = { "lateo" };
		return Arrays.asList(aliases);
	}

	@Override
	public int getExp() {
		return exp;
	}

	@Override
	public SpellType getType() {
		return SpellType.DISAPPEAR;
	}

	@Override
	public IPlayer getCaster() {
		return caster;
	}

	private void removeInvisibility() {
		target.removePotionEffect(PotionEffectType.INVISIBILITY);
	}

	private boolean hasInvisibility() {
		for (PotionEffect active : target.getActivePotionEffects()) {
			if (active.getType().equals(PotionEffectType.INVISIBILITY)) {
				return true;
			}
		}
		return false;
	}

	private void progress() {

		if (!target.isOnline() || target.isDead()) {
			removeInvisibility();
			remove();
			return;
		}

		if (!hasInvisibility()) {
			removeInvisibility();
			remove();
			return;
		}
	}

	private void remove() {
		if (instances.containsKey(target))
			instances.remove(target);
	}

	public static void handle() {
		for (Player player : instances.keySet()) {
			instances.get(player).progress();
		}
	}

	public static Disappear getDisappeared(Player player) {
		if (instances.containsKey(player)) {
			return instances.get(player);
		}
		return null;
	}

	public void cancel() {
		removeInvisibility();
		remove();
	}

	public static void cancelAll() {
		for (Player player : instances.keySet()) {
			instances.get(player).cancel();
		}
	}

}
