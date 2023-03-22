package com.mygdx.game.Characters;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.AI.Algorithm.AStarPathFinding;
import com.mygdx.game.AI.MapInfo.Node;
import com.mygdx.game.World.World;

import java.util.ArrayList;

public class Zombie extends GameCharacter {

    private int zombieId;
    private static int zombiesCreated, playerGameCharacterIdIndex;
    private AStarPathFinding aStarPathFinding;
    private int playerGameCharacterId;  // Zombie starts searching PlayerGameCharacter with the given id.
    private Node currentNode, nextNode;
    private float deltaX, deltaY;
    private direction xMovingDirection = direction.NULL,
            yMovingDirection = direction.NULL,
            movingDirection = direction.NULL;
    private World world;
    private enum direction {
        LEFT, RIGHT, UP, DOWN, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT, NULL
    }

    private static final int ZOMBIE_HEALTH = 10;
    private static final int MAX_PLAYER_GAME_CHARACTER_ID = 3;
    private static final float PLUS_MOVEMENT_COORDINATE = 0.35f;
    private static final float MINUS_MOVEMENT_COORDINATE = -0.35f;

    /**
     * Zombie class constructor.
     *
     * @param movementSpeed of the Zombie (float)
     * @param boundingBox encapsulates a 2D rectangle(bounding box) for the Zombie (Rectangle)
     * @param xPosition of the Zombie (float)
     * @param yPosition of the Zombie (float)
     * @param width of the Zombie (float)
     * @param height of the Zombie (float)
     * @param world where the Zombie is (World)
     */
    public Zombie(float movementSpeed, Rectangle boundingBox, float xPosition, float yPosition, float width,
                  float height, World world) {
        super(movementSpeed, boundingBox, xPosition, yPosition, width, height, world);
        this.movementSpeed = movementSpeed;
        this.boundingBox = boundingBox;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.world = world;
        this.health = ZOMBIE_HEALTH;
        this.zombieId = getAndIncrementNextId();
        this.playerGameCharacterId = findPlayerGameCharacterId();
        this.aStarPathFinding = new AStarPathFinding(world, this, world.getGameCharacter(playerGameCharacterId));
    }

    /**
     * Static method for creating a new Zombie instance.
     *
     * @param x coordinate of the Zombie (float)
     * @param y coordinate of the Zombie (float)
     * @param world where the Zombie is (World)
     * @return new Zombie instance
     */
    public static Zombie createZombie(float x, float y, World world) {
        Rectangle boundingBox = new Rectangle(x, y, 10f, 10f);
        Zombie zombie = new Zombie(1f, boundingBox, x, y, 10f, 10f, world);
        world.addZombieToServerWorldMap(zombie.getZombieId(), zombie);
        return zombie;
    }

    public void setCurrentNode(Node currentNode) {
        this.currentNode = currentNode;
    }

    public int getZombieId() {
        return zombieId;
    }

    /**
     * Get Zombie's id.
     *
     * @return Zombie id (int)
     */
    public static int getAndIncrementNextId() {
        return ++zombiesCreated;
    }

    public float getDeltaX() {
        return deltaX;
    }

    public float getDeltaY() {
        return deltaY;
    }

    /**
     * Method for checking whether Zombie instance has lives.
     *
     * @return boolean describing whether Zombie instance has health above 0.
     */
    public boolean zombieHasLives() {
        return health > 0;
    }

    /**
     * Get playerGameCharacter's id.
     *
     * First Zombie will search for the PlayerGameCharacter with id 1. Second Zombie will search for
     * the PlayerGameCharacter with id 2. Third with id 3. Then the playerGameCharacterIdCount will reset
     * and fourth Zombie will search for PlayerGameCharacter with id 1 and fifth with id 2 and so on.
     * @return PlayerGameCharacter's id (int)
     */
    public int getPlayerGameCharacterId() {
        int id = world.getClientsIds().get(playerGameCharacterIdIndex);
        ++playerGameCharacterIdIndex;
        if (playerGameCharacterIdIndex >= MAX_PLAYER_GAME_CHARACTER_ID) {
            playerGameCharacterIdIndex = 0;
        }
        return id;
    }

