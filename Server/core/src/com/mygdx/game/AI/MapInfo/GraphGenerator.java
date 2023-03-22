package com.mygdx.game.AI.MapInfo;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;


public class GraphGenerator {

    /**
     * Method generates a graph with nodes from tile map.
     *
     * @param map that nodes are created on.
     * @return Graph that has a array of nodes.
     */
    public static GraphImp generateGraph(TiledMap map) {
        Array<Node> nodes = new Array<>();

        // Get the correct tile layer.
        TiledMapTileLayer tiles = (TiledMapTileLayer) map.getLayers().get("Paths");

        // Width and height of the map in tiles.
        int mapHeight = map.getProperties().get("height", Integer.class);
        int mapWidth = map.getProperties().get("width", Integer.class);

        // Creates nodes on map whose x and y are corresponding to map tile x and y -s.
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                // Creates a node with x, y and indexes and that node.
                Node node = new Node(map, x , y );
                if (!node.collidesWithWalls()) {
                    node.setTypeToRegular();
                } else {
                    node.setTypeToWall();
                }
                nodes.add(node);
            }
        }

        // Creates connections form nodes to other nodes.
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                TiledMapTileLayer.Cell target = tiles.getCell(x, y);
                TiledMapTileLayer.Cell up = tiles.getCell(x, y + 1);
                TiledMapTileLayer.Cell left = tiles.getCell(x - 1, y);
                TiledMapTileLayer.Cell right = tiles.getCell(x + 1, y);
                TiledMapTileLayer.Cell down = tiles.getCell(x, y - 1);

                Node targetNode = nodes.get(mapWidth * y + x);
                if (target == null && targetNode.getType() == Node.Type.getRegular()) {
                    if (y != 0 && down == null) {
                        Node downNode = nodes.get(mapWidth * (y - 1) + x);
                        if (downNode.getType() == Node.Type.getRegular()) {
                            targetNode.createConnection(downNode, 1);
                        }
                    }
                    if (x != 0 && left == null) {
                        Node leftNode = nodes.get(mapWidth * y + x - 1);
                        if (leftNode.getType() == Node.Type.getRegular()) {
                            targetNode.createConnection(leftNode, 1);
                        }
                    }
                    if (x != mapWidth - 1 && right == null) {
                        Node rightNode = nodes.get(mapWidth * y + x + 1);
                        if (rightNode.getType() == Node.Type.getRegular()) {
                            targetNode.createConnection(rightNode, 1);
                        }
                    }
                    if (y != mapHeight - 1 && up == null) {
                        Node upNode = nodes.get(mapWidth * (y + 1) + x);
                        if (upNode.getType() == Node.Type.getRegular()) {
                            targetNode.createConnection(upNode, 1);
                        }

                    }
                }
            }
        }
        return new GraphImp(nodes);
    }
}
