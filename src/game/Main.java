package game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main extends Game {
	
	private PlayScreen playScreen;
	
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.width = 800;
		cfg.height = 640;
		new LwjglApplication(new Main(), cfg);
	}

	@Override
	public void create() {
		playScreen = new PlayScreen(this);
		setScreen(playScreen);
	}
}
