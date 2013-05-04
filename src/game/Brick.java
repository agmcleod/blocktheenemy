package game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Brick extends Block {
	public Brick(TextureRegion[] textureStates, Vector2 pos) {
		super(textureStates, pos, 8);
	}
}
