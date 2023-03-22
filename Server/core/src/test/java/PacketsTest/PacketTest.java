package test.java.PacketsTest;

import com.mygdx.game.Weapons.PistolBullet;
import org.junit.jupiter.api.Test;
import packets.*;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PacketCreatorTest {

    /**
     * Test class PacketCreator static method createPacketUpdateCharacterInformation.
     */
    @Test
    public void testCreatePacketUpdateCharacterInformation() {
        PacketUpdateCharacterInformation packetUpdateCharacterInformation = PacketCreator
                .createPacketUpdateCharacterInformation("Mati", 1, 10f, 30f,
                        "up",  90);

        assertEquals("Mati", packetUpdateCharacterInformation.getPlayerName());
        assertEquals(1, packetUpdateCharacterInformation.getId());
        assertEquals(10f, packetUpdateCharacterInformation.getX());
        assertEquals(30f, packetUpdateCharacterInformation.getY());
        assertEquals("up", packetUpdateCharacterInformation.getDirection());
    }

    /**
     * Test class PacketCreator static method createPacketAddCharacter.
     */
    @Test
    public void testCreatePacketAddCharacter() {
        PacketAddCharacter packetAddCharacter = PacketCreator.createPacketAddCharacter("Mati", 2,
                20f, 45f);

        assertEquals("Mati", packetAddCharacter.getPlayerName());
        assertEquals(2, packetAddCharacter.getId());
        assertEquals(20f, packetAddCharacter.getX());
        assertEquals(45f, packetAddCharacter.getY());
    }

    /**
     * Test class PacketCreator static method createPacketSendUpdatedBullets.
     */
    @Test
    public void testCreatePacketSendUpdatedBullets() {
        List<PistolBullet> pistolBullets = List.of(new PistolBullet(), new PistolBullet(), new PistolBullet());
        PacketSendUpdatedBullets packetSendUpdatedBullets = PacketCreator.createPacketSendUpdatedBullets(pistolBullets);

        assertEquals(3, packetSendUpdatedBullets.getUpdatedBullets().size());
        assertEquals(pistolBullets, packetSendUpdatedBullets.getUpdatedBullets());
    }

    /**
     * Test class PacketCreator static method createPacketNewZombies.
     */
    @Test
    public void testCreatePacketNewZombies() {
        Map<Integer, List<Float>> newZombies = Map.of(0, List.of(10f, 30f),
                1, List.of(20f, 80f), 2, List.of(60f, 75f));
        PacketNewZombies packetNewZombies = PacketCreator.createPacketNewZombies(newZombies);

        assertEquals(3, packetNewZombies.getZombieMap().size());
        assertEquals(newZombies, packetNewZombies.getZombieMap());
    }

    /**
     * Test class PacketCreator static method createPacketZombies.
     */
    @Test
    public void testCreatePacketZombies() {
        Map<Integer, List<Float>> zombies = Map.of(0, List.of(20f, 40f),
                1, List.of(30f, 50f), 4, List.of(50f, 10f));
        PacketNewZombies packetUpdateZombies = PacketCreator.createPacketNewZombies(zombies);

        assertEquals(3, packetUpdateZombies.getZombieMap().size());
        assertEquals(zombies, packetUpdateZombies.getZombieMap());
    }

    /**
     * Test class PacketCreator static method createPacketRemoveZombiesFromGame.
     */
    @Test
    public void testCreatePacketRemoveZombiesFromGame() {
        List<Integer> zombieIdList = List.of(1, 2, 4);
        PacketRemoveZombiesFromGame packetRemoveZombiesFromGame = PacketCreator.createPacketRemoveZombiesFromGame(zombieIdList);

        assertEquals(3, packetRemoveZombiesFromGame.getZombieIdList().size());
        assertEquals(zombieIdList, packetRemoveZombiesFromGame.getZombieIdList());
    }
}