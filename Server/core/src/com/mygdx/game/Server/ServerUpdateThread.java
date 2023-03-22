package com.mygdx.game.Server;

import com.mygdx.game.Characters.Zombie;
import com.mygdx.game.World.World;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;

import static java.lang.Thread.sleep;


/**
 * ServerUpdateThread class is used for updating World (Zombies, PistolBullets etc).
 */
public class ServerUpdateThread implements Runnable {

    private ServerConnection serverConnection;
    private World serverWorld;
    private int countBetweenSendingNewZombies = 0;
    private int newZombiesSent = 0;

    private static final int COUNT_BETWEEN_SENDING_NEW_ZOMBIES = 250;
    private static final int ZOMBIES_SENT_IN_ONE_TICK = 4;

    public void setServerConnection(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    public void setServerWorld(World serverWorld) {
        this.serverWorld = serverWorld;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (serverWorld.getClients().size() == 3) {
                    serverWorld.setNewGame(true);

                    // Update serverWorld bullets and send updated bullets to all connections.
                    try {
                        serverConnection.sendUpdatedBullets();
                    } catch (ConcurrentModificationException exception) {
                        System.out.println("ConcurrentModificationException");
                    }

                    // Send Zombies who are going to be removed from the game.
                    if (!serverWorld.getZombiesToBeRemoved().isEmpty()) {
                        serverConnection.sendZombiesToRemoveFromGame(serverWorld.getAndEmptyZombiesToBeRemovedList());
                    }

                    // Wave
                    if (serverWorld.isNewWave() && newZombiesSent < serverWorld.getZombiesInWave()
                            && countBetweenSendingNewZombies == COUNT_BETWEEN_SENDING_NEW_ZOMBIES) {
                        // If is new wave and has not sent enough Zombies
                        // and count between zombies is 250 then send new Zombies to all connections.
                        List<Zombie> newZombies = serverWorld.createZombies();
                        newZombiesSent += ZOMBIES_SENT_IN_ONE_TICK;
                        countBetweenSendingNewZombies = 0;
                        serverConnection.sendNewZombies(newZombies);
                    } else if (newZombiesSent == serverWorld.getZombiesInWave()) {
                        // If has sent enough Zombies then stop sending new Zombie.
                        serverWorld.setNewWave(false);
                        newZombiesSent = 0;
                        countBetweenSendingNewZombies = 0;
                    } else if (serverWorld.isNewWave() && newZombiesSent < serverWorld.getZombiesInWave()) {
                        countBetweenSendingNewZombies += 1;
                    }

                    // Update and send Zombies.
                    if (!serverWorld.getZombieMap().isEmpty()) {
                        serverConnection.sendUpdatedZombies();
                    }
                } else if (serverWorld.getClients().isEmpty() && serverWorld.isNewGame()) {
                    // When game is over Server variables are reset.
                    // Reset ServerConnection, World, Zombie and ServerUpdateThread variables.
                    System.out.println("ServerUpdateThread restart");
                    serverConnection.restartServer();
                    serverWorld.restartWorld();
                    Zombie.restartZombie();
                    countBetweenSendingNewZombies = 0;
                    newZombiesSent = 0;
                }
                sleep(5);

            } catch (InterruptedException e) {
                System.out.println("Exception: " + Arrays.toString(e.getStackTrace()));
                System.out.println("Cause: " + e.getCause().toString());
            }
        }
    }
}
