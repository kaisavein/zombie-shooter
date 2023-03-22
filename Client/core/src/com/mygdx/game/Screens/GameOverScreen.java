package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.GameInfo.ClientWorld;
import com.mygdx.game.GameInfo.GameClient;

public class GameOverScreen extends MenuScreen {

    private Stage stage;
    private boolean create = false;
    private Skin skin;
    private TextButton quitButton;
    private SpriteBatch spriteBatch;
    private Sprite sprite;
    private ClientWorld clientWorld;

    /**
     * GameOverScreen constructor.
     *
     * @param gameClient instance of the game that the screen is apart of.
     * @param clientWorld of the game, this is needed because this will be used to get games final score.
     */
    public GameOverScreen(GameClient gameClient, ClientWorld clientWorld) {
        super(gameClient);
        this.clientWorld = clientWorld;

    }

    /**
     * Create game over screen.
     *
     * The resources are the same that are on MenuScreen - uiBackground.jpg and uiskin.json are in Clinet/core/assets
     */
    @Override
    public void create() {
        StretchViewport stretchViewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Set background
        spriteBatch = new SpriteBatch();
        sprite = new Sprite(new Texture(Gdx.files.internal("MainScreen.png")));
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Setup stage and first screen
        this.stage = new Stage(stretchViewport);

        Table table = new Table();

        table.setWidth(stage.getWidth());
        table.align(Align.center|Align.top);

        table.setPosition(0, Gdx.graphics.getHeight());

        // Skins
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Setup input processor, so that stage can handle inputs.
        InputMultiplexer im = new InputMultiplexer(stage);
        Gdx.input.setInputProcessor(im);

        table.padTop(200);


        // This will display the game's score on the end screen.
        TextField textField = new TextField("Game over! \n Your score is " + clientWorld.getScore()
                , skin, "default");

        table.add(textField).width(600).height(80).padBottom(100);
        table.row();

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

        this.create = true;
    }


    /**
     * Render the game over screen.
     *
     * @param view is not used but it is for view
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
            spriteBatch.end();
            stage.draw();
            stage.act();
        }
    }
}
