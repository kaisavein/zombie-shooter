package packets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Packet for sending a map containing new Zombie instances (key - Zombie id, value - List of Zombie coordinates) to all connections.
 */
public class PacketNewZombies extends Packet {

    private Map<Integer, List<Float>> zombieMap = new HashMap<>();

    public void setZombieMap(Map<Integer, List<Float>> zombieMap) {
        this.zombieMap = zombieMap;
    }

    public Map<Integer, List<Float>> getZombieMap() {
        return zombieMap;
    }
}
