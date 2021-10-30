package ru.geekbrains.stargame.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.base.Sprite;
import ru.geekbrains.stargame.math.Rect;

public class Bullets extends Sprite {

    private final Vector2 v = new Vector2();

    private Rect worldBounds;
    private int dmg;
    private Sprite owner;

    public Bullets() {
        regions = new TextureRegion[1];
    }

    public void set(
            Sprite owner,
            TextureRegion region,
            Vector2 pos,
            Vector2 v,
            Rect worldBounds,
            float height,
            int dmg
    ) {
        this.owner = owner;
        this.regions[0] = region;
        this.pos.set(pos);
        this.v.set(v);
        this.worldBounds = worldBounds;
        setHeightProportion(height);
        this.dmg = dmg;
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        if (isOutside(worldBounds)) {
            destroy();
        }
    }

    public int getDmg() {
        return dmg;
    }

    public Sprite getOwner() {
        return owner;
    }
}
