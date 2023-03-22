package com.mygdx.game.GameInfo;

import ClientConnection.ClientConnection;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.mygdx.game.Screens.GameOverScreen;
import com.mygdx.game.Screens.GameScreen;
import com.mygdx.game.Screens.MenuScreen;

import java.io.IOException;


public class GameClient extends Game {

	private GameScreen gameScreen;
	private ClientConnection clientConnection;
	private ClientWorld clientWorld;
	private String playerName;
	private MenuScreen menuScreen;

	/**
	 * Method creates a new Client who connects to the Server with its ClientWorld and GameScreen.
	 */
	public void createClient(ClientWorld clientWorld, GameScreen gameScreen) throws IOException {
		clientConnection = new ClientConnection();
		clientConnection.setGameScreen(gameScreen);
		clientConnection.setClientWorld(clientWorld);
		clientConnection.setPlayerName(playerName);
		clientConnection.setGameClient(this);
		clientConnection.sendPacketConnect(playerName);
		gameScreen.registerClientConnection(clientConnection);
		clientWorld.registerClient(clientConnection);
	}

	public void setPlayerName(String name) {
		this.playerName = name;
	}

	/**
	 * Sets the screen to game over screen.
	 */
	public void setScreenToGameOver() {
		GameOverScreen gameOverScreen = new GameOverScreen(this, clientWorld);
		setScreen(gameOverScreen);
	}

	/**
	 * Creates the menu screen.
	 */
	@Override
	public void create() {
		this.menuScreen = new MenuScreen(this);
		setScreen(menuScreen);
	}

	public void showFull() {
		this.menuScreen.showFull(menuScreen.getStage());
	}

	/**
	 * Starts a game and tries to create a new client.
	 */
	public void startGame() {
		clientWorld = new ClientWorld();
		gameScreen = new GameScreen(clientWorld);
		setScreen(gameScreen);
		try {
			createClient(clientWorld, gameScreen);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Gdx.input.setInputProcessor(gameScreen);
	}

	/**
	 * Disposes the screen.
	 */
	@Override
	public void dispose() {
		gameScreen.dispose();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
}

