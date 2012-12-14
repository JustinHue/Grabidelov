package hellsten.inc.grabidelov;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.Entity;

public class GrabidelovEngine {
	
	// ===========================================================
	// Constants
	// ===========================================================
	
	public final static double GRAVITY_CONSTANT = 1;
	
	public final static int TOP_SIDE = 0;
	public final static int RIGHT_SIDE = 1;
	public final static int BOTTOM_SIDE = 2;
	public final static int LEFT_SIDE = 3;
	public final static int NO_SIDE = -1;
	
	public final static int NUMBER_OF_SIDES = 4;
	
	public final static int COLLISION_TYPE_RECT = 0; 
	public final static int COLLISION_TYPE_CIRCLE = 1;
	
	// ===========================================================
	// Interfaces
	// ===========================================================
	/* Call back functions */
	public interface OnEntityCollisionListener {
		void collision(GrabidelovEntity e1, GrabidelovEntity e2);
	}
	
	public interface OnBoundaryCollisionListener {
		void collision(int side, GrabidelovEntity e);
	}
	
	// ===========================================================
	// Fields
	// ===========================================================

	private OnEntityCollisionListener ecl;
	private OnBoundaryCollisionListener bcl;
	
	private ArrayList <GrabidelovEntity> entities;
	private Camera cameraContext;
	private Entity layerContext;
	private Engine engineContext;
	
	// Boundary fields
	private float leftBoundary, topBoundary, rightBoundary, bottomBoundary;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public GrabidelovEngine(Engine engineContext, Camera cameraContext, Entity layerContext) {
		
		this.leftBoundary = 0;
		this.topBoundary = 0;
		this.rightBoundary = 0;
		this.bottomBoundary = 0;
		
		this.engineContext = engineContext;
		this.cameraContext = cameraContext;
		this.layerContext = layerContext;
		this.entities = new ArrayList<GrabidelovEntity>();
		
	}
	
	//=====================================================================================
	// 									Public Methods
	//=====================================================================================
	
	// This method sets the boundary fields
	public void setBoundaryFields(float topBoundary, float rightBoundary, float bottomBoundary, float leftBoundary) {
		this.topBoundary = topBoundary;
		this.rightBoundary = rightBoundary;
		this.bottomBoundary = bottomBoundary;
		this.leftBoundary = leftBoundary;
	}
	
	// This method checks if the entities are within the boundary limit, if not they are removed
	private void checkBoundaryLimit(final GrabidelovEntity entity) {


	}
	
	// This method takes in an entity arguement and shoots the entity
	// from any boundary into the scene.
	public void shootEntityFromAnyBoundary(GrabidelovEntity entity, float minimumSpeed, float maximumSpeed) {
			
		// Add the entity to the list
		addGrabidelovEntity(entity);
			
		// Pick random side to shoot from
		Random randomGenerator = new Random();
		int sideToShootFrom = randomGenerator.nextInt(GrabidelovEngine.NUMBER_OF_SIDES);
			
		// Generate random speeds
		double xSpeed = randomGenerator.nextFloat() * (maximumSpeed - minimumSpeed) + minimumSpeed;
		double ySpeed = randomGenerator.nextFloat() * (maximumSpeed - minimumSpeed) + minimumSpeed;
		int randomDirection = randomGenerator.nextInt(1) == 0 ? - 1 : 1;
			
		switch (sideToShootFrom) {
			case TOP_SIDE:
				entity.setPosition(randomGenerator.nextFloat() * cameraContext.getWidth(), -entity.getHeight());
				entity.accelerate(xSpeed * randomDirection, ySpeed);
				break;
			case RIGHT_SIDE:
				entity.setPosition(entity.getWidth() + cameraContext.getWidth(), randomGenerator.nextFloat() * cameraContext.getHeight());
				entity.accelerate(xSpeed, ySpeed * randomDirection);
				break;
			case BOTTOM_SIDE:
				entity.setPosition(randomGenerator.nextFloat() * cameraContext.getWidth(), cameraContext.getHeight() + entity.getHeight());				
				entity.accelerate(xSpeed * randomDirection, -ySpeed);
				break;
			case LEFT_SIDE:
				entity.setPosition(-entity.getWidth(), randomGenerator.nextFloat() * cameraContext.getHeight());
				entity.accelerate(xSpeed, ySpeed * randomDirection);
			break;
			default:
				entity.setPosition(randomGenerator.nextFloat() * cameraContext.getWidth(), -entity.getHeight());
				entity.accelerate(xSpeed * randomDirection, ySpeed);
			break;
		}
			
	}
		
