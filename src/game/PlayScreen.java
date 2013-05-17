package game;

import java.nio.IntBuffer;
import java.util.Iterator;

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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.spine.SkeletonRenderer;

public class PlayScreen implements Screen, InputProcessor {
	
	private TextureRegion arrow;
	private TextureRegion backgroundDark;
	private TextureRegion backgroundLight;
	private TextureRegion backgroundTransition;
	private SpriteBatch batch;
	private Array<Block> blocks;
	private TextureRegion[] blockTextures;
	private OrthographicCamera camera;
	private int coin;
	private Integer cursor;
	private boolean drawBlock = false;
	private Cursor emptyCursor;
	private Array<Enemy> enemies;
	private BitmapFont font;
	private ObjectMap<Integer, TextureRegion> keyMappings;
	private ObjectMap<Integer, Block.Types> keyToType;
	private TextureRegion[] groundTiles;
	private Main main;
	private boolean newRound;
	private Vector3 mousePosition;
	private SkeletonRenderer renderer;
	private int roundCounter = 1;
	private int spawnAmount = 10;
	private float spawnTimer = 0;
	
	private Texture spriteSheet;
	private TextureRegion[] weaponTextures;
	
	public PlayScreen(Main main) {
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
		
	}
	
	public void initKeyBindings() {
		Gdx.input.setInputProcessor(this);
		keyMappings = new ObjectMap<Integer, TextureRegion>();
		keyMappings.put(Input.Keys.NUM_1, blockTextures[0]);
		keyMappings.put(Input.Keys.NUM_2, weaponTextures[0]);
		
		keyToType = new ObjectMap<Integer, Block.Types>();
		keyToType.put(Input.Keys.NUM_1, Block.Types.BRICK);
		keyToType.put(Input.Keys.NUM_2, Block.Types.WEAPON);
	}
	
	public void initTextures() {
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
		
		arrow = new TextureRegion(spriteSheet, 32, 128, 32, 5);
	}
	
	public boolean isSpotOccupied(Vector2 position) {
		Iterator<Block> it = blocks.iterator();
		while(it.hasNext()) {
			Block b = it.next();
			if(!b.isFalling() && b.getPosition().x == position.x && b.getPosition().y == position.y) {
				return true;
			}
		}
		return false;
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
			cursor = key;
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
	
	public void newRound() {
		if(roundCounter >= 12) {
			main.setScreen(main.endScreen);
		}
		else {
			newRound = true;
			spawnTimer = 0;
			spawnAmount += 5;
			for(int i = 0; i < spawnAmount; i++) {
				enemies.add(Enemy.spawn(i * 30));
			}
			
			coin += 5;
			Enemy.speed += 15;
			roundCounter++;
			if(spawnAmount % 10 == 0)
				Enemy.startHealth += 1;
		}
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float arg0) {
		update();
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();
		renderBackground();
		renderBlocks();
		renderCursor();
		
		font.draw(batch, "Coin: $" + coin, 20, Gdx.graphics.getHeight() - 20);
		font.draw(batch, "Round "+ roundCounter +" / 12", 20, Gdx.graphics.getHeight() - 50);
		
		Iterator<Enemy> itEnemies = enemies.iterator();
		while(itEnemies.hasNext()) {
			Enemy e = itEnemies.next();
			e.render(renderer, batch);
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
	
	public void renderBlocks() {
		Iterator<Block> it = blocks.iterator();
		while(it.hasNext()) {
			Block b = it.next();
			b.render(batch);
		}
	}
	
	public void renderCursor() {
		try {
			if(drawBlock) {
				setCursor(false);
				batch.draw(keyMappings.get(cursor), ((int) mousePosition.x / 32) * 32, ((int) mousePosition.y / 32) * 32);
				switch(keyToType.get(cursor)) {
				case BRICK:
					font.draw(batch, "Cost: $" + Brick.COST, 600, Gdx.graphics.getHeight() - 20);
					break;
				case WEAPON:
					font.draw(batch, "Cost: $" + WeaponBlock.COST, 600, Gdx.graphics.getHeight() - 20);
					break;
				default:
					break;
					
				}
			}
			else {
				setCursor(true);
			}
		}
		catch(LWJGLException e) {
			System.err.println(e.getMessage());
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
		initTextures();
		initKeyBindings();
		
		int min = Cursor.getMinCursorSize();
		IntBuffer tmp = BufferUtils.createIntBuffer(min * min);
		try {
			emptyCursor = new Cursor(min, min, min / 2, min / 2, 1, tmp, null);
		}
		catch(LWJGLException e) {
			System.err.println(e.getMessage());
		}
		
		mousePosition = new Vector3();
		
		blocks = new Array<Block>();
		renderer = new SkeletonRenderer();
		enemies = new Array<Enemy>();
		newRound = true;
		spawnTimer = 0;
		for(int i = 0; i < spawnAmount; i++) {
			enemies.add(Enemy.spawn(i * 30));
		}
		
		font = new BitmapFont(Gdx.files.internal("assets/xolo24.fnt"), Gdx.files.internal("assets/xolo24.png"), false);
		coin = 10;
	}

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		int x = ((int) mousePosition.x / 32) * 32;
		int y = ((int) mousePosition.y / 32) * 32;
		
		Vector2 position = new Vector2(x, y);
		if(cursor != null && !isSpotOccupied(position)) {
			switch(keyToType.get(cursor)) {
			case BRICK:
				if(coin >= Brick.COST) {
					blocks.add(new Brick(blockTextures, position));
					coin -= Brick.COST;
				}
				break;
			case WEAPON:
				if(coin >= WeaponBlock.COST) {
					blocks.add(new WeaponBlock(weaponTextures, position, arrow));
					coin -= WeaponBlock.COST;
				}
				break;
			default:
				break;
				
			}
		}
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
	
	public void update() {
		camera.update();
		Iterator<Block> it = blocks.iterator();
		while(it.hasNext()) {
			Block b = it.next();
			if(b instanceof WeaponBlock) {
				WeaponBlock wb = (WeaponBlock) b;
				wb.update(blocks, enemies);
			}
			else {
				b.update(blocks);
			}
			
		}
		
		Iterator<Enemy> itEnemies = enemies.iterator();
		while(itEnemies.hasNext()) {
			Enemy e = itEnemies.next();
			if(e.update(blocks)) {
				itEnemies.remove();
			}
			if(enemies.size == 0) {
				newRound();
			}
			// loss condition
			if(e.getRoot().getX() <= 0) {
				main.endScreen.result = false;
				main.setScreen(main.endScreen);
			}
		}
	}

}
