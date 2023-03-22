package com.mygdx.game.AI.MapInfo;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;


public class Node {

    private int x;
    private int y;
    private final int index;
    private int type;
    private Array<Connection<Node>> connections;
    private static Integer id = 0;
    private TiledMap map;

    /**
     * Node constructor.
     *
     * @param map that the node is on
     * @param x coordinate of the node
     * @param y coordinate of the node
     */
    public Node(TiledMap map, int x, int y) {
        this.x = x;
        this.y = y;
        this.index = id;
        this.type = 0;
        this.connections = new Array<>();
        this.map = map;
        id++;
    }

    public int getIndex () {
        return index;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    /**
     * Return node's connections to other nodes.
     *
     * Connections are used for zombie AI.
     * @return connections to other nodes
     */
    public Array<Connection<Node>> getConnections() {
        return connections;
    }

    /**
     * Create connection between two nodes.
     *
     * @param toNode node that the connection is created to
     * @param cost the cost of moving to that node
     */
    public void createConnection(Node toNode, float cost) {
        this.connections.add(new ConnectionImp(this, toNode, cost));
    }

    public void setTypeToRegular() {
        this.type = Type.getRegular();
    }

    public void setTypeToWall() {
        this.type = Type.getWall();
    }

    public int getType() {
        return type;
    }

    public static class Type {
        private static final int REGULAR = 1;
        private static final int Wall = 2;

        public static int getRegular() {
            return REGULAR;
        }

        public static int getWall() {
            return Wall;
        }
    }

    /**
     * Check if node is on a wall, if it is then the node will have no connections.
     *
     * @return boolean describing whether node collides with a wall.
     */
    public boolean collidesWithWalls() {
        Rectangle box = new Rectangle();
        box.set(x * 32, y * 32 , 32, 32);
        Array<RectangleMapObject> objects = this.map.getLayers().get("ObjWalls").getObjects().getByType(RectangleMapObject.class);
        for (int i = 0; i < objects.size; i++) {
            RectangleMapObject obj = objects.get(i);
            Rectangle rect = obj.getRectangle();
            if (box.overlaps(rect)) {
                box.set(0, 0 , 0, 0);
                return true;
            }
        }
        box.set(0, 0 , 0, 0);
        return false;
    }
}
