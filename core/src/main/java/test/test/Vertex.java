package test.test;

import com.badlogic.gdx.Screen;

import java.util.function.Supplier;

public class Vertex {
    // represent edges/ connections through array of other vertices

    // vertex recieves a func called screensupplier that when called returns a screen
    // supplier essentially supplies the screen for u
    public Vertex[] vertices;
    public Supplier<Screen> screensupplier;

    public Vertex (Supplier<Screen> screensupplier){
        this.screensupplier = screensupplier;
    }
    public void setVertices(Vertex[] rooms){
        vertices = rooms;
    }

}

