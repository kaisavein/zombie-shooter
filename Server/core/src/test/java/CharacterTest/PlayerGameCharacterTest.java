package test.java.CharacterTest;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Characters.PlayerGameCharacter;
import com.mygdx.game.Characters.Zombie;
import com.mygdx.game.Weapons.Pistol;
import com.mygdx.game.Weapons.PistolBullet;
import com.mygdx.game.World.World;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class PlayerGameCharacterTest {

    /**
     * Test making new PlayerGameCharacter instance.
     */
    @Test
    public void makePlayerGameCharacter() {
        World world = null;
        try {
            world = new World();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        // Make PistolBullet for Pistol.
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle = new Rectangle(75f, 75f, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle, "Fire Bullet", "up", 1);
        // Make Pistol.
        Pistol pistol = new Pistol(newBullet);
        // Make PlayerGameCharacter.
        Rectangle playerGameCharacterRectangle = new Rectangle(75f, 75f, 10f, 10f);
        PlayerGameCharacter playerGameCharacter = new PlayerGameCharacter("Mati", 2f, playerGameCharacterRectangle,
                75f, 75f, 10f, 10f, pistol, world);

        assertEquals("Mati", playerGameCharacter.getName());  // PlayerGameCharacter has a name.
        assertEquals(pistol, playerGameCharacter.getPlayerCharacterCurrentPistol());  // Unlike Zombie, PlayerGameCharacter has a Pistol.
        assertEquals(100, playerGameCharacter.getHealth());  // Has default health.
        assertEquals(pistol, playerGameCharacter.getPlayerCharacterCurrentPistol());
        assertNull(playerGameCharacter.getPlayerCharacterCurrentShotgun());  // Shotgun has not been set.
    }

    /**
     * Test method collidesWithZombie.
     */
    @Test
    public void testIfCollidesWithZombiePlayerGameCharacterHealthDecreases() {
        World world = null;
        try {
            world = new World();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        // Make PlayerGameCharacter.
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle = new Rectangle(75f, 75f, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle, "Fire Bullet", "up", 1);
        Pistol pistol = new Pistol(newBullet);
        Rectangle playerGameCharacterRectangle = new Rectangle(75f, 75f, 10f, 10f);
        PlayerGameCharacter playerGameCharacter = new PlayerGameCharacter("Mati", 2f,
                playerGameCharacterRectangle, 75f, 75f, 10f, 10f, pistol, world);
        // Make Zombie.
        Rectangle boundingBox = new Rectangle(80f, 82f, 10f, 10f);
        Zombie zombie = new Zombie(1f, boundingBox, 80f, 85f, 10f, 10f, world);
        Rectangle boundingBox2 = new Rectangle(64f, 64f, 10f, 10f);
        Zombie zombie2 = new Zombie(1f, boundingBox2, 64f, 64f, 10f, 10f, world);

        playerGameCharacter.collidesWithZombie(zombie);
        assertEquals(90, playerGameCharacter.getHealth());  // Health decreases.
        playerGameCharacter.collidesWithZombie(zombie);
        assertEquals(90, playerGameCharacter.getHealth());  // Health does not decreases because .

        for (int i = 1000; i > 0; i--) {
            playerGameCharacter.collidesWithZombie(zombie);
            if (i == 2) {
                assertEquals(0, playerGameCharacter.getHealth());  // Health is 0.
            }
        }
        assertEquals(0, playerGameCharacter.getHealth());  // Health stops decreasing.
    }

    /**
     * Test static method createPlayerGameCharacter.
     */
    @Test
    public void testMethodCreatePlayerGameCharacter() {
        World world = null;
        try {
            world = new World();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        PlayerGameCharacter playerGameCharacter = PlayerGameCharacter.createPlayerGameCharacter(50f, 80f, "Kati", world, 1);

        assertEquals("Kati", playerGameCharacter.getName());  // PlayerGameCharacter has a name.
        assertEquals(50f, playerGameCharacter.getBoundingBox().getX());
        assertEquals(80f, playerGameCharacter.getBoundingBox().getY());
        assertEquals(1, playerGameCharacter.getPlayerGameCharacterId());
        assertEquals(10f, playerGameCharacter.getBoundingBox().getHeight());
        assertEquals(10f, playerGameCharacter.getBoundingBox().getWidth());
        // Has a Pistol.
        assertEquals(50f, playerGameCharacter.getPlayerCharacterCurrentPistol().getBulletStraight().getBoundingBox().getX());
        assertEquals(80f, playerGameCharacter.getPlayerCharacterCurrentPistol().getBulletStraight().getBoundingBox().getY());
        // Has a Shotgun.
        assertEquals(50f, playerGameCharacter.getPlayerCharacterCurrentShotgun().getBulletStraight().getBoundingBox().getX());
        assertEquals(80f, playerGameCharacter.getPlayerCharacterCurrentShotgun().getBulletStraight().getBoundingBox().getY());
        assertEquals(100, playerGameCharacter.getHealth());  // Has default health.
    }

    /**
     * Test method characterIsHit.
     */
    @Test
    public void testCharacterIsHit() {
        World world = null;
        try {
            world = new World();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        // Make PlayerGameCharacter.
        PistolBullet newBullet1 = new PistolBullet();
        Rectangle pistolBulletRectangle1 = new Rectangle(75f, 75f, 5f, 5f);
        newBullet1.makePistolBullet(pistolBulletRectangle1, "Fire Bullet", "up", 1);
        Pistol pistol = new Pistol(newBullet1);
        Rectangle playerGameCharacterRectangle = new Rectangle(75f, 75f, 10f, 10f);
        PlayerGameCharacter playerGameCharacter = new PlayerGameCharacter("Mati", 2f,
                playerGameCharacterRectangle, 75f, 75f, 10f, 10f, pistol, world);

        // Make PistolBullet.
        PistolBullet newBullet2 = new PistolBullet();
        Rectangle pistolBulletRectangle2 = new Rectangle(75f, 75f, 5f, 5f);
        newBullet2.makePistolBullet(pistolBulletRectangle2, "Fire Bullet", "up", 1);

        playerGameCharacter.characterIsHit(newBullet2);

        assertEquals(95, playerGameCharacter.getHealth());
    }

    /**
     * Test PlayerGameCharacter's decreases when hit with multiple PistolBullets.
     */
    @Test
    public void testPlayerGameCharacterIsHitWithMultipleBullets() {
        World world = null;
        try {
            world = new World();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        // Make PlayerGameCharacter.
        PistolBullet newBullet1 = new PistolBullet();
        Rectangle pistolBulletRectangle1 = new Rectangle(75f, 75f, 5f, 5f);
        newBullet1.makePistolBullet(pistolBulletRectangle1, "Fire Bullet", "up", 1);
        Pistol pistol = new Pistol(newBullet1);
        Rectangle playerGameCharacterRectangle = new Rectangle(75f, 75f, 10f, 10f);
        PlayerGameCharacter playerGameCharacter = new PlayerGameCharacter("Mati", 2f,
                playerGameCharacterRectangle, 75f, 75f, 10f, 10f, pistol, world);

        for (int i = 0; i < 20; i++) {
            PistolBullet newBullet2 = new PistolBullet();
            Rectangle pistolBulletRectangle2 = new Rectangle(75f, 75f, 5f, 5f);
            newBullet2.makePistolBullet(pistolBulletRectangle2, "Fire Bullet", "up", 1);
            playerGameCharacter.characterIsHit(newBullet2);

            if (i == 9) {
                assertEquals(50, playerGameCharacter.getHealth());
            }
        }

        assertEquals(0, playerGameCharacter.getHealth());

        PistolBullet newBullet3 = new PistolBullet();
        Rectangle pistolBulletRectangle3 = new Rectangle(75f, 75f, 5f, 5f);
        newBullet3.makePistolBullet(pistolBulletRectangle3, "Fire Bullet", "up", 1);
        playerGameCharacter.characterIsHit(newBullet3);

        assertEquals(0, playerGameCharacter.getHealth());  // Health stops decreasing when health is 0.
    }
}