	//=====================================================================================================================================
	// 													   Accessors
	//=====================================================================================================================================

	
	//=====================================================================================
	// -- Gets the size of the entities list.
	//=====================================================================================	
	public int getListSize() {
		return entities.size();
	}
	
	//=====================================================================================================================================
	// 													Public Methods
	//=====================================================================================================================================

	//=====================================================================================
	// -- This method runs the Grabidelov engine by moving all the Grabidelov entities
	// -- base on the law of gravity between the masses.
	//=====================================================================================	
	public void run() {
		
		// Don't bother if the list is empty
		if (entities.isEmpty()) return;
		
		// Loop through all the entities and calculate the gravity force between them and apply it.
		for (int i = 0; i < entities.size(); i++) {
			
			GrabidelovEntity entity1 = entities.get(i);
			
			for (int k = 0; k < entities.size(); k++) {
				
				GrabidelovEntity entity2 = entities.get(k);
				
				if (entity1 != entity2) {
					// Find the radius between the entity and the point of gravitation.
					double radius = calculateRadius(entity1.getCenterX(), entity1.getCenterY(), entity2.getCenterX(), entity2.getCenterY());
		
					// Find the acceleration of the force
					double gravityForce = calculateGravitationalForce(entity1.getMass(), entity2.getMass(), radius);
					double acceleration = calculateAcceleration(gravityForce, entity2.getMass());
						
					// find the angle between the two masses
					double angle = Math.atan2(entity1.getCenterY() - entity2.getCenterY(), 
											  entity1.getCenterX() - entity2.getCenterX());
						
					// Apply the acceleration to the entity
					entity2.accelerateHorizontal(findHorizontalComponent(acceleration, angle));
					entity2.accelerateVertical(findVerticalComponent(acceleration, angle));
				}
			}    
			
		}

		// Move all the entities and check their boundaries
		for (GrabidelovEntity entity : entities) {
			
			entity.move();
			
		}
		
		/* Check for collisions beteween entities */
		for (int i = 0; i < entities.size(); i++) {
			
			GrabidelovEntity e1 = entities.get(i);
			
			if (e1 != null) {
				
				for (int k = i + 1; k < entities.size(); k++) {
					
					GrabidelovEntity e2 = entities.get(k);
					
					if (e2 != null) {
						 
						boolean collision = false;
						
						// Each entity can have different collision types, treat the collision type for the first entity for now....
						if (e1.getCollisionType() == GrabidelovEngine.COLLISION_TYPE_RECT) {
							collision = rectCollisionDetect(e1, e2);
						} else if (e1.getCollisionType() == GrabidelovEngine.COLLISION_TYPE_CIRCLE) {
							collision = circleCollisionDetect(e1, e2);
						}
						
						/* Call the on collision listener */
						if (collision) {
							ecl.collision(e1, e2);
							break;
						}
						
					}
					
				}
				
			}
			
		}
		
		/* Perform a destroy clean up */
		for(int i = 0; i < entities.size(); i++) {
	        GrabidelovEntity e = entities.get(i);
	        if(e.isDestroyed()) {
	                entities.remove(e);
	                i--; // collection modified, update index.
	        }
		}
		
		/* Check for boundary collisions */
		for (int i = 0; i < entities.size(); i++) {
			GrabidelovEntity e = entities.get(i);
			if (e != null) {
				int collision = isWithinBoundary(e, leftBoundary, topBoundary, rightBoundary, bottomBoundary);
				if (collision != NO_SIDE) {			
					bcl.collision(collision, e);				
				}
			}
		}

		/* Perform a destroy clean up */
		for(int i = 0; i < entities.size(); i++) {
	        GrabidelovEntity e = entities.get(i);
	        if(e.isDestroyed()) {
	                entities.remove(e);
	                i--; // collection modified, update index.
	        }
		}
		
	}
	
