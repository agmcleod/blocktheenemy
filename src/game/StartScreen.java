package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class StartScreen implements Screen {
	
	private TextureRegion backgroundDark;
	private TextureRegion backgroundLight;
	private TextureRegion backgroundTransition;
	private OrthographicCamera camera;
	private BitmapFont font;
	private Texture spriteSheet;
	private Main main;
	private SpriteBatch batch;
	
	public StartScreen(Main main) {
		this.main = main;
	}

	@Override
	public void dispose() {
		batch.dispose();
		spriteSheet.dispose();
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
		for(int r = 0; r < 4; r++) {
			for(int c = 0; c < 25; c++) {
				batch.draw(backgroundLight, c * 32, r * 32);
			}
		}
		for(int c = 0; c < 25; c++) {
			batch.draw(backgroundTransition, c * 32, 32 * 4);
		}
		
		for(int r = 9; r < 20; r++) {
			for(int c = 0; c < 25; c++) {
				batch.draw(backgroundDark, c * 32, r * 32);
			}
		}
		int height = Gdx.graphics.getHeight();
		font.draw(batch, "Defend your territory!", 20, height - 20);
		font.draw(batch, "Use number keys 1 & 2 to switch block types.", 20, height - 55);
		font.draw(batch, "1 blocks enemies, 2 stops them.", 20, height - 90);
		font.draw(batch, "Stop the 12 rounds to win. Press enter to begin.", 20, height - 125);
		batch.end();
		
		if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
			main.setScreen(main.playScreen);
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
		spriteSheet = new Texture(Gdx.files.internal("assets/blocks.png"));
		backgroundDark = new TextureRegion(spriteSheet, 32, 96, 32, 32);
		backgroundLight = new TextureRegion(spriteSheet, 64, 96, 32, 32);
		backgroundTransition = new TextureRegion(spriteSheet, 0, 96, 32, 160);
		camera = new OrthographicCamera();
		camera.setToOrtho(false);
		font = new BitmapFont(Gdx.files.internal("assets/xolo24.fnt"), Gdx.files.internal("assets/xolo24.png"), false);
		batch = new SpriteBatch();
	}

}
