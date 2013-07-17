package main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import spells.ChangeBlocksSpell;
import spells.CreateBlocksSpell;
import spells.Disappear;
import spells.Illusion;
import spells.Lightning;
import spells.Mutate;
import spells.SpellHandler;
import spells.Stop;
import tools.CreatedBlock;
import tools.EntityTools;

public class Manager implements Runnable {

	private ArrayList<Spell> spells = new ArrayList<Spell>();

	private IncantatioReloaded plugin;

	private class Spell {

		private Player player;
		private String[] args;

		public Spell(Player player, String[] args) {
			this.player = player;
			this.args = args;
		}

		public Player getPlayer() {
			return player;
		}

		public String[] getArgs() {
			return args;
		}

	}

	public Manager(IncantatioReloaded instance) {
		plugin = instance;
	}

	@Override
	public void run() {
		handleSpells();
		CreatedBlock.handle();
		Illusion.handle();
		CreateBlocksSpell.handle();
		ChangeBlocksSpell.handle();
		Mutate.handle();
		Lightning.handle();
		Disappear.handle();
		Stop.handle();
	}

	public void newSpell(Player player, String[] args) {
		Spell spell = new Spell(player, args);
		spells.add(spell);
	}

	private void handleSpells() {
		List<Spell> current = new ArrayList<Spell>();
		current.addAll(spells);
		spells.clear();

		for (Spell spell : current) {
			Player player = spell.getPlayer();
			String[] args = spell.getArgs();
			SpellHandler newspell = new SpellHandler(player, args);

			if (newspell.wasSuccessful()) {
				String message = newspell.getMessage();
				// ServerTools.verbose(message);
				if (message != "")
					for (Player recipient : EntityTools.getPlayersAroundPoint(
							player.getLocation(), 50)) {
						recipient.sendMessage(player.getDisplayName()
								+ " casts: " + message);
					}
			}
		}

	}

}
