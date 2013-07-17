package spells;

import java.util.Arrays;
import java.util.List;

import tools.IPlayer;



public class Perpetual {

	public Perpetual(IPlayer player) {
		player.togglePerpetual();
	}

	public static List<String> getAliases() {
		String[] aliases = { "perpetuum" };
		return Arrays.asList(aliases);
	}

}
