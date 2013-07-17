package spells;

import java.util.Arrays;
import java.util.List;

import org.bukkit.World;

public class Storm {

	public Storm(World world) {
		world.setStorm(true);
		world.setThundering(true);
	}

	public static List<String> getAliases() {
		String[] aliases = { "tempestas" };
		return Arrays.asList(aliases);
	}

}
