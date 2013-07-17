package listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;

import spells.Stop;
import tools.CreatedBlock;

public class BlockListener implements Listener {

	public BlockListener() {

	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockDamage(BlockDamageEvent event) {
		Block block = event.getBlock();
		if (CreatedBlock.isCreatedBlock(block)) {
			event.setCancelled(true);
			CreatedBlock.revert(block);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		CreatedBlock.revert(block);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockFade(BlockFadeEvent event) {
		Block block = event.getBlock();
		if (CreatedBlock.isCreatedBlock(block))
			event.setCancelled(true);
		if (Stop.isFrozen(block))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockBurn(BlockBurnEvent event) {
		Block block = event.getBlock();
		if (Stop.isFrozen(block)) {
			event.setCancelled(true);
			return;
		}
		CreatedBlock.revert(block);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockFlow(BlockFromToEvent event) {
		Block from = event.getBlock();
		Block to = event.getToBlock();

		if (Stop.isFrozen(to) || Stop.isFrozen(from)) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockPhysics(BlockPhysicsEvent event) {
		if (Stop.isFrozen(event.getBlock())) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockSpread(BlockSpreadEvent event) {
		Block source = event.getSource();
		Block block = event.getBlock();
		if (Stop.isFrozen(source) || Stop.isFrozen(block)) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onLeavesDecay(LeavesDecayEvent event) {
		if (Stop.isFrozen(event.getBlock())) {
			event.setCancelled(true);
		}
	}

}
