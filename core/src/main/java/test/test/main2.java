package test.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class main2 extends ApplicationAdapter {
    private SpriteBatch batch;
    private FitViewport viewport;
    private Texture background;
    private Texture bucketTexture;
    private Sprite bucketSprite;
    private Texture dropTexture;
    private Sound dropSound;
    private Music music;
    Rectangle bucketRectangle;
    Rectangle dropRectangle;
    BitmapFont font;

    Array<Sprite> dropSprites;
    float dropTimer = 0;

    int score = 0;

    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new FitViewport(8, 5);
        background = new Texture("background.png");
        bucketTexture = new Texture("bucket.png");
        bucketSprite = new Sprite(bucketTexture);
        bucketSprite.setSize(1, 1);
        dropTexture = new Texture("drop.png");
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        dropSprites = new Array<>();
        bucketRectangle = new Rectangle();
        dropRectangle = new Rectangle();
        font = new BitmapFont();
    }

    private void createDroplet() {
        // create local variables for convenience
        float dropWidth = 1;
        float dropHeight = 1;
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        // create the drop sprite
        Sprite dropSprite = new Sprite(dropTexture);
        dropSprite.setSize(dropWidth, dropHeight);
        dropSprite.setX(MathUtils.random(0f, worldWidth - dropWidth)); // Randomize the drop's x position
        dropSprite.setY(worldHeight);
        dropSprites.add(dropSprite); // Add it to the list
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    private void input() {
        float speed = 5f;
        float delta = Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            bucketSprite.translateX(speed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            bucketSprite.translateX(-speed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            bucketSprite.translateY(speed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            bucketSprite.translateY(-speed * delta);
        }
    }

    private void logic() {
        float delta = Gdx.graphics.getDeltaTime();
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        // Store the bucket size for brevity
        float bucketWidth = bucketSprite.getWidth();
        float bucketHeight = bucketSprite.getHeight();

        bucketSprite.setX(MathUtils.clamp(bucketSprite.getX(), 0, worldWidth - bucketWidth));
        bucketSprite.setY(MathUtils.clamp(bucketSprite.getY(), 0, worldHeight - bucketHeight));
        bucketRectangle.set(bucketSprite.getX(), bucketSprite.getY(), bucketWidth, bucketHeight);

        for (int i = dropSprites.size - 1; i >= 0; i--) {
            Sprite dropSprite = dropSprites.get(i); // Get the sprite from the list
            float dropWidth = dropSprite.getWidth();
            float dropHeight = dropSprite.getHeight();

            dropSprite.translateY(-2f * delta);

            dropRectangle.set(dropSprite.getX(), dropSprite.getY(), dropWidth, dropHeight);

            // if the top of the drop goes below the bottom of the view, remove it
            if (dropSprite.getY() < -dropHeight) dropSprites.removeIndex(i);
            else if (bucketRectangle.overlaps(dropRectangle)) { // Check if the bucket overlaps the drop
                dropSprites.removeIndex(i); // Remove the drop
                score++;
            }
        }

        dropTimer += delta; // Adds the current delta to the timer
        if (dropTimer > 1f) { // Check if it has been more than a second
            dropTimer = 0; // Reset the timer
            createDroplet(); // Create the droplet
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        // store the worldWidth and worldHeight as local variables for brevity
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        batch.begin();
        batch.draw(background, 0, 0, worldWidth, worldHeight);
        bucketSprite.draw(batch);
        for (Sprite dropSprite : dropSprites) {
            dropSprite.draw(batch);
        }

        font.draw(batch, score+"", 0.5f, worldHeight - 0.5f);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
