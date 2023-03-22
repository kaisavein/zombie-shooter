package com.mygdx.game.AI.Algorithm;

import com.badlogic.gdx.ai.pfa.Heuristic;
import com.mygdx.game.AI.MapInfo.Node;
import com.mygdx.game.World.World;

public class ManhattanDistanceHeuristic implements Heuristic<Node> {

    public World world;

    /**
     * Class ManhattanDistanceHeuristic constructor.
     *
     * @param world where the distance between nodes is calculated (World)
     */
    public ManhattanDistanceHeuristic(World world) {
        this.world = world;
    }

    /**
     * Calculate an estimated distance between start node and end node.
     *
     * @param node start node (Node)
     * @param endNode end node (Node)
     * @return distance between two nodes (float)
     */
    @Override
    public float estimate(Node node, Node endNode) {
        int startIndex = node.getIndex();
        int endIndex = endNode.getIndex();

        // Calculate start node x and y.
        int startNodeY = startIndex / world.getTiledMap().getProperties().get("width", Integer.class);
        int startNodeX = startIndex % world.getTiledMap().getProperties().get("width", Integer.class);

        // Calculate end node x and y.
        int endNodeY = endIndex / world.getTiledMap().getProperties().get("width", Integer.class);
        int endNodeX = endIndex % world.getTiledMap().getProperties().get("width", Integer.class);

        // Manhattan distance equation.
        return Math.abs(startNodeX - endNodeX) + Math.abs(startNodeY - endNodeY);
    }
}
