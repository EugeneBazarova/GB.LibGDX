package ru.geekbrains.stargame.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public abstract class SpritesPool<T extends Sprite> {

    protected final List<T> activeObjs = new ArrayList<>();
    protected final List<T> freeObjs = new ArrayList<>();

    protected abstract T newObj();

    public T obtain() {
        T obj;
        if (freeObjs.isEmpty()) {
            obj = newObj();
        } else {
            obj = freeObjs.remove(freeObjs.size() - 1);
        }
        activeObjs.add(obj);
        System.out.println(getClass().getSimpleName() + " active/free: " + activeObjs.size() + "/" + freeObjs.size());
        return obj;
    }

    public void updActiveObjs(float delta) {
        for (T obj : activeObjs) {
            if (!obj.isDestroyed()) {
                obj.update(delta);
            }
        }
    }

    public void drawActiveObjs(SpriteBatch batch) {
        for (T obj : activeObjs) {
            if (!obj.isDestroyed()) {
                obj.draw(batch);
            }
        }
    }

    public void freeAllDestroyed() {
        for (int i = 0; i < activeObjs.size(); i++) {
            T obj = activeObjs.get(i);
            if (obj.isDestroyed()) {
                free(obj);
                i--;
            }
        }
    }

    public List<T> getActiveObjs() {
        return activeObjs;
    }

    public void dispose() {
        activeObjs.clear();
        freeObjs.clear();
    }

    private void free(T obj) {
        obj.flushDestroy();
        if (activeObjs.remove(obj)) {
            freeObjs.add(obj);
            System.out.println(getClass().getSimpleName() + " active/free: " + activeObjs.size() + "/" + freeObjs.size());
        }
    }
}
