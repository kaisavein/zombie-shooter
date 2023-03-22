package test.java.CharacterTest;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Characters.PlayerGameCharacter;
import com.mygdx.game.Characters.Zombie;
import com.mygdx.game.Weapons.Pistol;
import com.mygdx.game.Weapons.PistolBullet;
import com.mygdx.game.World.World;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ZombieTest {

    /**
     * Tests if zombie moves.
     * @throws IOException
     */
    @Test
    public void testZombieFindPlayerGameCharacterId() throws IOException {
        World world = new World();
        world.addGameCharacter(1, mock(PlayerGameCharacter.class));
        world.addGameCharacter(2, mock(PlayerGameCharacter.class));
        world.addGameCharacter(3, mock(PlayerGameCharacter.class));
        Zombie zombie1 = mock(Zombie.class);
        Zombie zombie2 = mock(Zombie.class);
        // Test zombie adding
        world.addZombieToServerWorldMap(zombie1.getZombieId(), zombie1);
        world.addZombieToServerWorldMap(zombie2.getZombieId(), zombie2);
        assertEquals(1, world.getZombieMap().size());

        System.out.println(zombie1.getPlayerGameCharacterId());
        world.updateZombiesInTheWorldZombieMap();
    }


    /**
     * Test zombie utility methods.
     * @throws IOException
     */
    @Test
    public void zombieMethodsTest() throws IOException {
        World world = null;
        try {
            world = new World();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        Zombie zombie = Zombie.createZombie(1, 1, world);
        Zombie zombie2 = Zombie.createZombie(1, 1, world);
        assertTrue(zombie.zombieHasLives());
        // Test if zombie can collide with zombie
        assertTrue(zombie.collidesWithZombie());
        zombie2.moveToNewPos(10, 10);
        assertFalse(zombie.collidesWithZombie());
        assertFalse(zombie.zombieCollidesWithZombie(1, 1));

        // Test zombie and player character utili
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle = new Rectangle(75f, 75f, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle, "Fire Bullet", "up", 1);
        Pistol pistol = new Pistol(newBullet);
        Rectangle playerGameCharacterRectangle = new Rectangle(75f, 75f, 10f, 10f);
        PlayerGameCharacter playerGameCharacter = new PlayerGameCharacter("Mati", 2f,
                playerGameCharacterRectangle, 75f, 75f, 10f, 10f, pistol, world);

        // Test if zombie doesn't collide with player
        assert world != null;
        world.getClients().put(1, playerGameCharacter);
        zombie.getPlayerGameCharacterId();
        zombie.findPlayerGameCharacterId();
        assertFalse(zombie.collidesWithPlayerGameCharacter());


        // Test now if zombie collides with new player
        PlayerGameCharacter playerGameCharacter1 =
                PlayerGameCharacter.createPlayerGameCharacter(1, 1, "Peeter", world, 2);

        world.getClients().put(2, playerGameCharacter1);

        assertTrue(zombie.collidesWithPlayerGameCharacter());

    }
}