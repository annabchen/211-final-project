package test.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class TreasureRoom2 implements Screen {
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
    final int TILE_ROWS = 15;
    final int TILE_COLS = 24;
    ShapeRenderer shapeRenderer;
    int[][] maze;
    float worldWidth;
    float worldHeight;
    Rectangle wallRectangle;
    float wallRectHeight;
    float wallRectWidth;

    public TreasureRoom2(final DungeonAdventure game, Vertex vertex) {
        this.game = game;
        this.vertex = vertex;

        // loads the assets into memory after libgdx has started
        backgroundTexture = new Texture("background.jpeg");
        playerTexture = new Texture("player.jpeg");
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        music.setLooping(true);
        music.setVolume(.5f);

        playerSprite = new Sprite(playerTexture);
        playerSprite.setSize(.4f, .4f);

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

        int [][] maze = new int[][]{
            {1,1,1,1,1,0,0,1,1,1,1,0,0,1,1,1,1,0,0,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,1,0,0,0,0,1,0,1},
            {1,1,1,1,1,1,1,1,1,0,0,1,1,1,1,0,0,1,1,0,0,1,0,1},
            {1,1,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,1,1,0,1,0,1},
            {1,0,0,1,1,0,0,1,0,0,1,0,1,1,0,1,1,0,1,0,0,1,0,1},
            {1,0,0,0,0,1,0,1,0,1,0,0,0,1,0,1,0,0,1,0,0,1,0,1},
            {1,0,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,0,1,0,0,1,0,1},
            {1,0,1,1,0,1,0,1,0,0,1,1,0,1,0,1,1,0,1,1,0,1,0,1},
            {1,0,1,0,0,1,0,1,0,1,0,1,0,0,0,0,1,0,0,1,0,0,0,1},
            {1,0,1,0,1,0,0,1,0,1,0,1,0,0,1,0,0,0,0,1,1,0,0,1},
            {1,0,0,0,1,0,1,0,0,0,1,0,0,1,0,0,0,0,1,0,1,0,1,1},
            {1,0,0,1,1,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,1,0,1,1},
            {1,0,1,0,0,1,1,1,1,1,1,1,0,0,0,1,0,0,0,1,0,0,1,1},
            {0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,1,0,0,0,0,1,1},
            {0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
            };

        this.maze = new int[maze.length][maze[0].length];
        for (int i = 0; i < maze.length; i++) {
            this.maze[maze.length-1-i] = maze[i];
        }

        wallRectWidth = worldWidth / TILE_COLS;
        wallRectHeight = worldHeight / TILE_ROWS;

        wallRectangle = new Rectangle();
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
        float speed = 3f;
        float delta = Gdx.graphics.getDeltaTime();

        // separate x and y so that we can independently apply them when we check for collisions
        float dirX = 0f;
        float dirY = 0f;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            dirX += speed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            dirX -= speed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            dirY += speed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            dirY -= speed * delta;
        }

        float playerWidth = playerSprite.getWidth();
        float playerHeight = playerSprite.getHeight();

        playerRectangle.set(playerSprite.getX() + dirX, playerSprite.getY() + dirY, playerWidth, playerHeight);

        outer:
        for (int i = 0; i < TILE_ROWS; i++) {
            for (int j = 0; j < TILE_COLS; j++) {
                if (maze[i][j] == 1) { // for example, 0 is a wall
                    wallRectangle.set(j * wallRectWidth, i * wallRectHeight, wallRectWidth, wallRectHeight);
                    if (wallRectangle.overlaps(playerRectangle)) {
                        dirX = 0.0f;
                        dirY = 0.0f;
                        break outer;
                    }
                } else if (maze[i][j] == 0) {
                    // more maze logic with treasure or something
                }
            }
        }
        playerSprite.translate(dirX, dirY);
    }

    private void logic() {
        // store player size
        float playerWidth = playerSprite.getWidth();
        float playerHeight = playerSprite.getHeight();

        doorLeftSprite.setX(worldWidth / 2 - (wallRectWidth * 6));
        doorLeftSprite.setY(worldHeight - doorLeftSprite.getHeight());
        doorLeftRectangle.set(doorLeftSprite.getX(), doorLeftSprite.getY(), doorLeftSprite.getWidth(), doorLeftSprite.getHeight());
        if (playerRectangle.overlaps(doorLeftRectangle)) {
            Vertex leftDoor = vertex.vertices[0];
            Screen nextRoom = leftDoor.screenFactory.apply(game, leftDoor);
            game.setScreen(nextRoom);
            dispose();
        }

        doorTopSprite.setX(worldWidth / 2 - wallRectWidth);
        doorTopSprite.setY(worldHeight - doorTopSprite.getHeight());
        doorTopRectangle.set(doorTopSprite.getX(), doorTopSprite.getY(), doorTopSprite.getWidth(), doorTopSprite.getHeight());
        if (playerRectangle.overlaps(doorTopRectangle)) {
            Vertex topDoor = vertex.vertices[1];
            Screen nextRoom = topDoor.screenFactory.apply(game, topDoor);
            System.out.println("entering left door");
            game.setScreen(nextRoom);
            dispose();
        }

        doorRightSprite.setX(worldWidth / 2 + (wallRectWidth * 5));
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
        ScreenUtils.clear(Color.WHITE);
        game.viewport.apply();

        shapeRenderer.setProjectionMatrix(game.viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        for (int i = 0; i < TILE_ROWS; i++) {
            for (int j = 0; j < TILE_COLS; j++) {
                if (maze[i][j] == 1) {
                    shapeRenderer.rect(j * wallRectWidth, i * wallRectHeight, wallRectWidth, wallRectHeight);
                }
            }

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
