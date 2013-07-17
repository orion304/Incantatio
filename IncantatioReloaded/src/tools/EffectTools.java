package tools;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class EffectTools {

	private static Effect effect = Effect.POTION_BREAK;

	public static void playCreateBlockEffect(Block block) {

	}

	public static void playRemoveBlockEffect(Block block) {
		block.getWorld().playEffect(block.getLocation(), Effect.SMOKE, 4, 20);
	}

	public static void playLevelEffect(IPlayer iPlayer) {
		Player player = iPlayer.getPlayer();
		Location location = player.getLocation();
		location.getWorld().playEffect(location, effect, 0, 20);

	}
}
