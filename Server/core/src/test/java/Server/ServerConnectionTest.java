package test.java.Server;

import com.badlogic.gdx.math.Rectangle;
import com.esotericsoftware.kryonet.Connection;
import com.mygdx.game.Characters.PlayerGameCharacter;
import com.mygdx.game.Characters.Zombie;
import com.mygdx.game.Server.ServerConnection;
import com.mygdx.game.Weapons.Pistol;
import com.mygdx.game.Weapons.PistolBullet;
import com.mygdx.game.Weapons.Shotgun;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * To run ServerConnection's test World class method getMap filename has to be Server/core/src/com/mygdx/game/World/DesertMap.tmx.
 */
public class ServerConnectionTest {

    /**
     * Test ServerConnections method addCharacterToClientsGame.
     *
     * @throws InterruptedException is thrown when the thread is interrupted (Exception)
     */
    @Test
    public void testServerConnectionAddCharacterToClientGame() throws InterruptedException {
        ServerConnection serverConnection = new ServerConnection();
        TestClient testClient = new TestClient();

        // Make new PlayerGameCharacter for the new connection.
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle1 = new Rectangle(72f, 65f, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle1, "Fire Bullet", "up", 1);
        Pistol pistol = new Pistol(newBullet);
        Rectangle playerGameCharacterRectangle = new Rectangle(72f, 65f, 10f, 10f);
        PlayerGameCharacter playerGameCharacter = new PlayerGameCharacter("Mati", 2f,
                playerGameCharacterRectangle, 72f, 65f, 10f, 10f, pistol, serverConnection.getServerWorld());

        Connection connection = Mockito.mock(Connection.class);
        when(connection.getID()).thenReturn(1);

        // Send new PlayerGameCharacter's info to client.
        serverConnection.addCharacterToClientsGame(connection, playerGameCharacter);

        Thread.sleep(1000);
        // Client has received the packet and has made a new PlayerGameCharacter according to the info.
        assertEquals("Mati", testClient.getReceivedPacketAddCharacter().getPlayerName());
        assertEquals(1, testClient.getReceivedPacketAddCharacter().getId());
        assertEquals(72f, testClient.getReceivedPacketAddCharacter().getX());
        assertEquals(65f, testClient.getReceivedPacketAddCharacter().getY());
    }

    /**
     * Test ServerConnection method sendUpdatedGameCharacter.
     *
     * @throws InterruptedException is thrown when the thread is interrupted (Exception)
     */
    @Test
    public void testServerConnectionSendUpdatedGameCharacter() throws InterruptedException {
        ServerConnection serverConnection = new ServerConnection();
        TestClient testClient = new TestClient();

        // Make new PlayerGameCharacter for the world. Created PlayerGameCharacter is updated in the server.
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle1 = new Rectangle(72f, 65f, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle1, "Fire Bullet", "up", 1);
        Pistol pistol = new Pistol(newBullet);
        Rectangle playerGameCharacterRectangle = new Rectangle(72f, 65f, 10f, 10f);
        PlayerGameCharacter playerGameCharacter = new PlayerGameCharacter("Mati", 2f,
                playerGameCharacterRectangle, 72f, 65f, 10f, 10f, pistol, serverConnection.getServerWorld());
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

        // Add PlayerGameCharacter to the server's world.
        serverConnection.getServerWorld().addGameCharacter(1, playerGameCharacter);

        serverConnection.sendUpdatedGameCharacter(1, 45f, 65f, "up");

        Thread.sleep(1000);
        assertEquals(1, testClient.getReceivedPacketUpdateCharacterInformation().getId());
        // Sent x coordinate is 117f (72f + 45f) and y coordinate is 130f (65f + 65f).
        assertEquals(117f, testClient.getReceivedPacketUpdateCharacterInformation().getX());
        assertEquals(130f, testClient.getReceivedPacketUpdateCharacterInformation().getY());
        assertEquals("up", testClient.getReceivedPacketUpdateCharacterInformation().getDirection());
    }

