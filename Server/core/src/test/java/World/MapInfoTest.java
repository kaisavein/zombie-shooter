package test.java.World;

import com.mygdx.game.AI.MapInfo.GraphImp;
import com.mygdx.game.AI.MapInfo.Node;
import com.mygdx.game.World.World;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class MapInfoTest {

    /**
     * If world starts then a graph is generated, this method tests if graph corresponds to world (DesertMap.tmx)
     * @throws IOException if world cannot be made.
     */
    @Test
    public void testGraph() throws IOException {
        World world = new World();
        GraphImp graph = world.getGraph();
        assertEquals(289, graph.getNodeCount());
        Node node = graph.getNodes().get(1);
        assertEquals(2,  graph.getConnections(node).size);
        graph.setWorld(world);

        System.out.println(graph);

        Node node2 = null;
        // Get node that collides with walls
        for (Node node1: graph.getNodes()) {
            if (node1.collidesWithWalls()) {
                node2 = node1;
            }
        }
        assertTrue(node2.collidesWithWalls());

        // Get node that doesn't collide with walls
        Node node3 = graph.getNodeByXAndY(0, 0);
        assertFalse(node3.collidesWithWalls());
    }

    /**
     * Tests node creation and its methods.
     * @throws IOException
     */
    @Test
    public void testNode() throws IOException {
        World world = new World();

        // Create node that doesn't collide with wall
        Node node = new Node(world.getMap(), 1000, 1000);
        assertEquals(1000, node.getX());
        assertEquals(1000, node.getY());
        assertEquals(0, node.getConnections().size);
        assertEquals(578, node.getIndex());
        assertFalse(node.collidesWithWalls());

        // Create a new node that collides with wall
        Node node1 = new Node(world.getMap(), 4, 1);
        assertTrue(node1.collidesWithWalls());
    }
}
