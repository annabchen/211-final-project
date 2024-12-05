package test.test;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class EnemyRoom2 implements Screen {

    final DungeonAdventure game;
    private final Vertex vertex;

    public EnemyRoom2(final DungeonAdventure game, Vertex vertex) {
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
        game.font.draw(game.batch, "welcome to enemy room 2", 1, 1.5f);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

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
