package spells;

import java.util.Arrays;
import java.util.List;

import org.bukkit.World;

public class Night {

	private long time = 12550;

	public Night(World world) {
		world.setTime(time);
	}

	public static List<String> getAliases() {
		String[] aliases = { "noctem" };
		return Arrays.asList(aliases);
	}

}
