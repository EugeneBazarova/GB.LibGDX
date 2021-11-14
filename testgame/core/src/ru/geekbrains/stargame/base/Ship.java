package ru.geekbrains.stargame.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.pool.BulletsPool;
import ru.geekbrains.stargame.pool.ExplosionPool;
import ru.geekbrains.stargame.sprite.Bullets;
import ru.geekbrains.stargame.sprite.Explosion;

public class Ship extends Sprite {

    protected Rect worldBounds;
    protected BulletsPool bulletsPool;
    protected TextureRegion bulletsRegion;
    protected Vector2 bulletsV;
    protected Vector2 bulletsPos;
    protected TextureAtlas.AtlasRegion hpBar;
    protected float bulletsHeight;
    protected int dmg;
    protected Sound bulletsSound;
    protected int hp;
    protected ExplosionPool explosionPool;

    protected Vector2 v;
    protected Vector2 v0;

    protected float reloadTimer;
    protected float reloadInterval;

    private static final float DMG_ANIMATE_INTERVAL = 0.1f;
    private float dmgAnimateTimer = DMG_ANIMATE_INTERVAL;

    public Ship() {
    }

    public Ship(TextureRegion region, int rows, int cols, int frames) {
        super(region, rows, cols, frames);
    }


    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        reloadTimer += delta;
        if (reloadTimer >= reloadInterval) {
            reloadTimer = 0f;
            bulletsPos.set(pos);
            shoot();
        }
        dmgAnimateTimer += delta;
        if (dmgAnimateTimer >= DMG_ANIMATE_INTERVAL) {
            frame = 0;
        }
    }

    public void dmg(int hp) {
        this.hp -= hp;
        if (this.hp <= 0) {
            this.hp = 0;
            destroy();
        }
        dmgAnimateTimer = 0f;
        frame = 1;
    }

    public int getDmg() {
        return dmg;
    }

    public int getHp() {
        return hp;
    }


    @Override
    public void destroy() {
        super.destroy();
        boom();
    }

    private void shoot() {
        Bullets bullets = bulletsPool.obtain();
        bullets.set(this, bulletsRegion, bulletsPos, bulletsV, worldBounds, bulletsHeight, dmg);
        bulletsSound.play(0.2f);
    }

    private void boom() {
        Explosion explosion = explosionPool.obtain();
        explosion.set(this.pos, getHeight());
    }
}
