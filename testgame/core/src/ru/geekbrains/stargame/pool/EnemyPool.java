package ru.geekbrains.stargame.pool;

import com.badlogic.gdx.audio.Sound;

import ru.geekbrains.stargame.base.SpritesPool;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.sprite.EnemyShip;

public class EnemyPool extends SpritesPool<EnemyShip> {

    private final BulletsPool bulletsPool;
    private final ExplosionPool explosionPool;
    private final Rect worldBounds;
    private final Sound bulletSound;

    public EnemyPool(BulletsPool bulletPool, ExplosionPool explosionPool, Rect worldBounds, Sound bulletSound) {
        this.bulletsPool = bulletPool;
        this.worldBounds = worldBounds;
        this.bulletSound = bulletSound;
        this.explosionPool = explosionPool;
    }


    @Override
    protected EnemyShip newObj() {
        return new EnemyShip(bulletsPool, explosionPool, worldBounds, bulletSound);
    }

}
