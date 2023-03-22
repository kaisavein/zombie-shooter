package com.mygdx.game.Weapons;

import com.badlogic.gdx.math.Rectangle;

public class Pistol {

    private PistolBullet bullet;

    /**
     * Pistol constructor.
     *
     * @param bullet inside the Pistol (PistolBullet)
     */
    public Pistol(PistolBullet bullet) {
        this.bullet = bullet;
    }

    public PistolBullet getBulletStraight() { return bullet; }

    /**
     * Create a new PistolBullet instance with given coordinates.
     *
     * @param x coordinate of the bullet (float)
     * @param y coordinate of the bullet (float)
     * @return created PistolBullet
     */
    public static Pistol createPistol(float x, float y) {
        PistolBullet newBullet = new PistolBullet();
        Rectangle pistolBulletRectangle = new Rectangle(x, y, 5f, 5f);
        newBullet.makePistolBullet(pistolBulletRectangle, "Fire Bullet", "up", 1);
        return new Pistol(newBullet);
    }
}
