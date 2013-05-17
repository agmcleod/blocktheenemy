package game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main extends Game {
	
	EndScreen endScreen;
	PlayScreen playScreen;
	StartScreen startScreen;
	
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.width = 800;
		cfg.height = 640;
		new LwjglApplication(new Main(), cfg);
	}

	@Override
	public void create() {
		playScreen = new PlayScreen(this);
		startScreen = new StartScreen(this);
		endScreen = new EndScreen(this);
		setScreen(startScreen);
	}
}
