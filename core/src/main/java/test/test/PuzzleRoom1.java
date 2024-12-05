package test.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;

public class PuzzleRoom1 implements Screen {
    final DungeonAdventure game;
    private final Vertex vertex;
    Texture playerTexture;
    Sprite playerSprite;

    public PuzzleRoom1(final DungeonAdventure game, Vertex vertex) {
        this.game = game;

        playerTexture = new Texture("player.jpeg");
        playerSprite = new Sprite(playerTexture);
        playerSprite.setSize(1, 1);
        this.vertex = vertex;

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();
        // draw text, x and y are in meters
        // how text is rendered to the screen
        game.font.draw(game.batch, "welcome to puzzle room 1", 1, 1.5f);
        game.batch.end();

        // if screen has been touched, then put the player back in the beginning of the room which they died in
        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    private void logic() {
        // store worldWidth and worldHeight as local variables
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();
        // store player size
        float playerWidth = playerSprite.getWidth();
        float playerHeight = playerSprite.getHeight();

        // use clamp method to prevent user from going outside of 0 and worldwidth minus one unit from the right
        playerSprite.setX(MathUtils.clamp(playerSprite.getX(), 0, worldWidth - playerWidth));
        playerSprite.setY(MathUtils.clamp(playerSprite.getY(), 0, worldHeight - playerHeight));
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

    }
}
