package ru.geekbrains.stargame.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.base.Ship;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.pool.BulletsPool;
import ru.geekbrains.stargame.pool.ExplosionPool;

public class EnemyShip extends Ship {


    public EnemyShip(BulletsPool bulletsPool, ExplosionPool explosionPool, Rect worldBounds, Sound bulletsSound) {
        this.bulletsPool = bulletsPool;
        this.explosionPool = explosionPool;
        this.worldBounds = worldBounds;
        this.bulletsSound = bulletsSound;
        this.bulletsV = new Vector2();
        this.bulletsPos = new Vector2();
        this.v = new Vector2();
        this.v0 = new Vector2();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (getTop() < worldBounds.getTop()) {
         v.set(v0);
        } else {
            reloadTimer = reloadInterval*0.8f;
        }
        if (getBottom() < worldBounds.getBottom()) {
            destroy();
        }
    }

    public void set(
            TextureRegion[] regions,
            Vector2 v,
            TextureRegion bulletsRegion,
            float bulletsHeight,
            Vector2 bulletsV,
            int dmg,
            int hp,
            float reloadInterval,
            float height
    ) {
        this.regions = regions;
        this.v0.set(v);
        this.bulletsRegion = bulletsRegion;
        this.bulletsHeight = bulletsHeight;
        this.bulletsV.set(bulletsV);
        this.dmg = dmg;
        this.hp = hp;
        this.reloadInterval = reloadInterval;
        setHeightProportion(height);
        this.v.set(0, -0.4f);
    }

    public boolean isBulletsCollision(Bullets bullets) {
        return !(bullets.getRight() < getLeft()
                || bullets.getLeft() > getRight()
                || bullets.getBottom() > getTop()
                || bullets.getTop() < pos.y);
    }
}
