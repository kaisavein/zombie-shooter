package test.java.com.mygdx.game.Characters;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Characters.GameCharacter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameCharacterTest {

    /**
     * Test GameCharacter health getter and setter.
     */
    @Test
    public void testGameCharacterHealthGetterSetter() {
        Rectangle gameCharacterRectangle = new Rectangle(75f, 75f, 10f, 10f);
        GameCharacter gameCharacter = new GameCharacter(2f, gameCharacterRectangle, 150f, 150f, 10f, 10f);

        gameCharacter.setHealth(100);

        assertEquals(100, gameCharacter.getHealth());

    }
}
