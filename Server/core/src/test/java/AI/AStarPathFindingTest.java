package test.java.AI;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.AI.Algorithm.AStarPathFinding;
import com.mygdx.game.AI.MapInfo.Node;
import com.mygdx.game.Characters.PlayerGameCharacter;
import com.mygdx.game.Characters.Zombie;
import com.mygdx.game.Weapons.Pistol;
import com.mygdx.game.Weapons.PistolBullet;
import com.mygdx.game.World.World;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AStarPathFindingTest {

    World world;

    {
        try {
            world = new World();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Test class AStarPathFinding method calculatePath.
     *
     * Path should be a straight line from node 48(Zombie) to 36(PlayerGameCharacter).
     */
    @Test
    public void testAStarPathFindingCalculatePathSimple() throws IOException {
        // Zombie.
        Rectangle boundingBox = new Rectangle(450f, 80f, 10f, 10f);
        Zombie zombie = new Zombie(1f, boundingBox, 450f, 80f, 10f, 10f
                , world);
        world.addZombieToServerWorldMap(1, zombie);
        // PlayerGameCharacter.
        // Make PistolBullet.
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle = new Rectangle(80f, 80f, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle, "Fire Bullet", "up", 1);
        // Make Pistol
        Pistol newPistol = new Pistol(newBullet);
        Rectangle playerGameCharacterRectangle = new Rectangle(80f, 80f, 10f, 10f);
        // Make PlayerGameCharacter.
        playerGameCharacterRectangle.set(playerGameCharacterRectangle);
        PlayerGameCharacter playerGameCharacter = new PlayerGameCharacter("Mati", 1f,
                playerGameCharacterRectangle, 80f, 80f, 10f, 10f
                , newPistol, world);
        playerGameCharacter.setPlayerGameCharacterId(1);

        AStarPathFinding aStarPathFinding = new AStarPathFinding(world, zombie, playerGameCharacter);

        aStarPathFinding.calculatePath();  // Calculate path form start node to end node.
        assertEquals(13, aStarPathFinding.getSolutionGraphPath().getCount());
        // Zombie node.
        assertEquals(48, aStarPathFinding.getSolutionGraphPath().get(0).getIndex());
        // PlayerGameCharacter node.
        assertEquals(36, aStarPathFinding.getSolutionGraphPath().get(12).getIndex());
    }

    /**
     * Test class aStarPathFinding method calculatePath.
     *
     * First path should be from node 243 to 36. Zombie also avoids walls. Then PlayerGameCharacter instance moves
     * and path should be from node 243 to 53.
     */
    @Test
    public void testCalculatePathPlayerGameCharacterMoves() throws IOException {
        // Zombie
        Rectangle boundingBox = new Rectangle(180f, 450f, 10f, 10f);
        Zombie zombie = new Zombie(1f, boundingBox, 140f, 520f, 10f, 10f
                , world);
        world.addZombieToServerWorldMap(1, zombie);
        // PlayerGameCharacter
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle = new Rectangle(80f, 80f, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle, "Fire Bullet", "up", 1);
        Pistol newPistol = new Pistol(newBullet);
        Rectangle playerGameCharacterRectangle = new Rectangle(80f, 80f, 10f, 10f);
        playerGameCharacterRectangle.set(playerGameCharacterRectangle);
        PlayerGameCharacter playerGameCharacter = new PlayerGameCharacter("Mati", 1f,
                playerGameCharacterRectangle,80f, 80f, 10f, 10f
                , newPistol, world);
        playerGameCharacter.setPlayerGameCharacterId(1);
        AStarPathFinding aStarPathFinding = new AStarPathFinding(world, zombie, playerGameCharacter);

        aStarPathFinding.calculatePath();
        assertEquals(16, aStarPathFinding.getSolutionGraphPath().getCount());
        assertEquals(243, aStarPathFinding.getSolutionGraphPath().get(0).getIndex());
        assertEquals(36, aStarPathFinding.getSolutionGraphPath().get(15).getIndex());

        playerGameCharacter.moveToNewPos(0f, 32f);  // Moves up by one node.
        aStarPathFinding.calculatePath();
        assertEquals(15, aStarPathFinding.getSolutionGraphPath().getCount());
        assertEquals(243, aStarPathFinding.getSolutionGraphPath().get(0).getIndex());
        assertEquals(53, aStarPathFinding.getSolutionGraphPath().get(14).getIndex());
    }

    /**
     * Test getting AStarPathFinding SolutionGraph.
     */
    @Test
    public void testGetSolutionGraphPath() throws IOException {
        World world = new World();
        Zombie zombie = Zombie.createZombie(520f, 520f, world);
        PlayerGameCharacter playerGameCharacter = PlayerGameCharacter.createPlayerGameCharacter(20f, 20f, "Mati", world, 1);
        AStarPathFinding aStarPathFinding = new AStarPathFinding(world, zombie, playerGameCharacter);
        assert aStarPathFinding.getSolutionGraphPath() != null;
    }

    /**
     * Test getting ConnectionImp cost and nodes.
     */
    @Test
    public void testConnectionImpGetters() throws IOException {
        World world = new World();
        Node startNode = world.getGraph().getNodes().get(0);
        Node endNode = world.getGraph().getNodes().get(16);
        startNode.createConnection(endNode, 1);
        assertEquals(startNode, startNode.getConnections().get(1).getFromNode());
        assertEquals(endNode, startNode.getConnections().get(1).getToNode());
        assertEquals(1, startNode.getConnections().get(1).getCost());
    }
}

