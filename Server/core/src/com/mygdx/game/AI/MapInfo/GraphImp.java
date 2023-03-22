package com.mygdx.game.AI.MapInfo;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.World.World;

/**
 * A graph for IndexedAStarPathFinder.
 */
public class GraphImp implements IndexedGraph<Node> {

    private Array<Node> nodes;
    private World world;

    public void setWorld(World world) {
        this.world = world;
    }

    public GraphImp(Array<Node> nodes) {
        this.nodes = nodes;
    }

    public Array<Node> getNodes() {
        return nodes;
    }

    @Override
    public int getIndex(Node node) {
        return node.getIndex();
    }

    @Override
    public int getNodeCount() {
        return nodes.size;
    }

    @Override
    public Array<Connection<Node>> getConnections(Node fromNode) {
        return fromNode.getConnections();
    }

    /**
     * Find a node that corresponds to world's tile map x and y coordinates.
     *
     * @param x coordinate
     * @param y coordinate
     * @return Node that correspond to x and y coordinates
     */
    public Node getNodeByXAndY(int x, int y) {
        int modX = x / world.getTiledMap().getProperties().get("tilewidth", Integer.class);
        int modY = y / world.getTiledMap().getProperties().get("tileheight", Integer.class);

        return nodes.get(world.getTiledMap().getProperties().get("width", Integer.class) * modY + modX);
    }
}
