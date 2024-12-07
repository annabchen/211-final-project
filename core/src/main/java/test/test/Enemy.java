package test.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Enemy {
    // stores direction, sprite
    Sprite sprite;
    Vector2 direction;

    public Enemy(Sprite sprite, Vector2 direction) {
        this.sprite = sprite;
        this.direction = direction;
    }

    public void move() {
        float delta = Gdx.graphics.getDeltaTime();
        sprite.translate(direction.x * delta, direction.y * delta);
    }
}
