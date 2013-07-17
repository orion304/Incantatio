package spells;

import java.util.Arrays;
import java.util.List;

import tools.IPlayer;



public class Silent {

	public Silent(IPlayer player) {
		player.toggleSilent();
	}

	public static List<String> getAliases() {
		String[] aliases = { "silentus" };
		return Arrays.asList(aliases);
	}

}
