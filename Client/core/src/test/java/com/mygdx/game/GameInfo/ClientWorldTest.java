package test.java.com.mygdx.game.GameInfo;

import ClientConnection.ClientConnection;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Characters.PlayerGameCharacter;
import com.mygdx.game.Characters.Zombie;
import com.mygdx.game.GameInfo.ClientWorld;
import com.mygdx.game.Screens.GameScreen;
import com.mygdx.game.Weapons.Pistol;
import com.mygdx.game.Weapons.PistolBullet;
import com.mygdx.game.Weapons.Shotgun;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

class ClientWorldTest {

    /**
     * Test making ClientWorld.
     */
    @Test
    public void testMakingClientWorld() {
        ClientWorld clientWorld = new ClientWorld();

        assertEquals(Arrays.asList(), clientWorld.getPistolBullets());
        assertEquals(new HashMap<>(), clientWorld.getWorldGameCharactersMap());
        assertEquals(new HashMap<>(), clientWorld.getZombieHashMap());
        assertEquals(0, clientWorld.getScore());
        assertEquals(0, clientWorld.getScore());
    }

    /**
     * Test method that updates PlayerGameCharacter info (health, direction and coordinates).
     */
    @Test
    public void testClientWorldMovePlayerGameCharacter() {
        ClientWorld clientWorld = new ClientWorld();
        // Make PistolBullet for Pistol.
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle = new Rectangle(75f, 75f, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle, "Fire Bullet", "up", 1);
        // Make Pistol.
        Pistol pistol = new Pistol(newBullet);
        // Make PlayerGameCharacter.
        Rectangle playerGameCharacterRectangle = new Rectangle(75f, 75f, 10f, 10f);
        PlayerGameCharacter playerGameCharacter = new PlayerGameCharacter("Mati", 2f,
                playerGameCharacterRectangle, 75f, 75f, 10f, 10f, pistol);
        // Make Shotgun.
        PistolBullet shotgunLeft = new PistolBullet();
        Rectangle shotgunLeftRectangle = new Rectangle(75f, 75f, 5f, 5f);
        shotgunLeft.makePistolBullet(shotgunLeftRectangle, "Fire Bullet", "up-left", 1);
        PistolBullet shotgunStraight = new PistolBullet();
        Rectangle shotgunStraightRectangle = new Rectangle(75f, 75f, 5f, 5f);
        shotgunStraight.makePistolBullet(shotgunStraightRectangle, "Fire Bullet", "up", 1);
        PistolBullet shotgunRight = new PistolBullet();
        Rectangle shotgunRightRectangle = new Rectangle( 75f, 75f, 5f, 5f);
        shotgunRight.makePistolBullet(shotgunRightRectangle, "Fire Bullet", "up-right", 1);
        Shotgun shotgun = new Shotgun(shotgunLeft, shotgunStraight, shotgunRight);
        // Set Shotgun to PlayerGameCharacter.
        playerGameCharacter.setPlayerCharacterCurrentShotgun(shotgun);

        clientWorld.addGameCharacter(1, playerGameCharacter);
        HashMap<Integer, PlayerGameCharacter> hash = new HashMap<>();
        hash.put(1, playerGameCharacter);
        assertEquals(hash, clientWorld.getWorldGameCharactersMap());
        assertEquals(playerGameCharacter, clientWorld.getGameCharacter(1));

        clientWorld.movePlayerGameCharacter(1, 50f, 80f, "left", 90);
        assertEquals(50f, playerGameCharacter.getBoundingBox().getX());
        assertEquals(80f, playerGameCharacter.getBoundingBox().getY());
        assertEquals("left", playerGameCharacter.getPlayerDirection());
        assertEquals("left", playerGameCharacter.getPlayerCharacterCurrentPistol().getBulletStraight().getDirection());
        assertEquals(90, playerGameCharacter.getHealth());
    }

    /**
     * Test method that updates client's world PistolBullets(coordinates).
     */
    @Test
    public void testRemoveAndUpdateWorldPistolBulletList() {
        ClientWorld clientWorld = new ClientWorld();
        ClientConnection clientConnection = Mockito.mock(ClientConnection.class);
        GameScreen gameScreen = Mockito.mock(GameScreen.class);
        // Set GameScreen.
        when(clientConnection.getGameScreen()).thenReturn(gameScreen);
        // Register ClientConnection.
        clientWorld.registerClient(clientConnection);
        // Make PistolBullets.
        PistolBullet newBullet1 = new PistolBullet();
        Rectangle pistolBulletRectangle1 = new Rectangle(75, 75, 5f, 5f);
        newBullet1.makePistolBullet(pistolBulletRectangle1, "Fire Bullet", "up", 1);
        PistolBullet newBullet2 = new PistolBullet();
        Rectangle pistolBulletRectangle2 = new Rectangle(85, 45, 5f, 5f);
        newBullet2.makePistolBullet(pistolBulletRectangle2, "Fire Bullet", "right", 1);

        clientWorld.removeAndUpdateWorldPistolBulletList(Arrays.asList(newBullet1, newBullet2));
        assertEquals(Arrays.asList(newBullet1, newBullet2), clientWorld.getPistolBullets());
    }

