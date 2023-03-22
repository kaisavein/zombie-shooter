package com.mygdx.game.Weapons;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class PistolBullet implements Pool.Poolable {

    private Rectangle boundingBox;
    private String direction;
    private TextureRegion textureRegion, textureRegionRegular, textureRegionStrong;
    private String bulletTextureString;
    private int damage;

    /**
     * Constructor for PistolBullet.
     */
    public PistolBullet() {
        this.textureRegion = null;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(Rectangle boundingBox) {
        this.boundingBox = boundingBox;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getBulletTextureString() {
        return bulletTextureString;
    }

    public void setBulletTextureString(String bulletTextureString) {
        this.bulletTextureString = bulletTextureString;
    }

    public void setBulletTexture(TextureRegion texture) {
        this.textureRegion = texture;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * Method for creating a new PistolBullet with the given BoundingBox, texture string, direction and amount of damage.
     * @param newBoundingBox bullet's BoundingBox (Rectangle)
     * @param bulletTextureString bullet's texture string (String)
     * @param direction of the bullet (String)
     * @param damage the bullet can make (int)
     */
    public void makePistolBullet(Rectangle newBoundingBox, String bulletTextureString, String direction, int damage) {
        setBoundingBox(newBoundingBox);
        getBoundingBox().x = newBoundingBox.getX();
        getBoundingBox().y = newBoundingBox.getY();
        getBoundingBox().width = newBoundingBox.getWidth();
        getBoundingBox().height = newBoundingBox.getHeight();
        setDirection(direction);
        setBulletTextureString(bulletTextureString);
        setDamage(damage);
    }

    /**
     * Method for drawing a PistolBullet on the screen.
     *
     * @param batch is used to draw 2D rectangles (Batch)
     */
    public void draw(Batch batch) {
        if (this.textureRegion == null) {
            this.textureRegionRegular = new TextureAtlas("images4.atlas").findRegion("Fire Bullet");
            this.textureRegionStrong = new TextureAtlas("images4.atlas").findRegion("Red Bullet");
            this.textureRegion = textureRegionRegular;
        }
        batch.draw(this.textureRegion, boundingBox.getX(), boundingBox.getY(), boundingBox.getWidth(), boundingBox.getHeight());
    }

    /**
     * Method for setting the correct bullet texture region.
     */
    public void setTextureRegion() {
        switch (bulletTextureString) {
            case "Fire Bullet":
                setBulletTexture(textureRegionRegular);
                break;
            case "Red Bullet":
                setBulletTexture(textureRegionStrong);
                break;
        }
    }

    /**
     * Method for moving the PistolBullet according to its direction.
     */
    public void movePistolBullet() {
        float xPosition = 0, yPosition = 0;
        switch (direction) {
            case "up":
                yPosition += 2f;
                break;
            case "down":
                yPosition -= 2f;
                break;
            case "right":
                xPosition += 2f;
                break;
            case "left":
                xPosition -= 2f;
                break;
            case "up-right":
                xPosition += 0.5f;
                yPosition += 2f;
                break;
            case "up-left":
                xPosition -= 0.5f;
                yPosition += 2f;
                break;
            case "down-right":
                xPosition += 0.5f;
                yPosition -= 2f;
                break;
            case "down-left":
                xPosition -= 0.5f;
                yPosition -= 2f;
                break;
            case "right-up":
                xPosition += 2f;
                yPosition += 0.5f;
                break;
            case "left-up":
                xPosition -= 2f;
                yPosition += 0.5f;
                break;
            case "right-down":
                xPosition += 2f;
                yPosition -= 0.5f;
                break;
            case "left-down":
                xPosition -= 2f;
                yPosition -= 0.5f;
                break;
        }
        boundingBox.set(boundingBox.getX() + xPosition, boundingBox.getY() + yPosition, boundingBox.getWidth(), boundingBox.getHeight());
    }

    /**
     * Method for checking whether PistolBullet's BoundingBox is in the world or not.
     * @return boolean
     */
    public boolean checkIfPistolBulletIsInWorld() {
        int xCoordinateLeft = 65;
        int xCoordinateRight = 511;
        int yCoordinateUpper = 482;
        int yCoordinateLower = 62;
        return this.getBoundingBox().getX() >= xCoordinateLeft && this.getBoundingBox().getX() <= xCoordinateRight &&
                this.getBoundingBox().getY() <= yCoordinateUpper && this.getBoundingBox().getY() >= yCoordinateLower;
    }

    /**
     * Method for PistolBulletPool that is used when a PistolBullet is put into the pool in ClientWorld class.
     * This resets its BoundingBox parameters, direction, texture region and texture string.
     */
    @Override
    public void reset() {
        this.boundingBox.set(0, 0, 0, 0);
        this.direction = null;
        this.textureRegion = null;
        this.bulletTextureString = null;
    }

    /**
     * Method for checking whether PistolBullet has collided with walls or not.
     * @param mapLayer containing a set of objects and properties
     * @return boolean
     */
    public boolean collidesWithWalls(MapLayer mapLayer) {
        Array<RectangleMapObject> objects = mapLayer.getObjects().getByType(RectangleMapObject.class);
        for (int i = 0; i < objects.size; i++) {
            RectangleMapObject obj = objects.get(i);
            Rectangle rect = obj.getRectangle();
            if (boundingBox.overlaps(rect)) {
                return false;
            }
        }
        return true;
    }
}
