package com.mygdx.game.GameInfo;

import ClientConnection.ClientConnection;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Characters.PlayerGameCharacter;
import com.mygdx.game.Characters.Zombie;
import com.mygdx.game.Weapons.Pistol;
import com.mygdx.game.Weapons.PistolBullet;
import com.mygdx.game.Weapons.Shotgun;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Arrays;


public class ClientWorld {

    private ClientConnection clientConnection;
    private PlayerGameCharacter myPlayerGameCharacter;
    private final HashMap<Integer, PlayerGameCharacter> worldGameCharactersMap = new HashMap<>();
    private List<PistolBullet> pistolBullets = new LinkedList<>();
    private Map<Integer, Zombie> zombieMap = new HashMap<>();
    private int score = 0;
    private int waveCount = 0;

    /**
     * This adds the instance of ClientConnection to this class.
     */
    public void registerClient(ClientConnection clientConnection){
        this.clientConnection = clientConnection;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setWaveCount(int waveCount) {
        this.waveCount = waveCount;
    }

    public int getWaveCount() {
        return waveCount;
    }

    public void setMyPlayerGameCharacter(PlayerGameCharacter myPlayerGameCharacter) {
        this.myPlayerGameCharacter = myPlayerGameCharacter;
    }

    public PlayerGameCharacter getMyPlayerGameCharacter() {
        return myPlayerGameCharacter;
    }

    /**
     * Map of clients and their PlayerGameCharacters.
     *
     * Key: id, value: PlayerGameCharacter
     */
    public HashMap<Integer, PlayerGameCharacter> getWorldGameCharactersMap() {
        return worldGameCharactersMap;
    }

    public PlayerGameCharacter getGameCharacter(Integer id){
        return worldGameCharactersMap.get(id);
    }

    public List<PistolBullet> getPistolBullets() {
        return pistolBullets;
    }

    public Map<Integer, Zombie> getZombieHashMap() {
        return zombieMap;
    }

    /**
     * Add a PlayerGameCharacter to the characters map.
     *
     * @param id of the PlayerGameCharacter
     * @param newCharacter PlayerGameCharacter
     */
    public void addGameCharacter(Integer id, PlayerGameCharacter newCharacter) {
        worldGameCharactersMap.put(id, newCharacter);
    }

    /**
     * This moves the PlayerGameCharacter by changing  x and y coordinates of set character.
     *
     * Also updates PlayerGameCharacter's weapons coordinates.
     * @param id of the moving character - id is key in worldGameCharactersMap.
     * @param xPosChange the change of x
     * @param yPosChange the change of y
     * @param direction direction that the character is faceing
     * @param health of the character
     */
    public void movePlayerGameCharacter(Integer id, float xPosChange, float yPosChange, String direction, int health) {
        getGameCharacter(id).moveToNewPos(xPosChange, yPosChange);
        getGameCharacter(id).setPlayerDirection(direction);
        getGameCharacter(id).getPlayerCharacterCurrentPistol().getBulletStraight().setDirection(direction);
        getGameCharacter(id).getPlayerCharacterCurrentShotgun().getBulletStraight().setDirection(direction);
        getGameCharacter(id).getPlayerCharacterCurrentShotgun().getBulletLeft().setDirection(getGameCharacter(id).getPlayerCharacterCurrentShotgun().setBulletLeftDirection(direction));
        getGameCharacter(id).getPlayerCharacterCurrentShotgun().getBulletRight().setDirection(getGameCharacter(id).getPlayerCharacterCurrentShotgun().setBulletLeftDirection(direction));
        getGameCharacter(id).setTextureForDirection();
        getGameCharacter(id).setHealth(health);
    }

    /**
     * Method that updates the world's current PistolBullets.
     *
     * @param newPistolBullets list of updated PistolBullets
     */
    public void removeAndUpdateWorldPistolBulletList(List<PistolBullet> newPistolBullets) {
        if (!clientConnection.getGameScreen().isRenderingBullets()) {
            pistolBullets.clear();
            pistolBullets = newPistolBullets;
        }
    }

    /**
     * Makes a new Zombie and adds it to client's world map.
     *
     * @param id of zombie character
     * @param x coordinate of zombie
     * @param y coordinate of zombie
     */
    public void makeAndAddZombieToClientWorldMap(Integer id, Float x, Float y) {
        if (!zombieMap.containsKey(id)) {
            Rectangle boundingBox = new Rectangle(x, y, 10f, 10f);
            Zombie zombie = new Zombie(1f, boundingBox, x, y, 10f, 10f);
            zombie.setZombieId(id);
            zombieMap.put(id, zombie);
        }
    }

    /**
     * Removes a dead zombie from client's map.
     *
     * @param id of the given zombie
     */
    public void removeZombieFromClientWorldMap(Integer id) {
        zombieMap.remove(id);
    }

    /**
     * Adds Zombies to client's world map.
     *
     * @param zombieInfoMap map that holds zombies info (key: zombie's id; value: coordinates) in game.
     */
    public void addZombiesToClientWorldMap(Map<Integer, List<Float>> zombieInfoMap) {
        if (!zombieInfoMap.isEmpty()) {
            zombieInfoMap.forEach((key, value) -> makeAndAddZombieToClientWorldMap(key, value.get(0), value.get(1)));
        }
    }

    /**
     * Method moves zombies on the map.
     *
     * @param updatedZombieCoordinatesMap zombies' new coordinates (key: id; value: coordinates)
     */
    public void updateZombiesInClientWorld(Map<Integer, List<Float>> updatedZombieCoordinatesMap) {
        for (Map.Entry<Integer, Zombie> entry: zombieMap.entrySet()) {
            entry.getValue().moveToNewPos(updatedZombieCoordinatesMap.getOrDefault(entry.getKey(),
                    Arrays.asList(0f)).get(0), updatedZombieCoordinatesMap.getOrDefault(entry.getKey(), Arrays.asList(0f, 0f)).get(1));
        }
    }

    /**
     * Updates bullet's location.
     *
     * Method is used to update PlayerGameCharacter's bullet, that has not been shot, coordinates.
     * @param id of character that has the bullet
     * @param xPos of the bullet
     * @param yPos of the bullet
     * @param direction of the bullets course
     */
    public void updateBulletLocation(int id, float xPos, float yPos, String direction) {
        float newXPos = xPos + getGameCharacter(id).getBoundingBox().getWidth() / 2f;
        float newYPos = yPos + getGameCharacter(id).getBoundingBox().getHeight() / 2f;
        Pistol pistol = getGameCharacter(id).getPlayerCharacterCurrentPistol();
        Shotgun shotgun = getGameCharacter(id).getPlayerCharacterCurrentShotgun();
        switch (direction) {
            case "up-right":
                pistol.getBulletStraight().getBoundingBox().setPosition(newXPos + 3f, newYPos + 5f);
                shotgun.updateBulletsLocation(newXPos + 6f, newYPos + 5f, newXPos + 3f,
                        newYPos + 5f, newXPos + 5.5f, newYPos + 7f);
                break;
            case "down-right":
                pistol.getBulletStraight().getBoundingBox().setPosition(newXPos + 3f, newYPos - 10f);
                shotgun.updateBulletsLocation(newXPos + 6f, newYPos - 10f, newXPos + 3f,
                        newYPos - 10f, newXPos + 4f, newYPos - 10f);
                break;
            case "up-left":
                pistol.getBulletStraight().getBoundingBox().setPosition(newXPos - 6.5f, newYPos + 5f);
                shotgun.updateBulletsLocation(newXPos - 8f, newYPos + 7.5f, newXPos - 6.5f,
                        newYPos + 5f, newXPos - 9f, newYPos + 5f);
                break;
            case "down-left":
                pistol.getBulletStraight().getBoundingBox().setPosition(newXPos - 8f, newYPos - 10f);
                shotgun.updateBulletsLocation(newXPos - 10f, newYPos - 10f, newXPos - 8f,
                        newYPos - 10f, newXPos - 10f, newYPos - 10f);
                break;
            case "up":
                pistol.getBulletStraight().getBoundingBox().setPosition(newXPos + 1.55f, newYPos + 5f);
                shotgun.updateBulletsLocation(newXPos + 3f, newYPos + 5f, newXPos + 1.55f,
                        newYPos + 5f, newXPos, newYPos + 5f);
                break;
            case "left":
                pistol.getBulletStraight().getBoundingBox().setPosition(newXPos - 10f, newYPos - 4f);
                shotgun.updateBulletsLocation(newXPos - 10f, newYPos - 2f, newXPos - 10f,
                        newYPos - 4f, newXPos - 10f, newYPos - 5f);
                break;
            case "right":
                pistol.getBulletStraight().getBoundingBox().setPosition(newXPos + 5f, newYPos - 4f);
                shotgun.updateBulletsLocation(newXPos + 5f, newYPos - 5f, newXPos + 5f,
                        newYPos - 4f, newXPos + 5f, newYPos - 2f);
                break;
            case "down":
                pistol.getBulletStraight().getBoundingBox().setPosition(newXPos - 5.7f, newYPos - 10f);
                shotgun.updateBulletsLocation(newXPos - 7f, newYPos - 10f, newXPos - 5.7f,
                        newYPos - 10f, newXPos - 5f, newYPos - 10f);
                break;
        }
    }
}
