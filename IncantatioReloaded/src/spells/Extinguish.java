package spells;

import java.util.Arrays;
import java.util.List;

import tools.IPlayer;

public class Extinguish {

	public Extinguish(IPlayer player) {
		player.getPlayer().setFireTicks(0);
	}

	public static List<String> getAliases() {
		String[] aliases = { "extinguere" };
		return Arrays.asList(aliases);
	}

}
