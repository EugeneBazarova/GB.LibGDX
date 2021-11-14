package ru.geekbrains.stargame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import ru.geekbrains.stargame.base.BaseScreen;
import ru.geekbrains.stargame.base.Font;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.pool.BulletsPool;
import ru.geekbrains.stargame.pool.EnemyPool;
import ru.geekbrains.stargame.pool.ExplosionPool;
import ru.geekbrains.stargame.sprite.Background;
import ru.geekbrains.stargame.sprite.Bullets;
import ru.geekbrains.stargame.sprite.EnemyShip;
import ru.geekbrains.stargame.sprite.GameOver;
import ru.geekbrains.stargame.sprite.HpBar;
import ru.geekbrains.stargame.sprite.MainShip;
import ru.geekbrains.stargame.sprite.RestartButton;
import ru.geekbrains.stargame.sprite.Star;
import ru.geekbrains.stargame.util.EnemyEmitter;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 197;
    private static final float FONT_SIZE = 0.02f;
    private static final String FRAGS = "Frags: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "Level: ";
    private static final float MARGIN = 0.01f;

    private TextureAtlas atlas;
    private TextureAtlas hpAtlas;
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
    private HpBar hpBar;

    private int frags;
    private StringBuilder sbFrags;
    private StringBuilder sbHP;
    private StringBuilder sbLevel;

    private Font font;

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
        hpAtlas = new TextureAtlas("textures/hpbar.pack");
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
        hpBar = new HpBar(hpAtlas);
        gameOver = new GameOver(atlas);
        restartButton = new RestartButton(atlas, this);
        frags = 0;
        sbFrags = new StringBuilder();
        sbHP = new StringBuilder();
        sbLevel = new StringBuilder();
        font = new Font("font/DialogInputFont.fnt", "font/DialogInputFont.png");
        font.setSize(FONT_SIZE);
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
        hpBar.resize(worldBounds);
        gameOver.resize(worldBounds);
        restartButton.resize(worldBounds);
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        atlas.dispose();
        hpAtlas.dispose();
        bulletsPool.dispose();
        explosionPool.dispose();
        enemyPool.dispose();
        music.dispose();
        shootSound.dispose();
        laserSound.dispose();
        explosionSound.dispose();
        font.dispose();
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
            enemyEmitter.generate(delta, frags);
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
                    if (enemyShip.isDestroyed()) {
                        frags++;
                    }
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
        hpBar.draw(batch);
        explosionPool.drawActiveObjs(batch);
        printInfo();
        batch.end();
    }

    private void printInfo() {
        sbFrags.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft() + MARGIN, worldBounds.getTop() - MARGIN);
        sbHP.setLength(0);
        font.draw(batch, sbHP.append(HP).append(mainShip.getHp()), worldBounds.pos.x, worldBounds.getTop() - MARGIN, Align.center);
        sbLevel.setLength(0);
        font.draw(batch, sbLevel.append(LEVEL).append(enemyEmitter.getLevel()), worldBounds.getRight() - MARGIN, worldBounds.getTop() - MARGIN, Align.right);
    }
}