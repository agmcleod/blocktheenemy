package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Projectile {
	
	private float angle = 0;
	private int movementSpeed = 450;
	private Vector2 position;
	private Enemy target;
	private TextureRegion textureRegion;
	
	public Projectile(Vector2 spawn, Enemy target, TextureRegion textureRegion) {
		position = spawn;
		this.target = target;
		this.textureRegion = textureRegion;
	}
	
	public Enemy getTarget() {
		return target;
	}
	
	public void render(SpriteBatch batch) {
		//batch.draw(textureRegion, position.x, position.y, 0, 0, textureRegion.getRegionWidth(), textureRegion.getRegionHeight(), 1, 1, angle, false);
		batch.draw(textureRegion, position.x, position.y, textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
	}

	public void setTarget(Enemy target) {
		this.target = target;
	}
	
	public boolean update(Array<Enemy> enemies) {
		target = enemies.first();
		float targetY = position.y;
		float targetX = Gdx.graphics.getWidth();
		if(target != null) {
			targetX = target.getRoot().getX();
			targetY = target.getRoot().getY();
		}
		
		angle = MathUtils.atan2(targetY - position.y, targetX - position.x) * (180 / MathUtils.PI);
		if(angle < 0) {
			angle = 360 - (-angle);
		}
		
		int velocityX = (int) (MathUtils.cos(angle * MathUtils.PI / 180) * this.movementSpeed * Gdx.graphics.getDeltaTime());
		int velocityY = (int) (MathUtils.sin(angle * MathUtils.PI / 180) * this.movementSpeed * Gdx.graphics.getDeltaTime());
		position.x += velocityX;
		position.y += velocityY;
		return position.x >= targetX;
	}
}
