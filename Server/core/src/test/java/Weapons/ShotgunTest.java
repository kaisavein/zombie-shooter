package test.java.Weapons;

import com.mygdx.game.Weapons.PistolBullet;
import com.mygdx.game.Weapons.Shotgun;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShotgunTest {

    /**
     * Test class Shotgun method setBulletLeftDirection.
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
     * Test class Shotgun method setBulletsDirections.
     */
    @Test
    public void testShotgunSetBulletsDirections() {
        PistolBullet bulletLeft = new PistolBullet();
        PistolBullet bulletStraight = new PistolBullet();
        PistolBullet bulletRight = new PistolBullet();
        Shotgun shotgun = new Shotgun(bulletLeft, bulletStraight, bulletRight);

        shotgun.setBulletsDirections("down");
        assertEquals("down-left", shotgun.getBulletRight().getDirection());
        assertEquals("down", shotgun.getBulletStraight().getDirection());
        assertEquals("down-right", shotgun.getBulletLeft().getDirection());

        shotgun.setBulletsDirections("up-left");
        assertEquals("up", shotgun.getBulletRight().getDirection());
        assertEquals("up-left", shotgun.getBulletStraight().getDirection());
        assertEquals("left", shotgun.getBulletLeft().getDirection());
    }

    /**
     * Test class Shotgun method createShotgun.
     */
    @Test
    public void testShotgunCreateShotgun() {
        Shotgun shotgun = Shotgun.createShotgun(150f, 150f);
        assertEquals("up-right", shotgun.getBulletRight().getDirection());
        assertEquals(150f, shotgun.getBulletStraight().getBoundingBox().getX());
        assertEquals(150f, shotgun.getBulletLeft().getBoundingBox().getY());
        assertEquals(1, shotgun.getBulletRight().getDamage());
        assertEquals(5f, shotgun.getBulletStraight().getBoundingBox().getWidth());
        assertEquals(5f, shotgun.getBulletLeft().getBoundingBox().getHeight());
    }
}
