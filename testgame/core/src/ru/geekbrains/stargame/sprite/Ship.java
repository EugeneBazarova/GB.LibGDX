package ru.geekbrains.stargame.sprite;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.base.Sprite;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.pool.BulletsPool;

public class Ship extends Sprite {

    private static final float HEIGHT = 0.15f;
    private static final float BOTTOM_MARGIN = 0.05f;
    private static final int INVALID_POINTER = -1;

    private final BulletsPool bulletsPool;
    private final TextureRegion bulletsRegion;
    private final Vector2 bulletsV;
    private final float bulletsHeight;
    private final int dmg;

    private final Vector2 v;
    private final Vector2 v0;

    private Rect worldBounds;
    private boolean pressedLeft;
    private boolean pressedRight;

    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;

    private static final long AUTO_SHOOTING = 400;
    private long shootingTime = 0;

    private Sound shootSound;

    public Ship(TextureAtlas atlas, BulletsPool bulletsPool, Sound sound) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        this.bulletsPool = bulletsPool;
        this.bulletsRegion = atlas.findRegion("bulletMainShip");
        this.bulletsV = new Vector2(0, 0.5f);
        this.bulletsHeight = 0.01f;
        this.dmg = 1;
        this.v = new Vector2();
        this.v0 = new Vector2(0.5f, 0);
        shootSound = sound;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        this.worldBounds = worldBounds;
        setHeightProportion(HEIGHT);
        setBottom(worldBounds.getBottom() + BOTTOM_MARGIN);
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        if (getLeft() > worldBounds.getRight()) {
            setRight(worldBounds.getLeft());
        }
        if (getRight() < worldBounds.getLeft()) {
            setLeft(worldBounds.getRight());
        }
        if (shootingTime == 0 || System.currentTimeMillis() - shootingTime >= AUTO_SHOOTING) {
            shoot();
        }
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (touch.x < worldBounds.pos.x) {
            if (leftPointer != INVALID_POINTER) {
                return false;
            }
            leftPointer = pointer;
            moveLeft();
        } else {
            if (rightPointer != INVALID_POINTER) {
                return false;
            }
            rightPointer = pointer;
            moveRight();
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (pointer == leftPointer) {
            leftPointer = INVALID_POINTER;
            if (rightPointer != INVALID_POINTER) {
                moveRight();
            } else {
                stop();
            }
        } else if (pointer == rightPointer) {
            rightPointer = INVALID_POINTER;
            if (leftPointer != INVALID_POINTER) {
                moveLeft();
            } else {
                stop();
            }
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = true;
                moveLeft();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = true;
                moveRight();
                break;
            case Input.Keys.UP:
                shoot();
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = false;
                if (pressedRight) {
                    moveRight();
                } else {
                    stop();
                }
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = false;
                if (pressedLeft) {
                    moveLeft();
                } else {
                    stop();
                }
                break;
            case Input.Keys.UP:
                shoot();
                break;
        }
        return false;
    }

    private void moveRight() {
        v.set(v0);
    }

    private void moveLeft() {
        v.set(v0).rotateDeg(180);
    }

    private void stop() {
        v.setZero();
    }

    private void shoot() {
        Bullets bullets = bulletsPool.obtain();
        bullets.set(this, bulletsRegion, this.pos, bulletsV, worldBounds, bulletsHeight, dmg);
        shootingTime = System.currentTimeMillis();
        shootSound.play(0.05f);
    }
}