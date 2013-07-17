package tools;

import interfaces.CreateBlocks;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

public class CreatedBlock {

	private static ConcurrentHashMap<Block, CreatedBlock> blocks = new ConcurrentHashMap<Block, CreatedBlock>();

	private Block block;
	private BlockState original;
	private long time, duration;
	private CreateBlocks cause;

	public CreatedBlock(Block block, Material mat, CreateBlocks source) {
		this(block, mat, 0, source);
	}

	public CreatedBlock(Block block, Material mat, long duration,
			CreateBlocks source) {
		if (blocks.containsKey(block)) {
			blocks.get(block).update(mat, duration, source);
		} else {
			this.block = block;
			original = block.getState();
			update(mat, duration, source);
			if (duration != 0) {
				blocks.put(block, this);
			}
		}
	}

	public CreatedBlock(Block block, Material mat, byte data,
			CreateBlocks source) {
		this(block, mat, data, 0, source);
	}

	public CreatedBlock(Block block, Material mat, byte data, long duration,
			CreateBlocks source) {
		if (blocks.containsKey(block)) {
			blocks.get(block).update(mat, data, duration, source);
		} else {
			this.block = block;
			original = block.getState();
			update(mat, data, duration, source);
			if (duration != 0) {
				blocks.put(block, this);
			}
		}
	}

	public void update(Material mat, long duration, CreateBlocks source) {
		time = System.currentTimeMillis();
		cause = source;
		this.duration = duration;
		block.setType(mat);
		if (duration == 0) {
			remove();
		}
	}

	public void update(Material mat, byte data, long duration,
			CreateBlocks source) {
		update(mat, duration, source);
		block.setData(data);
	}

	public void revert() {
		original.update(true);
		remove();
	}

	private void remove() {
		if (blocks.containsKey(block))
			blocks.remove(block);
	}

	public void setOriginal(BlockState state) {
		original = state;
	}

	public void setOriginal(Material mat) {
		original.setType(mat);
	}

	public void setOriginal(Material mat, byte data) {
		setOriginal(mat);
		original.setRawData(data);
	}

	public CreateBlocks getCause() {
		return cause;
	}

	private void progress(long now) {
		if (now > time + duration) {
			cause.removeBlock(this);
			EffectTools.playRemoveBlockEffect(block);
		}
	}

	public static void handle() {
		long time = System.currentTimeMillis();
		for (Block block : blocks.keySet()) {
			CreatedBlock cblock = blocks.get(block);
			cblock.progress(time);
		}
	}

	public static void removeAll() {
		for (Block block : blocks.keySet()) {
			blocks.get(block).revert();
		}
	}

	public static void revert(Block block) {
		if (blocks.containsKey(block)) {
			blocks.get(block).revert();
		}
	}

	public static boolean isCreatedBlock(Block block) {
		return blocks.containsKey(block);

	}
}
