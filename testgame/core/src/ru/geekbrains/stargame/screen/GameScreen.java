package ru.geekbrains.stargame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import ru.geekbrains.stargame.base.BaseScreen;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.pool.BulletsPool;
import ru.geekbrains.stargame.pool.EnemyPool;
import ru.geekbrains.stargame.pool.ExplosionPool;
import ru.geekbrains.stargame.sprite.Background;
import ru.geekbrains.stargame.sprite.Bullets;
import ru.geekbrains.stargame.sprite.EnemyShip;
import ru.geekbrains.stargame.sprite.GameOver;
import ru.geekbrains.stargame.sprite.MainShip;
import ru.geekbrains.stargame.sprite.RestartButton;
import ru.geekbrains.stargame.sprite.Star;
import ru.geekbrains.stargame.util.EnemyEmitter;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 197;

    private TextureAtlas atlas;
    private Texture bg;
    private Background background;

    private Star[] stars;
    private BulletsPool bulletsPool;
    private ExplosionPool explosionPool;
    private EnemyPool enemyPool;

    private MainShip mainShip;

    private Music music;
    private Sound laserSound;
    private Sound shootSound;
    private Sound explosionSound;

    private EnemyEmitter enemyEmitter;
    private GameOver gameOver;
    private RestartButton restartButton;

    @Override
    public void show() {
        super.show();
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        music.play();
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        shootSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        atlas = new TextureAtlas("textures/mainAtlas.tpack");
        bg = new Texture("textures/bg.png");
        background = new Background(bg);
        stars = new Star[STAR_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(atlas);
        }
        bulletsPool = new BulletsPool();
        explosionPool = new ExplosionPool(atlas, explosionSound);
        enemyPool = new EnemyPool(bulletsPool, explosionPool, worldBounds, shootSound);

        mainShip = new MainShip(atlas, bulletsPool, explosionPool, shootSound);

        enemyEmitter = new EnemyEmitter(enemyPool, worldBounds, atlas);

        gameOver = new GameOver(atlas);
        restartButton = new RestartButton(atlas, this);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        checkCollisions();
        freeAllDestroyed();
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star : stars) {
            star.resize(worldBounds);
        }
        mainShip.resize(worldBounds);
        gameOver.resize(worldBounds);
        restartButton.resize(worldBounds);
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        atlas.dispose();
        bulletsPool.dispose();
        explosionPool.dispose();
        enemyPool.dispose();
        music.dispose();
        shootSound.dispose();
        laserSound.dispose();
        explosionSound.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        mainShip.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        mainShip.keyUp(keycode);
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        mainShip.touchDown(touch, pointer, button);
        restartButton.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        mainShip.touchUp(touch, pointer, button);
        restartButton.touchUp(touch, pointer, button);
        return false;
    }

    private void update(float delta) {
        for (Star star : stars) {
            star.update(delta);
        }
        if (!mainShip.isDestroyed()) {
            bulletsPool.updActiveObjs(delta);
            enemyPool.updActiveObjs(delta);
            mainShip.update(delta);
            enemyEmitter.generate(delta);
        }
        explosionPool.updActiveObjs(delta);
    }

    public void checkCollisions() {
        if (mainShip.isDestroyed()) {
            return;
        }
        List<EnemyShip> enemyShipList = enemyPool.getActiveObjs();
        for (EnemyShip enemyShip : enemyShipList) {
            float minDist = mainShip.getWidth();
            if (!enemyShip.isDestroyed()
                    && mainShip.pos.dst(enemyShip.pos) < minDist) {
                enemyShip.destroy();
                mainShip.dmg(enemyShip.getDmg() * 2);
            }
        }
        List<Bullets> bulletList = bulletsPool.getActiveObjs();
        for (Bullets bullet : bulletList) {
            if (bullet.isDestroyed()) {
                continue;
            }
            if (bullet.getOwner() != mainShip) {
                if (mainShip.isBulletsCollision(bullet)) {
                    mainShip.dmg(bullet.getDmg());
                    bullet.destroy();
                }
                continue;
            }
            for (EnemyShip enemyShip : enemyShipList) {
                if (enemyShip.isBulletsCollision(bullet)) {
                    enemyShip.dmg(bullet.getDmg());
                    bullet.destroy();
                }
            }
        }
    }

    private void freeAllDestroyed() {
        bulletsPool.freeAllDestroyed();
        explosionPool.freeAllDestroyed();
        enemyPool.freeAllDestroyed();
    }

    public void restartGame() {
        bulletsPool.dispose();
        enemyPool.dispose();
        explosionPool.dispose();
        mainShip.flushDestroy();
    }

    private void draw() {
        batch.begin();
        background.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        if (!mainShip.isDestroyed()) {
            bulletsPool.drawActiveObjs(batch);
            enemyPool.drawActiveObjs(batch);
            mainShip.draw(batch);
        } else {
            gameOver.draw(batch);
            restartButton.draw(batch);
        }
        explosionPool.drawActiveObjs(batch);
        batch.end();
    }
}