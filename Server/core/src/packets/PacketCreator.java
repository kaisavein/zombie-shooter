package packets;

import com.mygdx.game.Weapons.PistolBullet;

import java.util.List;
import java.util.Map;

/**
 * Class that is used to create new Packets to keep the code readable in ClientConnection and ServerConnection.
 */
public class PacketCreator {

    /**
     * Create a PacketUpdateCharacterInformation.
     *
     * @param name of the player (String)
     * @param id of the player's connection (int)
     * @param xPos change of player's x coordinate (float)
     * @param yPos change of player's y coordinate (float)
     * @param direction player's current direction (String)
     * @param health player's amount of lives (int)
     * @return new PacketUpdateCharacterInformation
     */
    public static PacketUpdateCharacterInformation createPacketUpdateCharacterInformation(String name, int id, float xPos, float yPos, String direction, int health) {
        PacketUpdateCharacterInformation packetUpdateCharacterInformation = new PacketUpdateCharacterInformation();
        packetUpdateCharacterInformation.setPlayerName(name);
        packetUpdateCharacterInformation.setId(id);
        packetUpdateCharacterInformation.setX(xPos);
        packetUpdateCharacterInformation.setY(yPos);
        packetUpdateCharacterInformation.setDirection(direction);
        packetUpdateCharacterInformation.setHealth(health);
        return packetUpdateCharacterInformation;
    }

    /**
     * Create a PacketConnect.
     *
     * @param name of the player that wants to connect (String)
     * @return new PacketConnect
     */
    public static PacketConnect createPacketConnect(String name) {
        PacketConnect packetConnect = new PacketConnect();
        packetConnect.setPlayerName(name);
        return packetConnect;
    }

    /**
     * Create a PacketAddCharacter.
     *
     * @param name of the player (String)
     * @param id of the player's connection (int)
     * @param xPos of the player (float)
     * @param yPos of the player (float)
     * @return new PacketAddCharacter
     */
    public static PacketAddCharacter createPacketAddCharacter(String name, int id, float xPos, float yPos) {
        PacketAddCharacter packetAddCharacter = new PacketAddCharacter();
        packetAddCharacter.setPlayerName(name);
        packetAddCharacter.setId(id);
        packetAddCharacter.setX(xPos);
        packetAddCharacter.setY(yPos);
        return packetAddCharacter;
    }

    /**
     * Create a PacketBullet.
     *
     * @param name of the player (String)
     * @param id of the player's connection (int)
     * @param xPos of the bullet (float)
     * @param yPos of the bullet (float)
     * @param bulletTextureRegion of the bullet (String)
     * @param damage that the bullet can make (int)
     * @param direction of the bullet (String)
     * @return new PacketBullet
     */
    public static PacketBullet createPacketBullet(String name, int id, float xPos, float yPos, String bulletTextureRegion,
                                                  int damage, String direction) {
        PacketBullet packetBullet = new PacketBullet();
        packetBullet.setPlayerName(name);
        packetBullet.setId(id);
        packetBullet.setBulletXCoordinate(xPos);
        packetBullet.setBulletYCoordinate(yPos);
        packetBullet.setBulletTextureString(bulletTextureRegion);
        packetBullet.setDamage(damage);
        packetBullet.setMovingDirection(direction);
        return packetBullet;
    }

    /**
     * Create a PacketSendUpdatedBullets.
     *
     * @param updatedPistolBullets list of current PistolBullets in the world (List<PistolBullet>)
     * @return new PacketSendUpdatedBullets
     */
    public static PacketSendUpdatedBullets createPacketSendUpdatedBullets(List<PistolBullet> updatedPistolBullets) {
        PacketSendUpdatedBullets packetSendUpdatedBullets = new PacketSendUpdatedBullets();
        packetSendUpdatedBullets.setUpdatedBullets(updatedPistolBullets);
        return packetSendUpdatedBullets;
    }

    /**
     * Create a PacketNewZombies.
     *
     * @param newZombiesMap map of new Zombies (Map<Integer, List<Float>>)
     * @return new PacketNewZombies
     */
    public static PacketNewZombies createPacketNewZombies(Map<Integer, List<Float>> newZombiesMap) {
        PacketNewZombies packetNewZombies = new PacketNewZombies();
        packetNewZombies.setZombieMap(newZombiesMap);
        return packetNewZombies;
    }

    /**
     * Create a PacketUpdateZombies.
     *
     * @param zombieMap map of current zombies in the world (Map<Integer, List<Float>>)
     * @return new PacketUpdateZombies
     */
    public static PacketUpdateZombies createPacketUpdateZombies(Map<Integer, List<Float>> zombieMap) {
        PacketUpdateZombies packetZombies = new PacketUpdateZombies();
        packetZombies.setZombieMap(zombieMap);
        return packetZombies;
    }

    /**
     * Create a PacketRemoveZombiesFromGame.
     *
     * @param zombieIdList list of Zombie ids who are going to be removed from the game (List<Integer>)
     * @return new PacketRemoveZombiesFromGame
     */
    public static PacketRemoveZombiesFromGame createPacketRemoveZombiesFromGame(List<Integer> zombieIdList) {
        PacketRemoveZombiesFromGame packetRemoveZombiesFromGame = new PacketRemoveZombiesFromGame();
        packetRemoveZombiesFromGame.setZombieIdList(zombieIdList);
        return packetRemoveZombiesFromGame;
    }

    /**
     * Create a PacketClientDisconnect.
     *
     * @return new PacketClientDisconnect
     */
    public static PacketClientDisconnect createPacketClientDisconnect(int id) {
        PacketClientDisconnect packetClientDisconnect = new PacketClientDisconnect();
        packetClientDisconnect.setId(id);
        return packetClientDisconnect;
    }
}
