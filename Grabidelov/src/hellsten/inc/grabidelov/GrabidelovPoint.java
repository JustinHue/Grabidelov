package hellsten.inc.grabidelov;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class GrabidelovPoint extends GrabidelovEntity {

	// ===========================================================
	// Static Variables
	// ===========================================================
	public static TextureRegion texture;
	
	// ===========================================================
	// Constants
	// ===========================================================
	
	
	public final static float DEFAULT_MASS = 0f;
	public final static int CLASS_ID = 0x006;
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	
	// ===========================================================
	// Constructors 
	// ===========================================================
	
	public GrabidelovPoint(double mass, float x, float y) {
		super(x, y, GrabidelovPoint.texture);
		this.mass = mass;
		this.classId = GrabidelovPoint.CLASS_ID;
		this.collisionType = GrabidelovEngine.COLLISION_TYPE_CIRCLE;
		this.attachChild(new Sprite(0, 0, GrabidelovPoint.texture));
	}
	
	public GrabidelovPoint(float x, float y) {
		this(GrabidelovPoint.DEFAULT_MASS, x, y);
	}
	
	public GrabidelovPoint() {
		this(GrabidelovPoint.DEFAULT_MASS, 0, 0);
	}
	
}
