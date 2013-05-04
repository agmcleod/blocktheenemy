package game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Block {
	
	private int activeState = 1;
	private int health;
	private int maxHealth;
	private boolean falling = true;
	private int maxFallingSpeed = 400;
	private Vector2 pos;
	private float stopPos = 32f;
	private TextureRegion[] textureStates;
	private int yVelocity = 50;
	
	static enum Types {
		BRICK,
		WEAPON
	}
	
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
	
	public Vector2 getPosition() {
		return this.pos;
	}
	
	public void incrementState() {
		activeState++;
	}
	
	public boolean isFalling() {
		return falling;
	}
	
	public void moveDown() {
		yVelocity += 20;
		if(yVelocity > maxFallingSpeed) {
			yVelocity = maxFallingSpeed;
		}
		this.pos.y += (-yVelocity) * Gdx.graphics.getDeltaTime();
	}

	public void render(SpriteBatch batch) {
		int state = maxHealth / 4;
		batch.draw(textureStates[(activeState / state) - 1], pos.x, pos.y);
	}

	public void setFalling(boolean falling) {
		this.falling = falling;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	public void update(Array<Block> blocks) {
		if(pos.y <= stopPos) {
			falling = false;
			pos.y = stopPos;
		}
		else {
			Iterator<Block> it = blocks.iterator();
			while(it.hasNext()) {
				Block b = it.next();
				Vector2 blockPos = b.getPosition();
				if(!b.isFalling() && blockPos.x == pos.x && (pos.y - blockPos.y) < 64) {
					stopPos = blockPos.y + 32f;
				}
			}
			moveDown();
		}
	}
}
