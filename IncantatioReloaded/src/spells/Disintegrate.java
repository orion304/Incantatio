package spells;

import interfaces.CreateBlocks;
import interfaces.Spell;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;

import tools.IPlayer;
import enums.SpellType;

public class Disintegrate extends Vanish implements Spell, CreateBlocks {

	public Disintegrate(IPlayer player, Location origin, Material mat,
			int power, boolean perpetual) {
		super(player, origin, mat, power, perpetual);
	}

	public static List<String> getAliases() {
		String[] aliases = { "evanescere" };
		return Arrays.asList(aliases);
	}

	public SpellType getType() {
		return SpellType.DISINTEGRATE;
	}

}
