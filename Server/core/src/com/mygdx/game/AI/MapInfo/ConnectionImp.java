package com.mygdx.game.AI.MapInfo;

import com.badlogic.gdx.ai.pfa.Connection;

public class ConnectionImp implements Connection<Node> {
    private Node toNode;
    private Node fromNode;
    private float cost;

    /**
     * Connection constructor.
     *
     * Connection form node to another node.
     * @param fromNode that connection is created to.
     * @param toNode that the connection goes to.
     * @param cost of moving between nodes.
     */
    public ConnectionImp(Node fromNode, Node toNode, float cost) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.cost = cost;
    }

    @Override
    public float getCost() {
        return cost;
    }

    @Override
    public Node getFromNode() {
        return fromNode;
    }

    @Override
    public Node getToNode() {
        return toNode;
    }
}
