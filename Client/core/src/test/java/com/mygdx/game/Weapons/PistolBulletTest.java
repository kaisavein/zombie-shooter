package test.java.com.mygdx.game.Weapons;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Weapons.PistolBullet;
import com.mygdx.game.Weapons.PistolBulletPool;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PistolBulletTest {

    /**
     * Test making a PistolBullet instance.
     */
    @Test
    public void testMakePistolBullet() {
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle = new Rectangle(75, 75, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle, "Fire Bullet", "up", 1);
        assertEquals(75, newBullet.getBoundingBox().getX());
        assertEquals(75, newBullet.getBoundingBox().getY());
        assertEquals(5, newBullet.getBoundingBox().getWidth());
        assertEquals(5, newBullet.getBoundingBox().getHeight());
        assertEquals(1, newBullet.getDamage());
        assertEquals("Fire Bullet", newBullet.getBulletTextureString());
        assertEquals("up", newBullet.getDirection());
    }

    /**
     * Test resetting a PistolBullet.
     */
    @Test
    public void testPistolBulletReset() {
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle = new Rectangle(75, 75, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle, "Fire Bullet", "up", 1);
        newBullet.reset();
        assertEquals(0, newBullet.getBoundingBox().getX());
        assertEquals(0, newBullet.getBoundingBox().getY());
        assertEquals(0, newBullet.getBoundingBox().getWidth());
        assertEquals(0, newBullet.getBoundingBox().getHeight());
        assertNull(newBullet.getDirection());
    }

    /**
     * Test PistolBulletPool class.
     */
    @Test
    public void testPistolBulletPool() {
        PistolBulletPool pistolBulletPool = new PistolBulletPool();
        assertEquals(PistolBullet.class, pistolBulletPool.newObject().getClass());
    }
}