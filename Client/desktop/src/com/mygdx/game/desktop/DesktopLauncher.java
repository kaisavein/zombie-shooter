package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.GameInfo.GameClient;


public class DesktopLauncher {

	/**
	 * Creates a game instance that starts the game.
	 */
	public static void createGame() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		GameClient gameClient = new GameClient();
		new LwjglApplication(gameClient, config);
	}

	public static void main(String[] arg) {
		createGame();
	}
}
