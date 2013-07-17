package spells;

import java.util.Arrays;
import java.util.List;

import org.bukkit.World;

public class Day {

	private long time = 23425;

	public Day(World world) {
		world.setTime(time);
	}

	public static List<String> getAliases() {
		String[] aliases = { "lucem" };
		return Arrays.asList(aliases);
	}
}
