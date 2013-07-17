package main;

import java.io.File;
import java.util.logging.Logger;

import listeners.BlockListener;
import listeners.EntityListener;
import listeners.PlayerListener;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import spells.Disappear;
import spells.Illusion;
import spells.SpellHandler;
import spells.Stop;
import tools.CreatedBlock;
import tools.IPlayer;
import tools.PermissionTools;
import tools.ServerTools;

public class IncantatioReloaded extends JavaPlugin {

	private File folder = getDataFolder();
	private String playersSaveFileName = "IncantatioPlayers.yml";

	public Manager manager = new Manager(this);
	private PlayerSaver saver = new PlayerSaver(folder, playersSaveFileName);
	private PlayerListener playerListener = new PlayerListener(this);
	private EntityListener entityListener = new EntityListener(this);
	private BlockListener blockListener = new BlockListener();
	private BukkitScheduler scheduler;
	private ServerTools tools = new ServerTools(this);
	private PermissionTools permissions = new PermissionTools(this);

	public Logger logger = Logger.getLogger("IncantatioReloaded");

	private long savetime = 5 * 60 * 1000;

	public void onEnable() {

		ConfigurationSerialization.registerClass(IPlayer.class, "IPlayer");

		Server server = getServer();
		PluginManager pluginManager = server.getPluginManager();
		scheduler = server.getScheduler();

		IPlayer.loadAll(server, folder, playersSaveFileName);

		SpellHandler.initialize();

		scheduleTasks(scheduler);
		hookListeners(pluginManager);

	}

	private void scheduleTasks(BukkitScheduler scheduler) {
		scheduler.scheduleSyncRepeatingTask(this, manager, 0, 1);
		scheduler.runTaskTimerAsynchronously(this, saver, savetime, savetime);
	}

	private void unscheduleTasks(BukkitScheduler scheduler) {
		scheduler.cancelTasks(this);
	}

	private void hookListeners(PluginManager manager) {
		manager.registerEvents(playerListener, this);
		manager.registerEvents(entityListener, this);
		manager.registerEvents(blockListener, this);
	}

	public void onDisable() {
		stopAll();
		IPlayer.saveAll(folder, playersSaveFileName);
		unscheduleTasks(scheduler);
	}

	void stopAll() {
		Disappear.cancelAll();
		Illusion.stopAll();
		CreatedBlock.removeAll();
		Stop.cancelAll();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		Player player = null;
		if (sender instanceof Player)
			player = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("incantatio")) {
			new CommandHandler(this, player, args);
		}
		return true;
	}

}
