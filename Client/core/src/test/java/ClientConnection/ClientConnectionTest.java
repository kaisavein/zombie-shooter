package test.java.ClientConnection;

import ClientConnection.ClientConnection;
import com.badlogic.gdx.math.Rectangle;
import com.esotericsoftware.kryonet.Connection;
import com.mygdx.game.Characters.PlayerGameCharacter;
import com.mygdx.game.Characters.Zombie;
import com.mygdx.game.GameInfo.ClientWorld;
import com.mygdx.game.Screens.GameScreen;
import com.mygdx.game.Weapons.Pistol;
import com.mygdx.game.Weapons.PistolBullet;
import com.mygdx.game.Weapons.Shotgun;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import packets.PacketBullet;
import packets.PacketUpdateCharacterInformation;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

/**
 * To run ClientConnection's test ClientConnection variable ip has to be 127.0.0.1.
 *
 * Each test has to be run separately.
 */
class ClientConnectionTest {

    /**
     * Test ClientConnection method sendPacketConnect.
     *
     * Client sends packet PacketConnect to the Server and the Server receives it.
     * @throws InterruptedException is thrown when the thread is interrupted (Exception)
     */
    @Test
    public void testClientConnectionSendPacketConnect() throws InterruptedException {
        TestServer testServer = new TestServer();
        ClientConnection clientConnection = new ClientConnection();
        ClientWorld clientWorld = new ClientWorld();
        clientConnection.setClientWorld(clientWorld);
        clientWorld.registerClient(clientConnection);

        // Client sends PacketConnect with information to the server.
        clientConnection.sendPacketConnect("Mati");

        Thread.sleep(1000);
        // Server received the packet with sent information.
        assertEquals("Mati", testServer.getReceivedPacketConnect().getPlayerName());
    }

    /**
     * Test ClientConnection method sendPlayerInformation.
     *
     * Client sends packet PacketUpdateCharacterInformation to the Server ant the Server receives it.
     * @throws InterruptedException is thrown when the thread is interrupted (Exception)
     */
    @Test
    public void testClientConnectionSendPlayerInformation() throws InterruptedException {
        TestServer testServer = new TestServer();
        ClientConnection clientConnection = new ClientConnection();
        ClientWorld clientWorld = new ClientWorld();
        clientConnection.setClientWorld(clientWorld);
        clientWorld.registerClient(clientConnection);

        // Client send PacketUpdateCharacterInformation with information to the server.
        clientConnection.sendPlayerInformation(10f, 20f, "left", 98);

        Thread.sleep(1000);
        // Server received the packet with sent information.
        PacketUpdateCharacterInformation packet = testServer.getPacketUpdateCharacterInformation();
        assertEquals(10f, packet.getX());
        assertEquals(20f, packet.getY());
        assertEquals("left", packet.getDirection());
        assertEquals(98, packet.getHealth());
    }

    /**
     * Test ClientConnection method sendPlayerBulletInfo.
     *
     * Client sends packet PacketBullet to the Server and the Server receives it.
     * @throws InterruptedException is thrown when the thread is interrupted (Exception)
     */
    @Test
    public void testClientConnectionSendPlayerBulletInfo() throws InterruptedException {
        TestServer testServer = new TestServer();
        ClientConnection clientConnection = new ClientConnection();
        ClientWorld clientWorld = new ClientWorld();
        clientConnection.setClientWorld(clientWorld);
        clientWorld.registerClient(clientConnection);

        // Client sends PacketBullet with information to the server.
        clientConnection.sendPlayerBulletInfo(45f, 48f, "Fire bullet", 1, "up");

        Thread.sleep(1000);
        PacketBullet packet = testServer.getReceivedPacketBullet();
        assertEquals(45f, packet.getBulletXCoordinate());
        assertEquals(48f, packet.getBulletYCoordinate());
        assertEquals("Fire bullet", packet.getBulletTextureString());
        assertEquals(1, packet.getDamage());
        assertEquals("up", packet.getMovingDirection());
    }