    /**
     * Test ServerConnection method sendUpdatedBullets.
     *
     * @throws InterruptedException is thrown when the thread is interrupted (Exception)
     */
    @Test
    public void testServerConnectionSendUpdatedBullets() throws InterruptedException {
        ServerConnection serverConnection = new ServerConnection();
        TestClient testClient = new TestClient();

        // Make new PistolBullet.
        PistolBullet newBullet1 = new PistolBullet();
        Rectangle pistolBulletRectangle1 = new Rectangle(72f, 65f, 5f, 5f);
        newBullet1.makePistolBullet(pistolBulletRectangle1, "Fire Bullet", "up", 1);

        // Add PistolBullet to the server's world.
        serverConnection.getServerWorld().addBullet(newBullet1);

        // Send updated bullets to the client.
        serverConnection.sendUpdatedBullets();

        Thread.sleep(1000);
        assertEquals(1, testClient.getReceivedPacketSendUpdatedBullets().getUpdatedBullets().size());
        // Sent PistolBullet's coordinates have been updated. PistolBullet's x should be 72f + 0f = 72f and
        // y should be 65f + 2f = 67f.
        assertEquals(72f, testClient.getReceivedPacketSendUpdatedBullets().getUpdatedBullets().get(0).getBoundingBox().getX());
        assertEquals(67f, testClient.getReceivedPacketSendUpdatedBullets().getUpdatedBullets().get(0).getBoundingBox().getY());

        // Make new PistolBullets for the server's world.
        PistolBullet shotgunLeft = new PistolBullet();
        Rectangle shotgunLeftRectangle = new Rectangle(70f, 75f, 5f, 5f);
        shotgunLeft.makePistolBullet(shotgunLeftRectangle, "Fire Bullet", "up-left", 1);
        PistolBullet shotgunStraight = new PistolBullet();
        Rectangle shotgunStraightRectangle = new Rectangle(75f, 75f, 5f, 5f);
        shotgunStraight.makePistolBullet(shotgunStraightRectangle, "Fire Bullet", "up", 1);
        PistolBullet shotgunRight = new PistolBullet();
        Rectangle shotgunRightRectangle = new Rectangle( 80f, 75f, 5f, 5f);
        shotgunRight.makePistolBullet(shotgunRightRectangle, "Fire Bullet", "up-right", 1);

        // Add bullets to the server's world.
        serverConnection.getServerWorld().addBullet(shotgunLeft);
        serverConnection.getServerWorld().addBullet(shotgunStraight);
        serverConnection.getServerWorld().addBullet(shotgunRight);

        // Send world's PistolBullets to the client.
        serverConnection.sendUpdatedBullets();

        Thread.sleep(1000);
        // Client has received 4 PistolBullets.
        assertEquals(4, testClient.getReceivedPacketSendUpdatedBullets().getUpdatedBullets().size());
    }

    /**
     * Test ServerConnection method sendNewZombies.
     *
     * @throws InterruptedException is thrown when the thread is interrupted (Exception)
     */
    @Test
    public void testServerConnectionSendNewZombies() throws InterruptedException {
        ServerConnection serverConnection = new ServerConnection();
        TestClient testClient = new TestClient();

        // Make Zombies whose info is going to be sent to the client.
        Rectangle boundingBox1 = new Rectangle(70f, 70f, 10f, 10f);
        Zombie zombie1 = new Zombie(1f, boundingBox1, 70f, 70f, 10f, 10f,
                serverConnection.getServerWorld());
        Rectangle boundingBox2 = new Rectangle(50f, 20f, 10f, 10f);
        Zombie zombie2 = new Zombie(1f, boundingBox2, 70f, 70f, 10f, 10f,
                serverConnection.getServerWorld());
        List<Zombie> zombies = new ArrayList<>();
        zombies.add(zombie1);
        zombies.add(zombie2);

        // Server send new Zombies to the client.
        serverConnection.sendNewZombies(zombies);

        Thread.sleep(1000);
        assertEquals(2, testClient.getReceivedPacketNewZombies().getZombieMap().size());
        assertTrue(testClient.getReceivedPacketNewZombies().getZombieMap().containsKey(1));
        assertTrue(testClient.getReceivedPacketNewZombies().getZombieMap().containsKey(2));
        // Zombie1 coordinates have been sent.
        assertEquals(70f, testClient.getReceivedPacketNewZombies().getZombieMap().get(1).get(0));
        assertEquals(70f, testClient.getReceivedPacketNewZombies().getZombieMap().get(1).get(1));
        // Zombie2 coordinates have been sent.
        assertEquals(50f, testClient.getReceivedPacketNewZombies().getZombieMap().get(2).get(0));
        assertEquals(20f, testClient.getReceivedPacketNewZombies().getZombieMap().get(2).get(1));
    }