    /**
     * Test method that makes and adds Zombie instance to client's world.
     */
    @Test
    public void testMakeAndAddZombieToClientWorldMap() {
        ClientWorld clientWorld = new ClientWorld();

        clientWorld.makeAndAddZombieToClientWorldMap(1, 50f, 60f);
        assertEquals(1, clientWorld.getZombieHashMap().size());
        assertEquals(Zombie.class, clientWorld.getZombieHashMap().get(1).getClass());
        assertEquals(50f, clientWorld.getZombieHashMap().get(1).getBoundingBox().getX());
        assertEquals(60f, clientWorld.getZombieHashMap().get(1).getBoundingBox().getY());
    }

    /**
     * Test method for removing Zombie instance from client's world.
     */
    @Test
    public void testRemoveZombieFromClientWorldMap() {
        ClientWorld clientWorld = new ClientWorld();
        clientWorld.makeAndAddZombieToClientWorldMap(1, 40f, 50f);
        clientWorld.makeAndAddZombieToClientWorldMap(2, 40f, 30f);

        assertEquals(2, clientWorld.getZombieHashMap().size());
        assertTrue(clientWorld.getZombieHashMap().containsKey(1));
        assertTrue(clientWorld.getZombieHashMap().containsKey(2));

        clientWorld.removeZombieFromClientWorldMap(1);
        assertEquals(1, clientWorld.getZombieHashMap().size());
        assertFalse(clientWorld.getZombieHashMap().containsKey(1));
        assertTrue(clientWorld.getZombieHashMap().containsKey(2));

        clientWorld.removeZombieFromClientWorldMap(2);
        assertEquals(0, clientWorld.getZombieHashMap().size());
        assertFalse(clientWorld.getZombieHashMap().containsKey(2));
    }

    /**
     * Test method that adds zombies to client's world.
     */
    @Test
    public void addZombiesToClientWorldMap() {
        ClientWorld clientWorld = new ClientWorld();
        Map<Integer, List<Float>> zombieInfoMap = new HashMap<>();
        // Map with zombies info. Key: zombie id; Value: zombie coordinates.

        zombieInfoMap.put(1, Arrays.asList(20f, 40f));
        zombieInfoMap.put(2,Arrays.asList(50f, 45f));
        zombieInfoMap.put(3, Arrays.asList(20f, 60f));

        clientWorld.addZombiesToClientWorldMap(zombieInfoMap);
        assertEquals(3, clientWorld.getZombieHashMap().size());
        assertTrue(clientWorld.getZombieHashMap().containsKey(1));
        assertTrue(clientWorld.getZombieHashMap().containsKey(2));
        assertTrue(clientWorld.getZombieHashMap().containsKey(3));
        assertEquals(20f, clientWorld.getZombieHashMap().get(1).getBoundingBox().getX());
        assertEquals(45f, clientWorld.getZombieHashMap().get(2).getBoundingBox().getY());
    }

    /**
     * Test method that updates zombies(zombie coordinates).
     */
    @Test
    public void updateZombiesInClientWorld() {
        ClientWorld clientWorld = new ClientWorld();
        Map<Integer, List<Float>> zombieInfoMap = new HashMap<>();
        zombieInfoMap.put(1, Arrays.asList(20f, 40f));
        zombieInfoMap.put(2, Arrays.asList(50f, 45f));
        zombieInfoMap.put(3, Arrays.asList(20f, 60f));

        Map<Integer, List<Float>> updatedZombiesInfoMap = new HashMap<>();

        updatedZombiesInfoMap.put(1, Arrays.asList(25f, 45f));
        updatedZombiesInfoMap.put(2, Arrays.asList(25f, 40f));
        updatedZombiesInfoMap.put(3, Arrays.asList(15f, 55f));


        clientWorld.addZombiesToClientWorldMap(zombieInfoMap);  // Add zombies.
        assertEquals(3, clientWorld.getZombieHashMap().size());
        assertEquals(20f, clientWorld.getZombieHashMap().get(1).getBoundingBox().getX());
        assertEquals(45f, clientWorld.getZombieHashMap().get(2).getBoundingBox().getY());
        assertEquals(60f, clientWorld.getZombieHashMap().get(3).getBoundingBox().getY());

        clientWorld.updateZombiesInClientWorld(updatedZombiesInfoMap);  // Update zombies.
        assertEquals(3, clientWorld.getZombieHashMap().size());
        assertEquals(25f, clientWorld.getZombieHashMap().get(1).getBoundingBox().getX());
        assertEquals(40f, clientWorld.getZombieHashMap().get(2).getBoundingBox().getY());
        assertEquals(55f, clientWorld.getZombieHashMap().get(3).getBoundingBox().getY());
    }

