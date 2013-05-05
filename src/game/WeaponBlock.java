package game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class WeaponBlock extends Block {
	
	private TextureRegion arrow;
	private final float ATTACK_RATE = 1;
	private float attackTimer;
	private Array<Projectile> projectiles;
	
	public WeaponBlock(TextureRegion[] textureStates, Vector2 pos, TextureRegion arrow) {
		super(textureStates, pos);
		attackTimer = ATTACK_RATE;
		projectiles = new Array<Projectile>();
		this.arrow = arrow;
	}
	
	public void attack(Array<Enemy> enemies) {
		if(attackTimer >= ATTACK_RATE) {
			Enemy target = enemies.first();
			if(target != null) {
				float distance = (target.getRoot().getX() - getPosition().x);
				boolean inRange = (target != null && distance <= (32 * 15) && distance > 32);
				if(inRange) {
					attackTimer = 0;
					projectiles.add(new Projectile(new Vector2(getPosition().x, getPosition().y + 16), target, arrow));
				}
			}
		}
	}
	
	public void render(SpriteBatch batch) {
		super.render(batch);
		Iterator<Projectile> it = projectiles.iterator();
		while(it.hasNext()) {
			Projectile p = it.next();
			p.render(batch);
		}
	}
	
	public void update(Array<Block> blocks, Array<Enemy> enemies) {
		super.update(blocks);
		attackTimer += Gdx.graphics.getDeltaTime();
		if(!isFalling()) {
			attack(enemies);
		}
		
		Iterator<Projectile> it = projectiles.iterator();
		while(it.hasNext()) {
			Projectile p = it.next();
			Enemy target = enemies.first();
			if(p.update(target)) {
				Enemy e = p.getTarget();
				if(e != null) {
					e.takeDamage();
				}
				
				it.remove();
			}
		}
	}
}
