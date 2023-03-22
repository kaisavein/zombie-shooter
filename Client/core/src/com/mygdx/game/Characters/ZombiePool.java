package com.mygdx.game.Characters;

import com.badlogic.gdx.utils.Pool;

/**
 * ZombiePool keeps Zombies so they could be reused for drawing. This helps to avoid memory leaks.
 */
public class ZombiePool extends Pool<Zombie> {

    /**
     * Returns a new Zombie instance.
     *
     * @return Zombie instance
     */
    @Override
    protected Zombie newObject() {
        return new Zombie();
    }
}
