package com.mygdx.game.Characters;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

public class Zombie extends GameCharacter implements Pool.Poolable {

    private int zombieId;
    private boolean zombieTextureBoolean = false;
    private TextureRegion characterTexture;


    /**
     * Zombie constructor.
     *
     * @param movementSpeed of the Zombie (float)
     * @param boundingBox encapsulates a 2D rectangle(bounding box) for the Zombie (Rectangle)
     * @param xPosition of the Zombie (float)
     * @param yPosition of the Zombie (float)
     * @param width of the Zombie (float)
     * @param height of the Zombie (float)
     */
    public Zombie(float movementSpeed, Rectangle boundingBox, float xPosition, float yPosition, float width, float height) {
        super(movementSpeed, boundingBox, xPosition, yPosition, width, height);
        this.movementSpeed = movementSpeed;
        this.boundingBox = boundingBox;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.health = 10;
    }

    /**
     * Zombie constructor.
     *
     * This constructor is used for drawing Zombies. Class ZombiePool uses this constructor for creating new Zombie instances.
     */
    public Zombie() { }

    public void makeZombie(float movementSpeed, Rectangle boundingBox, float xPosition, float yPosition, float width, float height) {
        this.movementSpeed = movementSpeed;
        this.boundingBox = boundingBox;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.health = 10;
    }

    /**
     * Method for ZombiePool that is used when a Zombie is put into the pool in ClientWorld class.
     *
     * Resets all Zombie variables.
     */
    @Override
    public void reset() {
        this.movementSpeed = 0;
        this.boundingBox = null;
        this.xPosition = 0;
        this.yPosition = 0;
        this.width = 0;
        this.height = 0;
        this.health = 10;
    }

    /**
     * Method for drawing a Zombie on the screen.
     *
     * @param batch is used to draw 2D rectangles (Batch)
     */
    public void draw(Batch batch) {
        if (!zombieTextureBoolean) {
            this.characterTexture = new TextureAtlas("images4.atlas").findRegion("goblin_idle_anim_f0");
            this.zombieTextureBoolean = true;
        }
        batch.draw(characterTexture, boundingBox.getX(), boundingBox.getY(), boundingBox.getWidth(), boundingBox.getHeight());
    }

    public void setZombieId(int zombieId) {
        this.zombieId = zombieId;
    }

    public int getZombieId() {
        return zombieId;
    }
}
