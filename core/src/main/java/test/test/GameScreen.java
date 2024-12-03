package test.test;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen implements Screen {
    final Drop game;
    Texture backgroundTexture;
    Texture dropTexture;
    Texture bucketTexture;
    Sound dropSound;
    Music music;
    private SpriteBatch batch;
    private Texture image;
    SpriteBatch spriteBatch;
    FitViewport viewport;
    Sprite bucketSprite; // sprite can keep its properties
    Vector2 touchPos;
    Array<Sprite> dropSprites;
    float dropTimer;
    // for collision
    Rectangle bucketRectangle;
    Rectangle dropRectangle;
    int dropsGathered;
    public GameScreen(final Drop game) {
        this.game = game;

        // loads the assets into memory after libgdx has started
        backgroundTexture = new Texture("background.png");
        bucketTexture = new Texture("bucket.png");
        dropTexture = new Texture("drop.png");
        // sound are loaded completely into memory se they can be played quickly and repeatedly
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        // music is streamed from the file in chunks
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        music.setLooping(true);
        music.setVolume(.5f);

        bucketSprite = new Sprite(bucketTexture);
        bucketSprite.setSize(1, 1);

        // use to find where player clicked, reuse Vector2 to reduce lag
        touchPos = new Vector2();

        // use a list to keep track of raindrops
        dropSprites = new Array<>();

        bucketRectangle = new Rectangle();
        dropRectangle = new Rectangle();
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
    private void input(){
        // dictates how fast the bucket moves
        float speed = 4f;
        float delta = Gdx.graphics.getDeltaTime();

        // check if a key is pressed every time a frame is drawn
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            // move the bucket right
            bucketSprite.translateX(speed * delta);
        } else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            // move the bucket left
            bucketSprite.translateX(-speed * delta);
        }
        if(Gdx.input.isTouched()) {
            // if user has clicked or tapped the screen move bucket to where touch happened
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            // convert the units to the world units of the viewport
            game.viewport.unproject(touchPos);
            // change the horizontally centered position of the bucket
            bucketSprite.setCenterX(touchPos.x);
        }
    }
    private void logic(){
        // store worldWidth and worldHeight as local variables
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();
        // store bucket size
        float bucketWidth = bucketSprite.getWidth();
        float bucketHeight = bucketSprite.getHeight();
        // drop logic
        float delta = Gdx.graphics.getDeltaTime();

        // use clamp method to prevent user from going outside of 0 and worldwidth minus one unit from the right
        bucketSprite.setX(MathUtils.clamp(bucketSprite.getX(), 0, worldWidth - bucketWidth));
        // apply the bucket position and size to the bucketRectangle
        bucketRectangle.set(bucketSprite.getX(), bucketSprite.getY(), bucketWidth, bucketHeight);
        // loop through the drop sprites to prevent out of bounds errors
        // otheriwse- using a normal loop, would get laggier the longer the game ran
        for (int i = dropSprites.size - 1; i >= 0; i--){
            Sprite dropSprite = dropSprites.get(i);
            float dropWidth = dropSprite.getWidth();
            float dropHeight = dropSprite.getHeight();

            dropSprite.translateY(-2f * delta);
            // apply the drop position and size to the dropRectangle
            dropRectangle.set(dropSprite.getX(), dropSprite.getY(), dropWidth, dropHeight);
            // remove if the top of the drop goes below the bottom of the window
            if (dropSprite.getY() < -dropHeight){
                dropSprites.removeIndex(i);
            } else if (bucketRectangle.overlaps(dropRectangle)){ // check for overlap
                dropsGathered++;
                dropSprites.removeIndex(i); // remove drop
                dropSound.play();
            }
        }

        // spacing out the droplets
        dropTimer += delta;
        if(dropTimer > 1f){ // check if it has been over a second
            dropTimer = 0; // reset timer
            createDroplet(); // create droplet
        }
    }

    private void draw(){
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
        bucketSprite.draw(game.batch);
        // display drops caught in upper left corner
        game.font.draw(game.batch, "Drops collected: " + dropsGathered, 0, worldHeight);

        // draw each drop
        for (Sprite dropSprite : dropSprites){
            dropSprite.draw(game.batch);
        }

        game.batch.end();
    }

    private void createDroplet() {
        // creating local variables
        float dropWidth = 1;
        float dropHeight = 1;
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();
        // create drop sprite
        Sprite dropSprite = new Sprite(dropTexture);
        dropSprite.setSize(dropWidth,dropHeight);
        dropSprite.setX(MathUtils.random(0f, worldWidth - dropWidth));
        dropSprite.setY(worldHeight);
        dropSprites.add(dropSprite); // adding it to the list
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
        dropSound.dispose();
        music.dispose();
        dropTexture.dispose();
        bucketTexture.dispose();
    }
}
