package game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class WeaponBlock extends Block {
	
	private float attackTimer;
	
	public WeaponBlock(TextureRegion[] textureStates, Vector2 pos) {
		super(textureStates, pos);
		attackTimer = 0.2f;
	}
	
	public void attack(Array<Enemy> enemies) {
		Enemy target = enemies.first();
		boolean inRange = target != null && (target.getRoot().getX() - getPosition().x) <= (32 * 15);
		if(attackTimer >= 0.2f && inRange) {
			
		}
	}
}
