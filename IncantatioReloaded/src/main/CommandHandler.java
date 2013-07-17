package main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import tools.IPlayer;
import tools.PermissionTools;

public class CommandHandler {

	public CommandHandler(IncantatioReloaded plugin, Player player,
			String[] args) {
		if (args.length == 0)
			return;
		String command = args[0].toLowerCase();

		if (!PermissionTools.hasPermission(player, "incantatio.admin."
				+ command))
			return;

		if (command.equalsIgnoreCase("clearexp")) {
			clearExp(player, args);
		} else if (command.equalsIgnoreCase("maxexp")) {
			maxExp(player, args);
		} else if (command.equalsIgnoreCase("reload")) {
			reloadServer(plugin);
		} else if (command.equalsIgnoreCase("bypassexp")) {
			bypassExp(player, args);
		}
	}

	private void clearExp(Player player, String[] args) {
		List<String> names = new ArrayList<String>();

		if (args.length > 1) {
			for (int i = 1; i < args.length; i++) {
				names.add(args[i]);
			}
		} else if (player != null) {
			names.add(player.getName());
		}

		for (String name : names) {
			IPlayer iPlayer = IPlayer.getPlayer(name);
			if (iPlayer != null)
				iPlayer.clearAllExp();
		}
	}

	private void maxExp(Player player, String[] args) {
		List<String> names = new ArrayList<String>();

		if (args.length > 1) {
			for (int i = 1; i < args.length; i++) {
				names.add(args[i]);
			}
		} else if (player != null) {
			names.add(player.getName());
		}

		for (String name : names) {
			IPlayer iPlayer = IPlayer.getPlayer(name);
			if (iPlayer != null)
				iPlayer.maxAllExp();
		}
	}

	private void bypassExp(Player player, String[] args) {
		List<String> names = new ArrayList<String>();

		if (args.length > 1) {
			for (int i = 1; i < args.length; i++) {
				names.add(args[i]);
			}
		} else if (player != null) {
			names.add(player.getName());
		}

		for (String name : names) {
			IPlayer iPlayer = IPlayer.getPlayer(name);
			if (iPlayer != null)
				iPlayer.bypassExp();
		}
	}

	private void reloadServer(IncantatioReloaded plugin) {
		plugin.stopAll();
	}

}