    /**
     * Find PlayerGameCharacter's id who the Zombie should search for.
     *
     * If game has three player (multiplayer) then each Zombie has a different PlayerGameCharacter who to search for.
     * @return id of the PlayerGameCharacter (int)
     */
    public int findPlayerGameCharacterId() {
        int id = 1;
        if (world.getClients().size() == MAX_PLAYER_GAME_CHARACTER_ID) {
            id = getPlayerGameCharacterId();
        }
        return id;
    }

    /**
     * AStarPathFinding instance calculates path from Zombie's node to PlayerGameCharacter's node.
     *
     * Method takes a node (node where the Zombie should go next) from the calculated path and sets it
     * to Zombie's nextNode.
     */
    public void findNextNode() {
        aStarPathFinding.calculatePath();
        if (aStarPathFinding.getSolutionGraphPath().getCount() == 1) {
            nextNode = currentNode;
        } else if (aStarPathFinding.getSolutionGraphPath().getCount() > 1) {
            nextNode = aStarPathFinding.getSolutionGraphPath().get(1);
        }
    }

    /**
     * Method find's values for deltaX and deltaY according to Zombie's current node and Zombie's direction.
     */
    public void findNextXAndY() {
        if (nextNode != null && currentNode != null) {
            if (nextNode.equals(currentNode)) {
                switch (xMovingDirection) {
                    case LEFT:
                        deltaX = MINUS_MOVEMENT_COORDINATE;
                        break;
                    case RIGHT:
                        deltaX = PLUS_MOVEMENT_COORDINATE;
                        break;
                    default:
                        deltaX = 0f;
                        break;
                }
                switch (yMovingDirection) {
                    case DOWN:
                        deltaY = MINUS_MOVEMENT_COORDINATE;
                        break;
                    case UP:
                        deltaY = PLUS_MOVEMENT_COORDINATE;
                        break;
                    default:
                        deltaY = 0f;
                        break;
                }
                if (deltaX == PLUS_MOVEMENT_COORDINATE && deltaY == PLUS_MOVEMENT_COORDINATE) {
                    movingDirection = direction.UP_RIGHT;
                } else if (deltaX == PLUS_MOVEMENT_COORDINATE && deltaY == MINUS_MOVEMENT_COORDINATE) {
                    movingDirection = direction.DOWN_RIGHT;
                } else if (deltaX == MINUS_MOVEMENT_COORDINATE && deltaY == PLUS_MOVEMENT_COORDINATE) {
                    movingDirection = direction.UP_LEFT;
                } else if (deltaX == MINUS_MOVEMENT_COORDINATE && deltaY == MINUS_MOVEMENT_COORDINATE) {
                    movingDirection = direction.DOWN_LEFT;
                }

            } else {
                moveZombie();

            } if (zombieCollidesWithZombie(boundingBox.getX() + deltaX, boundingBox.getY() + deltaY)) {
                moveZombieDoesNotCollide();
            }
        }
    }

    /**
     * Set deltaX, deltaY and direction according to Zombie's next node.
     */
    public void moveZombie() {
        if (nextNode.getX() < currentNode.getX()) {  // Moves left.
            deltaX = MINUS_MOVEMENT_COORDINATE;
            xMovingDirection = direction.LEFT;
            movingDirection = direction.LEFT;
        } else if (nextNode.getX() > currentNode.getX()) {  // Moves right.
            deltaX = PLUS_MOVEMENT_COORDINATE;
            xMovingDirection = direction.RIGHT;
            movingDirection = direction.RIGHT;
        } else {
            deltaX = 0f;
        }

        if (nextNode.getY() < currentNode.getY()) {  // Moves down.
            deltaY = MINUS_MOVEMENT_COORDINATE;
            yMovingDirection = direction.DOWN;
            movingDirection = direction.DOWN;
        } else if (nextNode.getY() > currentNode.getY()) {  // Moves up.
            deltaY = PLUS_MOVEMENT_COORDINATE;
            yMovingDirection = direction.UP;
            movingDirection = direction.UP;
        } else {
            deltaY = 0f;
        }
    }

    /**
     * Method that describes whether Zombie collides with another Zombie.
     *
     * @param x coordinate of the Zombie (float)
     * @param y coordinate of the Zombie (float)
     * @return boolean describing whether Zombie collides with another Zombie.
     */
    public boolean zombieCollidesWithZombie(float x, float y) {
        Rectangle theoreticalBoundingBox = new Rectangle(x, y, width, height);
        ArrayList<Zombie> zombieValues = new ArrayList<>(world.getZombieMap().values());
        for (int i = 0; i < zombieValues.size(); i++) {
            Zombie zombie = zombieValues.get(i);
            if (zombie.getBoundingBox().overlaps(theoreticalBoundingBox) && zombie != this) {
                return true;
            }
        }
        return false;
    }

