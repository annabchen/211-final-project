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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
// for this room maybe can have bullets/ enemies emit from certain points on the screen?
// can set the points at random locations + have them chang eover a period of time

public class EnemyRoom3 implements Screen {
    final DungeonAdventure game;
    Texture backgroundTexture;
    Texture enemyTexture;
    Texture playerTexture;
    Sound enemySound;
    Music music;
    private SpriteBatch batch;
    private Texture image;
    SpriteBatch spriteBatch;
    FitViewport viewport;
    Sprite playerSprite; // sprite can keep its properties
    Vector2 touchPos;
    // stores vertically dropping enemies
    Array<Enemy> enemies;
    // stores horizontal enemies
    Array<Sprite> enemySprites2;
    float enemyTimer;
    // for collision
    Rectangle playerRectangle;
    Rectangle enemyRectangle;
    Rectangle enemyRectangle2;
    Texture doorTexture;
    Rectangle doorRectangle;
    Sprite doorSprite;
    Texture door2Texture;
    Rectangle door2Rectangle;
    Sprite door2Sprite;
    Texture door3Texture;
    Rectangle door3Rectangle;
    Sprite door3Sprite;
    int health = 100;
    Vertex vertex;
    Vector2 bullet_emitter1;
    Vector2 bullet_emitter2;
    float worldWidth;
    float worldHeight;
    int enemyDirection;


    public EnemyRoom3(final DungeonAdventure game, Vertex vertex) {
        this.game = game;
        this.vertex = vertex;

        // loads the assets into memory after libgdx has started
        backgroundTexture = new Texture("background.jpeg");
        playerTexture = new Texture("player.jpeg");
        enemyTexture = new Texture("enemy.jpeg");
        // sound are loaded completely into memory se they can be played quickly and repeatedly
        enemySound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        // music is streamed from the file in chunks
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        music.setLooping(true);
        music.setVolume(.5f);

        playerSprite = new Sprite(playerTexture);
        playerSprite.setSize(1, 1);

        // use to find where player clicked, reuse Vector2 to reduce lag
        touchPos = new Vector2();

        // use a list to keep track of enemies
        enemies = new Array<>();
        enemySprites2 = new Array<>();

        playerRectangle = new Rectangle();
        enemyRectangle = new Rectangle();
        enemyRectangle2 = new Rectangle();

        doorRectangle = new Rectangle();
        doorTexture = new Texture("door.png");
        doorSprite = new Sprite(doorTexture);
        doorSprite.setSize(1, 1);

        door2Rectangle = new Rectangle();
        door2Texture = new Texture("door2.png");
        door2Sprite = new Sprite(door2Texture);
        door2Sprite.setSize(1, 1);

        door3Rectangle = new Rectangle();
        door3Texture = new Texture("door3.png");
        door3Sprite = new Sprite(door3Texture);
        door3Sprite.setSize(1, 1);

        bullet_emitter1 = new Vector2();
        bullet_emitter2 = new Vector2();

        worldWidth = game.viewport.getWorldWidth();
        worldHeight = game.viewport.getWorldHeight();

        bullet_emitter1.set(worldWidth / 2, worldHeight / 2);
        //bullet_emitter1.set(MathUtils.random(0f, worldWidth), MathUtils.random(0f, worldHeight));
        bullet_emitter2.set(MathUtils.random(0f, worldWidth), MathUtils.random(0f, worldHeight));

        enemyDirection = 0;
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
    }

