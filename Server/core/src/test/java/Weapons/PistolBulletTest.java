package test.java.Weapons;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Weapons.PistolBullet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PistolBulletTest {

    /**
     * Test class PistolBullet method makePistolBullet.
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
    }

    /**
     * Test class PistolBullet method movePistolBullet with basic directions.
     */
    @Test
    public void testMovePistolBulletBasicDirections() {
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle = new Rectangle(75, 75, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle, "Fire Bullet", "up", 1);

        newBullet.movePistolBullet();
        assertEquals(75, newBullet.getBoundingBox().getX());
        assertEquals(77, newBullet.getBoundingBox().getY());

        newBullet.setDirection("left");
        newBullet.movePistolBullet();
        assertEquals(73, newBullet.getBoundingBox().getX());
        assertEquals(77, newBullet.getBoundingBox().getY());
        assertEquals("left", newBullet.getDirection());

        newBullet.setDirection("down");
        newBullet.movePistolBullet();
        assertEquals(73, newBullet.getBoundingBox().getX());
        assertEquals(75, newBullet.getBoundingBox().getY());
        assertEquals("down", newBullet.getDirection());

        newBullet.setDirection("right");
        newBullet.movePistolBullet();
        assertEquals(75, newBullet.getBoundingBox().getX());
        assertEquals(75, newBullet.getBoundingBox().getY());
        assertEquals("right", newBullet.getDirection());
    }

    /**
     * Test class PistolBullet method movePistolBullet with diagonal directions.
     */
    @Test
    public void movePistolBulletDiagonal() {
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle = new Rectangle(75, 75, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle, "Fire Bullet", "up", 1);

        newBullet.setDirection("up-left");
        newBullet.movePistolBullet();
        assertEquals(74.5, newBullet.getBoundingBox().getX());
        assertEquals(77, newBullet.getBoundingBox().getY());
        assertEquals("up-left", newBullet.getDirection());

        newBullet.setDirection("down-left");
        newBullet.movePistolBullet();
        assertEquals(74, newBullet.getBoundingBox().getX());
        assertEquals(75, newBullet.getBoundingBox().getY());
        assertEquals("down-left", newBullet.getDirection());

        newBullet.setDirection("down-right");
        newBullet.movePistolBullet();
        assertEquals(74.5, newBullet.getBoundingBox().getX());
        assertEquals(73, newBullet.getBoundingBox().getY());
        assertEquals("down-right", newBullet.getDirection());

        newBullet.setDirection("up-right");
        newBullet.movePistolBullet();
        assertEquals(75, newBullet.getBoundingBox().getX());
        assertEquals(75, newBullet.getBoundingBox().getY());
        assertEquals("up-right", newBullet.getDirection());

        newBullet.setDirection("right-up");
        newBullet.movePistolBullet();
        assertEquals(77, newBullet.getBoundingBox().getX());
        assertEquals(75.5, newBullet.getBoundingBox().getY());
        assertEquals("right-up", newBullet.getDirection());

        newBullet.setDirection("left-up");
        newBullet.movePistolBullet();
        assertEquals(75, newBullet.getBoundingBox().getX());
        assertEquals(76, newBullet.getBoundingBox().getY());
        assertEquals("left-up", newBullet.getDirection());

        newBullet.setDirection("right-down");
        newBullet.movePistolBullet();
        assertEquals(77, newBullet.getBoundingBox().getX());
        assertEquals(75.5, newBullet.getBoundingBox().getY());
        assertEquals("right-down", newBullet.getDirection());

        newBullet.setDirection("left-down");
        newBullet.movePistolBullet();
        assertEquals(75, newBullet.getBoundingBox().getX());
        assertEquals(75, newBullet.getBoundingBox().getY());
        assertEquals("left-down", newBullet.getDirection());
    }

    /**
     * Test class PistolBullet method checkIfPistolBulletIsInWorld.
     */
    @Test
    public void testCheckIfPistolBulletIsInWorld() {
        // Both coordinates ok
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle = new Rectangle(75, 75, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle, "Fire Bullet", "up", 1);
        assertTrue(newBullet.checkIfPistolBulletIsInWorld());

        // Both coordinates out
        newBullet.getBoundingBox().setPosition(63, 485);
        assertFalse(newBullet.checkIfPistolBulletIsInWorld());

        // X coordinate ok
        newBullet.getBoundingBox().setPosition(100, 485);
        assertFalse(newBullet.checkIfPistolBulletIsInWorld());

        // Y coordinate ok
        newBullet.getBoundingBox().setPosition(60, 75);
        assertFalse(newBullet.checkIfPistolBulletIsInWorld());
    }

    /**
     * Test class PistolBullet method reset.
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
    }

    /**
     * Test PistolBullet class damage getters and setters.
     */
    @Test
    public void testPistolBulletGettersSetters() {
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle = new Rectangle(75, 75, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle, "Fire Bullet", "up", 1);

        assertEquals(1, newBullet.getDamage());
        newBullet.setDamage(2);
        assertEquals(2, newBullet.getDamage());
    }
}