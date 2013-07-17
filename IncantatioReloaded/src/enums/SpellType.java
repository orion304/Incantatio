package enums;

public enum SpellType {

	ILLUSION, BUBBLE, SPHERE, HEMISPHERE, WALL,

	RING, ENTOMB, VANISH, DISINTEGRATE, HOP,

	BREATHE, EXPLODE, SILENT, PERPETUAL,

	REPLENISHWATER, DISAPPEAR, LIGHT, MINE, MUTATE,

	LIGHTNING, HEAL, DAY, NIGHT, CALM, RAIN, STORM,

	PULL, PUSH, FREEZE, THAW, EXTINGUISH, STOP;

	public boolean useComponents() {
		return useComponents(this);
	}

	public boolean canForce() {
		return canForce(this);
	}

	public boolean canSilent() {
		return canSilent(this);
	}

	public boolean canPerpetual() {
		return canPerpetual(this);
	}

	private static int getProperty(SpellType type, SpellProperty property,
			int exp) {
		int force = 0;
		int silent = 0;
		int perpetual = 0;
		int component = 0;
		int level = 0;
		switch (type) {
		case ILLUSION:
			component = 0;
			force = 0;
			silent = 1;
			perpetual = 0;
			if (exp < 20) {
				level = 1;
			} else if (exp < 60) {
				level = 2;
			} else if (exp < 140) {
				level = 3;
			} else if (exp < 300) {
				level = 4;
			} else {
				level = 5;
			}
			break;
		case HOP:
			component = 0;
			force = 0;
			silent = 1;
			perpetual = 0;
			if (exp < 100) {
				level = 1;
			} else if (exp < 500) {
				level = 2;
			} else if (exp < 2100) {
				level = 3;
			} else if (exp < 8500) {
				level = 4;
			} else {
				level = 5;
			}
			break;
		case BUBBLE:
			component = 1;
			force = 1;
			silent = 1;
			perpetual = 1;
			if (exp < 20) {
				level = 1;
			} else if (exp < 60) {
				level = 2;
			} else if (exp < 140) {
				level = 3;
			} else if (exp < 300) {
				level = 4;
			} else {
				level = 5;
			}
			break;
		case SPHERE:
			component = 1;
			force = 1;
			silent = 1;
			perpetual = 1;
			if (exp < 20) {
				level = 1;
			} else if (exp < 60) {
				level = 2;
			} else if (exp < 140) {
				level = 3;
			} else if (exp < 300) {
				level = 4;
			} else {
				level = 5;
			}
			break;
		case HEMISPHERE:
			component = 1;
			force = 1;
			silent = 1;
			perpetual = 1;
			if (exp < 20) {
				level = 1;
			} else if (exp < 60) {
				level = 2;
			} else if (exp < 140) {
				level = 3;
			} else if (exp < 300) {
				level = 4;
			} else {
				level = 5;
			}
			break;
		case WALL:
			component = 1;
			force = 1;
			silent = 1;
			perpetual = 1;
			if (exp < 20) {
				level = 1;
			} else if (exp < 60) {
				level = 2;
			} else if (exp < 140) {
				level = 3;
			} else if (exp < 300) {
				level = 4;
			} else {
				level = 5;
			}
			break;
		case RING:
			component = 1;
			force = 1;
			silent = 1;
			perpetual = 1;
			if (exp < 20) {
				level = 1;
			} else if (exp < 60) {
				level = 2;
			} else if (exp < 140) {
				level = 3;
			} else if (exp < 300) {
				level = 4;
			} else {
				level = 5;
			}
			break;
		case ENTOMB:
			component = 1;
			force = 1;
			silent = 1;
			perpetual = 1;
			if (exp < 20) {
				level = 1;
			} else if (exp < 60) {
				level = 2;
			} else if (exp < 140) {
				level = 3;
			} else if (exp < 300) {
				level = 4;
			} else {
				level = 5;
			}
			break;
		case VANISH:
			component = 1;
			force = 0;
			silent = 1;
			perpetual = 1;
			if (exp < 20) {
				level = 1;
			} else if (exp < 60) {
				level = 2;
			} else if (exp < 140) {
				level = 3;
			} else if (exp < 300) {
				level = 4;
			} else {
				level = 5;
			}
			break;
		case DISINTEGRATE:
			component = 1;
			force = 0;
			silent = 1;
			perpetual = 1;
			if (exp < 20) {
				level = 1;
			} else if (exp < 60) {
				level = 2;
			} else if (exp < 140) {
				level = 3;
			} else if (exp < 300) {
				level = 4;
			} else {
				level = 5;
			}
			break;
		case BREATHE:
			component = 0;
			force = 0;
			silent = 1;
			perpetual = 1;
			if (exp < 10) {
				level = 1;
			} else if (exp < 30) {
				level = 2;
			} else if (exp < 70) {
				level = 3;
			} else if (exp < 150) {
				level = 4;
			} else {
				level = 5;
			}
			break;
		case EXPLODE:
			component = 0;
			force = 0;
			silent = 1;
			perpetual = 0;
			if (exp < 20) {
				level = 1;
			} else if (exp < 60) {
				level = 2;
			} else if (exp < 140) {
				level = 3;
			} else if (exp < 300) {
				level = 4;
			} else {
				level = 5;
			}
			break;
		case SILENT:
			component = 0;
			force = 0;
			silent = 0;
			perpetual = 0;
			break;
		case PERPETUAL:
			component = 0;
			force = 0;
			silent = 0;
			perpetual = 0;
			break;
		case REPLENISHWATER:
			component = 1;
			force = 0;
			silent = 1;
			perpetual = 1;
			if (exp < 10) {
				level = 1;
			} else if (exp < 30) {
				level = 2;
			} else if (exp < 70) {
				level = 3;
			} else if (exp < 140) {
				level = 4;
			} else {
				level = 5;
			}
			break;
		case DISAPPEAR:
			component = 0;
			force = 0;
			silent = 1;
			perpetual = 0;
			if (exp < 20) {
				level = 1;
			} else if (exp < 60) {
				level = 2;
			} else if (exp < 140) {
				level = 3;
			} else if (exp < 300) {
				level = 4;
			} else {
				level = 5;
			}
			break;
		case LIGHT:
			component = 0;
			force = 1;
			silent = 1;
			perpetual = 1;
			if (exp < 10) {
				level = 1;
			} else if (exp < 30) {
				level = 2;
			} else if (exp < 70) {
				level = 3;
			} else if (exp < 140) {
				level = 4;
			} else {
				level = 5;
			}
			break;
		case MINE:
			component = 0;
			force = 0;
			silent = 1;
			perpetual = 0;
			if (exp < 20) {
				level = 1;
			} else if (exp < 60) {
				level = 2;
			} else if (exp < 140) {
				level = 3;
			} else if (exp < 300) {
				level = 4;
			} else {
				level = 5;
			}
			break;
		case MUTATE:
			component = 0;
			force = 0;
			silent = 1;
			perpetual = 1;
			if (exp < 20) {
				level = 1;
			} else if (exp < 60) {
				level = 2;
			} else if (exp < 140) {
				level = 3;
			} else if (exp < 300) {
				level = 4;
			} else {
				level = 5;
			}
			break;
		case LIGHTNING:
			component = 0;
			force = 0;
			silent = 1;
			perpetual = 0;
			if (exp < 20) {
				level = 1;
			} else if (exp < 60) {
				level = 2;
			} else if (exp < 140) {
				level = 3;
			} else if (exp < 300) {
				level = 4;
			} else {
				level = 5;
			}
			break;
		case HEAL:
			component = 0;
			force = 0;
			silent = 1;
			perpetual = 0;
			if (exp < 100) {
				level = 1;
			} else if (exp < 300) {
				level = 2;
			} else if (exp < 700) {
				level = 3;
			} else if (exp < 1500) {
				level = 4;
			} else {
				level = 5;
			}
			break;
		case DAY:
			component = 0;
			force = 0;
			silent = 0;
			perpetual = 0;
			break;
		case NIGHT:
			component = 0;
			force = 0;
			silent = 0;
			perpetual = 0;
			break;
		case CALM:
			component = 0;
			force = 0;
			silent = 0;
			perpetual = 0;
			break;
		case RAIN:
			component = 0;
			force = 0;
			silent = 0;
			perpetual = 0;
			break;
		case STORM:
			component = 0;
			force = 0;
			silent = 0;
			perpetual = 0;
			break;
		case PULL:
			component = 0;
			force = 0;
			silent = 1;
			perpetual = 0;
			if (exp < 20) {
				level = 1;
			} else if (exp < 60) {
				level = 2;
			} else if (exp < 140) {
				level = 3;
			} else if (exp < 300) {
				level = 4;
			} else {
				level = 5;
			}
			break;
		case PUSH:
			component = 0;
			force = 0;
			silent = 1;
			perpetual = 0;
			if (exp < 20) {
				level = 1;
			} else if (exp < 60) {
				level = 2;
			} else if (exp < 140) {
				level = 3;
			} else if (exp < 300) {
				level = 4;
			} else {
				level = 5;
			}
			break;
		case FREEZE:
			component = 0;
			force = 0;
			silent = 1;
			perpetual = 1;
			if (exp < 20) {
				level = 1;
			} else if (exp < 60) {
				level = 2;
			} else if (exp < 140) {
				level = 3;
			} else if (exp < 300) {
				level = 4;
			} else {
				level = 5;
			}
			break;
		case THAW:
			component = 0;
			force = 0;
			silent = 1;
			perpetual = 1;
			if (exp < 20) {
				level = 1;
			} else if (exp < 60) {
				level = 2;
			} else if (exp < 140) {
				level = 3;
			} else if (exp < 300) {
				level = 4;
			} else {
				level = 5;
			}
			break;
		case EXTINGUISH:
			component = 0;
			force = 0;
			silent = 1;
			perpetual = 0;
			break;
		case STOP:
			component = 0;
			force = 0;
			silent = 1;
			perpetual = 0;
			if (exp < 60) {
				level = 1;
			} else if (exp < 180) {
				level = 2;
			} else if (exp < 420) {
				level = 3;
			} else if (exp < 900) {
				level = 4;
			} else {
				level = 5;
			}
			break;
		}

		switch (property) {
		case FORCE:
			return force;
		case SILENT:
			return silent;
		case PERPETUAL:
			return perpetual;
		case COMPONENT:
			return component;
		case LEVEL:
			return level;
		}

		return 0;
	}

	public static int getLevel(SpellType type, int exp) {
		return getProperty(type, SpellProperty.LEVEL, exp);
	}

	public static boolean useComponents(SpellType type) {
		int value = getProperty(type, SpellProperty.COMPONENT, 0);
		if (value == 1)
			return true;
		return false;
	}

	public static boolean canForce(SpellType type) {
		int value = getProperty(type, SpellProperty.FORCE, 0);
		if (value == 1)
			return true;
		return false;
	}

	public static boolean canSilent(SpellType type) {
		int value = getProperty(type, SpellProperty.SILENT, 0);
		if (value == 1)
			return true;
		return false;
	}

	public static boolean canPerpetual(SpellType type) {
		int value = getProperty(type, SpellProperty.PERPETUAL, 0);
		if (value == 1)
			return true;
		return false;
	}

}
