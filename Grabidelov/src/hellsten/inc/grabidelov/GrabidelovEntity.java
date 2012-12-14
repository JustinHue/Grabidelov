package hellsten.inc.grabidelov;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

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
	
	// ===========================================================
	// Constructors 
	// ===========================================================
	
	public GrabidelovEntity(float x, float y, TextureRegion textureReference) {
		super(x, y, textureReference);
		this.xSpeed = 0;
		this.ySpeed = 0;
		this.destroyed = false;
	}

	
	public GrabidelovEntity() {
		this(0, 0, null);
	}
	
	// ===========================================================
	// Mutators 
	// ===========================================================
	
	public void move() {
		move(this.xSpeed, this.ySpeed);
	}
	
	public void move(double horizontalDistance, double verticalDistance) {
		this.setPosition((float)(this.getX() + horizontalDistance), (float)(this.getY() + verticalDistance));
	}

	public void accelerate(double xAcceleration, double yAcceleration) {
		accelerateHorizontal(xAcceleration);
		accelerateVertical(yAcceleration);
	}
	
	public void accelerateHorizontal(double acceleration) {
		this.xSpeed += acceleration;
	}
	
	public void accelerateVertical(double acceleration) {
		this.ySpeed += acceleration;
	}
	
	public void setMass(double newMass) {
		this.mass = newMass;
	}
	
	public void destroy() {
		this.destroyed = true;
	}
	
	// ===========================================================
	// Accessors 
	// ===========================================================
	
	public double getMass() {
		return this.mass;
	}
	
	public double getXSpeed() {
		return xSpeed;
	}
	
	public double getYSpeed() {
		return ySpeed;
	}
	
	public double getCenterX() {
		return this.getX() + this.getWidth() / 2;
	}
	
	public double getCenterY() {
		return this.getY() + this.getHeight() / 2;
	}
	
	public int getClassId() {
		return classId;
	}
	
	public int getId() {
		return id;
	}
	
	public int getCollisionType() {
		return collisionType;
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}
	
}
