package com.mygdx.game.Screens;

import ClientConnection.ClientConnection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.Characters.PlayerGameCharacter;
import com.mygdx.game.Characters.Zombie;
import com.mygdx.game.Characters.ZombiePool;
import com.mygdx.game.GameInfo.ClientWorld;
import com.mygdx.game.Weapons.PistolBullet;
import com.mygdx.game.Weapons.PistolBulletPool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class GameScreen implements Screen, InputProcessor {

    // Screen
    private final OrthographicCamera camera;
    private final OrthographicCamera scoreCam;
    private StretchViewport stretchViewport;
    boolean buttonHasBeenPressed;
    private Integer counter = 0;
    private int shotgunTimer = 120, pistolTimer = 120, instructionsTimer = 330;
    private BitmapFont font;
    private BitmapFont nameFont;

    // Graphics and Texture
    private final SpriteBatch batch;
    private TextureAtlas textureAtlas;
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;

    // Pools, bullets and lives
    private PistolBulletPool pistolBulletPool = new PistolBulletPool();
    private ZombiePool zombiePool = new ZombiePool();
    private boolean playerGameCharactersHaveLives = true;
    private LinkedList<PistolBullet> playerPistolBulletList;
    private boolean isRenderingBullets = false;

    // World parameters
    private final float WORLD_WIDTH = 285;
    private final float WORLD_HEIGHT = 285;

    // Client's connection, world
    private ClientConnection clientConnection;
    private ClientWorld clientWorld;

    /**
     * GameScreen constructor
     *
     * @param clientWorld client's world
     */
    public GameScreen (ClientWorld clientWorld) {

        this.clientWorld = clientWorld;
        playerPistolBulletList = new LinkedList<>();

        // Cameras and screen
        float aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
        camera = new OrthographicCamera(WORLD_HEIGHT * aspectRatio, WORLD_HEIGHT);
        buttonHasBeenPressed = false;
        if (clientWorld.getMyPlayerGameCharacter() != null) {
            camera.position.set(clientWorld.getMyPlayerGameCharacter().getBoundingBox().getX(),
                    clientWorld.getMyPlayerGameCharacter().getBoundingBox().getY(), 0);
        } else {
            camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        }
        scoreCam = new OrthographicCamera(WORLD_HEIGHT * aspectRatio, WORLD_HEIGHT);
        scoreCam.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        this.stretchViewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stretchViewport.setCamera(scoreCam);

        // TextureAtlas and background texture
        textureAtlas = new TextureAtlas("images4.atlas");
        tiledMap = new TmxMapLoader().load("DesertMap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        prepareHUD();
    }

    public void setPlayerGameCharactersHaveLives(boolean playerGameCharactersHaveLives) {
        this.playerGameCharactersHaveLives = playerGameCharactersHaveLives;
    }

    public void registerClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    public boolean isRenderingBullets() {
        return isRenderingBullets;
    }

    /**
     * Method for drawing textures, heads-up display and handling camera positioning
     * @param delta time
     */
    @Override
    public void render(float delta) {
        if (clientWorld.getMyPlayerGameCharacter() != null && buttonHasBeenPressed) {
            camera.position.set(clientWorld.getMyPlayerGameCharacter().getBoundingBox().getX(),
                    clientWorld.getMyPlayerGameCharacter().getBoundingBox().getY(), 0);
        } else if (clientWorld.getMyPlayerGameCharacter() != null && counter < 10) {
            clientConnection.sendPlayerInformation(clientWorld.getMyPlayerGameCharacter().getMovementSpeed(),
                    clientWorld.getMyPlayerGameCharacter().getMovementSpeed(), "up-right", clientWorld.getMyPlayerGameCharacter().getHealth());
            camera.position.set(clientWorld.getMyPlayerGameCharacter().getBoundingBox().getX(), clientWorld.getMyPlayerGameCharacter().getBoundingBox().getY(), 0);
            counter++;
        }

        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        batch.setProjectionMatrix(camera.combined);

        // Sets game over screen when PlayerGameCharacters don't have lives
        if (!playerGameCharactersHaveLives) {
            clientConnection.getGameClient().setScreenToGameOver();
        }

        batch.begin();

        detectInput();

        // Drawing characters, bullets and zombies
        drawPlayerGameCharacters();
        drawPistolBullets();
        drawZombies();

        // Ends displaying textures
        batch.setProjectionMatrix(camera.combined);

        // HUD rendering
        batch.setProjectionMatrix(stretchViewport.getCamera().combined);
        updateAndRenderHUD();

        batch.end();
    }

    /**
     * Method for sending information about client's PlayerGameCharacter's new position based on keyboard input.
     */
    private void detectInput(){
        if (clientWorld.getMyPlayerGameCharacter() != null) {
            float movementSpeed = clientWorld.getMyPlayerGameCharacter().getMovementSpeed();
            int health = clientWorld.getMyPlayerGameCharacter().getHealth();

            if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
                buttonHasBeenPressed = true;
            }

            if ((Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.D)) || (Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.RIGHT))) {
                clientConnection.sendPlayerInformation(movementSpeed, movementSpeed, "up-right", health);
            }
            else if ((Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.D)) || (Gdx.input.isKeyPressed(Input.Keys.DOWN) && Gdx.input.isKeyPressed(Input.Keys.RIGHT))) {
                clientConnection.sendPlayerInformation(movementSpeed, -movementSpeed, "down-right", health);
            }
            else if ((Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.A)) || (Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.LEFT))) {
                clientConnection.sendPlayerInformation(-movementSpeed, movementSpeed, "up-left", health);
            }
            else if ((Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.A)) || (Gdx.input.isKeyPressed(Input.Keys.DOWN) && Gdx.input.isKeyPressed(Input.Keys.LEFT))) {
                clientConnection.sendPlayerInformation(-movementSpeed, -movementSpeed, "down-left", health);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                clientConnection.sendPlayerInformation(0, movementSpeed, "up", health);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                clientConnection.sendPlayerInformation(-movementSpeed, 0, "left", health);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                clientConnection.sendPlayerInformation(0, -movementSpeed, "down", health);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                clientConnection.sendPlayerInformation(movementSpeed, 0, "right", health);
            }
        }
    }

    /**
     * Method for preparing fonts for the heads-up display.
     */
    private void prepareHUD() {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ZombieCarshel-B8rx.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        FreeTypeFontGenerator fontGenerator2 = new FreeTypeFontGenerator(Gdx.files.internal("ZombieCarshel-B8rx.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParameter.size = 72;
        fontParameter.borderWidth = 3.6f;
        fontParameter.color = new Color(1, 1, 1, 0.3f);
        fontParameter.borderColor = new Color(0, 0, 0, 0.3f);

        fontParameter2.size = 100;
        fontParameter2.borderWidth = 0.1f;
        fontParameter2.color = new Color(1, 1, 1, 0.3f);
        fontParameter2.borderColor = new Color(0, 0, 0, 0.3f);

        font = fontGenerator.generateFont(fontParameter);
        font.getData().setScale(0.2f);
        nameFont = fontGenerator2.generateFont(fontParameter2);
        nameFont.getData().setScale(7 * nameFont.getScaleY() / nameFont.getLineHeight());
        nameFont.setUseIntegerPositions(false);
        fontGenerator.dispose();
        fontGenerator2.dispose();
    }

    /**
     * Method for updating and drawing heads-up display.
     */
    private void updateAndRenderHUD() {
        // Upper bar
        font.draw(batch, "Score", font.getCapHeight() / 2 - 112, scoreCam.viewportHeight / 2 - 3);
        font.draw(batch, "Lives", font.getCapHeight() / 2 + 55, scoreCam.viewportHeight / 2 - 3);
        font.draw(batch, "Wave:", font.getCapHeight() / 2 - 28, scoreCam.viewportHeight / 2 - 3);

        // Wave count
        if (clientWorld.getWaveCount() < 10) {
            font.draw(batch, String.format(Locale.getDefault(), "%01d", clientWorld.getWaveCount()), font.getCapHeight() / 2 - 15, scoreCam.viewportHeight / 2 - 19);
        } else {
            font.draw(batch, String.format(Locale.getDefault(), "%02d", clientWorld.getWaveCount()), font.getCapHeight() / 2 - 17, scoreCam.viewportHeight / 2 - 19);
        }

        // Score, health, current weapon
        font.draw(batch, String.format(Locale.getDefault(), "%06d", clientWorld.getScore()), font.getCapHeight() / 2 - 112, scoreCam.viewportHeight / 2 - 19);
        if (clientWorld.getMyPlayerGameCharacter() != null) {
            font.draw(batch, String.format(Locale.getDefault(), "%02d", clientWorld.getMyPlayerGameCharacter().getHealth()), font.getCapHeight() / 2 + 66, scoreCam.viewportHeight / 2 - 19);
            font.draw(batch, clientWorld.getMyPlayerGameCharacter().getCurrentWeapon(), font.getCapHeight() / 2 - 112, scoreCam.viewportHeight / 2 - 272);
        }

        // Notifications
        if (clientWorld.getScore() >= 2500 && pistolTimer > 0) {
            font.draw(batch, "You have unlocked a", font.getCapHeight() / 2 - 92, scoreCam.viewportHeight / 2 - 60);
            font.draw(batch, "new weapon", font.getCapHeight() / 2 - 57, scoreCam.viewportHeight / 2 - 80);
            pistolTimer--;
        }
        if (clientWorld.getScore() >= 1000 && shotgunTimer > 0) {
            font.draw(batch, "You have unlocked a", font.getCapHeight() / 2 - 92, scoreCam.viewportHeight / 2 - 60);
            font.draw(batch, "new weapon", font.getCapHeight() / 2 - 57, scoreCam.viewportHeight / 2 - 80);
            shotgunTimer--;
        }

        // Instructions
        if (instructionsTimer > 0) {
            font.draw(batch, "How to play", font.getCapHeight() / 2 - 53, scoreCam.viewportHeight / 2 - 160);
            font.draw(batch, "Move  WASD or arrows", font.getCapHeight() / 2 - 97, scoreCam.viewportHeight / 2 - 180);
            font.draw(batch, "Shoot          spacebar", font.getCapHeight() / 2 - 97, scoreCam.viewportHeight / 2 - 200);
            font.draw(batch, "Switch weapons    TAB", font.getCapHeight() / 2 - 97, scoreCam.viewportHeight / 2 - 220);
            instructionsTimer--;
        }
    }

    /**
     * Method for drawing PlayerGameCharacters.
     */
    public void drawPlayerGameCharacters() {
        List<PlayerGameCharacter> characterValues = new ArrayList<>(clientWorld.getWorldGameCharactersMap().values());
        for (int i = 0; i < characterValues.size(); i++) {
            PlayerGameCharacter character = characterValues.get(i);
            character.setTextureForDirection();
            character.draw(batch);
            if (character.getName() != null) {
                nameFont.draw(batch, character.getName(), character.getBoundingBox().getX() - nameFont.getSpaceXadvance() * character.getName().length() / 2, character.getBoundingBox().getY() + character.getBoundingBox().height + 12);
            }
        }
    }

    /**
     * Method for drawing and pooling PistolBullets.
     */
    public void drawPistolBullets() {
        List<PistolBullet> currentBullets = clientWorld.getPistolBullets();
        isRenderingBullets = true;
        try {
            for (int i = 0; i < currentBullets.size(); i++) {
                PistolBullet bullet = currentBullets.get(i);
                PistolBullet newPistolBullet = pistolBulletPool.obtain();
                newPistolBullet.makePistolBullet(bullet.getBoundingBox(), bullet.getBulletTextureString(), bullet.getDirection(), bullet.getDamage());
                newPistolBullet.setTextureRegion();
                newPistolBullet.draw(batch);
                pistolBulletPool.free(newPistolBullet);
            }
            currentBullets.clear();
            isRenderingBullets = false;
        } catch (IndexOutOfBoundsException ignored) {
            System.out.println("Wrong");
        }
    }

    /**
     * Method for drawing Zombies.
     */
    public void drawZombies() {
        int size = clientWorld.getZombieHashMap().values().size();
        List<Zombie> zombieValues = new ArrayList<>(clientWorld.getZombieHashMap().values());
        for (int i = 0; i < size; i++) {
            Zombie zombie = zombieValues.get(i);
            Zombie newZombie = zombiePool.obtain();
            newZombie.makeZombie(zombie.getMovementSpeed(), zombie.getBoundingBox(), zombie.getBoundingBox().getX(), zombie.getBoundingBox().getY(), zombie.getBoundingBox().getWidth(), zombie.getBoundingBox().getHeight());
            newZombie.draw(batch);
            zombiePool.free(newZombie);
        }
    }

    /**
     * Method for sending information about new bullets that were shot and for switching weapons.
     *
     * @param keycode current input
     * @return boolean
     */
    @Override
    public boolean keyDown(int keycode) {
        PlayerGameCharacter character = clientWorld.getMyPlayerGameCharacter();
        if (keycode == Input.Keys.SPACE) {
            switch (character.getCurrentWeapon()) {
                case "regular pistol":
                    clientConnection.sendPlayerBulletInfo(character.getPlayerCharacterCurrentPistol().getBulletStraight().getBoundingBox().getX(),
                            character.getPlayerCharacterCurrentPistol().getBulletStraight().getBoundingBox().getY(), "Fire Bullet", 1, character.getPlayerDirection());
                    break;
                case "shotgun":
                    clientConnection.sendPlayerBulletInfo(character.getPlayerCharacterCurrentShotgun().getBulletStraight().getBoundingBox().getX(),
                            character.getPlayerCharacterCurrentShotgun().getBulletStraight().getBoundingBox().getY(), "Fire Bullet", 1, character.getPlayerDirection());
                    clientConnection.sendPlayerBulletInfo(character.getPlayerCharacterCurrentShotgun().getBulletLeft().getBoundingBox().getX(),
                            character.getPlayerCharacterCurrentShotgun().getBulletLeft().getBoundingBox().getY(), "Fire Bullet", 1,
                            character.getPlayerCharacterCurrentShotgun().setBulletLeftDirection(character.getPlayerDirection()));
                    clientConnection.sendPlayerBulletInfo(character.getPlayerCharacterCurrentShotgun().getBulletRight().getBoundingBox().getX(),
                            character.getPlayerCharacterCurrentShotgun().getBulletRight().getBoundingBox().getY(), "Fire Bullet", 1,
                            character.getPlayerCharacterCurrentShotgun().setBulletRightDirection(character.getPlayerDirection()));
                    break;
                case "strong pistol":
                    clientConnection.sendPlayerBulletInfo(character.getPlayerCharacterCurrentPistol().getBulletStraight().getBoundingBox().getX(),
                            character.getPlayerCharacterCurrentPistol().getBulletStraight().getBoundingBox().getY(), "Red Bullet", 3, character.getPlayerDirection());
                    break;
            }
        } else if (keycode == Input.Keys.TAB) {
            if (character.getCurrentWeapon().equals("regular pistol") && clientWorld.getScore() >= 1000) {
                character.setWeapon("shotgun");
            } else if (character.getCurrentWeapon().equals("shotgun")) {
                if (clientWorld.getScore() >= 2000) {
                    character.setWeapon("strong pistol");
                } else {
                    character.setWeapon("regular pistol");
                }
            } else if (character.getCurrentWeapon().equals("strong pistol")) {
                character.setWeapon("regular pistol");
            }
        }
        return false;
    }

    /**
     * Resizing the camera.
     */
    @Override
    public void resize(int width, int height) {
        batch.setProjectionMatrix(camera.combined);
        camera.update();
    }

    /**
     * Disposing the batch.
     */
    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void show() { }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
