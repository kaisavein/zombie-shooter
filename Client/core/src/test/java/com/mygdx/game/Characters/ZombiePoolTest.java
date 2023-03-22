package test.java.com.mygdx.game.Characters;

import com.mygdx.game.Characters.Zombie;
import com.mygdx.game.Characters.ZombiePool;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ZombiePoolTest {

    /**
     * Test class ZombiePool method newObject that returns a new Zombie instance.
     */
    @Test
    public void testZombiePoolNewObject() {
        ZombiePool zombiePool = new ZombiePool();
        Zombie zombie = zombiePool.newObject();

        assertEquals(Zombie.class, zombie.getClass());
        assertEquals(0, zombie.getMovementSpeed());
        assertEquals(0, zombie.getHealth());
        assertNull(zombie.getBoundingBox());
    }

    /**
     * Test ZombiePool method obtain.
     */
    @Test
    public void testZombiePoolObtain() {
        ZombiePool zombiePool = new ZombiePool();
        Zombie zombie = zombiePool.obtain();

        assertEquals(Zombie.class, zombie.getClass());
        assertEquals(0, zombie.getMovementSpeed());
        assertEquals(0, zombie.getHealth());
        assertNull(zombie.getBoundingBox());
    }
}