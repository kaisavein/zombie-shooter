package com.mygdx.game.AI.Algorithm;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.AI.MapInfo.Node;

import java.util.Iterator;

/**
 * Class is used to store a path with nodes.
 */
public class SolutionGraphPath implements GraphPath<Node> {

    // Path from start node to end node is stored in nodes variable.
    private Array<Node> nodes = new Array<Node>();

    @Override
    public int getCount() {
        return nodes.size;
    }

    @Override
    public Node get(int index) {
        return nodes.get(index);
    }

    @Override
    public void add(Node node) {
        nodes.add(node);
    }

    @Override
    public void clear() {
        nodes.clear();
    }

    @Override
    public void reverse() {
        nodes.reverse();
    }

    @Override
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }
}
