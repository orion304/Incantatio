package tools;

import main.IncantatioReloaded;

public class ServerTools {

	private static IncantatioReloaded plugin;

	public ServerTools(IncantatioReloaded instance) {
		plugin = instance;
	}

	public static <T> void verbose(T... something) {
		for (T thing : something) {
			if (thing == null)
				plugin.logger.info("[IncantatioReloaded] null");
			else
				plugin.logger.info("[IncantatioReloaded] " + thing.toString());
		}
	}

}
