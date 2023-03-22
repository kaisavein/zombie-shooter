package packets;

import com.mygdx.game.Weapons.PistolBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * Packet for sending list of updated bullets to all connections.
 */
public class PacketSendUpdatedBullets extends Packet {

    private List<PistolBullet> updatedBullets = new LinkedList<>();

    public void setUpdatedBullets(List<PistolBullet> updatedBullets) {
        this.updatedBullets = updatedBullets;
    }

    public List<PistolBullet> getUpdatedBullets() {
        return updatedBullets;
    }
}
