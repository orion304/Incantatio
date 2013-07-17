package spells;

import interfaces.Spell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import tools.IPlayer;
import tools.TargetingTools;
import enums.Component;
import enums.SpellType;

public class SpellHandler {

	private static Map<String, SpellType> aliasMap = new HashMap<String, SpellType>();
	private static Map<SpellType, List<Component>> componentMap = new HashMap<SpellType, List<Component>>();

	private ArrayList<String> components;
	private boolean failure = false;
	private boolean silent = false;
	private boolean perpetual = false;
	private boolean force = false;
	private String componentString = "";
	private String message = "";

	public static void initialize() {
		initializeAliases();
		initializeComponents();
	}

	private static void initializeComponents() {
		componentMap.clear();

		componentMap.put(SpellType.BUBBLE, Bubble.getAllowedComponents());
		componentMap.put(SpellType.DISINTEGRATE,
				Disintegrate.getAllowedComponents());
		componentMap.put(SpellType.ENTOMB, Entomb.getAllowedComponents());
		componentMap.put(SpellType.HEMISPHERE,
				Hemisphere.getAllowedComponents());
		componentMap.put(SpellType.RING, Ring.getAllowedComponents());
		componentMap.put(SpellType.SPHERE, Sphere.getAllowedComponents());
		componentMap.put(SpellType.VANISH, Vanish.getAllowedComponents());
		componentMap.put(SpellType.WALL, Wall.getAllowedComponents());
		componentMap.put(SpellType.REPLENISHWATER,
				ReplenishWater.getAllowedComponents());
	}

	private static void initializeAliases() {
		aliasMap.clear();

		for (String string : Illusion.getAliases())
			aliasMap.put(string, SpellType.ILLUSION);

		for (String string : Bubble.getAliases())
			aliasMap.put(string, SpellType.BUBBLE);

		for (String string : Sphere.getAliases())
			aliasMap.put(string, SpellType.SPHERE);

		for (String string : Hemisphere.getAliases())
			aliasMap.put(string, SpellType.HEMISPHERE);

		for (String string : Wall.getAliases())
			aliasMap.put(string, SpellType.WALL);

		for (String string : Ring.getAliases())
			aliasMap.put(string, SpellType.RING);

		for (String string : Entomb.getAliases())
			aliasMap.put(string, SpellType.ENTOMB);

		for (String string : Vanish.getAliases())
			aliasMap.put(string, SpellType.VANISH);

		for (String string : Disintegrate.getAliases())
			aliasMap.put(string, SpellType.DISINTEGRATE);

		for (String string : Hop.getAliases())
			aliasMap.put(string, SpellType.HOP);

		for (String string : Breathe.getAliases())
			aliasMap.put(string, SpellType.BREATHE);

		for (String string : Explode.getAliases())
			aliasMap.put(string, SpellType.EXPLODE);

		for (String string : Silent.getAliases())
			aliasMap.put(string, SpellType.SILENT);

		for (String string : Perpetual.getAliases())
			aliasMap.put(string, SpellType.PERPETUAL);

		for (String string : ReplenishWater.getAliases())
			aliasMap.put(string, SpellType.REPLENISHWATER);

		for (String string : Disappear.getAliases())
			aliasMap.put(string, SpellType.DISAPPEAR);

		for (String string : Light.getAliases())
			aliasMap.put(string, SpellType.LIGHT);

		for (String string : Mine.getAliases())
			aliasMap.put(string, SpellType.MINE);

		for (String string : Mutate.getAliases())
			aliasMap.put(string, SpellType.MUTATE);

		for (String string : Lightning.getAliases())
			aliasMap.put(string, SpellType.LIGHTNING);

		for (String string : Day.getAliases())
			aliasMap.put(string, SpellType.DAY);

		for (String string : Night.getAliases())
			aliasMap.put(string, SpellType.NIGHT);

		for (String string : Calm.getAliases())
			aliasMap.put(string, SpellType.CALM);

		for (String string : Rain.getAliases())
			aliasMap.put(string, SpellType.RAIN);

		for (String string : Storm.getAliases())
			aliasMap.put(string, SpellType.STORM);

		for (String string : Pull.getAliases())
			aliasMap.put(string, SpellType.PULL);

		for (String string : Push.getAliases())
			aliasMap.put(string, SpellType.PUSH);

		for (String string : Freeze.getAliases())
			aliasMap.put(string, SpellType.FREEZE);

		for (String string : Thaw.getAliases())
			aliasMap.put(string, SpellType.THAW);

		for (String string : Extinguish.getAliases())
			aliasMap.put(string, SpellType.EXTINGUISH);

		for (String string : Heal.getAliases())
			aliasMap.put(string, SpellType.HEAL);

		for (String string : Stop.getAliases()) {
			aliasMap.put(string, SpellType.STOP);
		}
	}

