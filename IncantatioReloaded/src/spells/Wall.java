package spells;

import interfaces.CreateBlocks;
import interfaces.Spell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import tools.BlockTools;
import tools.CreatedBlock;
import tools.IPlayer;
import enums.Component;
import enums.SpellType;

public class Wall implements Spell, CreateBlocks {

	private static Component[] allowedComponents = { Component.FIRE,
			Component.GLASS, Component.LEAVES, Component.WEB };

	private int exp = 1;
	private int length = 4;
	private long duration = 10000;
	private IPlayer caster;

	private List<CreatedBlock> created = new ArrayList<CreatedBlock>();

	public Wall(IPlayer player, Location origin, Vector normal, Material mat,
			byte data, int power, boolean force, boolean perpetual) {
		caster = player;
		length *= power;
		duration *= Math.pow(2, power - 1);

		if (perpetual)
			duration = 0;

		List<Block> pending = new ArrayList<Block>();

		for (Block block : BlockTools.getBlocksOnPlane(origin, normal, length)) {
			pending.add(block);
		}

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
		String[] aliases = { "murus" };
		return Arrays.asList(aliases);
	}

	@Override
	public void addBlock(CreatedBlock block) {
		created.add(block);
	}

	public static List<Component> getAllowedComponents() {
		return Arrays.asList(allowedComponents);
	}

	@Override
	public int getExp() {
		return exp;
	}

	@Override
	public SpellType getType() {
		return SpellType.WALL;
	}

	@Override
	public IPlayer getCaster() {
		return caster;
	}
}
