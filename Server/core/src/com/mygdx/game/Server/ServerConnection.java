package com.mygdx.game.Server;


import com.badlogic.gdx.math.Rectangle;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.Characters.GameCharacter;
import com.mygdx.game.Characters.PlayerGameCharacter;
import com.mygdx.game.Characters.Zombie;
import com.mygdx.game.World.Headless;
import com.mygdx.game.Weapons.PistolBullet;
import com.mygdx.game.World.World;
import packets.*;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.stream.Collectors;


public class ServerConnection {

	static com.esotericsoftware.kryonet.Server server;
	static final int udpPort = 5007, tcpPort = 5008;
	private World serverWorld;
	private ServerUpdateThread serverUpdateThread;

	private float playerGameCharacterX = 280f;
	private float playerGameCharacterY = 250f;
	private int playerCount = 0;

	private static final float INCREASE_X_COORDINATE = 30f;
	private static final int SCORE_COEFFICIENT = 100;

	/**
	 * Server connection.
	 */
	public ServerConnection()  {
		try {
			server = new Server(4915200, 4915200);
			server.start();
			server.bind(tcpPort, udpPort);

			// Starts the game (create a new World instance for the game).
			this.serverWorld = new World();
			Headless.loadHeadless(serverWorld);

		} catch (IOException exception) {
			JOptionPane.showMessageDialog(null, "Can not start the Server.");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Register all packets that are sent over the network.
		server.getKryo().register(Packet.class);
		server.getKryo().register(PacketConnect.class);
		server.getKryo().register(PacketAddCharacter.class);
		server.getKryo().register(GameCharacter.class);
		server.getKryo().register(PacketUpdateCharacterInformation.class);
		server.getKryo().register(PacketCreator.class);
		server.getKryo().register(PacketBullet.class);
		server.getKryo().register(PistolBullet.class);
		server.getKryo().register(PacketSendUpdatedBullets.class);
		server.getKryo().register(ArrayList.class);
		server.getKryo().register(Rectangle.class);
		server.getKryo().register(PacketNewZombies.class);
		server.getKryo().register(PacketUpdateZombies.class);
		server.getKryo().register(HashMap.class);
		server.getKryo().register(PacketRemoveZombiesFromGame.class);
		server.getKryo().register(PacketClientDisconnect.class);
		server.getKryo().register(PacketServerIsFull.class);

		// Add listener to handle receiving objects.
		server.addListener(new Listener() {

			// Receive packets from clients.
			public void received(Connection connection, Object object){
				if (object instanceof PacketConnect && serverWorld.getClients().size() < 3) {
					PacketConnect packetConnect = (PacketConnect) object;
					playerCount += 1;
					System.out.println("Received message from the client: " + packetConnect.getPlayerName());

					// Creates new PlayerGameCharacter instance for the connection.
					PlayerGameCharacter newPlayerGameCharacter = PlayerGameCharacter
							.createPlayerGameCharacter(playerGameCharacterX, playerGameCharacterY,
									packetConnect.getPlayerName(), serverWorld, connection.getID());

					// Add new PlayerGameCharacter instance to all connections.
					addCharacterToClientsGame(connection, newPlayerGameCharacter);
					// Each PlayerGameCharacter instance has a different x coordinate.
					if (playerCount <= 3) {
						playerGameCharacterX += INCREASE_X_COORDINATE;
					} else {
						playerGameCharacterX = 280f;
						playerCount = 0;
					}

				} else if (object instanceof PacketConnect && serverWorld.getClients().size() == 3) {
					// If server is full then send a packet notifying that player cant join game.
					server.sendToTCP(connection.getID(), new PacketServerIsFull());

				} else if (object instanceof PacketUpdateCharacterInformation) {
					PacketUpdateCharacterInformation packet = (PacketUpdateCharacterInformation) object;
					// Update PlayerGameCharacter's coordinates and direction.
					// Send PlayerGameCharacter's new coordinate and direction to all connections.
					sendUpdatedGameCharacter(connection.getID(), packet.getX(), packet.getY(), packet.getDirection());

				} else if (object instanceof PacketBullet) {
					PacketBullet packetBullet = (PacketBullet) object;

					// Creates new PistolBullet instance.
					PistolBullet newPistolBullet = new PistolBullet();
					Rectangle newRectangle = new Rectangle(packetBullet.getBulletXCoordinate(),
							packetBullet.getBulletYCoordinate(), 5f, 5f);
					newPistolBullet.makePistolBullet(newRectangle, packetBullet.getBulletTextureString(),
							packetBullet.getMovingDirection(), packetBullet.getDamage());

					// Add new PistolBullet to the Server's world.
					if (!serverWorld.isUpdatingBullets()) {
						serverWorld.addBullet(newPistolBullet);
					}

				}
			}

			// Client disconnects from the Server.
			public void disconnected (Connection c) {
				PacketClientDisconnect packetClientDisconnect = PacketCreator.createPacketClientDisconnect(c.getID());
				System.out.println("Client " + c.getID() + " disconnected.");
				// Remove client from the game.
				serverWorld.removeClient(c.getID());
				// Send to other connections that client has disconnected from the game.
				server.sendToAllExceptTCP(c.getID(), packetClientDisconnect);
			}
		});

		System.out.println("Server is on!");

		// Start ServerUpdateThread.
		serverUpdateThread = new ServerUpdateThread();
		serverUpdateThread.setServerConnection(this);
		serverUpdateThread.setServerWorld(serverWorld);
		new Thread(serverUpdateThread).start();
		System.out.println("Thread is on!");
	}

	/**
	 * Method for sending new PlayerGameCharacter instance info to all connections and sending existing characters
	 * to the new connection.
	 *
	 * @param newCharacterConnection new connection (Connection)
	 * @param newPlayerGameCharacter new PlayerGameCharacter instance that was created for new connection (PlayerGameCharacter)
	 */
	public void addCharacterToClientsGame(Connection newCharacterConnection, PlayerGameCharacter newPlayerGameCharacter) {
		// Add existing PlayerGameCharacter instances to new connection.
		List<PlayerGameCharacter> clientsValues = new ArrayList<>(serverWorld.getClients().values());
		for (int i = 0; i < clientsValues.size(); i++) {
			PlayerGameCharacter character = clientsValues.get(i);
			// Create a new packet for sending PlayerGameCharacter instance info.
			PacketAddCharacter addCharacter = PacketCreator.createPacketAddCharacter(character.getName(),
					character.getPlayerGameCharacterId(), character.getBoundingBox().getX(), character.getBoundingBox().getY());
			// Send packet only to new connection.
			server.sendToTCP(newCharacterConnection.getID(), addCharacter);
		}

		// Add new PlayerGameCharacter instance to Server's world.
		serverWorld.addGameCharacter(newCharacterConnection.getID(), newPlayerGameCharacter);

		// Add new PlayerGameCharacter instance to all connections.
		// Create a packet to send new PlayerGameCharacter's info.
		PacketAddCharacter addCharacter = PacketCreator.createPacketAddCharacter(newPlayerGameCharacter.getName(),
				newCharacterConnection.getID(), newPlayerGameCharacter.getBoundingBox().getX(), newPlayerGameCharacter.getBoundingBox().getY());
		server.sendToAllTCP(addCharacter);  // Send packet to all connections.
	}

	/**
	 * Method for sending updated PlayerGameCharacter instance info to all connections.
	 *
	 * @param Id of the PlayerGameCharacter (int)
	 * @param xPos new x coordinate of the PlayerGameCharacter (float)
	 * @param yPos new y coordinate of the PlayerGameCharacter (float)
	 * @param direction of the PlayerGameCharacter (String)
	 */
	public void sendUpdatedGameCharacter(int Id, float xPos, float yPos, String direction) {
		serverWorld.movePlayerGameCharacter(Id, xPos, yPos);  // Update given PlayerGameCharacter.
		PlayerGameCharacter character = serverWorld.getGameCharacter(Id);
		System.out.println(character);
		// Send updated PlayerGameCharacter's info to all connections.
		PacketUpdateCharacterInformation packet = PacketCreator.createPacketUpdateCharacterInformation(character.getName(),
				Id, character.getBoundingBox().getX(), character.getBoundingBox().getY(), direction, character.getHealth());
		server.sendToAllUDP(packet);
	}

	/**
	 * Method for sending serverWorld's updated bullets to all connections.
	 */
	public void sendUpdatedBullets() {
		serverWorld.updateBulletsInTheWorldList();  // Update bullets.
		PacketSendUpdatedBullets packetSendUpdatedBullets = PacketCreator.createPacketSendUpdatedBullets(serverWorld.getPistolBulletsInTheWorld());
		server.sendToAllUDP(packetSendUpdatedBullets);
	}

	/**
	 * Method for sending new Zombie instances info (id and coordinates) to all connections.
	 *
	 * @param newZombies List of Zombies whose info is going to be sent (List<Zombie>)
	 */
	public void sendNewZombies(List<Zombie> newZombies) {
		// Zombie instance id (key) and coordinates (value) are sent to all connections.
		Map<Integer, List<Float>> newZombiesMap = newZombies
				.stream()
				.map(zombie -> new AbstractMap.SimpleEntry<>(zombie.getZombieId(),
						new ArrayList<>(Arrays.asList(zombie.getBoundingBox().getX(), zombie.getBoundingBox().getY()))))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		PacketNewZombies packetNewZombies = PacketCreator.createPacketNewZombies(newZombiesMap);
		server.sendToAllTCP(packetNewZombies);
	}

	/**
	 * Method for sending serverWorld's updated Zombie instances info to all connections.
	 */
	public void sendUpdatedZombies() {
		serverWorld.updateZombiesInTheWorldZombieMap();  // Update Zombie instances.
		// Zombie instance id (key) and new coordinates (value) are sent.
		Map<Integer, List<Float>> updatedZombieMap = serverWorld.getZombieMap().entrySet()
				.stream()
				.map(zombieMapEntry -> new AbstractMap.SimpleEntry<>(zombieMapEntry.getKey(),
						new ArrayList<>(Arrays.asList(zombieMapEntry.getValue().getBoundingBox().getX(),
								zombieMapEntry.getValue().getBoundingBox().getY()))))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		PacketUpdateZombies packetUpdateZombies = PacketCreator.createPacketUpdateZombies(updatedZombieMap);
		server.sendToAllUDP(packetUpdateZombies);
	}

	/**
	 * Method for sending info about Zombie instances who are going to removed from the game (serverWorld).
	 *
	 * @param zombieIdList List of Zombie ids who are going to be removed from the game (List<Integer>)
	 */
	public void sendZombiesToRemoveFromGame(List<Integer> zombieIdList) {
		PacketRemoveZombiesFromGame packetRemoveZombiesFromGame = PacketCreator.createPacketRemoveZombiesFromGame(zombieIdList);
		// Game score is updated by the number of Zombies who are going to be removed from the game.
		serverWorld.setScore(serverWorld.getScore() + (SCORE_COEFFICIENT * packetRemoveZombiesFromGame.getZombieIdList().size()));
		server.sendToAllTCP(packetRemoveZombiesFromGame);
	}

	/**
	 * Reset Server variable playerGameCharacterX.
	 */
	public void restartServer() {
		playerGameCharacterX = 280f;
	}

	public World getServerWorld() {
		return serverWorld;
	}

	public static void main(String[] args) {
		// Runs the main application.
		new ServerConnection();
	}
}
