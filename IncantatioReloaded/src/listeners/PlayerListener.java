package listeners;

import main.IncantatioReloaded;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import spells.SpellHandler;

public class PlayerListener implements Listener {

	private IncantatioReloaded plugin;

	public PlayerListener(IncantatioReloaded instance) {
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerChat(AsyncPlayerChatEvent event) {

		String message = event.getMessage();
		Player player = event.getPlayer();

		String[] args = message.split(" ++");

		if (SpellHandler.getSpell(args[0]) != null) {
			plugin.manager.newSpell(player, args);
			event.setCancelled(true);
		}

	}
}
