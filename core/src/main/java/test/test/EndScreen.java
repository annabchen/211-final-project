package test.test;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class EndScreen implements Screen {
    // only parameter necessary for this game is an instance of Drop, so we can use its methods and fields
    final DungeonAdventure game;

    public EndScreen(final DungeonAdventure game) {
        this.game = game;
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
        game.font.draw(game.batch, "game over! :(", 1, 1.5f);
        game.font.draw(game.batch, "click to restart from the last room you were in", 1, 1);
        game.batch.end();

        // if screen has been touched, then put the player back in the beginning of the room which they died in
        /*if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game, vertex));
            dispose();
        }*/
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