	public SpellHandler(Player player, String[] args) {

		components = new ArrayList<String>();
		IPlayer iPlayer = IPlayer.getPlayer(player);

		for (String arg : args) {
			components.add(arg.trim());
		}

		for (int i = 0; i < components.size(); i++) {
			String string = components.get(i);
			components.set(i, string.toLowerCase());
		}

		String spell = components.get(0);
		SpellType spellType = getSpell(spell);
		if (spellType == null) {
			failure = true;
			return;
		} else if (!iPlayer.hasCastPermission(spellType)) {
			failure = true;
			return;
		} else if (iPlayer.onCooldown(spellType)) {
			message = "";
			return;
		}

		components.remove(0);

		int power = 1;

		if (components.contains("perpetua")) {
			perpetual = true;
			components.remove("perpetua");
		}

		if (components.contains("vi")) {
			force = true;
			components.remove("vi");
		}

		if (components.contains("silentium")) {
			silent = true;
			components.remove("silentium");
		}

		power = getPower(iPlayer, spellType);
		if (power > iPlayer.getMaxPower(spellType))
			power = iPlayer.getMaxPower(spellType);

		Component component = getComponent();

		if (componentMap.containsKey(spellType))
			if (!componentMap.get(spellType).contains(component)) {
				failure = true;
				return;
			}

		if (!components.isEmpty()) {
			failure = true;
			return;
		}

		failure = newSpell(player, spellType, component, power);

		spell = spell.substring(0, 1).toUpperCase() + spell.substring(1);
		message = spell;
		if (component != null)
			message += " " + componentString;

		if (power != 0) {
			String powerstring = "";

			switch (power) {
			case 1:
				powerstring = "I";
				break;
			case 2:
				powerstring = "II";
				break;
			case 3:
				powerstring = "III";
				break;
			case 4:
				powerstring = "IV";
				break;
			case 5:
				powerstring = "V";
				break;
			}

			message += " " + powerstring;
		}

		if (force)
			message += " vi";
		if (perpetual)
			message += " perpetua";

		if (silent) {
			message = "";
		} else {
			message += ".";
		}

	}

	public boolean wasSuccessful() {
		return !failure;
	}

	public String getMessage() {
		return message;
	}

