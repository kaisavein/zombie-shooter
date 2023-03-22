package com.mygdx.game.Weapons;

import com.badlogic.gdx.utils.Pool;

/**
 * PistolBulletPool keeps PistolBullets so they could be reused for drawing. This helps to avoid memory leaks.
 */
public class PistolBulletPool extends Pool<PistolBullet> {

    /**
     * Returns a new PistolBullet.
     *
     * @return PistolBullet instance
     */
    @Override
    protected PistolBullet newObject() {
        return new PistolBullet();
    }
}
