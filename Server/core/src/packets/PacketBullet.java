package packets;

/**
 * Packet that is used to send information about a PistolBullet when a client shoots a new bullet.
 */
public class PacketBullet extends Packet {

    private String playerName;
    private int id;
    private float bulletXCoordinate;
    private float bulletYCoordinate;
    private String bulletTextureString;
    private int damage;
    private String movingDirection;


    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBulletXCoordinate(float bulletXCoordinate) {
        this.bulletXCoordinate = bulletXCoordinate;
    }

    public void setBulletYCoordinate(float bulletYCoordinate) {
        this.bulletYCoordinate = bulletYCoordinate;
    }

    public void setMovingDirection(String direction) {
        this.movingDirection = direction;
    }

    public void setBulletTextureString(String bulletTextureString) {
        this.bulletTextureString = bulletTextureString;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getId() {
        return id;
    }

    public float getBulletXCoordinate() {
        return bulletXCoordinate;
    }

    public float getBulletYCoordinate() {
        return bulletYCoordinate;
    }

    public String getMovingDirection() {
        return movingDirection;
    }

    public String getBulletTextureString() {
        return bulletTextureString;
    }

    public int getDamage() {
        return damage;
    }
}
