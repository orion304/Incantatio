package spells;

import java.util.Arrays;
import java.util.List;

import org.bukkit.World;

public class Calm {

	public Calm(World world) {
		world.setStorm(false);
		world.setThundering(false);
	}

	public static List<String> getAliases() {
		String[] aliases = { "sereno" };
		return Arrays.asList(aliases);
	}

}