    /**
     * Test method updateBulletLocation.
     *
     * Test with all directions.
     */
    @Test
    public void testClientWorldUpdateBulletLocation() {
        ClientWorld clientWorld = new ClientWorld();
        // Make PistolBullet for Pistol.
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle = new Rectangle(75f, 75f, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle, "Fire Bullet", "up", 1);
        // Make Pistol.
        Pistol pistol = new Pistol(newBullet);
        // Make PlayerGameCharacter.
        Rectangle playerGameCharacterRectangle = new Rectangle(75f, 75f, 10f, 10f);
        PlayerGameCharacter playerGameCharacter = new PlayerGameCharacter("Mati", 2f,
                playerGameCharacterRectangle, 75f, 75f, 10f, 10f, pistol);
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

        clientWorld.addGameCharacter(1, playerGameCharacter);  // Add PlayerGameCharacter to the ClientWorld.

        clientWorld.updateBulletLocation(1, 85f, 80f, "up");
        assertEquals(91.55f, playerGameCharacter.getPlayerCharacterCurrentPistol()
                .getBulletStraight().getBoundingBox().getX());
        assertEquals(90f, playerGameCharacter.getPlayerCharacterCurrentPistol()
                .getBulletStraight().getBoundingBox().getY());
        assertEquals(93f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletLeft().getBoundingBox().getX());
        assertEquals(90f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletLeft().getBoundingBox().getY());
        assertEquals(91.55f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletStraight().getBoundingBox().getX());
        assertEquals(90f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletStraight().getBoundingBox().getY());
        assertEquals(90f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletRight().getBoundingBox().getX());
        assertEquals(90f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletRight().getBoundingBox().getY());

        clientWorld.updateBulletLocation(1, 90f, 70f, "up-right");
        assertEquals(98f, playerGameCharacter.getPlayerCharacterCurrentPistol()
                .getBulletStraight().getBoundingBox().getX());
        assertEquals(80f, playerGameCharacter.getPlayerCharacterCurrentPistol()
                .getBulletStraight().getBoundingBox().getY());
        assertEquals(101f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletLeft().getBoundingBox().getX());
        assertEquals(80f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletLeft().getBoundingBox().getY());
        assertEquals(98f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletStraight().getBoundingBox().getX());
        assertEquals(80f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletStraight().getBoundingBox().getY());
        assertEquals(100.5f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletRight().getBoundingBox().getX());
        assertEquals(82f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletRight().getBoundingBox().getY());

        clientWorld.updateBulletLocation(1, 10f, 60f, "down-right");
        assertEquals(18f, playerGameCharacter.getPlayerCharacterCurrentPistol()
                .getBulletStraight().getBoundingBox().getX());
        assertEquals(55f, playerGameCharacter.getPlayerCharacterCurrentPistol()
                .getBulletStraight().getBoundingBox().getY());
        assertEquals(21f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletLeft().getBoundingBox().getX());
        assertEquals(55f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletLeft().getBoundingBox().getY());
        assertEquals(18f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletStraight().getBoundingBox().getX());
        assertEquals(55f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletStraight().getBoundingBox().getY());
        assertEquals(19f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletRight().getBoundingBox().getX());
        assertEquals(55f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletRight().getBoundingBox().getY());

        clientWorld.updateBulletLocation(1, 85f, 80f, "up-left");
        assertEquals(83.5f, playerGameCharacter.getPlayerCharacterCurrentPistol()
                .getBulletStraight().getBoundingBox().getX());
        assertEquals(90f, playerGameCharacter.getPlayerCharacterCurrentPistol()
                .getBulletStraight().getBoundingBox().getY());
        assertEquals(82f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletLeft().getBoundingBox().getX());
        assertEquals(92.5f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletLeft().getBoundingBox().getY());
        assertEquals(83.5f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletStraight().getBoundingBox().getX());
        assertEquals(90f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletStraight().getBoundingBox().getY());
        assertEquals(81f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletRight().getBoundingBox().getX());
        assertEquals(90f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletRight().getBoundingBox().getY());

        clientWorld.updateBulletLocation(1, 35f, 10f, "down-left");
        assertEquals(32f, playerGameCharacter.getPlayerCharacterCurrentPistol()
                .getBulletStraight().getBoundingBox().getX());
        assertEquals(5f, playerGameCharacter.getPlayerCharacterCurrentPistol()
                .getBulletStraight().getBoundingBox().getY());
        assertEquals(30f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletLeft().getBoundingBox().getX());
        assertEquals(5f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletLeft().getBoundingBox().getY());
        assertEquals(32f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletStraight().getBoundingBox().getX());
        assertEquals(5f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletStraight().getBoundingBox().getY());
        assertEquals(30f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletRight().getBoundingBox().getX());
        assertEquals(5f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletRight().getBoundingBox().getY());

        clientWorld.updateBulletLocation(1, 35f, 80f, "left");
        assertEquals(30f, playerGameCharacter.getPlayerCharacterCurrentPistol()
                .getBulletStraight().getBoundingBox().getX());
        assertEquals(81f, playerGameCharacter.getPlayerCharacterCurrentPistol()
                .getBulletStraight().getBoundingBox().getY());
        assertEquals(30f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletLeft().getBoundingBox().getX());
        assertEquals(83f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletLeft().getBoundingBox().getY());
        assertEquals(30f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletStraight().getBoundingBox().getX());
        assertEquals(81f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletStraight().getBoundingBox().getY());
        assertEquals(30f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletRight().getBoundingBox().getX());
        assertEquals(80f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletRight().getBoundingBox().getY());

        clientWorld.updateBulletLocation(1, 95f, 40f, "right");
        assertEquals(105f, playerGameCharacter.getPlayerCharacterCurrentPistol()
                .getBulletStraight().getBoundingBox().getX());
        assertEquals(41f, playerGameCharacter.getPlayerCharacterCurrentPistol()
                .getBulletStraight().getBoundingBox().getY());
        assertEquals(105f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletLeft().getBoundingBox().getX());
        assertEquals(40f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletLeft().getBoundingBox().getY());
        assertEquals(105f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletStraight().getBoundingBox().getX());
        assertEquals(41f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletStraight().getBoundingBox().getY());
        assertEquals(105f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletRight().getBoundingBox().getX());
        assertEquals(43f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletRight().getBoundingBox().getY());

        clientWorld.updateBulletLocation(1, 85f, 80f, "down");
        assertEquals(84.3f, playerGameCharacter.getPlayerCharacterCurrentPistol()
                .getBulletStraight().getBoundingBox().getX());
        assertEquals(75f, playerGameCharacter.getPlayerCharacterCurrentPistol()
                .getBulletStraight().getBoundingBox().getY());
        assertEquals(83f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletLeft().getBoundingBox().getX());
        assertEquals(75f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletLeft().getBoundingBox().getY());
        assertEquals(84.3f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletStraight().getBoundingBox().getX());
        assertEquals(75f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletStraight().getBoundingBox().getY());
        assertEquals(85f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletRight().getBoundingBox().getX());
        assertEquals(75f, playerGameCharacter.getPlayerCharacterCurrentShotgun()
                .getBulletRight().getBoundingBox().getY());
    }

    /**
     * Test ClientWorld's myPlayerGameCharacter, waveCount and score getters, setters.
     */
    @Test
    public void testClientWorldMyPlayerGameCharacterAndWaveCountAndScoreGetterSetter() {
        ClientWorld clientWorld = new ClientWorld();

        assertEquals(0, clientWorld.getWaveCount());
        assertEquals(0, clientWorld.getScore());
        assertNull(clientWorld.getMyPlayerGameCharacter());

        clientWorld.setWaveCount(1);
        clientWorld.setScore(100);
        PlayerGameCharacter playerGameCharacter = PlayerGameCharacter.createPlayerGameCharacter(30f, 10f, "Mati", 1);
        clientWorld.setMyPlayerGameCharacter(playerGameCharacter);

        assertEquals(1, clientWorld.getWaveCount());
        assertEquals(100, clientWorld.getScore());
        assertEquals(playerGameCharacter, clientWorld.getMyPlayerGameCharacter());
    }
}