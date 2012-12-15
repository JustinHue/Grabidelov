/*
 * File Name: GrabidelovGreen.java
 * Author's Name: Justin Hellsten
 * Date: Decemeber 14, 2012
 * Purpose: This defines the green projectile.
 * 
 */
package hellsten.inc.grabidelov;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

/* This class defines the green projectile. Green projectiles are greater in mass than all their other counterparts
 * However they are larger in mass compared to their red counterparts.
 */
public class GrabidelovGreen extends GrabidelovEntity {

	// ===========================================================
	// Static Variables
	// ===========================================================
	public static TextureRegion texture;
	
	// ===========================================================
	// Constants
	// ===========================================================
	
	
	public final static float DEFAULT_MASS = 1.0E2f;
	public final static int CLASS_ID = 0x003;
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	
	// ===========================================================
	// Constructors 
	// ===========================================================
	
	public GrabidelovGreen(double mass, float x, float y) {
		super(x, y, GrabidelovGreen.texture);
		this.xSpeed = 0;
		this.ySpeed = 0;
		this.mass = mass;
		this.classId = GrabidelovGreen.CLASS_ID;	
		this.collisionType = GrabidelovEngine.COLLISION_TYPE_CIRCLE;
		this.attachChild(new Sprite(0, 0, GrabidelovGreen.texture));
	}

	public GrabidelovGreen(float x, float y) {
		this(GrabidelovGreen.DEFAULT_MASS, x, y);
	}
	
	public GrabidelovGreen() {
		this(GrabidelovGreen.DEFAULT_MASS, 0, 0);
	}

}
