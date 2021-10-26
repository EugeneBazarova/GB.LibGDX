package ru.geekbrains.stargame.sprite;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.base.Sprite;
import ru.geekbrains.stargame.math.Rect;

public class Ship extends Sprite {

    private Vector2 touch;
    private Vector2 speed;

    private final float VECT_LEN = 0.01f;

    private Rect worldBounds;
    private boolean pressed;

    public Ship(TextureAtlas atlas) {
        super(new TextureRegion(atlas.findRegion("main_ship"), 0,0, 190, 300));
        speed = new Vector2();
        touch = new Vector2();
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        float height = 0.1f;
        setHeightProportion(height);
        pos.set(0, 0);
        setBottom(worldBounds.getBottom() + .1f);
    }

    @Override
    public void update(float delta) {
        if (pressed && getLeft() >= worldBounds.getLeft() && getRight() <= worldBounds.getRight()) {
            pos.add(speed);
        }
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        this.pressed = true;
        speed.set(touch.cpy().sub(pos)).setLength(VECT_LEN);
        speed.y = 0;
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        stopMove();
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A) {
            speed.set(0 - VECT_LEN, 0);
            pressed = true;
        }
        if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D) {
            speed.set(VECT_LEN, 0);
            pressed = true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == 21 || keycode == 22) {
            stopMove();
        }
        if (keycode == Input.Keys.A || keycode == Input.Keys.D) {
            stopMove();
        }
        return false;
    }

    private void stopMove() {
        this.pressed = false;
        if (getLeft() < worldBounds.getLeft()) {
            setLeft(worldBounds.getLeft());
        }
        if (getRight() > worldBounds.getRight()) {
            setRight(worldBounds.getRight());
        }
    }
}