    /**
     * Test ServerConnection method sendUpdatedZombies.
     *
     * @throws InterruptedException is thrown when the thread is interrupted (Exception)
     */
    @Test
    public void testServerConnectionSendUpdatedZombies() throws InterruptedException {
        ServerConnection serverConnection = new ServerConnection();
        TestClient testClient = new TestClient();

        // Make new PlayerGameCharacter for the world.
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle1 = new Rectangle(135f, 135f, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle1, "Fire Bullet", "up", 1);
        Pistol pistol = new Pistol(newBullet);
        Rectangle playerGameCharacterRectangle = new Rectangle(135f, 135f, 10f, 10f);
        PlayerGameCharacter playerGameCharacter = new PlayerGameCharacter("Mati", 2f,
                playerGameCharacterRectangle, 135f, 135f, 10f, 10f, pistol, serverConnection.getServerWorld());

        // Add PlayerGameCharacter to the server's world.
        serverConnection.getServerWorld().addGameCharacter(1, playerGameCharacter);

        // Make Zombies whose info is going to be sent to the client.
        Rectangle boundingBox1 = new Rectangle(135f, 100f, 10f, 10f);
        Zombie zombie1 = new Zombie(1f, boundingBox1, 70f, 70f, 10f, 10f, serverConnection.getServerWorld());
        Rectangle boundingBox2 = new Rectangle(135f, 160f, 10f, 10f);
        Zombie zombie2 = new Zombie(1f, boundingBox2, 70f, 70f, 10f, 10f, serverConnection.getServerWorld());
        List<Zombie> zombies = new ArrayList<>();
        zombies.add(zombie1);
        zombies.add(zombie2);

        // Add Zombies to server's world.
        serverConnection.getServerWorld().addZombieToServerWorldMap(1, zombie1);
        serverConnection.getServerWorld().addZombieToServerWorldMap(2, zombie2);

        serverConnection.sendUpdatedZombies();

        Thread.sleep(1000);
        assertTrue(testClient.getReceivedPacketUpdateZombies().getZombieMap().containsKey(1));
        assertTrue(testClient.getReceivedPacketUpdateZombies().getZombieMap().containsKey(2));
        // Zombie coordinates have been updated int the server and have been sent to the client.
        assertEquals(135f, testClient.getReceivedPacketUpdateZombies().getZombieMap().get(1).get(0));
        assertEquals(100.35f, testClient.getReceivedPacketUpdateZombies().getZombieMap().get(1).get(1));
        assertEquals(135f, testClient.getReceivedPacketUpdateZombies().getZombieMap().get(2).get(0));
        assertEquals(159.65f, testClient.getReceivedPacketUpdateZombies().getZombieMap().get(2).get(1));
    }

    /**
     * Test ServerConnection method sendZombiesToBeRemoved.
     *
     * @throws InterruptedException is thrown when the thread is interrupted (Exception)
     */
    @Test
    public void testServerConnectionSendZombiesToBeRemoved() throws InterruptedException {
        ServerConnection serverConnection = new ServerConnection();
        TestClient testClient = new TestClient();

        List<Integer> zombieIds = new ArrayList<>();
        zombieIds.add(1);
        zombieIds.add(4);

        serverConnection.sendZombiesToRemoveFromGame(zombieIds);

        Thread.sleep(1000);
        assertEquals(2, testClient.getReceivedPacketRemoveZombiesFromGame().getZombieIdList().size());
        assertTrue(testClient.getReceivedPacketRemoveZombiesFromGame().getZombieIdList().contains(1));
        assertTrue(testClient.getReceivedPacketRemoveZombiesFromGame().getZombieIdList().contains(4));
    }

