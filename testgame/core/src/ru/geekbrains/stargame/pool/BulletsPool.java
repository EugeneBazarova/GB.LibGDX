package ru.geekbrains.stargame.pool;

import ru.geekbrains.stargame.base.SpritesPool;
import ru.geekbrains.stargame.sprite.Bullets;

public class BulletsPool extends SpritesPool<Bullets> {
    @Override
    protected Bullets newObj() {
        return new Bullets();
    }
}
