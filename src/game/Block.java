package game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Block {
	
	private int activeState = 1;
	private int health;
	private int maxHealth;
	private Vector2 pos;
	private TextureRegion[] textureStates;
	
	public Block(TextureRegion[] textureStates, Vector2 pos) {
		this.textureStates = textureStates;
		this.health = 4;
		this.maxHealth = 4;
		this.pos = pos;
	}
	
	public Block(TextureRegion[] textureStates, Vector2 pos, int health) {
		this.health = health;
		this.textureStates = textureStates;
		this.maxHealth = health;
		this.pos = pos;
	}

	public int getHealth() {
		return health;
	}
	
	public void incrementState() {
		activeState++;
	}
	
	public void render(SpriteBatch batch) {
		int state = maxHealth % 4;
		batch.draw(textureStates[(activeState / state) - 1], pos.x, pos.y);
	}

	public void setHealth(int health) {
		this.health = health;
	}

}