    /**
     * If Zombie collides with another Zombie after moving to new position, then find new coordinates for the Zombie
     * according to Zombie's direction.
     */
    public void moveZombieDoesNotCollide() {
        if (movingDirection.equals(direction.UP_RIGHT)) {
            moveUpRight();
        } else if (movingDirection.equals(direction.UP_LEFT)) {
            moveUpLeft();
        } else if (movingDirection.equals(direction.DOWN_RIGHT)) {
            moveDownRight();
        } else if (movingDirection.equals(direction.DOWN_LEFT)) {
            moveDownLeft();
        } else if (movingDirection.equals(direction.UP)) {
            moveUp();
        } else if (movingDirection.equals(direction.DOWN)) {
            moveDown();
        } else if (movingDirection.equals(direction.LEFT)) {
            moveLeft();
        } else if (movingDirection.equals(direction.RIGHT)) {
            moveRight();
        }
    }

    /**
     * Move zombie up-right so that Zombie does not collide with another Zombie.
     */
    private void moveUpRight() {
        if (!zombieCollidesWithZombie(boundingBox.getX() + deltaX, boundingBox.getY())) {
            deltaX = PLUS_MOVEMENT_COORDINATE;
            deltaY = 0f;
            movingDirection = direction.RIGHT;
            xMovingDirection = direction.RIGHT;
        } else if (!zombieCollidesWithZombie(boundingBox.getX(), boundingBox.getY() + deltaY)) {
            deltaY = PLUS_MOVEMENT_COORDINATE;
            deltaX = 0f;
            movingDirection = direction.UP;
            yMovingDirection = direction.UP;
        } else if (!zombieCollidesWithZombie(boundingBox.getX() + PLUS_MOVEMENT_COORDINATE,
                boundingBox.getY() + MINUS_MOVEMENT_COORDINATE)) {
            deltaX = PLUS_MOVEMENT_COORDINATE;
            deltaY = MINUS_MOVEMENT_COORDINATE;
            movingDirection = direction.DOWN_RIGHT;
            xMovingDirection = direction.RIGHT;
            yMovingDirection = direction.DOWN;
        } else if (!zombieCollidesWithZombie(boundingBox.getX() + MINUS_MOVEMENT_COORDINATE, boundingBox.getY())) {
            deltaX = MINUS_MOVEMENT_COORDINATE;
            deltaY = 0f;
            movingDirection = direction.LEFT;
            xMovingDirection = direction.LEFT;
        } else if (!zombieCollidesWithZombie(boundingBox.getX(), boundingBox.getY() + MINUS_MOVEMENT_COORDINATE)) {
            deltaY = MINUS_MOVEMENT_COORDINATE;
            deltaX = 0f;
            movingDirection = direction.DOWN;
            yMovingDirection = direction.DOWN;
        }
    }

    /**
     * Move zombie up-left so that Zombie does not collide with another Zombie.
     */
    private void moveUpLeft() {
        if (!zombieCollidesWithZombie(boundingBox.getX() + deltaX, boundingBox.getY())) {
            deltaX = MINUS_MOVEMENT_COORDINATE;
            deltaY = 0f;
            movingDirection = direction.LEFT;
            xMovingDirection =direction.LEFT;
        } else if (!zombieCollidesWithZombie(boundingBox.getX(), boundingBox.getY() + deltaY)) {
            deltaY = PLUS_MOVEMENT_COORDINATE;
            deltaX = 0f;
            movingDirection = direction.UP;
            yMovingDirection = direction.UP;
        } else if (!zombieCollidesWithZombie(boundingBox.getX() + MINUS_MOVEMENT_COORDINATE,
                boundingBox.getY() + MINUS_MOVEMENT_COORDINATE)) {
            deltaX = MINUS_MOVEMENT_COORDINATE;
            deltaY = MINUS_MOVEMENT_COORDINATE;
            movingDirection = direction.DOWN_LEFT;
            xMovingDirection = direction.LEFT;
            yMovingDirection = direction.DOWN;
        } else if (!zombieCollidesWithZombie(boundingBox.getX() + PLUS_MOVEMENT_COORDINATE, boundingBox.getY())) {
            deltaX = PLUS_MOVEMENT_COORDINATE;
            deltaY = 0f;
            movingDirection = direction.RIGHT;
            xMovingDirection = direction.RIGHT;
        } else if (!zombieCollidesWithZombie(boundingBox.getX(), boundingBox.getY() + MINUS_MOVEMENT_COORDINATE)) {
            deltaY = MINUS_MOVEMENT_COORDINATE;
            deltaX = 0f;
            movingDirection = direction.DOWN;
            yMovingDirection = direction.DOWN;
        }
    }

