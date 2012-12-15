/*
 * File Name: GrabidelovEntity.java
 * Author's Name: Justin Hellsten
 * Date: Decemeber 14, 2012
 * Purpose: This is the ultimate entity in the hierchy of grabidelov objects. Use this to abstractly define lower grabidelov entities as
 * 			one.
 * 
 */
package hellsten.inc.grabidelov;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

/* This class defines the grabidelov entity. The grabidelov entity extends the sprite object. It is an extension of the sprite object
 * in order to easily define it's textures.
 */
public class GrabidelovEntity extends Sprite {
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	protected int id;
	protected int classId;
	
	protected double xSpeed;
	protected double ySpeed;
	protected double mass;
	
	protected int collisionType;
	protected boolean destroyed;
	protected boolean attracts;
	
	// ===========================================================
	// Constructors 
	// ===========================================================
	
	
	public GrabidelovEntity(float x, float y, TextureRegion textureReference) {
		super(x, y, textureReference);
		this.xSpeed = 0;
		this.ySpeed = 0;
		this.destroyed = false;
		this.attracts = true;
	}

	
	public GrabidelovEntity() {
		this(0, 0, null);
	}
	
	// ===========================================================
	// Mutators 
	// ===========================================================
	
	/* This method moves the object based on it's x and y speed */
	public void move() {
		move(this.xSpeed, this.ySpeed);
	}
	
	/* This method moves the object massed on the passed parameters horizontal and vertical distance. */
	public void move(double horizontalDistance, double verticalDistance) {
		this.setPosition((float)(this.getX() + horizontalDistance), (float)(this.getY() + verticalDistance));
	}

	/* This method accelerates the object based on the passed parameters xAcceleration and yAcceleration. */
	public void accelerate(double xAcceleration, double yAcceleration) {
		accelerateHorizontal(xAcceleration);
		accelerateVertical(yAcceleration);
	}
	
	/* This method only accelerates the object horizontally */
	public void accelerateHorizontal(double acceleration) {
		this.xSpeed += acceleration;
	}
	
	/* This method only accelerates the object vertically */
	public void accelerateVertical(double acceleration) {
		this.ySpeed += acceleration;
	}
	
	/* This method gives the object a new mass */
	public void setMass(double newMass) {
		this.mass = newMass;
	}
	
	/* This method sets the flag of the object to be destroyed so that the inner works of the grabidelov engine can work */
	public void destroy() {
		this.destroyed = true;
	}
	
	/* This method sets the attraction flag of the object. Turn this on or off will make it attract or not attract other grabidelov
	 * entites.
	 */
	public void setAttraction(boolean flag) {
		this.attracts = flag;
	}
	
	// ===========================================================
	// Accessors 
	// ===========================================================
	
	/* This method gets the mass of the object */
	public double getMass() {
		return this.mass;
	}
	
	/* This method gets the x speed of the object */
	public double getXSpeed() {
		return xSpeed;
	}
	
	/* This method gets the y speed of the object */
	public double getYSpeed() {
		return ySpeed;
	}
	
	/* This method gets the center x position of the object */
	public double getCenterX() {
		return this.getX() + this.getWidth() / 2;
	}
	
	/* This method gets the center y position of the object */
	public double getCenterY() {
		return this.getY() + this.getHeight() / 2;
	}
	
	/* This method gets the class id of the object, which is used to idenify what class the entity is instansiated on. */
	public int getClassId() {
		return classId;
	}
	
	/* This method gets the id of the object, this is used in special cases if we want to specifically address the object */
	public int getId() {
		return id;
	}
	
	/* This method gets the collision type of the object, the type of collision can be either rectangler or circular */
	public int getCollisionType() {
		return collisionType;
	}
	
	/* This method determines if the object is or is not destroyed, this is used for inner workings of the grabidelov engine */
	public boolean isDestroyed() {
		return destroyed;
	}
	
	/* This method determines if the object is or is not attracting other objects */
	public boolean isAttracting() {
		return attracts;
	}


}
