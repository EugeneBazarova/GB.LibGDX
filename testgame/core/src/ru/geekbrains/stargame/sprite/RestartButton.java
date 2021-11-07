package ru.geekbrains.stargame.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.stargame.base.BaseButton;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.screen.GameScreen;

public class RestartButton extends BaseButton {

    private static final float HEIGHT = 0.03f;
    private static final float PADDING = 0.03f;

    private final GameScreen gameScreen;

    public RestartButton(TextureAtlas atlas, GameScreen gameScreen) {
        super(atlas.findRegion("button_new_game"));
        this.gameScreen = gameScreen;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(HEIGHT);
        setBottom(worldBounds.getBottom() + PADDING);
    }

    @Override
    public void action() {
        gameScreen.restartGame();
    }
}
