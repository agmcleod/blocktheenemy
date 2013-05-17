package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EndScreen implements Screen {
	
	private BitmapFont font;
	private Main main;
	boolean result = true;
	private SpriteBatch batch;
	
	public EndScreen(Main main) {
		this.main = main;
	}

	@Override
	public void dispose() {
		font.dispose();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float arg0) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		String r = "won";
		if(!result) {
			r = "lost";
		}
		font.draw(batch, "You " + r + " the game.", 20, 620);
		font.draw(batch, "Press enter to play again, or escape to quit.", 20, 590);
		batch.end();
		
		if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
			main.setScreen(main.playScreen);
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}
	}

	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		font = new BitmapFont(Gdx.files.internal("assets/xolo24.fnt"), Gdx.files.internal("assets/xolo24.png"), false);
		batch = new SpriteBatch();
	}

}
