package spells;

import interfaces.CreateBlocks;
import interfaces.Spell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import tools.CreatedBlock;
import tools.IPlayer;
import enums.SpellType;

public class Light implements Spell, CreateBlocks {

	private int exp = 1;
	private int range = 20;
	private long duration = 10000;
	int power;
	private Material mat = Material.GLOWSTONE;
	private byte data = 0;
	private IPlayer caster;

	private List<CreatedBlock> created = new ArrayList<CreatedBlock>();

	public Light(IPlayer caster, int power, boolean force, boolean perpetual) {
		Player player = caster.getPlayer();
		this.caster = caster;
		this.power = power;
		duration *= Math.pow(2, power - 1);

		if (perpetual)
			duration = 0;

		List<Block> pending = new ArrayList<Block>();

		int i = 0;
		if (force)
			i = 1;

		pending.add(player.getLastTwoTargetBlocks(null, range).get(i));

		new CreateBlocksSpell(pending, mat, data, force, duration, this, 1000,
				this);

	}

	@Override
	public List<CreatedBlock> getCreatedBlocks() {
		return created;
	}

	public void removeBlock(CreatedBlock block) {
		block.revert();
	}

	public static List<String> getAliases() {
		String[] aliases = { "lux" };
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
		return SpellType.LIGHT;
	}

	@Override
	public IPlayer getCaster() {
		return caster;
	}

}
