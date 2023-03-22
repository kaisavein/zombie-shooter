package test.java.CharacterTest;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.mygdx.game.Characters.GameCharacter;
import com.mygdx.game.Characters.PlayerGameCharacter;
import com.mygdx.game.World.World;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

public class GameCharacterTest {

    @Test
    /**
     * Test BoundingBox getter.
     */
    public void testGameCharacterBoundingBox() throws IOException {
        World world = new World();
        Rectangle boundingBox = new Rectangle();
        boundingBox.set(150f, 150f, 10f, 10f);
        GameCharacter gameCharacter = new GameCharacter(2f, boundingBox, 150f, 150f, 5f, 5f, world);
        assertEquals(150f, gameCharacter.getBoundingBox().getX());
        assertEquals(150f, gameCharacter.getBoundingBox().getY());
        assertEquals(10f, gameCharacter.getBoundingBox().getWidth());
        assertEquals(10f, gameCharacter.getBoundingBox().getHeight());
    }

    @Test
    /**
     * Test health, direction and world getters.
     */
    public void testGameCharacterHealthDirectionAndWorldGetters() throws IOException {
        World world = new World();
        Rectangle boundingBox = new Rectangle();
        boundingBox.set(150f, 150f, 10f, 10f);
        GameCharacter gameCharacter = new GameCharacter(2f, boundingBox, 150f, 150f, 5f, 5f, world);
        assertEquals(0, gameCharacter.getHealth()); // no health yet

        gameCharacter.setCharacterDirection("left");
        assertEquals("left", gameCharacter.getCharacterDirection());

        assertEquals(world, gameCharacter.getWorld());
        World world1 = new World();
        gameCharacter.setWorld(world1);
        assertEquals(world1, gameCharacter.getWorld());
    }
}
