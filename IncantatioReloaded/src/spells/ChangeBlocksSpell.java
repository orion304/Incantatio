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

public class ChangeBlocksSpell {

	private static ConcurrentHashMap<Integer, ChangeBlocksSpell> instances = new ConcurrentHashMap<Integer, ChangeBlocksSpell>();
	private static int ID = Integer.MIN_VALUE;

	private long duration;
	private long starttime, time, animduration;
	private int id;
	private int i = 0, di, max;
	private Material mat, changemat;
	private byte data = 0;
	private CreateBlocks source;
	private Spell spell;
	private boolean dispell;

	private List<Block> pending = new ArrayList<Block>();

	public ChangeBlocksSpell(List<Block> blocks, Material mat,
			Material changemat, long duration, CreateBlocks source,
			long spellanimationduration, Spell spell, boolean dispell) {
		pending = blocks;
		this.spell = spell;
		this.dispell = dispell;

		max = pending.size();

		this.mat = mat;
		this.changemat = changemat;
		this.duration = duration;

		time = System.currentTimeMillis();
		starttime = time;
		animduration = spellanimationduration;
		this.source = source;

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
			if ((!CreatedBlock.isCreatedBlock(block) || dispell)
					&& spell.getCaster().hasSpellPermission(spell,
							block.getLocation())
					&& (block.getType() == mat
							|| (mat == Material.WATER && BlockTools
									.isWater(block)) || (mat == Material.LAVA && BlockTools
							.isLava(block))))
				source.addBlock(new CreatedBlock(block, changemat, data,
						duration, source));
		}

		if (stop)
			instances.remove(id);

		i += di;

	}

}
