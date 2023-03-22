package test.java.packets;

import org.junit.jupiter.api.Test;
import packets.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PacketCreatorTest {

    /**
     * Test class PacketCreator static method createPacketUpdateCharacterInformation.
     */
    @Test
    public void testCreatePacketUpdateCharacterInformation() {
        PacketUpdateCharacterInformation packetUpdateCharacterInformation = PacketCreator
                .createPacketUpdateCharacterInformation("Mati", 1, 45f, 85f,
                        "left", 95);

        assertEquals("Mati", packetUpdateCharacterInformation.getPlayerName());
        assertEquals(1, packetUpdateCharacterInformation.getId());
        assertEquals(45f, packetUpdateCharacterInformation.getX());
        assertEquals(85f, packetUpdateCharacterInformation.getY());
        assertEquals("left", packetUpdateCharacterInformation.getDirection());
    }

    /**
     * Test class PacketCreator static method createPacketConnect.
     */
    @Test
    public void testCreatePacketConnect() {
        PacketConnect packetConnect = PacketCreator.createPacketConnect("Mati");

        assertEquals("Mati", packetConnect.getPlayerName());
    }

    /**
     * Test class PacketCreator static method createPacketBullet.
     */
    @Test
    public void testCreatePacketBullet() {
        PacketBullet packetBullet = PacketCreator.createPacketBullet("Mati", 2, 35f, 65f,
                "Fire bullet", 1, "right");
        
        assertEquals("Mati", packetBullet.getPlayerName());
        assertEquals(2, packetBullet.getId());
        assertEquals(35f, packetBullet.getBulletXCoordinate());
        assertEquals(65f, packetBullet.getBulletYCoordinate());
        assertEquals("Fire bullet", packetBullet.getBulletTextureString());
        assertEquals(1, packetBullet.getDamage());
        assertEquals("right", packetBullet.getMovingDirection());
    }

    /**
     * Test class PacketCreator static method createPacketClientDisconnect.
     */
    @Test
    public void testCreatePacketClientDisconnect() {
        PacketClientDisconnect packetClientDisconnect = PacketCreator.createPacketClientDisconnect(1);

        assertEquals(1, packetClientDisconnect.getId());
    }
}