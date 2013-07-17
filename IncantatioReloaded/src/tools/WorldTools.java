package tools;

import org.bukkit.World;

public class WorldTools {

	public static float getTNTYield(World world, float yield) {
		float factor = 1F;
		switch (world.getDifficulty()) {
		case PEACEFUL:
			factor = 2F;
			break;
		case EASY:
			factor = 2F;
			break;
		case NORMAL:
			factor = 1F;
			break;
		case HARD:
			factor = 3F / 4F;
			break;
		}
		return yield * factor;
	}

}
