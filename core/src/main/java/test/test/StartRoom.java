package test.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class StartRoom implements Screen {
    final DungeonAdventure game;
    Texture backgroundTexture;
    Texture playerTexture;
    Music music;
    private SpriteBatch batch;
    private Texture image;
    SpriteBatch spriteBatch;
    FitViewport viewport;
    Sprite playerSprite; // sprite can keep its properties
    Rectangle playerRectangle;
    Texture doorTexture;
    Rectangle doorTopRectangle;
    Sprite doorTopSprite;
    Texture door2Texture;
    Rectangle doorLeftRectangle;
    Sprite doorLeftSprite;
    Texture door3Texture;
    Rectangle doorRightRectangle;
    Sprite doorRightSprite;

    Vertex vertex;

    public StartRoom(final DungeonAdventure game, Vertex vertex) {
        this.game = game;
        this.vertex = vertex;

        // loads the assets into memory after libgdx has started
        backgroundTexture = new Texture("background.jpeg");
        playerTexture = new Texture("player.jpeg");
        // sound are loaded completely into memory se they can be played quickly and repeatedly
        // music is streamed from the file in chunks
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        music.setLooping(true);
        music.setVolume(.5f);

        playerSprite = new Sprite(playerTexture);
        playerSprite.setSize(1, 1);


        playerRectangle = new Rectangle();

        doorTopRectangle = new Rectangle();
        doorTexture = new Texture("door.png");
        doorTopSprite = new Sprite(doorTexture);
        doorTopSprite.setSize(1, 1);

        doorLeftRectangle = new Rectangle();
        door2Texture = new Texture("door2.png");
        doorLeftSprite = new Sprite(door2Texture);
        doorLeftSprite.setSize(1, 1);

        doorRightRectangle = new Rectangle();
        door3Texture = new Texture("door3.png");
        doorRightSprite = new Sprite(door3Texture);
        doorRightSprite.setSize(1, 1);
    }


    @Override
    public void show() {
        // start playing background music when screen is shown
        music.play();
    }

    @Override
    public void render(float delta) {
        // organize code into three methods
        input();
        logic();
        draw();
    }

    private void input() {
        // dictates how fast the player moves
        float speed = 4f;
        float delta = Gdx.graphics.getDeltaTime();

        // check if a key is pressed every time a frame is drawn
        // start with diagonals
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.UP)) {
            // move the player left + up
            playerSprite.translateX(-speed * delta);
            playerSprite.translateY(speed * delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.UP)) {
            // move player right + up
            playerSprite.translateX(speed * delta);
            playerSprite.translateY(speed * delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            // move player left + down
            playerSprite.translateX(-speed * delta);
            playerSprite.translateY(-speed * delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            // move player right + down
            playerSprite.translateX(speed * delta);
            playerSprite.translateY(-speed * delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            // move the player right
            playerSprite.translateX(speed * delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            // move the player left
            playerSprite.translateX(-speed * delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            // move player up
            playerSprite.translateY(speed * delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            // move player down
            playerSprite.translateY(-speed * delta);
        }
    }

    private void logic() {
        // store worldWidth and worldHeight as local variables
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();
        // store player size
        float playerWidth = playerSprite.getWidth();
        float playerHeight = playerSprite.getHeight();
        // enemy logic
        float delta = Gdx.graphics.getDeltaTime();

        doorLeftSprite.setX(worldWidth / 2 - (doorLeftSprite.getWidth() * 2));
        doorLeftSprite.setY(worldHeight - doorTopSprite.getHeight());
        doorLeftRectangle.set(doorLeftSprite.getX(), doorLeftSprite.getY(), doorLeftSprite.getWidth(), doorLeftSprite.getHeight());
        if (playerRectangle.overlaps(doorLeftRectangle)) {
            Vertex leftDoor = vertex.vertices[0];
            Screen nextRoom = leftDoor.screenFactory.apply(game, leftDoor);
            game.setScreen(nextRoom);
            dispose();
        }

        doorTopSprite.setX(worldWidth / 2);
        doorTopSprite.setY(worldHeight - doorTopSprite.getHeight());
        doorTopRectangle.set(doorTopSprite.getX(), doorTopSprite.getY(), doorTopSprite.getWidth(), doorTopSprite.getHeight());
        if (playerRectangle.overlaps(doorTopRectangle)) {
            Vertex topDoor = vertex.vertices[1];
            Screen nextRoom = topDoor.screenFactory.apply(game, topDoor);
            game.setScreen(nextRoom);
            dispose();
        }

        doorRightSprite.setX(worldWidth / 2 + (doorRightSprite.getWidth() * 2));
        doorRightSprite.setY(worldHeight - doorRightSprite.getHeight());
        doorRightRectangle.set(doorRightSprite.getX(), doorRightSprite.getY(), doorRightSprite.getWidth(), doorRightSprite.getHeight());
        if (playerRectangle.overlaps(doorRightRectangle)) {
            Vertex leftDoor = vertex.vertices[2];
            Screen nextRoom = leftDoor.screenFactory.apply(game, leftDoor);
            game.setScreen(nextRoom);
            dispose();
        }

        // use clamp method to prevent user from going outside of 0 and worldwidth minus one unit from the right
        playerSprite.setX(MathUtils.clamp(playerSprite.getX(), 0, worldWidth - playerWidth));
        playerSprite.setY(MathUtils.clamp(playerSprite.getY(), 0, worldHeight - playerHeight));
        // apply the player position and size to the playerRectangle
        playerRectangle.set(playerSprite.getX(), playerSprite.getY(), playerWidth, playerHeight);

    }

    private void draw() {
        // clears screen
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        // shows how the Viewport is applied to the SpriteBatch, shows the images in the correct place
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.begin();
        // store worldWidth and wordleHeight as local variables
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        // draw background
        game.batch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
        // sprites have their own draw method
        playerSprite.draw(game.batch);
        // display enemies caught in upper left corner
        game.font.draw(game.batch, "Health: " + game.playerHealth, 0, worldHeight);
        doorTopSprite.draw(game.batch);
        doorLeftSprite.draw(game.batch);
        doorRightSprite.draw(game.batch);

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        music.dispose();
        playerTexture.dispose();
    }
}
