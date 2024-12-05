package test.test;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class DungeonAdventure extends Game {
    public SpriteBatch batch;
    public BitmapFont font;
    public FitViewport viewport;

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(); // libGDX's default font
        viewport = new FitViewport(16, 10);

        // scale font using ratio of viewport height to screen height
        font.setUseIntegerPositions(false);
        font.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight());

        // implementing the graph
        Vertex root = new Vertex(StartRoom::new);
        // first class functions- a function is like any other object but can still be called like a func
        // can be passed around, stored as a variable, etc
        // ::- instead of calling function, it refers to the function
        Vertex puzzle1 = new Vertex(PuzzleRoom1::new);
        // ^ function that runs the code (new MazeRoom1(this)) but it runs when you call the function line
        // whenever you call the function, make a new room with this in it, returns an instance of puzzleroom1 - closure
        Vertex puzzle2 = new Vertex(PuzzleRoom2::new);
        Vertex puzzle3 = new Vertex(PuzzleRoom3::new);
        Vertex enemy1 = new Vertex(EnemyRoom1::new);
        Vertex enemy2 = new Vertex(EnemyRoom2::new);
        Vertex enemy3 = new Vertex(EnemyRoom3::new);
        Vertex treasure1 = new Vertex(TreasureRoom1::new);
        Vertex treasure2 = new Vertex(TreasureRoom2::new);
        Vertex treasure3 = new Vertex(TreasureRoom3::new);
        Vertex finalRoom = new Vertex(FinalRoom::new);

        // establishing connections!!!!!
        root.setVertices(new Vertex[]{puzzle1, enemy1, treasure1});

        puzzle1.setVertices(new Vertex[]{treasure1, puzzle2, enemy1});
        enemy1.setVertices(new Vertex[]{puzzle1, enemy2, treasure1});
        treasure1.setVertices(new Vertex[]{enemy1, treasure2, puzzle1});

        puzzle2.setVertices(new Vertex[]{treasure2, puzzle3, enemy2});
        enemy2.setVertices(new Vertex[]{puzzle2, enemy3, treasure2});
        treasure2.setVertices(new Vertex[]{enemy2, treasure3, puzzle2});

        puzzle3.setVertices(new Vertex[]{treasure3, finalRoom, enemy3});
        enemy3.setVertices(new Vertex[]{puzzle3, finalRoom, treasure3});
        treasure3.setVertices(new Vertex[]{enemy3, finalRoom, puzzle3});

        finalRoom.setVertices(new Vertex[]{null, null, null});


        this.setScreen(new MainMenuScreen(this, root));
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
