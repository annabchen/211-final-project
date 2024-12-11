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

public class EnemyRoom2 implements Screen {
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
    Array<Enemy> enemies2;
    float enemyTimer;
    // for collision
    Rectangle playerRectangle;
    Rectangle enemyRectangle;
    Rectangle enemyRectangle2;
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
    Vector2 bullet_emitter1;
    Vector2 bullet_emitter2;
    float worldWidth;
    float worldHeight;
    int enemyDirection;
    int enemyDirection2;


    public EnemyRoom2(final DungeonAdventure game, Vertex vertex) {
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
        enemies2 = new Array<>();

        playerRectangle = new Rectangle();
        enemyRectangle = new Rectangle();
        enemyRectangle2 = new Rectangle();

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

        bullet_emitter1 = new Vector2();
        bullet_emitter2 = new Vector2();

        worldWidth = game.viewport.getWorldWidth();
        worldHeight = game.viewport.getWorldHeight();

        bullet_emitter1.set(3, worldHeight - 3);
        bullet_emitter2.set(worldWidth - 3, 3);

        enemyDirection = 0;
        enemyDirection2 = 0;

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
        float delta = Gdx.graphics.getDeltaTime();

        doorLeftSprite.setX(worldWidth / 2 - (doorLeftSprite.getWidth() * 2));
        doorLeftSprite.setY(worldHeight - doorLeftSprite.getHeight());
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
            System.out.println("entering left door");
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
                game.playerHealth -= 5;
                if (game.playerHealth <= 0) {
                    game.setScreen(new EndScreen(game));
                    dispose();
                }
                enemies.removeIndex(i); // remove enemy
                enemySound.play();
            }
        }

        for (int i = enemies2.size - 1; i >= 0; i--) {
            Enemy enemy2 = enemies2.get(i);
            Sprite enemySprite2 = enemy2.sprite;
            float enemyWidth = enemySprite2.getWidth();
            float enemyHeight = enemySprite2.getHeight();

            enemy2.move();

            // apply the enemy position and size to the enemyRectangle
            enemyRectangle.set(enemySprite2.getX(), enemySprite2.getY(), enemyWidth, enemyHeight);
            // remove if the top of the enemy goes below the bottom of the window
            if (enemySprite2.getY() < -enemyHeight) {
                enemies2.removeIndex(i);
            } else if (playerRectangle.overlaps(enemyRectangle)) { // check for overlap
                game.playerHealth -= 5;
                if (game.playerHealth <= 0) {
                    game.setScreen(new EndScreen(game));
                    dispose();
                }
                enemies2.removeIndex(i); // remove enemy
                enemySound.play();
            }
        }

        // spacing out the enemy
        enemyTimer += delta;
        if (enemyTimer > .25f) {
            enemyTimer = 0; // reset timer
            createEnemy(); // create enemy
        }
    }

    private void draw() {
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
        game.font.draw(game.batch, "Health: " + game.playerHealth, 0, worldHeight);
        doorTopSprite.draw(game.batch);
        doorLeftSprite.draw(game.batch);
        doorRightSprite.draw(game.batch);


        // draw each enemy
        for (Enemy enemy : enemies) {
            enemy.sprite.draw(game.batch);
        }
        for (Enemy enemy : enemies2) {
            enemy.sprite.draw(game.batch);
        }

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

        Sprite enemySprite2 = new Sprite(enemyTexture);
        enemySprite2.setSize(enemyWidth, enemyHeight);
        enemySprite2.setX(bullet_emitter2.x);
        enemySprite2.setY(bullet_emitter2.y);

        Vector2 direction2;

        if (enemyDirection2 % 4 == 0) {
            direction2 = new Vector2(0, -2f);
            enemyDirection2++;
        } else if (enemyDirection2 % 4 == 1) {
            direction2 = new Vector2(-2f, 0);
            enemyDirection2++;
        } else if (enemyDirection2 % 4 == 2) {
            direction2 = new Vector2(0, 2f);
            enemyDirection2++;
        } else {
            direction2 = new Vector2(2f, 0);
            enemyDirection2 = 0;
        }

        Enemy enemy2 = new Enemy(enemySprite2, direction2);
        enemies.add(enemy2);
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