    private void logic() {
        // store player size
        float playerWidth = playerSprite.getWidth();
        float playerHeight = playerSprite.getHeight();
        // enemy logic
        float delta = Gdx.graphics.getDeltaTime();

        doorSprite.setX(worldWidth / 2);
        doorSprite.setY(worldHeight - doorSprite.getHeight());
        doorRectangle.set(doorSprite.getX(), doorSprite.getY(), doorSprite.getWidth(), doorSprite.getHeight());
        if (playerRectangle.overlaps(doorRectangle)) {
            Vertex topDoor = vertex.vertices[0];
            Screen nextRoom = topDoor.screenFactory.apply(game, topDoor);
            game.setScreen(nextRoom);
            dispose();
        }

        door2Sprite.setX(worldWidth / 2 - (door2Sprite.getWidth() * 2));
        door2Sprite.setY(worldHeight - doorSprite.getHeight());
        door2Rectangle.set(door2Sprite.getX(), door2Sprite.getY(), door2Sprite.getWidth(), door2Sprite.getHeight());
        if (playerRectangle.overlaps(door2Rectangle)) {
            Vertex leftDoor = vertex.vertices[1];
            Screen nextRoom = leftDoor.screenFactory.apply(game, leftDoor);
            game.setScreen(nextRoom);
            dispose();
        }

        door3Sprite.setX(worldWidth / 2 + (door3Sprite.getWidth() * 2));
        door3Sprite.setY(worldHeight - door3Sprite.getHeight());
        door3Rectangle.set(door3Sprite.getX(), door3Sprite.getY(), door3Sprite.getWidth(), door3Sprite.getHeight());
        if (playerRectangle.overlaps(door3Rectangle)) {
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
        // loop through the enemy sprites to prevent out of bounds errors
        // otheriwse- using a normal loop, would get laggier the longer the game ran

        for (int i = enemies.size - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            Sprite enemySprite = enemy.sprite;
            float enemyWidth = enemySprite.getWidth();
            float enemyHeight = enemySprite.getHeight();

            enemy.move();

            // apply the enemy position and size to the enemyRectangle
            enemyRectangle.set(enemySprite.getX(), enemySprite.getY(), enemyWidth, enemyHeight);
            // remove if the top of the enemy goes below the bottom of the window
            if (enemySprite.getY() < -enemyHeight) {
                enemies.removeIndex(i);
            } else if (playerRectangle.overlaps(enemyRectangle)) { // check for overlap
                health -= 5;
                if (health <= 0) {
                    game.setScreen(new EndScreen(game));
                    dispose();
                }
                enemies.removeIndex(i); // remove enemy
                enemySound.play();
            }
        }

        // spacing out the enemy
        enemyTimer += delta;
        if (enemyTimer > .5f) { // check if it has been over a second
            enemyTimer = 0; // reset timer
            createEnemy(); // create enemy
        }
    }

    private void draw() {
        float delta = Gdx.graphics.getDeltaTime();
        // clears screen
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        // shows how the Viewport is applied to the SpriteBatch, shows the images in the correct place
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.begin();

        // draw background
        game.batch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
        // sprites have their own draw method
        playerSprite.draw(game.batch);
        // display enemies caught in upper left corner
        game.font.draw(game.batch, "Health: " + health, 0, worldHeight);
        doorSprite.draw(game.batch);
        door2Sprite.draw(game.batch);
        door3Sprite.draw(game.batch);


        // draw each enemy
        for (Enemy enemy : enemies) {
            enemy.sprite.draw(game.batch);
        }
        /*for (Sprite enemySprite2 : enemySprites2) {
            enemySprite2.draw(game.batch);
        }*/

        game.batch.end();
    }

    private void createEnemy() {
        // creating local variables
        float enemyWidth = 1;
        float enemyHeight = 1;
        // create enemy sprite
        Sprite enemySprite = new Sprite(enemyTexture);
        enemySprite.setSize(enemyWidth, enemyHeight);
        enemySprite.setX(bullet_emitter1.x);
        enemySprite.setY(bullet_emitter1.y);

        Vector2 direction;

        if (enemyDirection % 4 == 0) {
            direction = new Vector2(0, -2f);
            enemyDirection++;
        } else if (enemyDirection % 4 == 1) {
            direction = new Vector2(-2f, 0);
            enemyDirection++;
        } else if (enemyDirection % 4 == 2) {
            direction = new Vector2(0, 2f);
            enemyDirection++;
        } else {
            direction = new Vector2(2f, 0);
            enemyDirection = 0;
        }

        Enemy enemy = new Enemy(enemySprite, direction);
        enemies.add(enemy); // adding it to the list

        /*// create enemy sprite
        Sprite enemySprite2 = new Sprite(enemyTexture);
        enemySprite2.setSize(enemyWidth, enemyHeight);
        enemySprite2.setX(bullet_emitter2.x);
        enemySprite2.setY(bullet_emitter2.y);
        enemySprites2.add(enemySprite2); // adding it to the list*/
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
        enemySound.dispose();
        music.dispose();
        enemyTexture.dispose();
        playerTexture.dispose();
    }
}
