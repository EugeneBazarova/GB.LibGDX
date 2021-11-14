package ru.geekbrains.stargame.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.geekbrains.stargame.base.Sprite;
import ru.geekbrains.stargame.math.Rect;


public class HpBar extends Sprite {

    private static final float HEIGHT = 0.015f;
    private static final float PADDING = 0.03f;

    private final TextureRegion hpBar;

    public HpBar(TextureAtlas hpAtlas) {
        super(hpAtlas.findRegion("loading1"), 1, 1, 2);
        hpBar = hpAtlas.findRegion("loading2");
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(HEIGHT);
        setBottom(worldBounds.getBottom() + PADDING);
    }
}