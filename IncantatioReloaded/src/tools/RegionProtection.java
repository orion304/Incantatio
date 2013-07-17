package tools;

import java.util.ArrayList;
import java.util.List;

import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.sacredlabyrinth.Phaed.PreciousStones.FieldFlag;
import net.sacredlabyrinth.Phaed.PreciousStones.PreciousStones;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.massivecraft.factions.listeners.FactionsBlockListener;
import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.object.Coord;
import com.palmergames.bukkit.towny.object.PlayerCache;
import com.palmergames.bukkit.towny.object.PlayerCache.TownBlockStatus;
import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.palmergames.bukkit.towny.object.TownyWorld;
import com.palmergames.bukkit.towny.object.WorldCoord;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;
import com.palmergames.bukkit.towny.war.flagwar.TownyWar;
import com.palmergames.bukkit.towny.war.flagwar.TownyWarConfig;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;

import enums.SpellType;

public class RegionProtection {

	private static boolean respectWorldGuard = true;
	private static boolean respectFactions = true;
	private static boolean respectTowny = true;
	private static boolean respectPreciousStones = true;
	private static boolean respectGriefPrevention = true;

	public static boolean isRegionProtectedFromBuild(Player player,
			SpellType type, Location loc) {

		List<SpellType> ignite = new ArrayList<SpellType>();

		List<SpellType> explode = new ArrayList<SpellType>();
		explode.add(SpellType.EXPLODE);
		explode.add(SpellType.LIGHTNING);

		// if (ability == null && allowharmless)
		// return false;
		// if (isHarmlessAbility(ability) && allowharmless)
		// return false;

		// if (ignite.contains(ability)) {
		// BlockIgniteEvent event = new BlockIgniteEvent(location.getBlock(),
		// IgniteCause.FLINT_AND_STEEL, player);
		// Bending.plugin.getServer().getPluginManager().callEvent(event);
		// if (event.isCancelled())
		// return false;
		// event.setCancelled(true);
		// }

		Plugin wgp = Bukkit.getPluginManager().getPlugin("WorldGuard");
		Plugin psp = Bukkit.getPluginManager().getPlugin("PreciousStone");
		Plugin fcp = Bukkit.getPluginManager().getPlugin("Factions");
		Plugin twnp = Bukkit.getPluginManager().getPlugin("Towny");
		Plugin gpp = Bukkit.getPluginManager().getPlugin("GriefPrevention");

		for (Location location : new Location[] { loc, player.getLocation() }) {

			if (wgp != null && respectWorldGuard) {
				WorldGuardPlugin wg = (WorldGuardPlugin) Bukkit
						.getPluginManager().getPlugin("WorldGuard");
				if (!player.isOnline())
					return true;

				if (ignite.contains(type)) {
					if (!wg.hasPermission(player, "worldguard.override.lighter")) {
						if (wg.getGlobalStateManager().get(location.getWorld()).blockLighter)
							return true;
						if (!wg.getGlobalRegionManager().hasBypass(player,
								location.getWorld())
								&& !wg.getGlobalRegionManager()
										.get(location.getWorld())
										.getApplicableRegions(location)
										.allows(DefaultFlag.LIGHTER,
												wg.wrapPlayer(player)))
							return true;
					}

				}

				if (explode.contains(type)) {
					if (wg.getGlobalStateManager().get(location.getWorld()).blockTNTExplosions)
						return true;
					if (!wg.getGlobalRegionManager().get(location.getWorld())
							.getApplicableRegions(location)
							.allows(DefaultFlag.TNT))
						return true;
				}

				if ((!(wg.getGlobalRegionManager().canBuild(player, location)) || !(wg
						.getGlobalRegionManager()
						.canConstruct(player, location)))) {
					return true;
				}
			}

			if (psp != null && respectPreciousStones) {
				PreciousStones ps = (PreciousStones) psp;

				if (ignite.contains(type)) {
					if (ps.getForceFieldManager().hasSourceField(location,
							FieldFlag.PREVENT_FIRE))
						return true;
				}

				if (explode.contains(type)) {
					if (ps.getForceFieldManager().hasSourceField(location,
							FieldFlag.PREVENT_EXPLOSIONS))
						return true;
				}

				if (ps.getForceFieldManager().hasSourceField(location,
						FieldFlag.PREVENT_PLACE))
					return true;
			}

			if (fcp != null && respectFactions) {
				if (ignite.contains(type)) {

				}

				if (explode.contains(type)) {

				}

				if (!FactionsBlockListener.playerCanBuildDestroyBlock(player,
						location, "build", true)) {
					return true;
				}
			}

			if (twnp != null && respectTowny) {
				Towny twn = (Towny) twnp;

				WorldCoord worldCoord;

				try {
					TownyWorld world = TownyUniverse.getDataSource().getWorld(
							location.getWorld().getName());
					worldCoord = new WorldCoord(world.getName(),
							Coord.parseCoord(location));

					boolean bBuild = PlayerCacheUtil.getCachePermission(player,
							location, 3, (byte) 0,
							TownyPermission.ActionType.BUILD);

					if (ignite.contains(type)) {

					}

					if (explode.contains(type)) {

					}

					if (!bBuild) {
						PlayerCache cache = twn.getCache(player);
						TownBlockStatus status = cache.getStatus();

						if (((status == TownBlockStatus.ENEMY) && TownyWarConfig
								.isAllowingAttacks())) {

							try {
								TownyWar.callAttackCellEvent(twn, player,
										location.getBlock(), worldCoord);
							} catch (Exception e) {
								TownyMessaging.sendErrorMsg(player,
										e.getMessage());
							}

							return true;

						} else if (status == TownBlockStatus.WARZONE) {
						} else {
							return true;
						}

						if ((cache.hasBlockErrMsg()))
							TownyMessaging.sendErrorMsg(player,
									cache.getBlockErrMsg());
					}

				} catch (Exception e1) {
					TownyMessaging.sendErrorMsg(player, TownySettings
							.getLangString("msg_err_not_configured"));
				}

			}

			if (gpp != null && respectGriefPrevention) {
				String reason = GriefPrevention.instance.allowBuild(player,
						location);

				if (ignite.contains(type)) {

				}

				if (explode.contains(type)) {

				}

				if (reason != null)
					return true;
			}
		}

		return false;
	}

}
