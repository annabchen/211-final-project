package test.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class StartRoom implements Screen {
    // only parameter necessary for this game is an instance of Drop, so we can use its methods and fields
    final DungeonAdventure game;
    private Vertex root;
    public StartRoom(final DungeonAdventure game, Vertex root) {
        // allows for access to the different rooms of root's vertices
        this.game = game;
        this.root = root;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        this.root.vertices[0].screensupplier.get();
        // returns a screen


        ScreenUtils.clear(Color.BLACK);

        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();
        // draw text, x and y are in meters
        // how text is rendered to the screen
        game.font.draw(game.batch, "welcome to the start room", 1, 1.5f);
        game.batch.end();

        // if screen has been touched, then dispose of the current menu and set screen to a GameScreen instance
        if (Gdx.input.isTouched()){
            game.setScreen(new GameScreen(game));
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
