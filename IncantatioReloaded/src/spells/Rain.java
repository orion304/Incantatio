package spells;

import java.util.Arrays;
import java.util.List;

import org.bukkit.World;

public class Rain {

	public Rain(World world) {
		world.setStorm(true);
		world.setThundering(false);
	}

	public static List<String> getAliases() {
		String[] aliases = { "pluvia" };
		return Arrays.asList(aliases);
	}

}
