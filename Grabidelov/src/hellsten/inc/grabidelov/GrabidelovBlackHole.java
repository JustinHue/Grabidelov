package hellsten.inc.grabidelov;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class GrabidelovBlackHole extends GrabidelovEntity {


	// ===========================================================
	// Static Variables
	// ===========================================================
	public static TextureRegion texture;
	
	// ===========================================================
	// Constants
	// ===========================================================
	
	
	public final static float DEFAULT_MASS = 5.0E2f;
	public final static int CLASS_ID = 0x005;
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	
	// ===========================================================
	// Constructors 
	// ===========================================================
	
	public GrabidelovBlackHole(double mass, float x, float y) {
		super(x, y, GrabidelovBlackHole.texture);
		this.mass = mass;
		this.classId = GrabidelovBlackHole.CLASS_ID;
		this.collisionType = GrabidelovEngine.COLLISION_TYPE_CIRCLE;
		this.attachChild(new Sprite(0, 0, GrabidelovBlackHole.texture));
	}
	
	public GrabidelovBlackHole(float x, float y) {
		this(GrabidelovBlackHole.DEFAULT_MASS, x, y);
	}
	
	public GrabidelovBlackHole() {
		this(GrabidelovBlackHole.DEFAULT_MASS, 0, 0);
	}

}
