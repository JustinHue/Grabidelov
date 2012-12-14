package hellsten.inc.grabidelov;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class GrabidelovSun extends GrabidelovEntity {
	
	// ===========================================================
	// Static Variables
	// ===========================================================
	public static TextureRegion texture;
	
	// ===========================================================
	// Constants
	// ===========================================================
	
	public final static float DEFAULT_MASS = 5.0E3f;
	public final static int CLASS_ID = 0x001;
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	
	// ===========================================================
	// Constructors 
	// ===========================================================
	
	public GrabidelovSun(double mass, float x, float y) {
		super(x, y, GrabidelovSun.texture);
		this.xSpeed = 0;
		this.ySpeed = 0;
		this.mass = mass;
		this.classId = GrabidelovSun.CLASS_ID;
		this.collisionType = GrabidelovEngine.COLLISION_TYPE_CIRCLE;
		this.attachChild(new Sprite(0, 0, GrabidelovSun.texture));
	}

	public GrabidelovSun(float x, float y) {
		this(GrabidelovSun.DEFAULT_MASS, x, y);
	}
	
	public GrabidelovSun() {
		this(GrabidelovSun.DEFAULT_MASS, 0, 0);
	}
	
}
