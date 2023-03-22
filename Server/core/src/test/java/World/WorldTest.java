package test.java.World;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.AI.Algorithm.AStarPathFinding;
import com.mygdx.game.Characters.PlayerGameCharacter;
import com.mygdx.game.Characters.Zombie;
import com.mygdx.game.Weapons.Pistol;
import com.mygdx.game.Weapons.PistolBullet;
import com.mygdx.game.Weapons.Shotgun;
import com.mygdx.game.World.World;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class WorldTest {

    /**
     * Test playerCharacter and World. Test character creation if playerCharacter is added to world.
     * @throws IOException
     */
    @Test
    public void worldTestPlayerCharacter() throws IOException {
        World world = new World();
        Rectangle boundingBox = new Rectangle(520f, 20f, 10f, 10f);
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle = new Rectangle(25f, 25f, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle, "Fire Bullet", "up", 1);
        Pistol newPistol = new Pistol(newBullet);

        PlayerGameCharacter playerGameCharacter = new PlayerGameCharacter("Mati", 1f,
                boundingBox, 20f, 20f, 10f, 10f
                , newPistol, world);
        playerGameCharacter.setPlayerGameCharacterId(1);


        world.addGameCharacter(playerGameCharacter.getPlayerGameCharacterId(), playerGameCharacter);
        assertEquals(1, world.getClients().size());
        PistolBullet bulletLeft = new PistolBullet();
        PistolBullet bulletStraight = new PistolBullet();
        PistolBullet bulletRight = new PistolBullet();
        Shotgun shotgun = new Shotgun(bulletLeft, bulletStraight, bulletRight);
        playerGameCharacter.setPlayerCharacterCurrentShotgun(shotgun);
        assertEquals(1, world.getClientsIds().size());
        world.removeClient(1);
        assertEquals(0, world.getClientsIds().size());


    }

    /**
     * Test player movement and directions.
     * @throws IOException if world is wrong
     */
    @Test
    public void testCharacterMovement() throws IOException {
        World world = new World();
        Rectangle boundingBox = new Rectangle(520f, 20f, 10f, 10f);
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle = new Rectangle(25f, 25f, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle, "Fire Bullet", "up", 1);
        Pistol newPistol = new Pistol(newBullet);
        PlayerGameCharacter playerGameCharacter = new PlayerGameCharacter("Mati", 1f,
                boundingBox, 20f, 20f, 10f, 10f
                , newPistol, world);
        playerGameCharacter.setPlayerGameCharacterId(0);
        world.addGameCharacter(playerGameCharacter.getPlayerGameCharacterId(), playerGameCharacter);

        PistolBullet bulletLeft = new PistolBullet();
        PistolBullet bulletStraight = new PistolBullet();
        PistolBullet bulletRight = new PistolBullet();
        Shotgun shotgun = new Shotgun(bulletLeft, bulletStraight, bulletRight);
        playerGameCharacter.setPlayerCharacterCurrentShotgun(shotgun);

        playerGameCharacter.setCharacterDirection("up");
        world.movePlayerGameCharacter(playerGameCharacter.getPlayerGameCharacterId(), 0, 0);
        assertEquals("up", playerGameCharacter.getCharacterDirection());
        world.movePlayerGameCharacter(playerGameCharacter.getPlayerGameCharacterId(), 1, 1);
        assertEquals("up-right", playerGameCharacter.getCharacterDirection());
        world.movePlayerGameCharacter(playerGameCharacter.getPlayerGameCharacterId(), 1, 0);
        assertEquals("right", playerGameCharacter.getCharacterDirection());
        world.movePlayerGameCharacter(playerGameCharacter.getPlayerGameCharacterId(), 0, 1);
        assertEquals("up", playerGameCharacter.getCharacterDirection());
        world.movePlayerGameCharacter(playerGameCharacter.getPlayerGameCharacterId(), 0, 0);
        assertEquals("up", playerGameCharacter.getCharacterDirection());
        world.movePlayerGameCharacter(playerGameCharacter.getPlayerGameCharacterId(), -1, -1);
        assertEquals("down-left", playerGameCharacter.getCharacterDirection());
        world.movePlayerGameCharacter(playerGameCharacter.getPlayerGameCharacterId(), -1, 0);
        assertEquals("left", playerGameCharacter.getCharacterDirection());
        world.movePlayerGameCharacter(playerGameCharacter.getPlayerGameCharacterId(), 0, -1);
        assertEquals("down", playerGameCharacter.getCharacterDirection());
        world.movePlayerGameCharacter(playerGameCharacter.getPlayerGameCharacterId(), -1, 1);
        assertEquals("up-left", playerGameCharacter.getCharacterDirection());
        world.movePlayerGameCharacter(playerGameCharacter.getPlayerGameCharacterId(), 1, -1);
        assertEquals("down-right", playerGameCharacter.getCharacterDirection());
        assertTrue(world.detectBulletCollisionBetweenPlayerAndBullet(newBullet));
    }

    /**
     * Test zombies methods in world class. Adds zombies ,new zombie waves and zombie removal.
     * @throws IOException
     */
    @Test
    public void worldTestZombies() throws IOException {
        World world = new World();
        Rectangle boundingBox = new Rectangle(520f, 20f, 10f, 10f);
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle = new Rectangle(25f, 25f, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle, "Fire Bullet", "up", 1);
        Pistol newPistol = new Pistol(newBullet);
        PlayerGameCharacter playerGameCharacter = new PlayerGameCharacter("Mati", 1f,
                boundingBox, 20f, 20f, 10f, 10f
                , newPistol, world);
        playerGameCharacter.setPlayerGameCharacterId(0);
        world.addGameCharacter(playerGameCharacter.getPlayerGameCharacterId(), playerGameCharacter);

        PistolBullet bulletLeft = new PistolBullet();
        PistolBullet bulletStraight = new PistolBullet();
        PistolBullet bulletRight = new PistolBullet();
        assertFalse(world.isUpdatingBullets());
        Shotgun shotgun = new Shotgun(bulletLeft, bulletStraight, bulletRight);
        playerGameCharacter.setPlayerCharacterCurrentShotgun(shotgun);

        world.createZombies();
        assertEquals(4, world.getZombieMap().size());
        assertTrue(world.detectBulletCollisionBetweenZombieAndBullet(newBullet));
        world.setNewWave(true);
        assertTrue(world.isNewWave());
        assertEquals(4, world.getZombiesInWave());
        world.setScore(1);
        assertEquals(1, world.getScore());
        Zombie zombie = new Zombie(1, boundingBox, 1, 1, 1, 1 , world);
        world.addZombieToServerWorldMap(1, zombie);


        assertEquals(0, world.getAndEmptyZombiesToBeRemovedList().size());
        assertEquals(0, world.getZombiesToBeRemoved().size());

    }

    /**
     * Test servers restart method.
     * @throws IOException if World is wrong
     */
    @Test
    public void testRestart() throws IOException {
        World world = new World();
        world.setNewGame(true);
        assertTrue(world.isNewGame());
        Rectangle boundingBox = new Rectangle(520f, 20f, 10f, 10f);
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle = new Rectangle(25f, 25f, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle, "Fire Bullet", "up", 1);
        Pistol newPistol = new Pistol(newBullet);
        PlayerGameCharacter playerGameCharacter = new PlayerGameCharacter("Mati", 1f,
                boundingBox, 20f, 20f, 10f, 10f
                , newPistol, world);
        playerGameCharacter.setPlayerGameCharacterId(0);
        world.addGameCharacter(playerGameCharacter.getPlayerGameCharacterId(), playerGameCharacter);
        assertEquals(1, world.getClients().size());


        world.restartWorld();
        assertEquals(0, world.getClients().size());
        assertEquals(0, world.getZombieMap().size());
        assertEquals(0, world.getScore());
        assertEquals(0, world.getPistolBulletsInTheWorld().size());
        assertEquals(0, world.getZombiesToBeRemoved().size());
        assertEquals(4 ,world.getZombiesInWave());
    }

    /**
     * Tests bullets logic in world class. Test bullet adding, updating and bullets in world.
     * @throws IOException
     */
    @Test
    public void testBullet() throws IOException {
        World world = new World();
        Rectangle boundingBox = new Rectangle(520f, 20f, 10f, 10f);
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle = new Rectangle(25f, 25f, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle, "Fire Bullet", "up", 1);
        Pistol newPistol = new Pistol(newBullet);
        PlayerGameCharacter playerGameCharacter = new PlayerGameCharacter("Mati", 1f,
                boundingBox, 20f, 20f, 10f, 10f
                , newPistol, world);
        playerGameCharacter.setPlayerGameCharacterId(0);
        world.addGameCharacter(playerGameCharacter.getPlayerGameCharacterId(), playerGameCharacter);
        world.updateBulletsInTheWorldList();
        world.addBullet(newBullet);
        world.updateBulletsInTheWorldList();
        assertEquals(0, world.getPistolBulletsInTheWorld().size());


    }

}
