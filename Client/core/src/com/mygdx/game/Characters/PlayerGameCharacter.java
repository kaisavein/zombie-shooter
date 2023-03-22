package com.mygdx.game.Characters;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Weapons.Pistol;
import com.mygdx.game.Weapons.Shotgun;

public class PlayerGameCharacter extends GameCharacter {

    private final String playerName;
    private int playerGameCharacterId;

    private final Pistol playerCharacterCurrentPistol;
    private Shotgun playerCharacterCurrentShotgun;
    private String currentWeapon = "regular pistol";

    private TextureRegion characterTexture, characterTexture0, characterTexture1, characterTexture2, characterTexture3;
    private boolean textureBoolean = false;
    private String playerDirection = "up";

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
     */
    public PlayerGameCharacter(String playerName, float movementSpeed, Rectangle boundingBox, float xPosition,
                               float yPosition, float width, float height, Pistol playerCharacterCurrentPistol) {
        super(movementSpeed, boundingBox, xPosition, yPosition, width, height);
        this.playerName = playerName;
        this.movementSpeed = movementSpeed;
        this.boundingBox = boundingBox;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
        this.playerCharacterCurrentPistol = playerCharacterCurrentPistol;
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

    public void setWeapon(String newWeapon) {
        currentWeapon = newWeapon;
    }

    public String getCurrentWeapon() {
        return currentWeapon;
    }

    public void setPlayerDirection(String playerDirection) {
        this.playerDirection = playerDirection;
    }

    public String getPlayerDirection() {
        return playerDirection;
    }

    public void setCharacterTexture(TextureRegion texture) {
        this.characterTexture = texture;
    }

    /**
     * PlayerGameCharacter static method for creating a new PlayerGameCharacter instance.
     *
     * @param x coordinate of the PlayerGameCharacter (float)
     * @param y coordinate of the PlayerGameCharacter (float)
     * @param name of the player (String)
     * @return new PlayerGameCharacter instance
     */
    public static PlayerGameCharacter createPlayerGameCharacter(float x, float y, String name, int id) {
        Pistol pistol = Pistol.createPistol(x, y);
        Shotgun shotgun = Shotgun.createShotgun(x, y);
        Rectangle playerGameCharacterRectangle = new Rectangle(x, y, 10f, 10f);
        PlayerGameCharacter newGameCharacter = new PlayerGameCharacter(name, 2f, playerGameCharacterRectangle, x, y, 10f, 10f, pistol);
        newGameCharacter.setPlayerCharacterCurrentShotgun(shotgun);
        newGameCharacter.setPlayerGameCharacterId(id);
        return newGameCharacter;
    }

    /**
     * PlayerGameCharacter method for drawing the PlayerCharacter on the Screen.
     *
     * @param batch (Batch) is used to draw 2D rectangles
     */
    public void draw(Batch batch) {
        if (!textureBoolean) {
            this.characterTexture0 = new TextureAtlas("images4.atlas").findRegion("idle up1");
            this.characterTexture1 = new TextureAtlas("images4.atlas").findRegion("idle left1");
            this.characterTexture2 = new TextureAtlas("images4.atlas").findRegion("idle right1");
            this.characterTexture3 = new TextureAtlas("images4.atlas").findRegion("idle down1");
            this.characterTexture = characterTexture0;
            this.textureBoolean = true;
        }
        batch.draw(characterTexture, boundingBox.getX(), boundingBox.getY(), boundingBox.getWidth(), boundingBox.getHeight());
    }

    /**
     * Set PlayerGameCharacter's texture according to the PlayerGameCharacter's direction.
     */
    public void setTextureForDirection() {
        switch (playerDirection) {
            case "up-right":
            case "right":
            case "down-right":
                setCharacterTexture(characterTexture2);
                break;
            case "up":
                setCharacterTexture(characterTexture0);
                break;
            case "left":
            case "up-left":
            case "down-left":
                setCharacterTexture(characterTexture1);
                break;
            case "down":
                setCharacterTexture(characterTexture3);
                break;
        }
    }
}
