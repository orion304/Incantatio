package spells;

import interfaces.CreateBlocks;
import interfaces.Spell;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;

import tools.BlockTools;
import tools.CreatedBlock;

public class CreateBlocksSpell {

	private static ConcurrentHashMap<Integer, CreateBlocksSpell> instances = new ConcurrentHashMap<Integer, CreateBlocksSpell>();
	private static int ID = Integer.MIN_VALUE;

	private long duration;
	private long starttime, time, animduration;
	private int id;
	private int i = 0, di, max;
	private Material mat;
	private byte data;
	private CreateBlocks source;
	private Spell spell;
	private boolean force = false;

	private List<Block> pending = new ArrayList<Block>();

	public CreateBlocksSpell(List<Block> blocks, Material mat, byte data,
			boolean force, long duration, CreateBlocks source,
			long spellanimationduration, Spell spell) {
		pending = blocks;
		this.spell = spell;

		max = pending.size();

		this.mat = mat;
		this.data = data;
		this.duration = duration;

		time = System.currentTimeMillis();
		starttime = time;
		animduration = spellanimationduration;
		this.source = source;
		this.force = force;

		id = ID++;
		if (ID >= Integer.MAX_VALUE)
			ID = Integer.MIN_VALUE;

		instances.put(id, this);
	}

	public static void handle() {
		for (int id : instances.keySet())
			instances.get(id).progress();
	}

	private void progress() {
		long remaining = starttime + animduration - time;
		long elapsed = System.currentTimeMillis() - time;

		di = (int) (((double) (max - i)) / ((double) remaining) * ((double) elapsed));
		boolean stop = false;
		if (di <= 1)
			di = 2;
		if (i + di >= max) {
			di = max - i;
			stop = true;
		}

		for (Block block : pending.subList(i, i + di)) {
			if (BlockTools.isOverwriteable(block, mat, force)
					&& spell.getCaster().hasSpellPermission(spell,
							block.getLocation())
					&& (!mat.equals(Material.FIRE) || BlockTools
							.isIgnitable(block)))
				source.addBlock(new CreatedBlock(block, mat, data, duration,
						source));
		}

		if (stop)
			instances.remove(id);

		i += di;

	}
}
