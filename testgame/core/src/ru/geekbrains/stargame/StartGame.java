package ru.geekbrains.stargame;


import com.badlogic.gdx.Game;


import ru.geekbrains.stargame.screen.MenuScreen;

public class StartGame extends Game {

	@Override
	public void create() {
		setScreen(new MenuScreen(this));
	}
}