    /**
     * Test if client sends another packet then the new packet is also received.
     *
     * @throws InterruptedException is thrown when the thread is interrupted (Exception)
     */
    @Test
    public void testClientConnectionSendsTwoPackets() throws InterruptedException {
        TestServer testServer = new TestServer();
        ClientConnection clientConnection = new ClientConnection();
        ClientWorld clientWorld = new ClientWorld();
        clientConnection.setClientWorld(clientWorld);
        clientWorld.registerClient(clientConnection);

        // Client sends PacketUpdateCharacterInformation.
        clientConnection.sendPlayerInformation(25f, 45f, "right", 95);
        Thread.sleep(1000);
        PacketUpdateCharacterInformation packet1 = testServer.getPacketUpdateCharacterInformation();
        assertEquals(25f, packet1.getX());
        assertEquals(45f, packet1.getY());
        assertEquals("right", packet1.getDirection());
        assertEquals(95, packet1.getHealth());

        // Client send another PacketUpdateCharacterInformation.
        clientConnection.sendPlayerInformation(28f, 50f, "left", 90);
        Thread.sleep(1000);
        PacketUpdateCharacterInformation packet2 = testServer.getPacketUpdateCharacterInformation();
        assertEquals(28f, packet2.getX());
        assertEquals(50f, packet2.getY());
        assertEquals("left", packet2.getDirection());
        assertEquals(90, packet2.getHealth());
    }

    /**
     * Test sending packet PacketAddCharacter to the ClientConnection instance.
     *
     * @throws InterruptedException is thrown when the thread is interrupted (Exception)
     */
    @Test
    public void testClientConnectionReceivesPacketAddCharacter() throws InterruptedException {
        TestServer testServer = new TestServer();
        ClientConnection clientConnection = new ClientConnection();
        ClientWorld clientWorld = new ClientWorld();
        clientConnection.setClientWorld(clientWorld);
        clientWorld.registerClient(clientConnection);

        // Make new PlayerGameCharacter for the new connection.
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle1 = new Rectangle(72f, 65f, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle1, "Fire Bullet", "up", 1);
        Pistol pistol = new Pistol(newBullet);
        Rectangle playerGameCharacterRectangle = new Rectangle(72f, 65f, 10f, 10f);
        PlayerGameCharacter playerGameCharacter = new PlayerGameCharacter("Mati", 2f,
                playerGameCharacterRectangle, 72f, 65f, 10f, 10f, pistol);

        Connection connection = Mockito.mock(Connection.class);
        when(connection.getID()).thenReturn(1);

        // Send new PlayerGameCharacter's info to client.
        testServer.addCharacterToClientsGame(connection, playerGameCharacter);

        Thread.sleep(1000);
        // Client has received the packet and has made a new PlayerGameCharacter according to the info.
        assertEquals("Mati", clientWorld.getMyPlayerGameCharacter().getName());
        assertEquals(1, clientWorld.getMyPlayerGameCharacter().getPlayerGameCharacterId());
        assertEquals(72f, clientWorld.getMyPlayerGameCharacter().getBoundingBox().getX());
        assertEquals(65f, clientWorld.getMyPlayerGameCharacter().getBoundingBox().getY());

        // Client has also received info about other PlayerGameCharacters.
        assertEquals(2, clientWorld.getWorldGameCharactersMap().size());
        assertEquals("Mati", clientWorld.getGameCharacter(1).getName());
        assertEquals("Kati", clientWorld.getGameCharacter(2).getName());
        assertEquals(2, clientWorld.getGameCharacter(2).getPlayerGameCharacterId());
        assertEquals(75f, clientWorld.getGameCharacter(2).getBoundingBox().getX());
        assertEquals(75f, clientWorld.getGameCharacter(2).getBoundingBox().getY());
    }

