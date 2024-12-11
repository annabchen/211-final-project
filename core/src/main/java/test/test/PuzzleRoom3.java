package test.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.Iterator;

public class PuzzleRoom3 implements Screen {
    final DungeonAdventure game;
    Texture backgroundTexture;
    Texture playerTexture;
    Music music;
    private SpriteBatch batch;
    private Texture image;
    SpriteBatch spriteBatch;
    FitViewport viewport;
    Sprite playerSprite; // sprite can keep its properties
    // for collision
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
    final int TILE_ROWS = 5;
    final int TILE_COLS = 8;
    ShapeRenderer shapeRenderer;
    ArrayList<Rectangle> exited;
    ArrayList<Rectangle> in;
    ArrayList<Rectangle> notEntered;
    float worldWidth;
    float worldHeight;

    public PuzzleRoom3(final DungeonAdventure game, Vertex vertex) {
        this.game = game;
        this.vertex = vertex;

        // loads the assets into memory after libgdx has started
        backgroundTexture = new Texture("background.jpeg");
        playerTexture = new Texture("player.jpeg");
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

        shapeRenderer = new ShapeRenderer();

        worldWidth = game.viewport.getWorldWidth();
        worldHeight = game.viewport.getWorldHeight();
        float tileWidth = worldWidth / TILE_COLS;
        float tileHeight = worldHeight / TILE_ROWS;

        notEntered = new ArrayList<>();
        in = new ArrayList<>();
        exited = new ArrayList<>();

        // instantiating tiles
        for (int i = 0; i < TILE_ROWS; i++) {
            for (int j = 0; j < TILE_COLS; j++) {
                Rectangle tile = new Rectangle(j * tileWidth, i * tileHeight, tileWidth, tileHeight);
                notEntered.add(tile);
            }
        }
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
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            // move the player right
            playerSprite.translateX(speed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            // move the player left
            playerSprite.translateX(-speed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            // move player up
            playerSprite.translateY(speed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            // move player down
            playerSprite.translateY(-speed * delta);
        }

        float playerWidth = playerSprite.getWidth();
        float playerHeight = playerSprite.getHeight();

        playerRectangle.set(playerSprite.getX(), playerSprite.getY(), playerWidth, playerHeight);

        // check all the exited tiles to see if we collide
        for (Rectangle tile : exited){
            if (playerRectangle.overlaps(tile)){
                // send player to ending screen to restart
                Screen PuzzleRoom1ending = vertex.screenFactory.apply(game, vertex);
                game.setScreen(PuzzleRoom1ending);
                dispose();
            }
        }

        for (Iterator<Rectangle> currTile = in.iterator(); currTile.hasNext();) {
            Rectangle tile = currTile.next();
            if(!playerRectangle.overlaps(tile)){
                currTile.remove();
                exited.add(tile);
            }
        }

        for (Iterator<Rectangle> currTile = notEntered.iterator(); currTile.hasNext(); ){
            Rectangle tile = currTile.next();
            if(playerRectangle.overlaps(tile)){
                currTile.remove();
                in.add(tile);
            }
        }
    }

    private void logic() {
        // store player size
        float playerWidth = playerSprite.getWidth();
        float playerHeight = playerSprite.getHeight();
        float tileWidth = worldWidth / TILE_COLS;

        doorLeftSprite.setX(1.5f*tileWidth);
        doorLeftSprite.setY(.5f*tileWidth);
        doorLeftRectangle.set(doorLeftSprite.getX(), doorLeftSprite.getY(), doorLeftSprite.getWidth(), doorLeftSprite.getHeight());
        if (playerRectangle.overlaps(doorLeftRectangle) && notEntered.isEmpty()) {
            Vertex leftDoor = vertex.vertices[0];
            Screen nextRoom = leftDoor.screenFactory.apply(game, leftDoor);
            game.setScreen(nextRoom);
            dispose();
        }

        doorTopSprite.setX(5.5f*tileWidth);
        doorTopSprite.setY(0.5f*tileWidth);
        doorTopRectangle.set(doorTopSprite.getX(), doorTopSprite.getY(), doorTopSprite.getWidth(), doorTopSprite.getHeight());
        if (playerRectangle.overlaps(doorTopRectangle) && notEntered.isEmpty()) {
            Vertex topDoor = vertex.vertices[1];
            Screen nextRoom = topDoor.screenFactory.apply(game, topDoor);
            System.out.println("entering left door");
            game.setScreen(nextRoom);
            dispose();
        }

        doorRightSprite.setX(5.5f*tileWidth);
        doorRightSprite.setY(worldHeight-0.5f*tileWidth);
        doorRightRectangle.set(doorRightSprite.getX(), doorRightSprite.getY(), doorRightSprite.getWidth(), doorRightSprite.getHeight());
        if (playerRectangle.overlaps(doorRightRectangle) && notEntered.isEmpty()) {
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

        shapeRenderer.setProjectionMatrix(game.viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        for (Rectangle tile: notEntered) {
            shapeRenderer.rect(tile.getX(), tile.getY(), tile.getWidth(),tile.getHeight());
        }
        shapeRenderer.setColor(Color.RED);
        for (Rectangle tile: in) {
            shapeRenderer.rect(tile.getX(), tile.getY(), tile.getWidth(),tile.getHeight());
        }
        shapeRenderer.setColor(Color.BLUE);
        for (Rectangle tile: exited) {
            shapeRenderer.rect(tile.getX(), tile.getY(), tile.getWidth(),tile.getHeight());
        }
        shapeRenderer.end();

        // shows how the Viewport is applied to the SpriteBatch, shows the images in the correct place
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.begin();

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
