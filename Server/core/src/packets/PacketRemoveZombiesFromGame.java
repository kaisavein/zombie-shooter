package packets;

import java.util.List;

/**
 * Packet for sending a list of Zombie ids who are going to removed from the game.
 */
public class PacketRemoveZombiesFromGame extends Packet {

    private List<Integer> zombieIdList;

    public void setZombieIdList(List<Integer> zombieIdList) {
        this.zombieIdList = zombieIdList;
    }

    public List<Integer> getZombieIdList() {
        return zombieIdList;
    }
}