    /**
     * Test sending PacketConnect to ServerConnection instance.
     *
     * @throws InterruptedException is thrown when the thread is interrupted (Exception)
     */
    @Test
    public void testServerConnectionReceivesPacketConnect() throws InterruptedException {
        ServerConnection serverConnection = new ServerConnection();
        TestClient testClient = new TestClient();

        testClient.sendPacketConnect("Kati");

        Thread.sleep(1000);
        // Connection's PlayerGameCharacter has been made and added to server's world.
        assertTrue(serverConnection.getServerWorld().getClients().containsKey(1));
        assertEquals("Kati", serverConnection.getServerWorld().getGameCharacter(1).getName());
        assertEquals(280f, serverConnection.getServerWorld().getGameCharacter(1).getBoundingBox().getX());
        assertEquals(250f, serverConnection.getServerWorld().getGameCharacter(1).getBoundingBox().getY());
        assertEquals(serverConnection.getServerWorld(), serverConnection.getServerWorld().getGameCharacter(1).getWorld());
        assertEquals(1, serverConnection.getServerWorld().getGameCharacter(1).getPlayerGameCharacterId());
    }

    /**
     * Test sending PacketUpdateCharacterInformation to ServerConnection instance.
     *
     * @throws InterruptedException is thrown when the thread is interrupted (Exception)
     */
    @Test
    public void testServerConnectionReceivesPacketUpdateCharacterInformation() throws InterruptedException {
        ServerConnection serverConnection = new ServerConnection();
        TestClient testClient = new TestClient();

        // Make new PlayerGameCharacter for the world.
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle1 = new Rectangle(80f, 80f, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle1, "Fire Bullet", "up", 1);
        Pistol pistol = new Pistol(newBullet);
        Rectangle playerGameCharacterRectangle = new Rectangle(80f, 80f, 10f, 10f);
        PlayerGameCharacter playerGameCharacter = new PlayerGameCharacter("Mati", 2f,
                playerGameCharacterRectangle, 80f, 80f, 10f, 10f, pistol, serverConnection.getServerWorld());
        // Make Shotgun.
        PistolBullet shotgunLeft = new PistolBullet();
        Rectangle shotgunLeftRectangle = new Rectangle(80f, 80f, 5f, 5f);
        shotgunLeft.makePistolBullet(shotgunLeftRectangle, "Fire Bullet", "up-left", 1);
        PistolBullet shotgunStraight = new PistolBullet();
        Rectangle shotgunStraightRectangle = new Rectangle(80f, 80f, 5f, 5f);
        shotgunStraight.makePistolBullet(shotgunStraightRectangle, "Fire Bullet", "up", 1);
        PistolBullet shotgunRight = new PistolBullet();
        Rectangle shotgunRightRectangle = new Rectangle( 80f, 80f, 5f, 5f);
        shotgunRight.makePistolBullet(shotgunRightRectangle, "Fire Bullet", "up-right", 1);
        Shotgun shotgun = new Shotgun(shotgunLeft, shotgunStraight, shotgunRight);
        // Set Shotgun to PlayerGameCharacter.
        playerGameCharacter.setPlayerCharacterCurrentShotgun(shotgun);

        // Add PlayerGameCharacter to the server's world.
        serverConnection.getServerWorld().addGameCharacter(1, playerGameCharacter);

        // Client sent PlayerGameCharacters info.
        testClient.sendPlayerInformation(20f, 60f, "right", 100);

        Thread.sleep(1000);
        // Server received PacketUpdateCharacterInformation.
        assertTrue(serverConnection.getServerWorld().getClients().containsKey(1));
        // Coordinates have been updated.
        assertEquals(100f, serverConnection.getServerWorld().getGameCharacter(1).getBoundingBox().getX());
        assertEquals(140f, serverConnection.getServerWorld().getGameCharacter(1).getBoundingBox().getY());
        assertEquals("up-right", serverConnection.getServerWorld().getGameCharacter(1).getCharacterDirection());
        // PlayerGameCharacter's PistolBullets direction been updated.
        assertEquals("up-right", serverConnection.getServerWorld().getGameCharacter(1)
                .getPlayerCharacterCurrentPistol().getBulletStraight().getDirection());
        assertEquals("up-right", serverConnection.getServerWorld().getGameCharacter(1)
                .getPlayerCharacterCurrentShotgun().getBulletStraight().getDirection());

        Thread.sleep(1000);
        // Server sent updated PlayerGameCharacter's coordinates and direction to the client.
        assertEquals(1, testClient.getReceivedPacketUpdateCharacterInformation().getId());
        assertEquals("Mati", testClient.getReceivedPacketUpdateCharacterInformation().getPlayerName());
        assertEquals(100f, testClient.getReceivedPacketUpdateCharacterInformation().getX());
        assertEquals(140f, testClient.getReceivedPacketUpdateCharacterInformation().getY());
        assertEquals("right", testClient.getReceivedPacketUpdateCharacterInformation().getDirection());
        assertEquals(100, testClient.getReceivedPacketUpdateCharacterInformation().getHealth());
    }

