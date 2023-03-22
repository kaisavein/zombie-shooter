package test.java.Server;

import com.badlogic.gdx.math.Rectangle;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import com.mygdx.game.Characters.GameCharacter;
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
import java.util.ArrayList;
import java.util.HashMap;


/**
 * TestClient class is used to test communication between Server and Client.
 *
 * TestClient class is a simplification of class ClientConnection. Class TestClient has the same methods
 * for receiving packets as class ClientConnection. Class TestClient also send packets to the server
 * and has the same methods with ClientConnection class for sending packets.

 */

public class TestClient {
    private Client client;
    private String playerName = "Mati";

    // Variables hold sent packets.
    private PacketAddCharacter receivedPacketAddCharacter;
    private PacketUpdateCharacterInformation receivedPacketUpdateCharacterInformation;
    private PacketSendUpdatedBullets receivedPacketSendUpdatedBullets;
    private PacketNewZombies receivedPacketNewZombies;
    private PacketUpdateZombies receivedPacketUpdateZombies;
    private PacketRemoveZombiesFromGame receivedPacketRemoveZombiesFromGame;
    private PacketClientDisconnect receivedPacketClientDisconnect;

    public TestClient() {
        String ip = "127.0.0.1";
        // Server 193.40.255.23
        // local  127.0.0.1
        int udpPort = 5007, tcpPort = 5008;

        client = new Client(49152, 49152);
        client.start();

        // Register all packets that are sent over the network.
        client.getKryo().register(Packet.class);
        client.getKryo().register(PacketConnect.class);
        client.getKryo().register(PacketAddCharacter.class);
        client.getKryo().register(GameCharacter.class);
        client.getKryo().register(PacketUpdateCharacterInformation.class);
        client.getKryo().register(PacketCreator.class);
        client.getKryo().register(PacketBullet.class);
        client.getKryo().register(PistolBullet.class);
        client.getKryo().register(PacketSendUpdatedBullets.class);
        client.getKryo().register(ArrayList.class);
        client.getKryo().register(Rectangle.class);
        client.getKryo().register(PacketNewZombies.class);
        client.getKryo().register(PacketUpdateZombies.class);
        client.getKryo().register(HashMap.class);
        client.getKryo().register(PacketRemoveZombiesFromGame.class);
        client.getKryo().register(PacketClientDisconnect.class);


        // Add a listener to handle receiving objects.
        client.addListener(new Listener.ThreadedListener(new Listener()) {
            // Receive packets from the server.
            public void received(Connection connection, Object object) {
                if (object instanceof Packet) {

                    if (object instanceof PacketAddCharacter) {
                        PacketAddCharacter packetAddCharacter = (PacketAddCharacter) object;
                        receivedPacketAddCharacter = packetAddCharacter;

                    } else  if (object instanceof PacketUpdateCharacterInformation) {
                        PacketUpdateCharacterInformation packetUpdateCharacterInformation = (PacketUpdateCharacterInformation) object;
                        receivedPacketUpdateCharacterInformation = packetUpdateCharacterInformation;

                    } else if (object instanceof PacketSendUpdatedBullets) {
                        PacketSendUpdatedBullets packetSendUpdatedBullets = (PacketSendUpdatedBullets) object;
                        receivedPacketSendUpdatedBullets = packetSendUpdatedBullets;

                    } else if (object instanceof PacketNewZombies) {
                        PacketNewZombies packetNewZombies = (PacketNewZombies) object;
                        receivedPacketNewZombies = packetNewZombies;

                    } else if (object instanceof PacketUpdateZombies) {
                        PacketUpdateZombies packetUpdateZombies = (PacketUpdateZombies) object;
                        receivedPacketUpdateZombies = packetUpdateZombies;

                    } else if (object instanceof PacketRemoveZombiesFromGame) {
                        PacketRemoveZombiesFromGame packetRemoveZombiesFromGame = (PacketRemoveZombiesFromGame) object;
                        receivedPacketRemoveZombiesFromGame = packetRemoveZombiesFromGame;

                    } else if (object instanceof PacketClientDisconnect) {
                        PacketClientDisconnect packetClientDisconnect = (PacketClientDisconnect) object;
                        receivedPacketClientDisconnect = packetClientDisconnect;
                    }
                }
            }
        });

        try {
            // Connected to the server - wait 5000ms before failing.
            client.connect(5000, ip, tcpPort, udpPort);
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(null, "Can not connect to the Server.");
            return;
        }
    }

    /**
     * Send PacketConnect to the server.
     *
     * Is sent when a client wants to connect to the server.
     * @param playerName of the client (String)
     */
    public void sendPacketConnect(String playerName) {
        PacketConnect packetConnect = PacketCreator.createPacketConnect(playerName);
        client.sendTCP(packetConnect);
    }

    /**
     * Send client's PlayerGameCharacter new coordinates and direction to the server.
     *
     * @param xChange of the PlayerGameCharacters x coordinate (float)
     * @param yChange of the PlayerGameCharacters y coordinate (float)
     * @param direction of the PlayerGameCharacter
     * @param health of the PlayerGameCharacter
     */
    public void sendPlayerInformation(float xChange, float yChange, String direction, int health) {
        PacketUpdateCharacterInformation packet = PacketCreator.createPacketUpdateCharacterInformation(playerName,
                client.getID(), xChange, yChange, direction, health);
        client.sendTCP(packet);
    }

    /**
     * Send client's new PistolBullet's info to the server.
     *
     * This is sent when client shoots a new bullet.
     * @param xPos of the PistolBullet (float)
     * @param yPos of the PistolBullet (float)
     * @param bulletTextureRegion PistolBullet's texture string (String)
     * @param damage the bullet can make (int)
     * @param direction of the PistolBullet (String)
     */
    public void sendPlayerBulletInfo(float xPos, float yPos, String bulletTextureRegion, int damage, String direction) {
        PacketBullet packetBullet = PacketCreator.createPacketBullet(playerName, client.getID(), xPos, yPos,
                bulletTextureRegion, damage, direction);
        client.sendUDP(packetBullet);
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public PacketAddCharacter getReceivedPacketAddCharacter() {
        return receivedPacketAddCharacter;
    }

    public PacketUpdateCharacterInformation getReceivedPacketUpdateCharacterInformation() {
        return receivedPacketUpdateCharacterInformation;
    }

    public PacketSendUpdatedBullets getReceivedPacketSendUpdatedBullets() {
        return receivedPacketSendUpdatedBullets;
    }

    public PacketNewZombies getReceivedPacketNewZombies() {
        return receivedPacketNewZombies;
    }

    public PacketUpdateZombies getReceivedPacketUpdateZombies() {
        return receivedPacketUpdateZombies;
    }

    public PacketRemoveZombiesFromGame getReceivedPacketRemoveZombiesFromGame() {
        return receivedPacketRemoveZombiesFromGame;
    }

    public PacketClientDisconnect getReceivedPacketClientDisconnect() {
        return receivedPacketClientDisconnect;
    }

    public static void main(String[] args) {
        new TestClient();  // Runs the main application.
    }
}
