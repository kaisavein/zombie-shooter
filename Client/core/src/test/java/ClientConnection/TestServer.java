package test.java.ClientConnection;

import com.badlogic.gdx.math.Rectangle;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.Characters.GameCharacter;
import com.mygdx.game.Characters.PlayerGameCharacter;
import com.mygdx.game.Characters.Zombie;
import com.mygdx.game.Weapons.Pistol;
import com.mygdx.game.Weapons.PistolBullet;
import packets.Packet;
import packets.PacketCreator;
import packets.PacketConnect;
import packets.PacketBullet;
import packets.PacketClientDisconnect;
import packets.PacketUpdateCharacterInformation;
import packets.PacketNewZombies;
import packets.PacketUpdateZombies;
import packets.PacketRemoveZombiesFromGame;
import packets.PacketSendUpdatedBullets;
import packets.PacketAddCharacter;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.AbstractMap;
import java.util.stream.Collectors;

/**
 * TestServer class is used to test communication between Server and Client.
 *
 * TestServer class is a simplification of class ServerConnection. Class TestServer has the same methods
 * for receiving packets as class ServerConnection. Class TestServer also send packets to the client
 * and has similar method with ServerConnection class for sending packets.
 */
public class TestServer {

    static com.esotericsoftware.kryonet.Server server;
    static final int udpPort = 5007, tcpPort = 5008;

    // Variables hold sent packets.
    private PacketConnect receivedPacketConnect;
    private PacketUpdateCharacterInformation packetUpdateCharacterInformation;
    private PacketBullet receivedPacketBullet;

