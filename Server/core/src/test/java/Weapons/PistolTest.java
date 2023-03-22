package test.java.Weapons;

import com.mygdx.game.Weapons.Pistol;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PistolTest {

    /**
     * Test class Pistol method createPistol.
     */
    @Test
    public void testMakePistolBullet() {
        Pistol pistol = Pistol.createPistol(150f, 150f);
        assertEquals(150f, pistol.getBulletStraight().getBoundingBox().getX());
        assertEquals(150f, pistol.getBulletStraight().getBoundingBox().getY());
        assertEquals(5, pistol.getBulletStraight().getBoundingBox().getWidth());
        assertEquals(5, pistol.getBulletStraight().getBoundingBox().getHeight());
        assertEquals(1, pistol.getBulletStraight().getDamage());
    }
}
