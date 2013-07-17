package main;

import java.io.File;

import tools.IPlayer;


public class PlayerSaver implements Runnable {

	private File folder;
	private String filename;

	public PlayerSaver(File folder, String filename) {
		this.folder = folder;
		this.filename = filename;
	}

	@Override
	public void run() {
		IPlayer.saveAll(folder, filename);
	}

}
