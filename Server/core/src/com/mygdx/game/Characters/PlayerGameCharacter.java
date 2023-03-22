package com.mygdx.game.Characters;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Weapons.Pistol;
import com.mygdx.game.Weapons.PistolBullet;
import com.mygdx.game.World.World;
import com.mygdx.game.Weapons.Shotgun;

public class PlayerGameCharacter extends GameCharacter {

    private final String playerName;
    private int playerGameCharacterId;
    private Pistol playerCharacterCurrentPistol;
    private World world;
    private Shotgun playerCharacterCurrentShotgun;
    private int timeBetweenZombieCollision = 0;

    private static final int TIME_BETWEEN_COLLISIONS = 100;
    private static final int ZOMBIE_DAMAGE = 10;
    private static final int BULLET_DAMAGE_MULTIPLIER = 5;

    /**
     * Class PlayerGameCharacter constructor.
     *
     * @param playerName name of the player (String)
     * @param movementSpeed of the PlayerGameCharacter (float)
     * @param boundingBox encapsulates a 2D rectangle(bounding box) for the PlayerGameCharacter (Rectangle)
     * @param xPosition of the PlayerGameCharacter (float)
     * @param yPosition of the PlayerGameCharacter (float)
     * @param width of the PlayerGameCharacter (float)
     * @param height of the PlayerGameCharacter (float)
     * @param playerCharacterCurrentPistol current Pistol of the PlayerGameCharacter (Pistol)
     * @param world where the PlayerGameCharacter is
     */
    public PlayerGameCharacter(String playerName, float movementSpeed, Rectangle boundingBox, float xPosition, float yPosition, float width,
                               float height, Pistol playerCharacterCurrentPistol, World world) {
        super(movementSpeed, boundingBox, xPosition, yPosition, width, height, world);
        this.playerName = playerName;
        this.movementSpeed = movementSpeed;
        this.boundingBox = boundingBox;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.height = height;
        this.width = width;
        this.playerCharacterCurrentPistol = playerCharacterCurrentPistol;
        this.world = world;
        this.health = 100;
    }

    public String getName() {
        return playerName;
    }

    public Pistol getPlayerCharacterCurrentPistol() {
        return playerCharacterCurrentPistol;
    }

    public void setPlayerGameCharacterId(int playerGameCharacterId) {
        this.playerGameCharacterId = playerGameCharacterId;
    }

    public int getPlayerGameCharacterId() {
        return playerGameCharacterId;
    }

    public void setPlayerCharacterCurrentShotgun(Shotgun playerCharacterCurrentShotgun) {
        this.playerCharacterCurrentShotgun = playerCharacterCurrentShotgun;
    }

    public Shotgun getPlayerCharacterCurrentShotgun() {
        return playerCharacterCurrentShotgun;
    }

    /**
     * PlayerGameCharacter static method for creating a new PlayerGameCharacter instance.
     *
     * @param x coordinate of the PlayerGameCharacter (float)
     * @param y coordinate of the PlayerGameCharacter (float)
     * @param name of the player (String)
     * @param world where the PlayerGameCharacter is (World)
     * @param id unique id (int)
     * @return new PlayerGameCharacter instance
     */
    public static PlayerGameCharacter createPlayerGameCharacter(float x, float y, String name, World world, int id) {
        Pistol pistol = Pistol.createPistol(x, y);
        Shotgun shotgun = Shotgun.createShotgun(x, y);
        Rectangle playerGameCharacterRectangle = new Rectangle(x, y, 10f, 10f);
        PlayerGameCharacter newGameCharacter = new PlayerGameCharacter(name, 2f, playerGameCharacterRectangle, x, y, 10f, 10f, pistol, world);
        newGameCharacter.setPlayerCharacterCurrentShotgun(shotgun);
        newGameCharacter.setPlayerGameCharacterId(id);
        return newGameCharacter;
    }

    /**
     * PlayerGameCharacter method that decreases PlayerGameCharacter's health when PlayerGameCharacter
     * collides with Zombie instance.
     *
     * @param zombie (Zombie) with who the PlayerGameCharacter collides with
     */
    public void collidesWithZombie(Zombie zombie) {
        Rectangle zombieBoundingBox = zombie.getBoundingBox();
        boolean collidesWithZombie = boundingBox.overlaps(zombieBoundingBox);
        if (collidesWithZombie) {
            if (health > 0 && timeBetweenZombieCollision <= 0) {
                health -= ZOMBIE_DAMAGE;
                timeBetweenZombieCollision = TIME_BETWEEN_COLLISIONS;
            }
        } else {
            timeBetweenZombieCollision = TIME_BETWEEN_COLLISIONS;
        }
        timeBetweenZombieCollision -= 1;
    }

    @Override
    public void characterIsHit(PistolBullet pistolBullet) {
        if (health > 0) {
            health -= pistolBullet.getDamage() * BULLET_DAMAGE_MULTIPLIER;
        }
    }

}
