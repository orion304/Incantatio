package tools;

import main.IncantatioReloaded;

import org.bukkit.entity.Player;

public class PermissionTools {

	private static IncantatioReloaded plugin;

	public PermissionTools(IncantatioReloaded instance) {
		plugin = instance;
	}

	public static boolean hasPermission(Player player, String permission) {
		if (player == null)
			return true;
		return player.hasPermission(permission);
	}

}
