package test.java.AI;

import com.mygdx.game.AI.Algorithm.ManhattanDistanceHeuristic;
import com.mygdx.game.AI.MapInfo.Node;
import com.mygdx.game.World.World;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ManhattanDistanceHeuristicTest {

    /**
     * Test class ManhattanDistanceHeuristic method estimate.
     *
     * Path from node 0 to 16.
     */
    @Test
    public void testManhattanDistanceHeuristicEstimateSimple() throws IOException {
        World world = new World();

        Node startNode = world.getGraph().getNodes().get(0);  // Node 0.
        Node endNode = world.getGraph().getNodes().get(16);  // Node 16.

        ManhattanDistanceHeuristic manhattanDistanceHeuristic = new ManhattanDistanceHeuristic(world);

        assertEquals(16, manhattanDistanceHeuristic.estimate(startNode, endNode));
    }

    /**
     * Test class ManhattanDistanceHeuristic method estimate.
     *
     * Path from node 0 to 288.
     */
    @Test
    public void testManhattanDistanceHeuristicEstimate() throws IOException {
        World world = new World();
        Node startNode = world.getGraph().getNodes().get(0);
        Node endNode = world.getGraph().getNodes().get(288);
        ManhattanDistanceHeuristic manhattanDistanceHeuristic = new ManhattanDistanceHeuristic(world);

        assertEquals(32, manhattanDistanceHeuristic.estimate(startNode, endNode));
    }

    /**
     * Test getting node X, Y and connections.
     */
    @Test
    public void testManhattanDistanceHeuristicAndNodeGetters() throws IOException {
        World world = new World();
        Node startNode = world.getGraph().getNodes().get(0);  // Node 0.
        assertEquals(0, startNode.getX());
        assertEquals(0, startNode.getY());
        assertEquals(1, startNode.getConnections().size);

        Node endNode = world.getGraph().getNodes().get(16);  // Node 16.
        assertEquals(16, endNode.getX());
        assertEquals(0, endNode.getY());
        assertEquals(1, endNode.getConnections().size);
    }
}

