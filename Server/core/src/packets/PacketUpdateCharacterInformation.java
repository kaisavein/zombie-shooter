package packets;

/**
 * Packet for sending info about updated PlayerGameCharacter instance to all connections.
 */
public class PacketUpdateCharacterInformation extends Packet {

    private String playerName;
    private int id;
    private float x;
    private float y;
    private String direction;
    private int health;

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getId() {
        return id;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