    /**
     * Test sending PacketBullet to ServerConnection instance.
     *
     * @throws InterruptedException is thrown when the thread is interrupted (Exception)
     */
    @Test
    public void testServerConnectionReceivesPacketBullet() throws InterruptedException {
        ServerConnection serverConnection = new ServerConnection();
        TestClient testClient = new TestClient();

        testClient.sendPlayerBulletInfo(10f, 50f, "Fire bullet", 1, "up");

        Thread.sleep(1000);
        // Server received PacketBullet and added new bullet with sent info to server's world.
        assertEquals(10f, serverConnection.getServerWorld().getPistolBulletsInTheWorld().get(0).getBoundingBox().getX());
        assertEquals(50f, serverConnection.getServerWorld().getPistolBulletsInTheWorld().get(0).getBoundingBox().getY());
        assertEquals("Fire bullet", serverConnection.getServerWorld().getPistolBulletsInTheWorld().get(0).getBulletTextureString());
        assertEquals(1, serverConnection.getServerWorld().getPistolBulletsInTheWorld().get(0).getDamage());
        assertEquals("up", serverConnection.getServerWorld().getPistolBulletsInTheWorld().get(0).getDirection());
    }

    /**
     * Test two clients connect to the ServerConnection instance.
     *
     * @throws InterruptedException is thrown when the thread is interrupted (Exception)
     */
    @Test
    public void testTwoClientConnectToTheServer() throws InterruptedException {
        ServerConnection serverConnection = new ServerConnection();
        TestClient testClient1 = new TestClient();
        TestClient testClient2 = new TestClient();

        testClient1.sendPacketConnect("Kati");

        Thread.sleep(1000);
        assertTrue(serverConnection.getServerWorld().getClients().containsKey(1));

        testClient2.sendPacketConnect("Mati");
        Thread.sleep(1000);
        assertTrue(serverConnection.getServerWorld().getClients().containsKey(2));
        assertEquals(2, serverConnection.getServerWorld().getClients().size());
        // Second client has been added to server's world.
        assertEquals("Mati", serverConnection.getServerWorld().getGameCharacter(2).getName());
        assertEquals(310f, serverConnection.getServerWorld().getGameCharacter(2).getBoundingBox().getX());
        assertEquals(250f, serverConnection.getServerWorld().getGameCharacter(2).getBoundingBox().getY());
        assertEquals(serverConnection.getServerWorld(), serverConnection.getServerWorld().getGameCharacter(2).getWorld());
        assertEquals(2, serverConnection.getServerWorld().getGameCharacter(2).getPlayerGameCharacterId());
    }

    /**
     * Test ServerConnection method restartServer.
     *
     * @throws InterruptedException is thrown when the thread is interrupted (Exception)
     */
    @Test
    public void testServerConnectionRestartServer() throws InterruptedException {
        ServerConnection serverConnection = new ServerConnection();
        TestClient testClient1 = new TestClient();
        TestClient testClient2 = new TestClient();
        TestClient testClient3 = new TestClient();

        testClient1.sendPacketConnect("Kati");

        Thread.sleep(1000);
        assertEquals(280f, serverConnection.getServerWorld().getGameCharacter(1).getBoundingBox().getX());

        testClient2.sendPacketConnect("Mati");
        Thread.sleep(1000);
        assertEquals(310f, serverConnection.getServerWorld().getGameCharacter(2).getBoundingBox().getX());

        serverConnection.restartServer();

        testClient3.sendPacketConnect("Malle");

        Thread.sleep(1000);
        // Server's default value for PlayerGameCharacter's x coordinates has been reset to 280.
        assertEquals(280f, serverConnection.getServerWorld().getGameCharacter(1).getBoundingBox().getX());
    }
}
