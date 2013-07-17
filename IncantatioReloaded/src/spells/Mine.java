package spells;

import interfaces.Spell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import tools.BlockTools;
import tools.IPlayer;
import enums.SpellType;

public class Mine implements Spell {

	private double radius = 1;
	private int exp = 0;
	private IPlayer caster;

	public Mine(IPlayer player, Block origin, int power) {
		caster = player;
		radius = radius * power / 2. + .5;

		List<Block> blocks = new ArrayList<Block>();
		if (power == 1) {
			blocks.add(origin);
		} else {
			blocks.addAll(BlockTools.getBlocksAroundPoint(origin.getLocation(),
					radius));
		}

		Material mat = Material.AIR;
		int maxPower = player.getMaxPower(SpellType.MINE);
		switch (maxPower) {
		case 2:
			mat = Material.WOOD_PICKAXE;
		case 3:
			mat = Material.STONE_PICKAXE;
		case 4:
			mat = Material.IRON_PICKAXE;
		case 5:
			mat = Material.DIAMOND_PICKAXE;
		}

		ItemStack item = new ItemStack(mat);

		for (Block block : blocks) {
			if (block.getType() != Material.BEDROCK
					&& caster.hasSpellPermission(this, block.getLocation())) {
				block.breakNaturally(item);
				exp++;
			}
		}

	}

	public static List<String> getAliases() {
		String[] aliases = { "perforo" };
		return Arrays.asList(aliases);
	}

	@Override
	public int getExp() {
		return exp;
	}

	@Override
	public SpellType getType() {
		return SpellType.MINE;
	}

	@Override
	public IPlayer getCaster() {
		return caster;
	}
}