    /**
     * Move zombie down-right so that Zombie does not collide with another Zombie.
     */
    private void moveDownRight() {
        if (!zombieCollidesWithZombie(boundingBox.getX() + deltaX, boundingBox.getY())) {
            deltaX = PLUS_MOVEMENT_COORDINATE;
            deltaY = 0f;
            movingDirection = direction.RIGHT;
            xMovingDirection = direction.RIGHT;
        } else if (!zombieCollidesWithZombie(boundingBox.getX(), boundingBox.getY() + deltaY)) {
            deltaY = MINUS_MOVEMENT_COORDINATE;
            deltaX = 0f;
            movingDirection = direction.DOWN;
            yMovingDirection = direction.DOWN;
        } else if (!zombieCollidesWithZombie(boundingBox.getX() + PLUS_MOVEMENT_COORDINATE,
                boundingBox.getY() + PLUS_MOVEMENT_COORDINATE)) {
            deltaX = PLUS_MOVEMENT_COORDINATE;
            deltaY = PLUS_MOVEMENT_COORDINATE;
            movingDirection = direction.UP_RIGHT;
            xMovingDirection = direction.RIGHT;
            yMovingDirection = direction.UP;
        } else if (!zombieCollidesWithZombie(boundingBox.getX() + MINUS_MOVEMENT_COORDINATE, boundingBox.getY())) {
            deltaX = MINUS_MOVEMENT_COORDINATE;
            deltaY = 0f;
            movingDirection = direction.LEFT;
            xMovingDirection = direction.LEFT;
        } else if (!zombieCollidesWithZombie(boundingBox.getX(), boundingBox.getY() + PLUS_MOVEMENT_COORDINATE)) {
            deltaY = PLUS_MOVEMENT_COORDINATE;
            deltaX = 0f;
            movingDirection = direction.UP;
            yMovingDirection = direction.UP;
        }
    }

    /**
     * Move zombie down-left so that Zombie does not collide with another Zombie.
     */
    private void moveDownLeft() {
        if (!zombieCollidesWithZombie(boundingBox.getX() + deltaX, boundingBox.getY())) {
            deltaX = MINUS_MOVEMENT_COORDINATE;
            deltaY = 0f;
            movingDirection = direction.LEFT;
            xMovingDirection = direction.LEFT;
        } else if (!zombieCollidesWithZombie(boundingBox.getX(), boundingBox.getY() + deltaY)) {
            deltaY = MINUS_MOVEMENT_COORDINATE;
            deltaX = 0f;
            movingDirection = direction.DOWN;
            yMovingDirection = direction.DOWN;
        } else if (!zombieCollidesWithZombie(boundingBox.getX() + MINUS_MOVEMENT_COORDINATE,
                boundingBox.getY() + PLUS_MOVEMENT_COORDINATE)) {
            deltaX = MINUS_MOVEMENT_COORDINATE;
            deltaY = PLUS_MOVEMENT_COORDINATE;
            movingDirection = direction.UP_LEFT;
            xMovingDirection = direction.LEFT;
            yMovingDirection = direction.UP;
        } else if (!zombieCollidesWithZombie(boundingBox.getX() + PLUS_MOVEMENT_COORDINATE, boundingBox.getY())) {
            deltaX = PLUS_MOVEMENT_COORDINATE;
            deltaY = 0f;
            movingDirection = direction.RIGHT;
            xMovingDirection = direction.RIGHT;
        } else if (!zombieCollidesWithZombie(boundingBox.getX(), boundingBox.getY() + PLUS_MOVEMENT_COORDINATE)) {
            deltaY = PLUS_MOVEMENT_COORDINATE;
            deltaX = 0f;
            movingDirection = direction.UP;
            yMovingDirection = direction.UP;
        }
    }

