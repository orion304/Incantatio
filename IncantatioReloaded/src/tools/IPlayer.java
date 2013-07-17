package tools;

import interfaces.Spell;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import enums.SpellType;

public class IPlayer implements ConfigurationSerializable {

	private static ConcurrentHashMap<String, IPlayer> players = new ConcurrentHashMap<String, IPlayer>();
	private static Server server;

	private String name;
	private long lasttime;
	private long lastlightning = 0;
	private long lastexplosion = 0;
	private ConcurrentHashMap<SpellType, Integer> SpellExp = new ConcurrentHashMap<SpellType, Integer>();
	private ConcurrentHashMap<SpellType, Long> cooldowns = new ConcurrentHashMap<SpellType, Long>();
	private boolean silent = false, perpetual = false, expbypass = false;

	public IPlayer(Player player) {
		if (players.containsKey(player.getName()))
			return;

		name = player.getName();

		for (SpellType type : SpellType.values()) {
			SpellExp.put(type, 0);
			cooldowns.put(type, 0L);
		}

		players.put(name, this);
	}

	public static IPlayer getPlayer(Player player) {
		if (players.containsKey(player.getName())) {
			return players.get(player.getName());
		}
		return new IPlayer(player);
	}

	public int getMaxPower(SpellType spell) {
		lasttime = System.currentTimeMillis();
		int exp = getExp(spell);
		if (expbypass)
			exp = Integer.MAX_VALUE;
		return SpellType.getLevel(spell, exp);
	}

	public void togglePerpetual() {
		perpetual = !perpetual;
	}

	public boolean isPerpetual() {
		return perpetual;
	}

	public void toggleSilent() {
		silent = !silent;
	}

	public boolean isSilent() {
		return silent;
	}

	public boolean canLightningStrike() {
		return (System.currentTimeMillis() > lastlightning + 2500L);
	}

	public boolean canExplosion() {
		return (System.currentTimeMillis() > lastexplosion + 2500L);
	}

	public void lightningStrike() {
		lastlightning = System.currentTimeMillis();
	}

	public void explosion() {
		lastexplosion = System.currentTimeMillis();
	}

	public boolean canSilent(SpellType type) {
		if (!PermissionTools.hasPermission(getPlayer(),
				"incantatio.modification.silent"))
			return false;
		int level = SpellType.getLevel(type, getExp(type));
		if (level >= 3 || expbypass)
			return true;
		return false;
	}

	public boolean canPerpetual(SpellType type) {
		if (!PermissionTools.hasPermission(getPlayer(),
				"incantatio.modification.perpetual"))
			return false;
		int level = SpellType.getLevel(type, getExp(type));
		if (level >= 5 || expbypass)
			return true;
		return false;
	}

	public boolean canForce(SpellType spellType) {
		if (!PermissionTools.hasPermission(getPlayer(),
				"incantatio.modification.force"))
			return false;
		return true;
	}

	private int getExp(SpellType type) {
		return SpellExp.get(type);
	}

	public void gainExp(Spell spell) {
		if (expbypass)
			return;
		SpellType type = spell.getType();
		int exp = spell.getExp();
		int oldlevel = SpellType.getLevel(type, SpellExp.get(type));
		exp += SpellExp.get(type);
		int newlevel = SpellType.getLevel(type, exp);
		if (newlevel > oldlevel)
			EffectTools.playLevelEffect(this);
		SpellExp.replace(type, exp);
	}

	public Player getPlayer() {
		return server.getPlayerExact(name);
	}

	public void setExp(SpellType type, int exp) {
		SpellExp.replace(type, exp);
	}

	public IPlayer(Map<String, Object> map) {
		name = (String) map.get("Name");
		// lasttime = (long) map.get("LastTime");

		for (SpellType type : SpellType.values()) {
			if (map.containsKey(type.toString())) {
				int exp = (Integer) map.get(type.toString());
				SpellExp.put(type, exp);
			} else {
				SpellExp.put(type, 0);
			}
			cooldowns.put(type, 0L);
		}

		if (players.containsKey(name)) {
			players.replace(name, this);
		} else {
			players.put(name, this);
		}
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("Name", name);
		// map.put("LastTime", lasttime);

		for (SpellType type : SpellType.values()) {
			map.put(type.toString(), getExp(type));
		}

		return map;
	}

	public static IPlayer deserialize(Map<String, Object> map) {
		return new IPlayer(map);
	}

	public static IPlayer valueOf(Map<String, Object> map) {
		return deserialize(map);
	}

	public static void saveAll(File folder, String filename) {
		File playersFile = new File(folder, filename);
		FileConfiguration playersConfig = new YamlConfiguration();
		for (String string : players.keySet()) {
			IPlayer player = players.get(string);

			playersConfig.set(string, player.serialize());
		}

		try {
			playersConfig.save(playersFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void loadAll(Server iserver, File folder, String filename) {
		File playersFile = new File(folder, filename);
		FileConfiguration playersConfig = YamlConfiguration
				.loadConfiguration(playersFile);
		List<String> names = new ArrayList<String>(playersConfig.getKeys(false));
		for (String name : names) {
			new IPlayer(playersConfig.getConfigurationSection(name).getValues(
					false));
		}
		server = iserver;
	}

	public String toString() {
		return "IncantatioPlayer-" + name;
	}

	public static IPlayer getPlayer(String name) {
		if (players.containsKey(name)) {
			return players.get(name);
		}
		return null;
	}

	public void clearAllExp() {
		for (SpellType type : SpellExp.keySet()) {
			SpellExp.replace(type, 0);
		}
	}

	public void maxAllExp() {
		for (SpellType type : SpellExp.keySet()) {
			SpellExp.replace(type, 10000);
		}
	}

	public boolean hasCastPermission(SpellType spellType) {
		return PermissionTools.hasPermission(getPlayer(), "incantatio.spell."
				+ spellType.toString());
	}

	public boolean hasSpellPermission(Spell spell, Location location) {
		return !RegionProtection.isRegionProtectedFromBuild(getPlayer(),
				spell.getType(), location);
	}

	public void bypassExp() {
		expbypass = !expbypass;

	}

	public boolean onCooldown(SpellType spellType) {
		if (!cooldowns.containsKey(spellType)) {
			cooldowns.put(spellType, 0L);
			return false;
		}
		return System.currentTimeMillis() <= cooldowns.get(spellType) + 1000L;
	}

	public void cooldown(SpellType type) {
		cooldowns.put(type, System.currentTimeMillis());
	}

}
