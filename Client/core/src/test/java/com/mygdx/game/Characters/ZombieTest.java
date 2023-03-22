package test.java.com.mygdx.game.Characters;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Characters.Zombie;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ZombieTest {

    /**
     * Test making a Zombie instance.
     */
    @Test
    public void testMakeZombie() {
        Rectangle boundingBox = new Rectangle(70f, 70f, 10f, 10f);
        Zombie zombie = new Zombie(1f, boundingBox, 70f, 70f, 10f, 10f);

        assertEquals(1f, zombie.getMovementSpeed());
        assertEquals(boundingBox, zombie.getBoundingBox());
        // Default health is 10.
        assertEquals(10, zombie.getHealth());
    }

    /**
     * Test class Zombie method makeZombie that makes a Zombie instance with given parameters.
     */
    @Test
    public void makeZombie() {
        Rectangle boundingBox = new Rectangle(50f, 50f, 10f, 10f);
        Zombie zombie = new Zombie();

        assertEquals(0, zombie.getMovementSpeed());
        assertNull(zombie.getBoundingBox());
        assertEquals(0, zombie.getHealth());

        zombie.makeZombie(1f, boundingBox, 50f, 50f, 10f, 10f);

        assertEquals(1f, zombie.getMovementSpeed());
        assertEquals(boundingBox, zombie.getBoundingBox());
        assertEquals(10, zombie.getHealth());
    }

    /**
     * Test resetting class Zombie instance.
     */
    @Test
    public void testResetZombie() {
        Rectangle boundingBox = new Rectangle(90f, 80f, 10f, 10f);
        Zombie zombie = new Zombie(1f, boundingBox, 90f, 80f, 10f, 10f);

        assertEquals(1f, zombie.getMovementSpeed());
        assertEquals(boundingBox, zombie.getBoundingBox());
        assertEquals(10, zombie.getHealth());

        zombie.reset();

        assertEquals(0, zombie.getMovementSpeed());
        assertNull(zombie.getBoundingBox());
        assertEquals(10, zombie.getHealth());
    }
}