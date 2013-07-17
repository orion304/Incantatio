package interfaces;

import tools.IPlayer;
import enums.SpellType;

public interface Spell {

	public int getExp();

	public SpellType getType();

	public IPlayer getCaster();

}