	//=====================================================================================
	// -- Sets the collision listeners.
	//=====================================================================================	
	public void setOnEntityCollisionListener(OnEntityCollisionListener onCollisionListener) {
		ecl = onCollisionListener;
	}
	public void setOnBoundaryCollisionListener(OnBoundaryCollisionListener onCollisionListener) {
		bcl = onCollisionListener;
	}
	

	//=====================================================================================
	// -- Determines if the entity is within the boundaries arguements passed, returns
	// -- the side the collision occured.
	//=====================================================================================	
	public int isWithinBoundary(GrabidelovEntity entity, 
				float leftBoundary, float topBoundary, float rightBoundary, float bottomBoundary) {
		if (entity.getX() < leftBoundary) {
			return GrabidelovEngine.LEFT_SIDE;
		} else if (entity.getY() < topBoundary) {
			return GrabidelovEngine.TOP_SIDE;
		} else if (entity.getX() + entity.getWidth() > rightBoundary ) {
			return GrabidelovEngine.RIGHT_SIDE;
		} else if (entity.getY() + entity.getHeight() > bottomBoundary) {
			return GrabidelovEngine.BOTTOM_SIDE;
		} else {
			return NO_SIDE;
		}
	}
	
	//=====================================================================================
	// -- Determines if two Grabidelov entites have collided, returns true if they collided,
	// -- and false if they have not. The collision detection is for rectangle like entities.
	//=====================================================================================	
	public boolean rectCollisionDetect(GrabidelovEntity e1, GrabidelovEntity e2) {
		if (e1.getX() + e1.getWidth() > e2.getX() && e1.getX() < e2.getX() + e2.getWidth() &&
			e1.getY() + e1.getHeight() > e2.getY() && e1.getY() < e2.getY() + e2.getHeight()) {
			return true;
		} else {
			return false;
		}
	}
	
	//=====================================================================================
	// -- Determines if two Grabidelov entites have collided, returns true if they collided,
	// -- and false if they have not. The collision detection is for circle like entities.
	//=====================================================================================	
	public boolean circleCollisionDetect(GrabidelovEntity e1, GrabidelovEntity e2) {
		double radius = calculateRadius(e1.getCenterX(), e1.getCenterY(), e2.getCenterX(), e2.getCenterY());
		double totalDiameter = e1.getWidth() / 2 + e2.getHeight() / 2;
		// compare the radius between the entities and their combined diameters
		if (totalDiameter - radius > 0) {
			return true;
		} else {
			return false;
		}
	}
	//=====================================================================================
	// -- Adds Grabidelov entities to the list
	//=====================================================================================
	public void addGrabidelovEntity(GrabidelovEntity entity) {
		entities.add(entity);
	}
	
	//=====================================================================================
	// -- Grabs the grabidelov entities
	//=====================================================================================	
	public ArrayList<GrabidelovEntity> getGrabidelovEntities() {
		return entities;
	}
	
	//=====================================================================================================================================
	// 													Private Methods
	//=====================================================================================================================================

	//=====================================================================================
	// -- Calculate force of gravity
	//=====================================================================================
	private double calculateGravitationalForce(double m1, double m2, double r) {
		return GRAVITY_CONSTANT * (m1*m2) / (r*r);
	}

	//=====================================================================================
	// -- Calculate radius between masses
	//=====================================================================================
	private double calculateRadius(double m1x, double m1y, double m2x, double m2y) {
		return Math.sqrt((m1x - m2x) * (m1x - m2x) + (m1y - m2y) * (m1y - m2y));
	}

	//=====================================================================================
	// -- Calculate acceleration due to force and mass
	//=====================================================================================
	private double calculateAcceleration(double f, double m) {
		return f / m;
	}
	
	//=====================================================================================
	// -- Find components  
	//=====================================================================================
	double findHorizontalComponent(double component, double angle) {
		return component * Math.cos(angle);
	} 
	double findVerticalComponent(double component, double angle) {
		return component * Math.sin(angle);
	}
	
	
	
}
