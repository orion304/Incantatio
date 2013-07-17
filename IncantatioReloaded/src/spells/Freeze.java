package spells;

import interfaces.CreateBlocks;
import interfaces.Spell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import tools.BlockTools;
import tools.CreatedBlock;
import tools.IPlayer;
import enums.SpellType;

public class Freeze implements Spell, CreateBlocks {

	private int exp = 1;
	private int radius = 4;
	private long duration = 10000;
	int power;
	private IPlayer caster;

	private List<CreatedBlock> created = new ArrayList<CreatedBlock>();

	public Freeze(IPlayer player, Location origin, int power, boolean perpetual) {
		caster = player;
		this.power = power;
		radius *= power;
		duration *= Math.pow(2, power - 1);
		Material mat = Material.WATER;

		if (perpetual)
			duration = 0;

		List<Block> pending = new ArrayList<Block>();

		for (Block block : BlockTools.getBlocksAroundPoint(origin, radius)) {
			double distance = block.getLocation().distance(origin);
			if (distance <= (double) radius) {
				pending.add(block);
			}
		}

		new ChangeBlocksSpell(pending, mat, Material.ICE, duration, this, 1000,
				this, true);

	}

	@Override
	public List<CreatedBlock> getCreatedBlocks() {
		return created;
	}

	public void removeBlock(CreatedBlock block) {
		block.revert();
	}

	public static List<String> getAliases() {
		String[] aliases = { "frigidus" };
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
		return SpellType.FREEZE;
	}

	@Override
	public IPlayer getCaster() {
		return caster;
	}

}
