package test.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class PuzzleRoom1ending implements Screen {
    final DungeonAdventure game;
    Vertex vertex;

    public PuzzleRoom1ending(final DungeonAdventure game, Vertex vertex) {
        this.game = game;
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
        game.font.draw(game.batch, "game over, take care not to step on a tile twice!", 1, 1.5f);
        game.font.draw(game.batch, "click space to restart from the beginning of the room", 1, 1);
        game.batch.end();

        // if screen has been touched, then put the player back in the beginning of the room which they died in
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            game.setScreen(new PuzzleRoom1(game, vertex));
            dispose();
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
