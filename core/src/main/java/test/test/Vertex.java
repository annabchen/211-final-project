package test.test;

import com.badlogic.gdx.Screen;

import java.util.function.BiFunction;

public class Vertex {
    // represent edges/ connections through array of other vertices

    // vertex recieves a func called screensupplier that when called returns a screen
    // supplier essentially supplies the screen for u
    public Vertex[] vertices;
    public BiFunction<DungeonAdventure, Vertex, Screen> screenFactory;

    public Vertex(BiFunction<DungeonAdventure, Vertex, Screen> screenFactory) {
        this.screenFactory = screenFactory;
    }

    public void setVertices(Vertex[] rooms) {
        vertices = rooms;
    }

}

