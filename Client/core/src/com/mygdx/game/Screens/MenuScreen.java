package com.mygdx.game.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.GameInfo.GameClient;


public class MenuScreen extends ApplicationAdapter implements Screen {

    private Stage stage;
    private boolean create = false;
    private GameClient gameClient;
    private Skin skin;
    private TextButton startButton;
    private TextButton quitButton;
    private SpriteBatch spriteBatch;
    private Sprite sprite;
    private String playerName;
    private TextField textField;
    private BitmapFont font;

    /**
     * Constructor of the main screen.
     *
     * @param gameClient game instance of which the game screen is part of.
     */
    public MenuScreen(GameClient gameClient) {
        this.gameClient = gameClient;
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ZombieCarshel-B8rx.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 70;
        fontParameter.borderWidth = 2.5f;
        fontParameter.color = new Color(1, 1, 1, 0.5f);
        fontParameter.borderColor = new Color(0, 0, 0, 0.5f);
        font = fontGenerator.generateFont(fontParameter);
        font.setUseIntegerPositions(false);
        fontGenerator.dispose();
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * This method creates the menu screen using Scene2d part of libGDX.
     *
     * The background of the screen is uiBackground2.jpg.
     * Skin used is uiskin.json.
     * All resources are stored in Client/core/assets.
     */
    @Override
    public void create() {
        StretchViewport stretchViewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Set background
        spriteBatch = new SpriteBatch();
        sprite = new Sprite(new Texture(Gdx.files.internal("MainScreen.png")));
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Setup stage and first screen.
        this.stage = new Stage(stretchViewport);

        Table table = new Table();

        createScreenSetup(table);

        // Dialog for event that player hasn't given name.
        Dialog dialog = new Dialog("Invalid username!", skin);
        dialog.getTitleLabel().setColor(skin.getColor("red"));


        createTextField(table);

        createStart(table, dialog);

        // Adds start button to table and creates a space of 300 units between it and Quit button.

        createQuit(table);

        this.create = true;
    }

    public void createScreenSetup(Table table) {
        table.setWidth(stage.getWidth());
        table.align(Align.center|Align.top);

        table.setPosition(0, Gdx.graphics.getHeight());

        // Skins
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Setup input processor, so that stage can handle inputs.
        InputMultiplexer im = new InputMultiplexer(stage);
        Gdx.input.setInputProcessor(im);
    }

    /**
     * Creates text field for screen
     */
    public void createTextField(Table table) {
        table.padTop(205);
        // Username text field.
        textField = new TextField("Enter username", skin, "default");
        table.add(textField).width(220).height(50).padBottom(40);
        table.row();
    }

    /**
     * Creates start button
     */
    public void createStart(Table table, Dialog dialog) {
        // Create start button with json defined style.
        startButton = new TextButton("Start", skin, "default");

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPlayerName(textField.getText());
                if (playerName.equals("") || playerName.equals("Enter username") || playerName.length() <= 2) {
                    // Shows message that name is incorrect
                    dialog.show(stage);
                    dialog.setPosition(stage.getWidth() / 2 - 115, 240);
                    dialog.setSize(250, 50);
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            dialog.hide();
                        }}, 1);
                } else {
                    setPlayerName(playerName);
                    gameClient.setPlayerName(playerName);
                    gameClient.startGame();
                }
            }
        });
        // Adds space between top and start button.
        table.add(startButton).width(100).padBottom(80);

        // Adds start button to table and creates a space of 300 units between it and Quit button.
    }

    /**
     *  Create quit button
     */
    public void createQuit(Table table) {
        table.row();
        // Create quit button with json defined style.
        quitButton = new TextButton("Quit", skin, "default");
        quitButton.setWidth(300);
        quitButton.setHeight(80);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        table.add(quitButton);
        stage.addActor(table);
    }

    public Stage getStage() {
        return stage;
    }

    public void showFull(Stage stage) {
        Dialog dialog = new Dialog("Server is full, please wait for a while!", skin);
        dialog.getTitleLabel().setColor(skin.getColor("red"));
        dialog.show(stage);
        dialog.setPosition(stage.getWidth() / 2 - 115, 240);
        dialog.setSize(250, 50);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                dialog.hide();
            }}, 1);
    }

    /**
     * Resize.
     *
     * @param width of the window
     * @param height of the window
     */
    @Override
    public void resize (int width, int height) {
        if (stage != null) {
            stage.getViewport().update(width, height, true);
        }
    }

    /**
     * This is here because of the implemented method, but it helps to display the window.
     */
    @Override
    public void show() {
    }

    /**
     * This method renders menu of the game.
     *
     * @param view is not used but it is for view.
     */
    @Override
    public void render(float view) {
        if (!this.create) {
            create();
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (stage != null) {
            spriteBatch.begin();
            sprite.draw(spriteBatch);
            font.draw(spriteBatch, "Zombieshooter", stage.getWidth() / 2 - 306, 400);
            spriteBatch.end();
            stage.draw();
            stage.act();
        }
    }

    /**
     * Enable hiding window.
     */
    @Override
    public void hide() {

    }
}
