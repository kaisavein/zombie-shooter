package com.mygdx.game.World;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.AI.MapInfo.GraphGenerator;
import com.mygdx.game.AI.MapInfo.GraphImp;
import com.mygdx.game.Characters.PlayerGameCharacter;
import com.mygdx.game.Characters.Zombie;
import com.mygdx.game.TMXLoaders.HijackedTmxLoader;
import com.mygdx.game.TMXLoaders.MyServer;
import com.mygdx.game.Weapons.PistolBullet;


import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class World {

    private com.badlogic.gdx.physics.box2d.World gdxWorld;
    private GraphImp graph;
    private Map<Integer, PlayerGameCharacter> clients = new HashMap<>();
    private TiledMap tiledMap;
    MapLayer mapLayer;

    private List<PistolBullet> pistolBulletsInTheWorld = new LinkedList<>();
    private boolean updatingBullets = false;
    private Map<Integer, Zombie> zombieMap = new HashMap<>();
    private List<Integer> zombiesToBeRemoved = new ArrayList<>();
    private int score = 0;
    private boolean newWave = true;
    private int zombiesInWave = 4;
    private boolean isNewGame = false;

    /**
     * World constructor.
     */
    public World() throws IOException {
        Headless.loadHeadless(this);
        this.gdxWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, 0), true);
        this.gdxWorld.step(1/60f, 6, 2);
        initializeMap();
        initializeObjects();
    }

    /**
     * Initialize world map objects.
     */
    public void initializeObjects() {
        BodyDef bodyDef = new BodyDef();
        PolygonShape polygonShape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        Array<RectangleMapObject> objects = mapLayer.getObjects().getByType(RectangleMapObject.class);
        for (int i = 0; i < objects.size; i++) {
            RectangleMapObject obj = objects.get(i);
            Rectangle rect = obj.getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.height / 2);
            body = this.gdxWorld.createBody(bodyDef);
            polygonShape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fixtureDef.shape = polygonShape;
            body.createFixture(fixtureDef);
        }
    }

    public com.badlogic.gdx.physics.box2d.World getGdxWorld() {
        return gdxWorld;
    }

    public Map<Integer, Zombie> getZombieMap() {
        return zombieMap;
    }

    public void setNewWave(boolean newWave) {
        this.newWave = newWave;
    }

    public boolean isNewWave() {
        return newWave;
    }

    public int getZombiesInWave() {
        return zombiesInWave;
    }

    public List<Integer> getZombiesToBeRemoved() {
        return zombiesToBeRemoved;
    }

    /**
     * Get and empty zombiesToBeRemovedList.
     *
     * @return list of zombie ids before emptying the list.
     */
    public List<Integer> getAndEmptyZombiesToBeRemovedList() {
        List<Integer> zombiesToBeRemovedBeforeEmptyingList = zombiesToBeRemoved;
        zombiesToBeRemoved = new ArrayList<>();
        return zombiesToBeRemovedBeforeEmptyingList;
    }

    public MapLayer getMapLayer() {
        return mapLayer;
    }

    public GraphImp getGraph() {
        return graph;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public PlayerGameCharacter getGameCharacter(int id){
        return clients.get(id);
    }

    public Map<Integer, PlayerGameCharacter> getClients(){
        return clients;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public List<PistolBullet> getPistolBulletsInTheWorld() {
        return pistolBulletsInTheWorld;
    }

    public void setNewGame(boolean newGame) {
        isNewGame = newGame;
    }

    public boolean isNewGame() {
        return isNewGame;
    }

    public boolean isUpdatingBullets() {
        return updatingBullets;
    }

    public TiledMap getMap() {
        return new HijackedTmxLoader(new MyServer.MyFileHandleResolver())
                // Ubuntu: /home/ubuntu/iti0301-2021/Server/core/assets/DesertMap.tmx
                // Peeter's computer: C:/Users/37256/IdeaProjects/iti0301-2021/Server/core/assets/DesertMap.tmx"
                // Your computer: Server/core/src/com/mygdx/game/World/DesertMap.tmx
                .load("Server/core/src/com/mygdx/game/World/DesertMap.tmx");
    }

    /**
     * Initialize world map.
     */
    public void initializeMap() {
        this.tiledMap = getMap();
        this.mapLayer = tiledMap.getLayers().get("ObjWalls");
        this.graph = GraphGenerator.generateGraph(tiledMap);
    }

    /**
     * Add a new PlayerGameCharacter to the clients map.
     *
     * @param id of the PlayerGameCharacter and connection whose the playerGameCharacter is
     * @param gameCharacter new PlayerGamCharacter
     */
    public void addGameCharacter(Integer id, PlayerGameCharacter gameCharacter){
        clients.put(id, gameCharacter);
    }

    /**
     * Remove a client from the clients map.
     *
     * @param id of the PlayerGameCharacter and connection
     */
    public void removeClient(int id){
        clients.remove(id);
    }

    /**
     * Get a list of clients ids.
     *
     * @return list of ids (List<Integer>)
     */
    public List<Integer> getClientsIds() {
        return new LinkedList<>(clients.keySet());
    }

    /**
     * Move the PlayerGameCharacter and update PlayerGameCharacter's direction.
     *
     * @param id of the PlayerGameCharacter
     * @param xPosChange how much the x coordinate has changed
     * @param yPosChange how much the y coordinate has changed
     */
    public void movePlayerGameCharacter(int id, float xPosChange, float yPosChange) {
        PlayerGameCharacter character = getGameCharacter(id);
        if (character != null) {
            character.moveToNewPos(xPosChange, yPosChange);
            if (xPosChange == 0 && yPosChange > 0) {
                character.setCharacterDirection("up");
                character.getPlayerCharacterCurrentPistol().getBulletStraight().setDirection("up");
                character.getPlayerCharacterCurrentShotgun().setBulletsDirections("up");
            } else if (xPosChange < 0 && yPosChange == 0) {
                character.setCharacterDirection("left");
                character.getPlayerCharacterCurrentPistol().getBulletStraight().setDirection("left");
                character.getPlayerCharacterCurrentShotgun().setBulletsDirections("left");
            } else if (xPosChange == 0 && yPosChange < 0) {
                character.setCharacterDirection("down");
                character.getPlayerCharacterCurrentPistol().getBulletStraight().setDirection("down");
                character.getPlayerCharacterCurrentShotgun().setBulletsDirections("down");
            } else if (xPosChange > 0 && yPosChange == 0) {
                character.setCharacterDirection("right");
                character.getPlayerCharacterCurrentPistol().getBulletStraight().setDirection("right");
                character.getPlayerCharacterCurrentShotgun().setBulletsDirections("right");
            } else if (xPosChange < 0 && yPosChange > 0) {
                character.setCharacterDirection("up-left");
                character.getPlayerCharacterCurrentPistol().getBulletStraight().setDirection("up-left");
                character.getPlayerCharacterCurrentShotgun().setBulletsDirections("up-left");
            } else if (xPosChange > 0 && yPosChange > 0) {
                character.setCharacterDirection("up-right");
                character.getPlayerCharacterCurrentPistol().getBulletStraight().setDirection("up-right");
                character.getPlayerCharacterCurrentShotgun().setBulletsDirections("up-right");
            } else if (xPosChange < 0 && yPosChange < 0) {
                character.setCharacterDirection("down-left");
                character.getPlayerCharacterCurrentPistol().getBulletStraight().setDirection("down-left");
                character.getPlayerCharacterCurrentShotgun().setBulletsDirections("down-left");
            } else if (xPosChange > 0 && yPosChange < 0) {
                character.setCharacterDirection("down-right");
                character.getPlayerCharacterCurrentPistol().getBulletStraight().setDirection("down-right");
                character.getPlayerCharacterCurrentShotgun().setBulletsDirections("down-right");
            }
        }
    }

    /**
     * Add a PistolBullet to the list of world's current PistolBullets.
     *
     * @param pistolBullet given bullet
     */
    public void addBullet(PistolBullet pistolBullet) {
        if (!updatingBullets) {
            pistolBulletsInTheWorld.add(pistolBullet);
        }
    }

    /**
     * Update PistolBullets that are currently in the world.
     */
    public void updateBulletsInTheWorldList() {
        updatingBullets = true;
        pistolBulletsInTheWorld = pistolBulletsInTheWorld.stream()
                .filter(pistolBullet -> {
                    pistolBullet.movePistolBullet();
                    return pistolBullet.checkIfPistolBulletIsInWorld()
                            && detectBulletCollisionBetweenPlayerAndBullet(pistolBullet)
                            && detectBulletCollisionBetweenZombieAndBullet(pistolBullet)
                            && pistolBullet.collidesWithWalls(mapLayer);
                }).collect(Collectors.toList());
        updatingBullets = false;
    }

    /**
     * Detect if bullet has collided with a PlayerGameCharacter.
     *
     * @param pistolBullet given bullet
     * @return boolean describing whether bullet collides with a PlayerGamCharacter
     */
    public boolean detectBulletCollisionBetweenPlayerAndBullet(PistolBullet pistolBullet) {
        ArrayList<PlayerGameCharacter> clientsValues = new ArrayList<>(clients.values());
        for (int i = 0; i < clientsValues.size(); i++) {
            PlayerGameCharacter playerGameCharacter = clientsValues.get(i);
            if (playerGameCharacter.collidesWithPistolBullet(pistolBullet)) {
                return false;
            }
        } return true;
    }

    /**
     * Detect if collides with a Zombie.
     *
     * @param pistolBullet given bullet
     * @return boolean describing whether bullet collides with a Zombie
     */
    public boolean detectBulletCollisionBetweenZombieAndBullet(PistolBullet pistolBullet) {
        return zombieMap.values().stream().filter(zombie -> zombie.collidesWithPistolBullet(pistolBullet)).toArray().length == 0;
    }

    /**
     * Add a Zombie to the server's zombies map.
     *
     * @param id of the Zombie
     * @param zombie given Zombie
     */
    public void addZombieToServerWorldMap(Integer id, Zombie zombie) {
        if (!zombieMap.containsKey(id)) {
            zombieMap.put(id, zombie);
        }
    }

    /**
     * Create 4 new zombies.
     *
     * @return list of the new zombies
     */
    public List<Zombie> createZombies() {
        Zombie zombie = Zombie.createZombie(175f, 240f, this);
        Zombie zombie2 = Zombie.createZombie(190f, 240f, this);
        Zombie zombie3 = Zombie.createZombie(420f, 450f, this);
        Zombie zombie4 = Zombie.createZombie(435f, 450f, this);
        List<Zombie> zombies = new LinkedList<>();
        zombies.add(zombie);
        zombies.add(zombie2);
        zombies.add(zombie3);
        zombies.add(zombie4);
        return zombies;
    }

    /**
     * Update Zombies that are currently in the world.
     */
    public void updateZombiesInTheWorldZombieMap() {
        Set<Integer> allZombies = zombieMap.keySet();
        zombieMap = zombieMap.entrySet()
                .stream()
                .filter(zombieEntry -> {
                    zombieEntry.getValue().findNextNode();
                    zombieEntry.getValue().findNextXAndY();
                    zombieEntry.getValue().moveToNewPos(zombieEntry.getValue().getDeltaX(), zombieEntry.getValue().getDeltaY());
                    return zombieEntry.getValue().zombieHasLives();
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (zombieMap.isEmpty()) {
            setNewWave(true);
            zombiesInWave += 4;
        }
        zombiesToBeRemoved = allZombies.stream().filter(id -> !zombieMap.containsKey(id)).collect(Collectors.toList());
    }

    /**
     * Reset World instance variables.
     */
    public void restartWorld() {
        clients.clear();
        pistolBulletsInTheWorld.clear();
        zombieMap.clear();
        zombiesToBeRemoved.clear();
        score = 0;
        newWave = true;
        zombiesInWave = 4;
        isNewGame = false;
    }
}
