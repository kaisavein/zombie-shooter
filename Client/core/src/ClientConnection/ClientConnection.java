package ClientConnection;

import com.badlogic.gdx.math.Rectangle;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import com.mygdx.game.Characters.GameCharacter;
import com.mygdx.game.GameInfo.ClientWorld;
import com.mygdx.game.GameInfo.GameClient;
import com.mygdx.game.Screens.GameScreen;
import com.mygdx.game.Characters.PlayerGameCharacter;
import com.mygdx.game.Weapons.PistolBullet;
import packets.*;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class ClientConnection {

    private GameScreen gameScreen;
    private ClientWorld clientWorld;
    private GameClient gameClient;
    private Client client;
    private String playerName;

    private static final int SCORE_COEFFICIENT = 100;

    /**
     * Client connection.
     */
    public ClientConnection() {

        String ip = "193.40.255.23";
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
        client.getKryo().register(PacketServerIsFull.class);

        // Add a listener to handle receiving objects.
        client.addListener(new Listener.ThreadedListener(new Listener()) {

            // Receive packets from the Server.
            public void received(Connection connection, Object object) {
                if (object instanceof Packet && clientWorld != null) {
                    if (object instanceof PacketAddCharacter) {
                        PacketAddCharacter packetAddCharacter = (PacketAddCharacter) object;
                        // Create a new PlayerGameCharacter instance from received info.
                        PlayerGameCharacter newGameCharacter = PlayerGameCharacter.createPlayerGameCharacter(packetAddCharacter.getX(), packetAddCharacter.getY(), packetAddCharacter.getPlayerName(), packetAddCharacter.getId());
                        // Add new PlayerGameCharacter to client's game.
                        clientWorld.addGameCharacter(packetAddCharacter.getId(), newGameCharacter);
                        if (packetAddCharacter.getId() == connection.getID()) {
                            // If new PlayerGameCharacter is client's PlayerGameCharacter.
                            clientWorld.setMyPlayerGameCharacter(newGameCharacter);
                        }

                    } else if (object instanceof PacketServerIsFull) {
                        gameClient.showFull();
                    } else  if (object instanceof PacketUpdateCharacterInformation) {
                        PacketUpdateCharacterInformation packetUpdateCharacterInformation = (PacketUpdateCharacterInformation) object;
                        if (clientWorld.getWorldGameCharactersMap().containsKey(packetUpdateCharacterInformation.getId())) {
                            // Update PlayerGameCharacter's coordinates, direction and health.
                            clientWorld.movePlayerGameCharacter(packetUpdateCharacterInformation.getId(),
                                    packetUpdateCharacterInformation.getX(), packetUpdateCharacterInformation.getY(),
                                    packetUpdateCharacterInformation.getDirection(), packetUpdateCharacterInformation.getHealth());
                            // Update PlayerGameCharacter's PistolBullet's coordinates and direction.
                            clientWorld.updateBulletLocation(packetUpdateCharacterInformation.getId(),
                                    packetUpdateCharacterInformation.getX(), packetUpdateCharacterInformation.getY(),
                                    packetUpdateCharacterInformation.getDirection());
                            // If PlayerGameCharacters health is 0 then the game is over.
                            if (packetUpdateCharacterInformation.getHealth() <= 0) {
                                gameScreen.setPlayerGameCharactersHaveLives(false);
                            }
                        }

                    } else if (object instanceof PacketSendUpdatedBullets) {
                        PacketSendUpdatedBullets packetSendUpdatedBullets = (PacketSendUpdatedBullets) object;
                        // Update PistolBullets in the game.
                        clientWorld.removeAndUpdateWorldPistolBulletList(packetSendUpdatedBullets.getUpdatedBullets());

                    } else if (object instanceof PacketNewZombies) {
                        PacketNewZombies packetNewZombies = (PacketNewZombies) object;
                        // New wave
                        if (clientWorld.getZombieHashMap().isEmpty()) {
                            clientWorld.setWaveCount(clientWorld.getWaveCount() + 1);
                        }
                        // Add Zombies to the game.
                        clientWorld.addZombiesToClientWorldMap(packetNewZombies.getZombieMap());

                    } else if (object instanceof PacketUpdateZombies) {
                        PacketUpdateZombies packetUpdateZombies = (PacketUpdateZombies) object;
                        if (!packetUpdateZombies.getZombieMap().isEmpty()) {
                            // Update Zombies in the game.
                            clientWorld.updateZombiesInClientWorld(packetUpdateZombies.getZombieMap());
                        }

                    } else if (object instanceof PacketRemoveZombiesFromGame) {
                        PacketRemoveZombiesFromGame packetRemoveZombiesFromGame = (PacketRemoveZombiesFromGame) object;
                        // Game score is updated by the number of Zombies who are going to be removed from the game.
                        clientWorld.setScore(clientWorld.getScore() + (SCORE_COEFFICIENT * packetRemoveZombiesFromGame.getZombieIdList().size()));
                        // Zombies are removed from the game.
                        packetRemoveZombiesFromGame.getZombieIdList().forEach(id -> clientWorld.removeZombieFromClientWorldMap(id));

                    } else if (object instanceof PacketClientDisconnect) {
                        PacketClientDisconnect packetClientDisconnect = (PacketClientDisconnect) object;
                        System.out.println("Client " + packetClientDisconnect.getId() + " disconnected.");
                        gameScreen.setPlayerGameCharactersHaveLives(false);
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
     * This is sent when a client wants to connect to the server.
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
        PacketUpdateCharacterInformation packet = PacketCreator.createPacketUpdateCharacterInformation(playerName, client.getID(), xChange, yChange, direction, health);
        client.sendUDP(packet);
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
        PacketBullet packetBullet = PacketCreator.createPacketBullet(playerName, client.getID(), xPos, yPos, bulletTextureRegion, damage, direction);
        client.sendUDP(packetBullet);
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public void setClientWorld(ClientWorld clientWorld){
        this.clientWorld = clientWorld;
    }

    public void setGameClient(GameClient gameClient) {
        this.gameClient = gameClient;
    }

    public GameClient getGameClient() {
        return gameClient;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public static void main(String[] args) {
        new ClientConnection();  // Runs the main application.
    }
}
