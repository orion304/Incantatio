package listeners;

import main.IncantatioReloaded;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import spells.Disappear;
import spells.Explode;
import spells.Illusion;
import spells.Lightning;
import spells.Mutate;
import tools.BlockTools;
import tools.CreatedBlock;
import tools.IPlayer;

public class EntityListener implements Listener {

	private IncantatioReloaded plugin;

	public EntityListener(IncantatioReloaded instance) {
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityDamage(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		Illusion illusion = Illusion.getCreated(entity);
		Mutate mutate = Mutate.getCreated(entity);
		if (illusion != null) {
			illusion.removeEntity(entity);
			event.setCancelled(true);
		} else if (mutate != null) {
			mutate.revert(entity);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		Entity entity = event.getEntity();
		Entity source = event.getDamager();
		Location loc1 = entity.getLocation(), loc2 = source.getLocation();
		if (source instanceof Player) {
			Disappear disappear = Disappear.getDisappeared((Player) source);
			if (disappear != null)
				disappear.cancel();
		}
		int originaldamage = event.getDamage();
		if (Lightning.wasCause(source)) {
			if (BlockTools.isObstructed(loc1, loc2)) {
				event.setCancelled(true);
				return;
			}
			if (entity instanceof Player) {
				IPlayer iPlayer = IPlayer.getPlayer((Player) entity);
				if (!iPlayer.canLightningStrike()) {
					event.setCancelled(true);
					return;
				}
				iPlayer.lightningStrike();
			}
			event.setDamage(Lightning.getDamage(source, originaldamage));
		}
		if (Explode.wasCause(source)) {
			if (BlockTools.isObstructed(loc1, loc2)) {
				event.setCancelled(true);
				return;
			}
			if (entity instanceof Player) {
				IPlayer iPlayer = IPlayer.getPlayer((Player) entity);
				if (!iPlayer.canExplosion()) {
					event.setCancelled(true);
					return;
				}
				iPlayer.explosion();
			}
			event.setDamage(Explode.getDamage(source, originaldamage));
		}
		Illusion illusion = Illusion.getCreated(entity);
		Mutate mutate = Mutate.getCreated(entity);
		boolean cancel = false;
		if (illusion != null) {
			illusion.removeEntity(entity);
			cancel = true;
		} else if (mutate != null) {
			mutate.revert(entity);
			cancel = true;
		}
		illusion = Illusion.getCreated(source);
		mutate = Mutate.getCreated(source);
		if (illusion != null) {
			illusion.removeEntity(source);
			cancel = true;
		} else if (mutate != null) {
			mutate.revert(source);
			cancel = true;
		}
		if (cancel) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityTarget(EntityTargetEvent event) {
		// ServerTools.verbose("target entity");
		Entity entity = event.getEntity();
		Entity target = event.getTarget();
		Illusion illusion = Illusion.getCreated(entity);
		if (illusion != null) {
			if (illusion.isIngored(target)) {
				event.setTarget(null);
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent event) {
		// ServerTools.verbose("target living entity");
		Entity entity = event.getEntity();
		LivingEntity target = event.getTarget();
		Illusion illusion = Illusion.getCreated(entity);
		if (illusion != null) {
			if (illusion.isIngored(target)) {
				event.setTarget(null);
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onExplosionPrime(ExplosionPrimeEvent event) {
		Entity entity = event.getEntity();
		Illusion illusion = Illusion.getCreated(entity);
		if (illusion != null) {
			illusion.removeEntity(entity);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityExplode(EntityExplodeEvent event) {
		for (Block block : event.blockList()) {
			CreatedBlock.revert(block);
		}
	}

}
