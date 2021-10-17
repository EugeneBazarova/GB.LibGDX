package ru.geekbrains.stargame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.base.BaseScreen;

public class MenuScreen extends BaseScreen {

    private Texture img;
    private Vector2 touch;
    private Vector2 position;
    private Vector2 speed;
    private Vector2 onClick;


    @Override
    public void show() {
        super.show();
        img = new Texture("suprematism.jpg");
        touch = new Vector2();
        position = new Vector2();
        speed = new Vector2();
        onClick = new Vector2();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        batch.draw(img, position.x, position.y, 200, 120);
        position.add(speed);
        if (Vector2.dst(onClick.x, onClick.y, position.x, position.y) < 1f) {
            speed.set(0, 0);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        img.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        onClick.set(screenX, Gdx.graphics.getHeight() - screenY);
        speed.set(onClick.cpy().sub(position).nor());
        return false;
    }
}