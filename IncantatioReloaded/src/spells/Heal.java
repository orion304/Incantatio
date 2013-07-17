package spells;

import interfaces.Spell;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import tools.IPlayer;
import enums.SpellType;

public class Heal implements Spell {

	private IPlayer caster;
	private int exp = 0;

	public Heal(IPlayer player, int power) {
		caster = player;

		Player source = caster.getPlayer();

		if (!player.hasSpellPermission(this, source.getLocation()))
			return;

		for (PotionEffect active : source.getActivePotionEffects()) {
			if (active.getType().equals(PotionEffectType.REGENERATION))
				return;
		}

		int strength = 0;
		int duration = power * 3 * 20;

		int missinghp = 20 - source.getHealth();

		int maxhealing = (int) (0.8 * power * 3);

		exp = missinghp;
		if (missinghp > maxhealing) {
			exp = maxhealing;
		}

		if (power > 3)
			strength = 1;

		PotionEffect effect = new PotionEffect(PotionEffectType.REGENERATION,
				duration, strength);
		source.addPotionEffect(effect);
	}

	@Override
	public int getExp() {
		return exp;
	}

	@Override
	public SpellType getType() {
		return SpellType.HEAL;
	}

	@Override
	public IPlayer getCaster() {
		return caster;
	}

	public static List<String> getAliases() {
		String[] aliases = { "remedium" };
		return Arrays.asList(aliases);
	}

}
