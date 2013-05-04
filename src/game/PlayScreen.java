package game;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;

public class PlayScreen implements Screen, InputProcessor {
	
	private TextureRegion backgroundDark;
	private TextureRegion backgroundLight;
	private TextureRegion backgroundTransition;
	private SpriteBatch batch;
	private TextureRegion[] blockTextures;
	private OrthographicCamera camera;
	private TextureRegion cursor;
	private boolean drawBlock = false;
	private Cursor emptyCursor;
	private ObjectMap<Integer, TextureRegion> keyMappings;
	private TextureRegion[] groundTiles;
	private Main main;
	private Vector3 mousePosition;
	private Texture spriteSheet;
	private TextureRegion[] weaponTextures;
	
	public PlayScreen(Main main) {
		this.main = main;
	}

	@Override
	public void dispose() {
		batch.dispose();
		spriteSheet.dispose();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean keyDown(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean keyUp(int key) {
		if(keyMappings.containsKey(key)) {
			drawBlock = true;
			cursor = keyMappings.get(key);
		}
		else {
			drawBlock = false;
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int x, int y) {
		camera.unproject(mousePosition.set(x, y, 0));
		return false;
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float arg0) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.begin();
		renderBackground();
		try {
			if(drawBlock) {
				setCursor(false);
				batch.draw(cursor, ((int) mousePosition.x / 32) * 32, ((int) mousePosition.y / 32) * 32);
			}
			else {
				setCursor(true);
			}
		}
		catch(LWJGLException e) {
			System.err.println(e.getMessage());
		}
		batch.end();
	}

	public void renderBackground() {
		for(int c = 0; c < 25; c++) {
			if(c == 0) {
				batch.draw(groundTiles[0], c * 32, 0);
			}
			else {
				batch.draw(groundTiles[1], c * 32, 0);
			}
		}
		for(int r = 1; r < 4; r++) {
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
	public boolean scrolled(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void setCursor(boolean set) throws LWJGLException {
		Mouse.setNativeCursor(set ? null : emptyCursor);
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		spriteSheet = new Texture(Gdx.files.internal("assets/blocks.png"));
		backgroundDark = new TextureRegion(spriteSheet, 32, 96, 32, 32);
		backgroundLight = new TextureRegion(spriteSheet, 64, 96, 32, 32);
		backgroundTransition = new TextureRegion(spriteSheet, 0, 96, 32, 160);
		camera = new OrthographicCamera();
		camera.setToOrtho(false);
		groundTiles = new TextureRegion[2];
		groundTiles[0] = new TextureRegion(spriteSheet, 0, 64, 32, 32);
		groundTiles[1] = new TextureRegion(spriteSheet, 32, 64, 32, 32);
		
		blockTextures = new TextureRegion[4];
		blockTextures[0] = new TextureRegion(spriteSheet, 0, 0, 32, 32);
		blockTextures[1] = new TextureRegion(spriteSheet, 32, 0, 32, 32);
		blockTextures[2] = new TextureRegion(spriteSheet, 64, 0, 32, 32);
		blockTextures[3] = new TextureRegion(spriteSheet, 96, 0, 32, 32);
		
		weaponTextures = new TextureRegion[4];
		weaponTextures[0] = new TextureRegion(spriteSheet, 0, 32, 32, 32);
		weaponTextures[1] = new TextureRegion(spriteSheet, 32, 32, 32, 32);
		weaponTextures[2] = new TextureRegion(spriteSheet, 64, 32, 32, 32);
		weaponTextures[3] = new TextureRegion(spriteSheet, 96, 32, 32, 32);
		
		Gdx.input.setInputProcessor(this);
		keyMappings = new ObjectMap<Integer, TextureRegion>();
		keyMappings.put(Input.Keys.NUM_1, blockTextures[0]);
		keyMappings.put(Input.Keys.NUM_2, weaponTextures[0]);
		
		int min = Cursor.getMinCursorSize();
		IntBuffer tmp = BufferUtils.createIntBuffer(min * min);
		try {
			emptyCursor = new Cursor(min, min, min / 2, min / 2, 1, tmp, null);
		}
		catch(LWJGLException e) {
			System.err.println(e.getMessage());
		}
		
		mousePosition = new Vector3();
	}

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

}
