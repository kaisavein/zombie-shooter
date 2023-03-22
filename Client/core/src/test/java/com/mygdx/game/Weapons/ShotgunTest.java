package test.java.com.mygdx.game.Weapons;

import com.mygdx.game.Weapons.PistolBullet;
import com.mygdx.game.Weapons.Shotgun;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShotgunTest {

    /**
     * Test class Shotgun method setBulletLeftDirection.
     *
     * Tests setting all available directions.
     */
    @Test
    public void testShotgunSetBulletLeftDirection() {
        PistolBullet bulletLeft = new PistolBullet();
        PistolBullet bulletStraight = new PistolBullet();
        PistolBullet bulletRight = new PistolBullet();
        Shotgun shotgun = new Shotgun(bulletLeft, bulletStraight, bulletRight);
        assertEquals("up-left", shotgun.setBulletLeftDirection("up"));
        assertEquals("left", shotgun.setBulletLeftDirection("up-left"));
        assertEquals("left-down", shotgun.setBulletLeftDirection("left"));
        assertEquals("down", shotgun.setBulletLeftDirection("down-left"));
        assertEquals("down-right", shotgun.setBulletLeftDirection("down"));
        assertEquals("right", shotgun.setBulletLeftDirection("down-right"));
        assertEquals("right-up", shotgun.setBulletLeftDirection("right"));
        assertEquals("up", shotgun.setBulletLeftDirection("up-right"));
    }

    /**
     * Test class Shotgun method setBulletRightDirection.
     *
     * Tests setting all available directions.
     */
    @Test
    public void testShotgunSetBulletRightDirection() {
        PistolBullet bulletLeft = new PistolBullet();
        PistolBullet bulletStraight = new PistolBullet();
        PistolBullet bulletRight = new PistolBullet();
        Shotgun shotgun = new Shotgun(bulletLeft, bulletStraight, bulletRight);
        assertEquals("up-right", shotgun.setBulletRightDirection("up"));
        assertEquals("up", shotgun.setBulletRightDirection("up-left"));
        assertEquals("left-up", shotgun.setBulletRightDirection("left"));
        assertEquals("left", shotgun.setBulletRightDirection("down-left"));
        assertEquals("down-left", shotgun.setBulletRightDirection("down"));
        assertEquals("down", shotgun.setBulletRightDirection("down-right"));
        assertEquals("right-down", shotgun.setBulletRightDirection("right"));
        assertEquals("right", shotgun.setBulletRightDirection("up-right"));
    }

    /**
     * Test class Shotgun static method createShotgun.
     *
     * Method makes a Shotgun instances and makes PistolBullet instances for it.
     */
    @Test
    public void testShotgunCreateShotgun() {
        Shotgun shotgun = Shotgun.createShotgun(60f, 40f);

        assertEquals(60f, shotgun.getBulletLeft().getBoundingBox().getX());
        assertEquals(40f, shotgun.getBulletLeft().getBoundingBox().getY());
        assertEquals(60f, shotgun.getBulletRight().getBoundingBox().getX());
        assertEquals(40f, shotgun.getBulletRight().getBoundingBox().getY());
        assertEquals(60f, shotgun.getBulletStraight().getBoundingBox().getX());
        assertEquals(40f, shotgun.getBulletStraight().getBoundingBox().getY());

        assertEquals("Fire Bullet", shotgun.getBulletLeft().getBulletTextureString());
        assertEquals("Fire Bullet", shotgun.getBulletRight().getBulletTextureString());
        assertEquals("Fire Bullet", shotgun.getBulletStraight().getBulletTextureString());

        assertEquals("up-left", shotgun.getBulletLeft().getDirection());
        assertEquals("up-right", shotgun.getBulletRight().getDirection());
        assertEquals("up", shotgun.getBulletStraight().getDirection());

        assertEquals(1, shotgun.getBulletLeft().getDamage());
        assertEquals(1, shotgun.getBulletRight().getDamage());
        assertEquals(1, shotgun.getBulletStraight().getDamage());
    }
}