    /**
     * Test sending packet PacketUpdateCharacterInformation to the ClientConnection instance.
     *
     * @throws InterruptedException is thrown when the thread is interrupted (Exception)
     */
    @Test
    public void testClientConnectionReceivesPacketUpdateCharacterInformation() throws InterruptedException {
        TestServer testServer = new TestServer();
        ClientConnection clientConnection = new ClientConnection();
        ClientWorld clientWorld = new ClientWorld();
        clientConnection.setClientWorld(clientWorld);
        clientWorld.registerClient(clientConnection);

        // Make PlayerGameCharacter who is going to be updated.
        PistolBullet newBullet1 = new PistolBullet();
        Rectangle pistolBulletRectangle1 = new Rectangle(72f, 65f, 5f, 5f);
        newBullet1.makePistolBullet(pistolBulletRectangle1, "Fire Bullet", "up", 1);
        Pistol pistol1 = new Pistol(newBullet1);
        Rectangle playerGameCharacterRectangle1 = new Rectangle(72f, 65f, 10f, 10f);
        PlayerGameCharacter playerGameCharacter = new PlayerGameCharacter("Mati", 2f,
                playerGameCharacterRectangle1, 72f, 65f, 10f, 10f, pistol1);
        // Make Shotgun.
        PistolBullet shotgunLeft = new PistolBullet();
        Rectangle shotgunLeftRectangle = new Rectangle(70f, 75f, 5f, 5f);
        shotgunLeft.makePistolBullet(shotgunLeftRectangle, "Fire Bullet", "up-left", 1);
        PistolBullet shotgunStraight = new PistolBullet();
        Rectangle shotgunStraightRectangle = new Rectangle(75f, 75f, 5f, 5f);
        shotgunStraight.makePistolBullet(shotgunStraightRectangle, "Fire Bullet", "up", 1);
        PistolBullet shotgunRight = new PistolBullet();
        Rectangle shotgunRightRectangle = new Rectangle( 80f, 75f, 5f, 5f);
        shotgunRight.makePistolBullet(shotgunRightRectangle, "Fire Bullet", "up-right", 1);
        Shotgun shotgun = new Shotgun(shotgunLeft, shotgunStraight, shotgunRight);
        // Set Shotgun to PlayerGameCharacter.
        playerGameCharacter.setPlayerCharacterCurrentShotgun(shotgun);
        // Add PlayerGameCharacter to client's game.
        clientWorld.addGameCharacter(1, playerGameCharacter);

        // Server sends info.
        testServer.sendUpdatedGameCharacter(1, 20f, 36f, "down");

        Thread.sleep(1000);
        // Client's PlayerGameCharacter is updated.
        assertEquals(20f, clientWorld.getGameCharacter(1).getBoundingBox().getX());
        assertEquals(36f, clientWorld.getGameCharacter(1).getBoundingBox().getY());
        assertEquals("down", clientWorld.getGameCharacter(1).getPlayerDirection());
    }

    /**
     * Test sending packet PacketSendUpdatedBullets to the ClientConnection instance.
     *
     * @throws InterruptedException is thrown when the thread is interrupted (Exception)
     */
    @Test
    public void testClientConnectionReceivesPacketSendUpdatedBullets() throws InterruptedException {
        TestServer testServer = new TestServer();
        ClientConnection clientConnection = new ClientConnection();
        ClientWorld clientWorld = new ClientWorld();
        clientConnection.setClientWorld(clientWorld);
        clientWorld.registerClient(clientConnection);

        GameScreen gameScreen = Mockito.mock(GameScreen.class);
        when(gameScreen.isRenderingBullets()).thenReturn(false);
        clientConnection.setGameScreen(gameScreen);

        assertEquals(0, clientWorld.getPistolBullets().size());

        // Server sends updated bullets.
        testServer.sendUpdatedBullets();

        Thread.sleep(1000);
        // Client's game has updated bullets.
        assertEquals(2, clientWorld.getPistolBullets().size());
        assertEquals(75f, clientWorld.getPistolBullets().get(0).getBoundingBox().getX());
        assertEquals(70f, clientWorld.getPistolBullets().get(0).getBoundingBox().getY());
        assertEquals(70f, clientWorld.getPistolBullets().get(1).getBoundingBox().getX());
        assertEquals(50f, clientWorld.getPistolBullets().get(1).getBoundingBox().getY());
    }

    /**
     * Test sending packet PacketNewZombies to the ClientConnection instance.
     *
     * @throws InterruptedException is thrown when the thread is interrupted (Exception)
     */
    @Test
    public void testClientConnectionReceivesPacketNewZombies() throws InterruptedException {
        TestServer testServer = new TestServer();
        ClientConnection clientConnection = new ClientConnection();
        ClientWorld clientWorld = new ClientWorld();
        clientConnection.setClientWorld(clientWorld);
        clientWorld.registerClient(clientConnection);

        // Make Zombies whose info is going to be sent to the client.
        Rectangle boundingBox1 = new Rectangle(70f, 70f, 10f, 10f);
        Zombie zombie1 = new Zombie(1f, boundingBox1, 70f, 70f, 10f, 10f);
        zombie1.setZombieId(1);
        Rectangle boundingBox2 = new Rectangle(50f, 20f, 10f, 10f);
        Zombie zombie2 = new Zombie(1f, boundingBox2, 70f, 70f, 10f, 10f);
        zombie2.setZombieId(2);
        List<Zombie> zombies = new ArrayList<>();
        zombies.add(zombie1);
        zombies.add(zombie2);

        testServer.sendNewZombies(zombies);

        Thread.sleep(1000);
        // Client game has new zombies.
        assertEquals(2, clientWorld.getZombieHashMap().size());
        // zombie1
        assertEquals(70f, clientWorld.getZombieHashMap().get(1).getBoundingBox().getX());
        assertEquals(70f, clientWorld.getZombieHashMap().get(1).getBoundingBox().getY());
        // zombie2
        assertEquals(50f, clientWorld.getZombieHashMap().get(2).getBoundingBox().getX());
        assertEquals(20f, clientWorld.getZombieHashMap().get(2).getBoundingBox().getY());
    }

