package test.java.com.mygdx.game.Characters;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Characters.PlayerGameCharacter;
import com.mygdx.game.Weapons.Pistol;
import com.mygdx.game.Weapons.PistolBullet;
import com.mygdx.game.Weapons.Shotgun;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PlayerGameCharacterTest {

    /**
     * Test making a PlayerGameCharacter instance.
     */
    @Test
    public void testMakePlayerGameCharacter() {
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

        assertEquals("Mati", playerGameCharacter.getName());  // PlayerGameCharacter has a name.
        assertEquals(pistol, playerGameCharacter.getPlayerCharacterCurrentPistol());  // Unlike Zombie, PlayerGameCharacter has a Pistol.
        assertEquals(100, playerGameCharacter.getHealth());  // Has default health.
        assertEquals("regular pistol", playerGameCharacter.getCurrentWeapon());
        assertNull(playerGameCharacter.getPlayerCharacterCurrentShotgun());  // Shotgun has not been set.
    }

    /**
     * Test setting a Shotgun to PlayerGameCharacter.
     */
    @Test
    public void testSetShotgunToPlayerGameCharacter() {
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

        assertEquals(shotgun, playerGameCharacter.getPlayerCharacterCurrentShotgun());
        assertEquals(pistol, playerGameCharacter.getPlayerCharacterCurrentPistol());
    }

    /**
     * Test method GameCharacter method moveToPos.
     */
    @Test
    public void testPlayerGameCharacterMoveToPos() {
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

        playerGameCharacter.moveToNewPos(85f, 100f);
        assertEquals(85f, playerGameCharacter.getBoundingBox().getX());
        assertEquals(100f, playerGameCharacter.getBoundingBox().getY());

        playerGameCharacter.moveToNewPos(10f, 40f);
        assertEquals(10f, playerGameCharacter.getBoundingBox().getX());
        assertEquals(40f, playerGameCharacter.getBoundingBox().getY());
    }

    /**
     * Test static method createPlayerGameCharacter that creates a new PlayerGameCharacter instance.
     */
    @Test
    public void testCreatePlayerGameCharacter() {
        PlayerGameCharacter playerGameCharacter = PlayerGameCharacter.createPlayerGameCharacter(30f, 10f, "Mati", 1);

        assertEquals("Mati", playerGameCharacter.getName());
        assertEquals(30f, playerGameCharacter.getBoundingBox().getX());
        assertEquals(10f, playerGameCharacter.getBoundingBox().getY());
        assertEquals(1, playerGameCharacter.getPlayerGameCharacterId());
        assertEquals(2f, playerGameCharacter.getMovementSpeed());
        assertEquals(10f, playerGameCharacter.getBoundingBox().getWidth());
        assertEquals(10f, playerGameCharacter.getBoundingBox().getHeight());
        assertEquals(100, playerGameCharacter.getHealth());
        assertEquals(Pistol.class, playerGameCharacter.getPlayerCharacterCurrentPistol().getClass());
        assertEquals(Shotgun.class, playerGameCharacter.getPlayerCharacterCurrentShotgun().getClass());
    }

    /**
     * Test PlayerGameCharacter weapon setter and getter.
     */
    @Test
    public void testPlayerGameCharacterWeaponSetterGetter() {
        PlayerGameCharacter playerGameCharacter = PlayerGameCharacter.createPlayerGameCharacter(30f, 10f, "Mati", 1);

        playerGameCharacter.setWeapon("strong pistol");

        assertEquals("strong pistol", playerGameCharacter.getCurrentWeapon());
    }

    /**
     * Test PlayerGameCharacter direction setter and getter.
     */
    @Test
    public void testPlayerGameCharacterDirectionSetterGetter() {
        PlayerGameCharacter playerGameCharacter = PlayerGameCharacter.createPlayerGameCharacter(30f, 10f, "Mati", 1);

        playerGameCharacter.setPlayerDirection("left");

        assertEquals("left", playerGameCharacter.getPlayerDirection());
    }
}