	public boolean newSpell(Player player, SpellType spellType,
			Component component, int power) {

		World world = player.getWorld();

		IPlayer iPlayer = IPlayer.getPlayer(player);

		if (!(component == null ^ spellType.useComponents()))
			return false;

		if (perpetual && !spellType.canPerpetual())
			return false;

		if (silent && !spellType.canSilent())
			return false;

		if (force && !spellType.canForce())
			return false;

		if (iPlayer.isSilent() && spellType.canSilent())
			silent = true;
		if (iPlayer.isPerpetual() && spellType.canPerpetual())
			perpetual = true;

		if (!iPlayer.canPerpetual(spellType) && perpetual)
			perpetual = false;
		if (!iPlayer.canSilent(spellType) && silent)
			silent = false;
		if (!iPlayer.canForce(spellType) && force)
			force = false;

		Location location = TargetingTools.getTargetLocation(player);

		Material mat = null;
		if (component != null)
			mat = component.getMaterial();
		byte data = 0;

		Spell spell = null;

		switch (spellType) {
		case ILLUSION:
			List<LivingEntity> ignore = new ArrayList<LivingEntity>();
			ignore.add(player);
			spell = new Illusion(iPlayer, location, power, ignore);
			break;
		case BUBBLE:
			spell = new Bubble(iPlayer, player.getLocation(), mat, data, power,
					force, perpetual);
			break;
		case SPHERE:
			spell = new Sphere(iPlayer, player.getLocation(), mat, data, power,
					force, perpetual);
			break;
		case HEMISPHERE:
			spell = new Hemisphere(iPlayer, player.getLocation(), mat, data,
					power, force, perpetual);
			break;
		case RING:
			spell = new Ring(iPlayer, player.getLocation(), mat, data, power,
					force, perpetual);
			break;
		case ENTOMB:
			spell = new Entomb(iPlayer,
					TargetingTools.getTargetLocation(player), mat, data, power,
					force, perpetual);
			break;
		case WALL:
			Vector normal = player.getEyeLocation().getDirection();
			normal.setY(0);
			spell = new Wall(iPlayer, TargetingTools.getTargetLocation(player),
					normal, mat, data, power, force, perpetual);
			break;
		case VANISH:
			spell = new Vanish(iPlayer, TargetingTools.getTargetLocation(
					player, 20, false), mat, power, perpetual);
			break;
		case DISINTEGRATE:
			spell = new Disintegrate(iPlayer, player.getLocation(), mat, power,
					perpetual);
			break;
		case HOP:
			spell = new Hop(iPlayer, power);
			break;
		case BREATHE:
			spell = new Breathe(iPlayer, power, perpetual,
					player.getEyeLocation());
			break;
		case EXPLODE:
			spell = new Explode(iPlayer,
					TargetingTools.getTargetLocation(player), power);
			break;
		case PERPETUAL:
			new Perpetual(iPlayer);
			break;
		case SILENT:
			new Silent(iPlayer);
			break;
		case REPLENISHWATER:
			spell = new ReplenishWater(iPlayer,
					TargetingTools.getTargetLocation(player, 20, false), power,
					perpetual);
			break;
		case DISAPPEAR:
			spell = new Disappear(iPlayer, power);
			break;
		case LIGHT:
			spell = new Light(iPlayer, power, force, perpetual);
			break;
		case MINE:
			spell = new Mine(iPlayer, TargetingTools.getTargetBlock(player),
					power);
			break;
		case MUTATE:
			spell = new Mutate(iPlayer,
					TargetingTools.getTargetLocation(player), power, perpetual);
			break;
		case LIGHTNING:
			spell = new Lightning(iPlayer,
					TargetingTools.getTargetLocation(player), power);
			break;
		case HEAL:
			spell = new Heal(iPlayer, power);
			break;
		case DAY:
			new Day(world);
			break;
		case NIGHT:
			new Night(world);
			break;
		case CALM:
			new Calm(world);
			break;
		case RAIN:
			new Rain(world);
			break;
		case STORM:
			new Storm(world);
			break;
		case PULL:
			spell = new Pull(iPlayer, TargetingTools.getTargetLocation(player),
					power);
			break;
		case PUSH:
			spell = new Push(iPlayer, TargetingTools.getTargetLocation(player),
					power);
			break;
		case FREEZE:
			spell = new Freeze(iPlayer,
					TargetingTools.getTargetLocationNoTransparent(player),
					power, perpetual);
			break;
		case THAW:
			spell = new Thaw(iPlayer, TargetingTools.getTargetLocation(player,
					20, false), power, perpetual);
			break;
		case EXTINGUISH:
			new Extinguish(iPlayer);
			break;
		case STOP:
			new Stop(iPlayer,
					TargetingTools.getTargetLocation(player, 20, true), power);
			break;
		}

		iPlayer.cooldown(spellType);

		if (spell != null) {
			iPlayer.gainExp(spell);
		}

		Disappear disappear = Disappear.getDisappeared(player);
		if (disappear != null && spell.getType() != SpellType.DISAPPEAR)
			disappear.cancel();

		return false;
	}

	private Component getComponent() {
		for (Component component : Component.values()) {
			List<String> aliases = component.getAliases();
			for (String string : aliases)
				if (components.contains(string.toLowerCase())) {
					components.remove(string.toLowerCase());
					componentString = string.toLowerCase();
					return component;
				}
		}
		return null;
	}

	private int getPower(IPlayer player, SpellType spell) {
		for (int i = 1; i <= 5; i++) {
			if (components.contains(Integer.toString(i))) {
				components.remove(Integer.toString(i));
				return i;
			}
		}
		if (components.contains("v")) {
			components.remove("v");
			return 5;
		}
		if (components.contains("iv")) {
			components.remove("iv");
			return 4;
		}
		if (components.contains("iii")) {
			components.remove("iii");
			return 3;
		}
		if (components.contains("ii")) {
			components.remove("II");
			return 2;
		}
		if (components.contains("i")) {
			components.remove("i");
			return 1;
		}
		return player.getMaxPower(spell);
	}

	public static SpellType getSpell(String string) {
		if (aliasMap.containsKey(string)) {
			return aliasMap.get(string);
		}
		return null;
	}

}