    /**
     * Test sending packet PacketUpdateZombies to the ClientConnection instance.
     *
     * @throws InterruptedException is thrown when the thread is interrupted (Exception)
     */
    @Test
    public void testClientConnectionReceivePacketUpdateZombies() throws InterruptedException {
        TestServer testServer = new TestServer();
        ClientConnection clientConnection = new ClientConnection();
        ClientWorld clientWorld = new ClientWorld();
        clientConnection.setClientWorld(clientWorld);
        clientWorld.registerClient(clientConnection);

        // Make Zombies whose info is going to be updated.
        Map<Integer, List<Float>> clientWorldZombies = new HashMap<>();
        List<Float> zombieCoordinates1 = new LinkedList<>();
        zombieCoordinates1.add(40f);
        zombieCoordinates1.add(60f);
        clientWorldZombies.put(1, zombieCoordinates1);
        List<Float> zombieCoordinates2 = new LinkedList<>();
        zombieCoordinates2.add(30f);
        zombieCoordinates2.add(10f);
        clientWorldZombies.put(2, zombieCoordinates2);

        clientWorld.addZombiesToClientWorldMap(clientWorldZombies);
        // Zombies before updating.
        assertEquals(40f, clientWorld.getZombieHashMap().get(1).getBoundingBox().getX());
        assertEquals(60f, clientWorld.getZombieHashMap().get(1).getBoundingBox().getY());
        assertEquals(30f, clientWorld.getZombieHashMap().get(2).getBoundingBox().getX());
        assertEquals(10f, clientWorld.getZombieHashMap().get(2).getBoundingBox().getY());

        testServer.sendUpdatedZombies();

        // Zombies after updating.
        Thread.sleep(1000);
        assertEquals(75f, clientWorld.getZombieHashMap().get(1).getBoundingBox().getX());
        assertEquals(72f, clientWorld.getZombieHashMap().get(1).getBoundingBox().getY());
        assertEquals(58f, clientWorld.getZombieHashMap().get(2).getBoundingBox().getX());
        assertEquals(21f, clientWorld.getZombieHashMap().get(2).getBoundingBox().getY());
    }

    /**
     * Test sending packet PacketRemoveZombiesFromGame to the ClientConnection instance.
     *
     * @throws InterruptedException is thrown when the thread is interrupted (Exception)
     */
    @Test
    public void testClientConnectionReceivePacketRemoveZombiesFromGame() throws InterruptedException {
        TestServer testServer = new TestServer();
        ClientConnection clientConnection = new ClientConnection();
        ClientWorld clientWorld = new ClientWorld();
        clientConnection.setClientWorld(clientWorld);
        clientWorld.registerClient(clientConnection);

        // Make Zombies to the client's game.
        Map<Integer, List<Float>> clientWorldZombies = new HashMap<>();
        List<Float> zombieCoordinates1 = new LinkedList<>();
        zombieCoordinates1.add(40f);
        zombieCoordinates1.add(60f);
        clientWorldZombies.put(1, zombieCoordinates1);
        List<Float> zombieCoordinates2 = new LinkedList<>();
        zombieCoordinates2.add(30f);
        zombieCoordinates2.add(10f);
        clientWorldZombies.put(2, zombieCoordinates2);

        // Add Zombies to the game.
        clientWorld.addZombiesToClientWorldMap(clientWorldZombies);

        // Make a list of Zombie ids who are going to be removed from the game.
        List<Integer> removeZombiesIdList = new ArrayList<>();
        removeZombiesIdList.add(1);

        testServer.sendZombiesToRemoveFromGame(removeZombiesIdList);

        Thread.sleep(1000);
        // Zombie with id 1 has been removed from the client's game.
        assertEquals(1, clientWorld.getZombieHashMap().size());
        assertTrue(clientWorld.getZombieHashMap().containsKey(2));
        assertFalse(clientWorld.getZombieHashMap().containsKey(1));
        assertEquals(2, clientWorld.getZombieHashMap().get(2).getZombieId());
    }
}
