package enums;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

public enum Component {

	GLASS, WEB, WATER, AIR, LEAVES, FIRE, LAVA, ICE, DIRT, STONE;

	private static String[] glassAliases = { "vitrea" };
	private static String[] webAliases = { "aranea" };
	private static String[] waterAliases = { "aquae" };
	private static String[] iceAliases = { "glacies" };
	private static String[] airAliases = { "aeris" };
	private static String[] leavesAliases = { "folia" };
	private static String[] fireAliases = { "ignis" };
	private static String[] lavaAliases = { "silice" };
	private static String[] dirtAliases = { "sordes" };
	private static String[] stoneAliases = { "lapis" };

	public static Component getComponent(String alias) {
		for (Component component : values()) {
			if (getAliases(component).contains(alias))
				return component;
		}
		return null;
	}

	public List<String> getAliases() {
		return getAliases(this);
	}

	private static List<String> getAliases(Component component) {
		String[] aliases = {};
		switch (component) {
		case GLASS:
			aliases = glassAliases;
			break;
		case WEB:
			aliases = webAliases;
			break;
		case WATER:
			aliases = waterAliases;
			break;
		case ICE:
			aliases = iceAliases;
			break;
		case AIR:
			aliases = airAliases;
			break;
		case LEAVES:
			aliases = leavesAliases;
			break;
		case FIRE:
			aliases = fireAliases;
			break;
		case LAVA:
			aliases = lavaAliases;
			break;
		case DIRT:
			aliases = dirtAliases;
			break;
		case STONE:
			aliases = stoneAliases;
			break;
		}
		return Arrays.asList(aliases);
	}

	public static Material getMaterial(Component component) {
		Material mat = null;
		switch (component) {
		case GLASS:
			mat = Material.GLASS;
			break;
		case WEB:
			mat = Material.WEB;
			break;
		case WATER:
			mat = Material.WATER;
			break;
		case ICE:
			mat = Material.ICE;
			break;
		case AIR:
			mat = Material.AIR;
			break;
		case LEAVES:
			mat = Material.LEAVES;
			break;
		case FIRE:
			mat = Material.FIRE;
			break;
		case LAVA:
			mat = Material.LAVA;
			break;
		case DIRT:
			mat = Material.DIRT;
			break;
		case STONE:
			mat = Material.STONE;
			break;
		}
		return mat;
	}

	public Material getMaterial() {
		return getMaterial(this);
	}
}
