/*
 * File Name: GrabidelovBlue.java
 * Author's Name: Justin Hellsten
 * Date: Decemeber 14, 2012
 * Purpose: This defines the blue projectile. 
 * 
 */
package hellsten.inc.grabidelov;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

/* This class defines the blue projectile. Blue projectiles are smaller in mass than their green counterparts.
 * However they are larger in mass compared to their red counterparts.
 */
public class GrabidelovBlue extends GrabidelovEntity {

	// ===========================================================
	// Static Variables
	// ===========================================================
	public static TextureRegion texture;
	
	// ===========================================================
	// Constants
	// ===========================================================
	
	public final static float DEFAULT_MASS = 5.0f;
	public final static int CLASS_ID = 0x004;
	
	// ===========================================================
	// Constructors 
	// ===========================================================
	
	public GrabidelovBlue(double mass, float x, float y) {
		super(x, y, GrabidelovBlue.texture);
		this.xSpeed = 0;
		this.ySpeed = 0;
		this.mass = mass;
		this.classId = GrabidelovBlue.CLASS_ID;
		this.collisionType = GrabidelovEngine.COLLISION_TYPE_CIRCLE;
		this.attachChild(new Sprite(0, 0, GrabidelovBlue.texture));
	}

	public GrabidelovBlue(float x, float y) {
		this(GrabidelovBlue.DEFAULT_MASS, x, y);
	}
	
	public GrabidelovBlue() {
		this(GrabidelovBlue.DEFAULT_MASS, 0, 0);
	}

}
