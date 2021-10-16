package ru.geekbrains.stargame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;


public class StarGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	TextureRegion region;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("suprematism.jpg");
		region = new TextureRegion(img, 278, 393, 150, 150);
	}

	@Override
	public void render () {
		ScreenUtils.clear(207, 178, 178, 1);
		batch.begin();
		batch.draw(img, 3, 5, 200, 200);
		batch.draw(region, 300, 65, 300, 300);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