    public TestServer()  {
        try {
            server = new Server(49152, 49152);
            server.start();
            server.bind(tcpPort, udpPort);

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

            // Add listener to handle receiving objects.
            server.addListener(new Listener() {

                // Receive packets from clients.
                public void received(Connection connection, Object object){
                    if (object instanceof Packet) {

                        if (object instanceof PacketConnect) {
                            PacketConnect packetConnect = (PacketConnect) object;
                            receivedPacketConnect = packetConnect;

                        } else if (object instanceof PacketUpdateCharacterInformation) {
                            PacketUpdateCharacterInformation packet = (PacketUpdateCharacterInformation) object;
                            packetUpdateCharacterInformation = packet;

                        } else if (object instanceof PacketBullet) {
                            PacketBullet packetBullet = (PacketBullet) object;
                            receivedPacketBullet = packetBullet;
                        }
                    }
                }

                // Client disconnects from the Server.
                public void disconnected (Connection c) {
                    PacketClientDisconnect packetClientDisconnect = PacketCreator.createPacketClientDisconnect(c.getID());
                }
            });
    }

    /**
     * Method is a simplification of ServerConnection method addCharacterToClientsGame.
     *
     * @param newCharacterConnection new connection (Connection)
     * @param newPlayerGameCharacter new PlayerGameCharacter instance that was created for new connection (PlayerGameCharacter)
     */
    public void addCharacterToClientsGame(Connection newCharacterConnection, PlayerGameCharacter newPlayerGameCharacter) {
        // Make existing PlayerGameCharacter who is sent to the client.
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle = new Rectangle(75f, 75f, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle, "Fire Bullet", "up", 1);
        Pistol pistol2 = new Pistol(newBullet);
        Rectangle playerGameCharacterRectangle2 = new Rectangle(75f, 75f, 10f, 10f);
        PlayerGameCharacter playerGameCharacter2 = new PlayerGameCharacter("Kati", 2f,
                playerGameCharacterRectangle2, 75f, 75f, 10f, 10f, pistol2);
        playerGameCharacter2.setPlayerGameCharacterId(2);

        List<PlayerGameCharacter> clientsValues = new ArrayList<>();
        clientsValues.add(playerGameCharacter2);

        // Add existing PlayerGameCharacter instances to new connection.
        for (int i = 0; i < clientsValues.size(); i++) {
            PlayerGameCharacter character = clientsValues.get(i);
            // Create a new packet for sending PlayerGameCharacter instance info.
            PacketAddCharacter addCharacter = PacketCreator.createPacketAddCharacter(character.getName(),
                    character.getPlayerGameCharacterId(), character.getBoundingBox().getX(), character.getBoundingBox().getY());
            // Send packet only to new connection.
            server.sendToTCP(newCharacterConnection.getID(), addCharacter);
        }

        // Add new PlayerGameCharacter instance to all connections.
        // Create a packet to send new PlayerGameCharacter's info.
        PacketAddCharacter addCharacter = PacketCreator.createPacketAddCharacter(newPlayerGameCharacter.getName(),
                newCharacterConnection.getID(), newPlayerGameCharacter.getBoundingBox().getX(), newPlayerGameCharacter.getBoundingBox().getY());
        server.sendToAllTCP(addCharacter);  // Send packet to all connections.
    }

    /**
     * Method is a simplification of ServerConnections method sendUpdatedGameCharacter.
     *
     * @param Id of the PlayerGameCharacter (int)
     * @param xPos new x coordinate of the PlayerGameCharacter (float)
     * @param yPos new y coordinate of the PlayerGameCharacter (float)
     * @param direction of the PlayerGameCharacter (String)
     */
    public void sendUpdatedGameCharacter(int Id, float xPos, float yPos, String direction) {
        // Make updated PlayerGameCharacter.
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle = new Rectangle(20f, 36f, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle, "Fire Bullet", "up", 1);
        Pistol pistol = new Pistol(newBullet);
        Rectangle playerGameCharacterRectangle = new Rectangle(20f, 36f, 10f, 10f);
        PlayerGameCharacter character = new PlayerGameCharacter("Mati", 2f,
                playerGameCharacterRectangle, 20f, 36f, 10f, 10f, pistol);

        // Send updated PlayerGameCharacter's info to all connections.
        PacketUpdateCharacterInformation packet = PacketCreator.createPacketUpdateCharacterInformation(character.getName(),
                Id, xPos, yPos, direction, character.getHealth());
        server.sendToAllTCP(packet);
    }

    /**
     * Method is a simplification of ServerConnections method sendUpdatedBullets.
     */
    public void sendUpdatedBullets() {
        // Make updated bullets that are going to be sent to the client.
        PistolBullet newBullet1 = new PistolBullet();
        Rectangle pistolBulletRectangle1 = new Rectangle(75f, 70f, 5f, 5f);
        newBullet1.makePistolBullet(pistolBulletRectangle1, "Fire Bullet", "up", 1);
        PistolBullet newBullet2 = new PistolBullet();
        Rectangle pistolBulletRectangle2 = new Rectangle(70f, 50f, 5f, 5f);
        newBullet2.makePistolBullet(pistolBulletRectangle2, "Fire Bullet", "left", 1);
        List<PistolBullet> pistolBullets = new ArrayList<>();
        pistolBullets.add(newBullet1);
        pistolBullets.add(newBullet2);

        PacketSendUpdatedBullets packetSendUpdatedBullets = PacketCreator.createPacketSendUpdatedBullets(pistolBullets);
        server.sendToAllTCP(packetSendUpdatedBullets);
    }

    /**
     * Method is a simplification of ServerConnections method sendNewZombies.
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
     * Method is a simplification of ServerConnection method sendUpdatedZombies.
     */
    public void sendUpdatedZombies() {
        // Make updated Zombies whose info is going to be sent to the client.
        Rectangle boundingBox1 = new Rectangle(75f, 72f, 10f, 10f);
        Zombie zombie1 = new Zombie(1f, boundingBox1, 75f, 72f, 10f, 10f);
        Rectangle boundingBox2 = new Rectangle(58f, 21f, 10f, 10f);
        Zombie zombie2 = new Zombie(1f, boundingBox2, 75f, 72f, 10f, 10f);
        Map<Integer, Zombie> zombieMap = new HashMap<>();
        zombieMap.put(1, zombie1);
        zombieMap.put(2, zombie2);

        // Zombie instance id (key) and new coordinates (value) are sent.
        Map<Integer, List<Float>> updatedZombieMap = zombieMap.entrySet()
                .stream()
                .map(zombieMapEntry -> new AbstractMap.SimpleEntry<>(zombieMapEntry.getKey(),
                        new ArrayList<>(Arrays.asList(zombieMapEntry.getValue().getBoundingBox().getX(),
                                zombieMapEntry.getValue().getBoundingBox().getY()))))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        PacketUpdateZombies packetUpdateZombies = PacketCreator.createPacketUpdateZombies(updatedZombieMap);
        server.sendToAllTCP(packetUpdateZombies);
    }

    /**
     * Method is a simplification of ServerConnection method sendZombiesToRemoveFromGame.
     *
     * @param zombieIdList List of Zombie ids who are going to be removed from the game (List<Integer>)
     */
    public void sendZombiesToRemoveFromGame(List<Integer> zombieIdList) {
        PacketRemoveZombiesFromGame packetRemoveZombiesFromGame = PacketCreator.createPacketRemoveZombiesFromGame(zombieIdList);
        server.sendToAllTCP(packetRemoveZombiesFromGame);
    }

    public PacketConnect getReceivedPacketConnect() {
        return receivedPacketConnect;
    }

    public PacketUpdateCharacterInformation getPacketUpdateCharacterInformation() {
        return packetUpdateCharacterInformation;
    }

    public PacketBullet getReceivedPacketBullet() {
        return receivedPacketBullet;
    }

    public static void main(String[] args) throws Exception {
        // Runs the main application.
        new TestServer();
    }
}
