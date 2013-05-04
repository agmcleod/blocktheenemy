package game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;

public class Enemy {
	
	private static TextureAtlas atlas;
	private Vector2 attackSpot;
	private int health;
	private boolean moving;
	private Bone root;
	private Skeleton skeleton;
	private float speed;
	private AnimationState state;
	
	public Enemy(Vector2 pos) {
		SkeletonJson json = new SkeletonJson(Enemy.getAtlas());
		SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("assets/skeleton.json"));
		
		AnimationStateData stateData = new AnimationStateData(skeletonData);
		state = new AnimationState(stateData);
		state.setAnimation("Move", true);
		
		skeleton = new Skeleton(skeletonData);
		root = skeleton.getRootBone();
		root.setX(pos.x);
		root.setY(pos.y);
		speed = 20;
		moving = true;
		attackSpot = new Vector2();
		setHealth(2);
	}
	
	public static void dispose() {
		atlas.dispose();
	}
	
	public static TextureAtlas getAtlas() {
		if(atlas == null) {
			atlas = new TextureAtlas(Gdx.files.internal("assets/defendthewall.pack"));
		}
		return atlas;
	}
	
	public Bone getRoot() {
		return root;
	}
	
	public static Enemy spawn() {
		return new Enemy(new Vector2(800, 50));
	}
	
	public void render(SkeletonRenderer renderer, SpriteBatch batch) {
		state.apply(skeleton);
		renderer.draw(batch, skeleton);
	}
	
	public boolean update(Array<Block> blocks) {
		state.update(Gdx.graphics.getDeltaTime());
		skeleton.update(Gdx.graphics.getDeltaTime());
		skeleton.updateWorldTransform();
		
		/* if(!moving && state.isComplete()) {
			state.setAnimation("Attack", false);
		} */
		
		float x = root.getX();
		
		Iterator<Block> it = blocks.iterator();
		boolean setX = false;
		boolean noBlock = true;
		while(it.hasNext()) {
			Block b = it.next();
			if(moving) {
				if(!b.isFalling() && b.getPosition().y == 32 && (b.getPosition().x + 32) > x - 18) {
					setX = true;
					x = b.getPosition().x + 50f;
					root.setX(x);
					moving = false;
					state.setAnimation("Attack", false);
					attackSpot.set(b.getPosition());
				}
			}
			else if(!moving && !b.isFalling() && state.isComplete() && b.getPosition().y == attackSpot.y && b.getPosition().x == attackSpot.x) {
				state.setAnimation("Attack", false);
				noBlock = false;
				if(!b.takeDamage()) {
					it.remove();
					moving = true;
					state.setAnimation("Move", true);
				}
			}
		}
		
		if(moving) {
			if(!setX) {
				x -= speed * Gdx.graphics.getDeltaTime();
			}
			root.setX(x);
		}
		else if(!moving && state.isComplete() && noBlock) {
			moving = true;
		}
		return health <= 0;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	public void takeDamage() {
		this.health--;
	}
}
