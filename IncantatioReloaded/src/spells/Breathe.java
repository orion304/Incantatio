package spells;

import interfaces.CreateBlocks;
import interfaces.Spell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import tools.CreatedBlock;
import tools.IPlayer;
import enums.SpellType;

public class Breathe implements Spell, CreateBlocks {

	private long duration = 10000;
	private byte data = 0;
	private int exp = 0;
	private IPlayer caster;

	private List<CreatedBlock> created = new ArrayList<CreatedBlock>();

	public Breathe(IPlayer player, int power, boolean perpetual,
			Location... origin) {
		duration *= Math.pow(2, power - 1);
		caster = player;

		if (perpetual)
			duration = 0;

		List<Block> pending = new ArrayList<Block>();

		for (Location location : origin) {
			pending.add(location.getBlock());
			if (location.getBlock().getType() != Material.AIR)
				exp = 1;
		}

		new CreateBlocksSpell(pending, Material.AIR, data, true, duration,
				this, 1000, this);

	}

	@Override
	public List<CreatedBlock> getCreatedBlocks() {
		return created;
	}

	public void removeBlock(CreatedBlock block) {
		block.revert();
	}

	public static List<String> getAliases() {
		String[] aliases = { "respiro" };
		return Arrays.asList(aliases);
	}

	@Override
	public void addBlock(CreatedBlock block) {
		created.add(block);

	}

	@Override
	public int getExp() {
		return exp;
	}

	@Override
	public SpellType getType() {
		return SpellType.BREATHE;
	}

	@Override
	public IPlayer getCaster() {
		return caster;
	}
}
