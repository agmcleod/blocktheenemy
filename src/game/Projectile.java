package game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Projectile {
	
	private Vector2 position;
	private Enemy target;
	private TextureRegion textureRegion;
	
	
	public Projectile(Vector2 spawn, Enemy target, TextureRegion textureRegion) {
		position = spawn;
		this.target = target;
		this.textureRegion = textureRegion;
	}
	
	public void update() {
		
	}
}