    /**
     * Move zombie up so that Zombie does not collide with another Zombie.
     */
    private void moveUp() {
        deltaY = 0f;
        if (!zombieCollidesWithZombie(boundingBox.getX() + PLUS_MOVEMENT_COORDINATE, boundingBox.getY())) {
            deltaX = PLUS_MOVEMENT_COORDINATE;
            movingDirection = direction.RIGHT;
            xMovingDirection = direction.RIGHT;
        } else if (!zombieCollidesWithZombie(boundingBox.getX() + MINUS_MOVEMENT_COORDINATE, boundingBox.getY())) {
            deltaX = MINUS_MOVEMENT_COORDINATE;
            movingDirection = direction.LEFT;
            xMovingDirection = direction.LEFT;
        } else if (!zombieCollidesWithZombie(boundingBox.getX(), boundingBox. getY() + MINUS_MOVEMENT_COORDINATE)) {
            deltaY = MINUS_MOVEMENT_COORDINATE;
            movingDirection = direction.DOWN;
            yMovingDirection = direction.DOWN;
        }
    }

    /**
     * Move zombie down so that Zombie does not collide with another Zombie.
     */
    private void moveDown() {
        deltaY = 0f;
        if (!zombieCollidesWithZombie(boundingBox.getX() + PLUS_MOVEMENT_COORDINATE, boundingBox.getY())) {
            deltaX = PLUS_MOVEMENT_COORDINATE;
            movingDirection = direction.RIGHT;
            xMovingDirection = direction.RIGHT;
        } else if (!zombieCollidesWithZombie(boundingBox.getX() + MINUS_MOVEMENT_COORDINATE, boundingBox.getY())) {
            deltaX = MINUS_MOVEMENT_COORDINATE;
            movingDirection = direction.LEFT;
            xMovingDirection = direction.LEFT;
        } else if (!zombieCollidesWithZombie(boundingBox.getX(), boundingBox. getY() + PLUS_MOVEMENT_COORDINATE)) {
            deltaY = PLUS_MOVEMENT_COORDINATE;
            movingDirection = direction.UP;
            yMovingDirection = direction.UP;
        }
    }

    /**
     * Move zombie left so that Zombie does not collide with another Zombie.
     */
    private void moveLeft() {
        deltaX = 0f;
        if (!zombieCollidesWithZombie(boundingBox.getX(), boundingBox.getY() + PLUS_MOVEMENT_COORDINATE)) {
            deltaY = PLUS_MOVEMENT_COORDINATE;
            movingDirection = direction.UP;
            yMovingDirection = direction.UP;
        } else if (!zombieCollidesWithZombie(boundingBox.getX(), boundingBox.getY() + MINUS_MOVEMENT_COORDINATE)) {
            deltaY = MINUS_MOVEMENT_COORDINATE;
            movingDirection = direction.DOWN;
            yMovingDirection = direction.DOWN;
        } else if (!zombieCollidesWithZombie(boundingBox.getX() + PLUS_MOVEMENT_COORDINATE, boundingBox. getY())) {
            deltaY = PLUS_MOVEMENT_COORDINATE;
            movingDirection = direction.RIGHT;
            xMovingDirection = direction.RIGHT;
        }
    }

    /**
     * Move zombie right so that Zombie does not collide with another Zombie.
     */
    private void moveRight() {
        deltaX = 0f;
        if (!zombieCollidesWithZombie(boundingBox.getX(), boundingBox.getY() + PLUS_MOVEMENT_COORDINATE)) {
            deltaY = PLUS_MOVEMENT_COORDINATE;
            movingDirection = direction.UP;
            yMovingDirection = direction.UP;
        } else if (!zombieCollidesWithZombie(boundingBox.getX(), boundingBox.getY() + MINUS_MOVEMENT_COORDINATE)) {
            deltaY = MINUS_MOVEMENT_COORDINATE;
            movingDirection = direction.DOWN;
            yMovingDirection = direction.DOWN;
        } else if (!zombieCollidesWithZombie(boundingBox.getX() + MINUS_MOVEMENT_COORDINATE, boundingBox. getY())) {
            deltaX = MINUS_MOVEMENT_COORDINATE;
            movingDirection = direction.LEFT;
            xMovingDirection = direction.LEFT;
        }
    }

    /**
     * Reset Zombie class static variables.
     */
    public static void restartZombie() {
        zombiesCreated = 0;
        playerGameCharacterIdIndex = 0;
    }
}
