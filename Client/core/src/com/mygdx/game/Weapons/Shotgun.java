package com.mygdx.game.Weapons;

import com.badlogic.gdx.math.Rectangle;

public class Shotgun extends Pistol {

    private PistolBullet bulletLeft;
    private PistolBullet bulletStraight;
    private PistolBullet bulletRight;

    /**
     * Shotgun constructor.
     *
     * @param bulletLeft shotgun's bullet that shoots to the left (PistolBullet)
     * @param bulletStraight shotgun's bullet that shoots straight ahead (PistolBullet)
     * @param bulletRight shotgun's bullet that shoots to the right (PistolBullet)
     */
    public Shotgun(PistolBullet bulletLeft, PistolBullet bulletStraight, PistolBullet bulletRight) {
        super(bulletStraight);
        this.bulletLeft = bulletLeft;
        this.bulletStraight = bulletStraight;
        this.bulletRight = bulletRight;
    }

    public PistolBullet getBulletLeft() {
        return bulletLeft;
    }

    public PistolBullet getBulletStraight() {
        return bulletStraight;
    }

    public PistolBullet getBulletRight() {
        return bulletRight;
    }

    /**
     * Method for creating a shotgun instance with three bullets inside.
     *
     * @param x coordinate of all bullets (float)
     * @param y coordinate of all bullets (float)
     * @return a Shotgun instance
     */
    public static Shotgun createShotgun(float x, float y) {
        PistolBullet shotgunLeft = new PistolBullet();
        Rectangle shotgunLeftRectangle = new Rectangle(x, y, 5f, 5f);
        shotgunLeft.makePistolBullet(shotgunLeftRectangle, "Fire Bullet", "up-left", 1);
        PistolBullet shotgunStraight = new PistolBullet();
        Rectangle shotgunStraightRectangle = new Rectangle(x, y, 5f, 5f);
        shotgunStraight.makePistolBullet(shotgunStraightRectangle, "Fire Bullet", "up", 1);
        PistolBullet shotgunRight = new PistolBullet();
        Rectangle shotgunRightRectangle = new Rectangle(x, y, 5f, 5f);
        shotgunRight.makePistolBullet(shotgunRightRectangle, "Fire Bullet", "up-right", 1);
        return new Shotgun(shotgunLeft, shotgunStraight, shotgunRight);
    }

    /**
     * Update the BoundingBox coordinates for every bullet.
     *
     * @param leftX x coordinate for the left bullet (float)
     * @param leftY y coordinate for the left bullet (float)
     * @param straightX x coordinate for the straight bullet (float)
     * @param straightY y coordinate for the straight bullet (float)
     * @param rightX x coordinate for the right bullet (float)
     * @param rightY y coordinate for the right bullet (float)
     */
    public void updateBulletsLocation(float leftX, float leftY, float straightX, float straightY, float rightX,
                                      float rightY) {
        bulletStraight.getBoundingBox().setPosition(straightX, straightY);
        bulletLeft.getBoundingBox().setPosition(leftX, leftY);
        bulletRight.getBoundingBox().setPosition(rightX, rightY);
    }

    /**
     * Set a direction for the bullet that shoots to the left, given the direction of the bullet that shoots ahead.
     *
     * @param direction of the bullet that shoots straight ahead (String)
     * @return left bullet's direction
     */
    public String setBulletLeftDirection(String direction) {
        String newDirection = "";
        switch (direction) {
            case "up":
                newDirection = "up-left";
                break;
            case "up-left":
                newDirection = "left";
                break;
            case "left":
                newDirection = "left-down";
                break;
            case "down-left":
                newDirection = "down";
                break;
            case "down":
                newDirection = "down-right";
                break;
            case "down-right":
                newDirection = "right";
                break;
            case "right":
                newDirection = "right-up";
                break;
            case "up-right":
                newDirection = "up";
                break;
        }
        return newDirection;
    }

    /**
     * Set a direction for the bullet that shoots to the right, given the direction of the bullet that shoots ahead.
     *
     * @param direction of the bullet that shoots straight ahead (String)
     * @return right bullet's direction
     */
    public String setBulletRightDirection(String direction) {
        String newDirection = "";
        switch (direction) {
            case "up":
                newDirection = "up-right";
                break;
            case "up-right":
                newDirection = "right";
                break;
            case "right":
                newDirection = "right-down";
                break;
            case "down-right":
                newDirection = "down";
                break;
            case "down":
                newDirection = "down-left";
                break;
            case "down-left":
                newDirection = "left";
                break;
            case "left":
                newDirection = "left-up";
                break;
            case "up-left":
                newDirection = "up";
                break;
        }
        return newDirection;
    }
}
