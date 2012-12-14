package hellsten.inc.grabidelov;


import java.io.IOException;

import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;

public class MainGameScreen extends BaseGameActivity {

	// ===========================================================
	// Constants
	// ===========================================================
	
	/* Defines splash screen width and height */
	private static final int CAMERA_WIDTH = 1024;
	private static final int CAMERA_HEIGHT = 614;
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	private int playerOrderCounter;
	
	/* Music and Sound variables */
	private Music mainTheme;
	private Sound projectileCollisionSound;
	private Sound sunCollisionSound;
	private Sound blackHoleCollisionSound;
	
	/* Game Textures (put these as static variables in the classes later ) */
	private Texture mSunTexture;
	private Texture mRedTexture;
	private Texture mBlueTexture;
	private Texture mGreenTexture;
	private Texture mDarkHoleTexture;
	
	private Camera mMainGameScreenCamera;
	private Engine mEngine;
	
	private GrabidelovEngine gEngine;
	
	private Scene mMainGameScene;
	private Entity gravityLayer;
	
	private GrabidelovEntity player;
	
	// use to help shoot the player
	private double lastX;
	private double lastY; 
	
	private Line dragLine;
	
	private Entity healthBarStatus;
	
	@Override
	public Engine onLoadEngine() {
		
		this.mMainGameScreenCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		
		/* Set the Grabidelov Engine */
		this.gEngine = new GrabidelovEngine(this.mEngine, this.mMainGameScreenCamera, gravityLayer);
		this.gEngine.setBoundaryFields(0, CAMERA_WIDTH, CAMERA_HEIGHT, 0);
		this.gEngine.setOnEntityCollisionListener(new GrabidelovEngine.OnEntityCollisionListener() {
			@Override
			public void collision(GrabidelovEntity e1, GrabidelovEntity e2) {
				
				/* Only remove the entity if it is hitting a sun */
				if (e1.getClassId() == GrabidelovSun.CLASS_ID && e2.getClassId() != GrabidelovSun.CLASS_ID) {
					gravityLayer.detachChild(e2);
					e2.destroy();
					MainGameScreen.this.projectileCollisionSound.play();
				} else if (e2.getClassId() == GrabidelovSun.CLASS_ID && e1.getClassId() != GrabidelovSun.CLASS_ID) {
					gravityLayer.detachChild(e1);
					e1.destroy();
					MainGameScreen.this.projectileCollisionSound.play();
				} 
				
				/* Check if projectiles hit the black hole, if so you win */
				if (e1.getClassId() == GrabidelovBlackHole.CLASS_ID && e2.getClassId() != GrabidelovBlackHole.CLASS_ID ||
						e2.getClassId() == GrabidelovBlackHole.CLASS_ID && e1.getClassId() != GrabidelovBlackHole.CLASS_ID) {
					gravityLayer.detachChild(e1);
					e1.destroy();
					gravityLayer.detachChild(e2);
					e2.destroy();				
					MainGameScreen.this.blackHoleCollisionSound.play();
				}
				
				/* Recreate the player */
				player = createNextPlayer(MyConstants.START_LOCATION_X, MyConstants.START_LOCATION_Y);
				gravityLayer.attachChild(player);
				mMainGameScene.registerTouchArea(player);
				
				/* Deduct entity from health bar */
				healthBarStatus.detachChild(healthBarStatus.getFirstChild());
			}
		
			
		});
		
		this.gEngine.setOnBoundaryCollisionListener(new GrabidelovEngine.OnBoundaryCollisionListener() {
			
			@Override
			public void collision(int side, GrabidelovEntity e) {
				
				gravityLayer.detachChild(e);
				e.destroy();
				MainGameScreen.this.projectileCollisionSound.play();

				/* Recreate the player */
				player = createNextPlayer(MyConstants.START_LOCATION_X, MyConstants.START_LOCATION_Y);
				gravityLayer.attachChild(player);
				mMainGameScene.registerTouchArea(player);
			}
			
		});
	
		this.mEngine = new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
				this.mMainGameScreenCamera).setNeedsSound(true).setNeedsMusic(true));
		
		return mEngine;
	}

	@Override
	public void onLoadResources() {

		//===========================================================
		// Load In Game Resources
		//===========================================================
		
		/* Import sound/music files */
		SoundFactory.setAssetBasePath("mfx/");
		try {
			this.projectileCollisionSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "projectile_collision.wav");
			this.sunCollisionSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "sun_collision.wav");
			this.blackHoleCollisionSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "black_hole_collision.wav");
		} catch (final IOException e) {
			Debug.e(e);
		}
		
		MusicFactory.setAssetBasePath("mfx/");
		try {
			this.mainTheme = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), MainGameScreen.this, "Pulse.mp3");
			this.mainTheme.setLooping(true);
		} catch (final IOException e) {
			Debug.e(e);
		}
		
		/* Set the texture set up in the gfx directory */
		TextureRegionFactory.setAssetBasePath("gfx/");

		/* Load in game resources */
		
		/* Set the Game textures */
		this.mSunTexture = new Texture(64, 64);
		GrabidelovSun.texture = TextureRegionFactory.createFromAsset(
				this.mSunTexture, this, "game/sun.png", 0, 0);

		this.mRedTexture = new Texture(32, 32);
		GrabidelovRed.texture = TextureRegionFactory.createFromAsset(
				this.mRedTexture, this, "game/red_ball.png", 0, 0);
		
		this.mBlueTexture = new Texture(32, 32);
		GrabidelovBlue.texture = TextureRegionFactory.createFromAsset(
				this.mBlueTexture, this, "game/blue_ball.png", 0, 0);
		
		this.mGreenTexture = new Texture(32, 32);
		GrabidelovGreen.texture = TextureRegionFactory.createFromAsset(
				this.mGreenTexture, this, "game/green_ball.png", 0, 0);
		
		this.mDarkHoleTexture = new Texture(64, 64);
		GrabidelovBlackHole.texture = TextureRegionFactory.createFromAsset(
				this.mDarkHoleTexture, this, "game/dark_hole.png", 0, 0);
		
		/* Load the game textures in to the texture manager */
		this.mEngine.getTextureManager().loadTexture(this.mSunTexture);	
		this.mEngine.getTextureManager().loadTexture(this.mRedTexture);	
		this.mEngine.getTextureManager().loadTexture(this.mBlueTexture);	
		this.mEngine.getTextureManager().loadTexture(this.mGreenTexture);	
		this.mEngine.getTextureManager().loadTexture(this.mDarkHoleTexture);	
		
	}

	@Override
	public Scene onLoadScene() {
		
		/* Initialize game variables */
		playerOrderCounter = 0;
		dragLine = null;
		
		this.mEngine.registerUpdateHandler(new FPSLogger());
		
		this.mainTheme.play();
		
		mMainGameScene = new Scene(1);
		mMainGameScene.setBackground(new ColorBackground(0.8f, 0.8f, 0.9f)); // set the scene background to a light light grey
		
		this.gravityLayer = new Entity();
		
		/* Create the health bar status */
		final Entity healthBarStatus = new Entity(CAMERA_WIDTH - 200, 10);
		healthBarStatus.attachChild(new GrabidelovRed(40, 0));
		healthBarStatus.attachChild(new GrabidelovBlue(80, 0));
		healthBarStatus.attachChild(new GrabidelovGreen(120, 0));
		
		/* Create the level */
		GrabidelovSun sun1 = new GrabidelovSun(450, 400);
		GrabidelovSun sun2 = new GrabidelovSun(450, 200);
		GrabidelovBlackHole darkHole = new GrabidelovBlackHole(100, 300);
		player = createNextPlayer(MyConstants.START_LOCATION_X, MyConstants.START_LOCATION_Y);
		
		/* Add the sun and dark hole to the immune gravity list because we do not want them 
		 * to fall into eachother.
		 */
		
		this.gEngine.addToGravityImmuneList(GrabidelovSun.CLASS_ID);
		this.gEngine.addToGravityImmuneList(GrabidelovBlackHole.CLASS_ID);
		
		this.gEngine.addGrabidelovEntity(sun1);
		this.gEngine.addGrabidelovEntity(sun2);
		this.gEngine.addGrabidelovEntity(darkHole);
		
		this.gravityLayer.attachChild(sun1);
		this.gravityLayer.attachChild(sun2);
		this.gravityLayer.attachChild(darkHole);
		this.gravityLayer.attachChild(player);

	
		
		/* Add gravity layer to the scene */
		this.mMainGameScene.attachChild(this.gravityLayer);
		this.mMainGameScene.attachChild(healthBarStatus);
		
		this.mMainGameScene.registerTouchArea(player);
		this.mMainGameScene.setTouchAreaBindingEnabled(true);
		
		//this.mMainGameScene.setTouchAreaBindingEnabled(true);
		
		// Set up the main game scene loop
		mMainGameScene.registerUpdateHandler(new IUpdateHandler() {                    
            public void reset() {        
            }             
            public void onUpdate(float pSecondsElapsed) {      
            	gEngine.run();
            }
        });
		
		return mMainGameScene;
	}

	@Override
	public void onLoadComplete() {
	}

	/*
	 * This method creates the next character in the sequence
	 */
	public GrabidelovEntity createNextPlayer(float x, float y) {
		
		GrabidelovEntity retEntity = null;
		
		if (playerOrderCounter > MyConstants.MAX_NUM_LIVES) {
			/* Game over */
			
		} else {
		
			if (MyConstants.playerOrder[playerOrderCounter].equals("red")) {
				retEntity = new GrabidelovRed(x, y) {
					@Override
					public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
						
						playerTouch(this, pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
	         
						return true;
					}
	        }; 
			} else if (MyConstants.playerOrder[playerOrderCounter].equals("blue")) {
				retEntity = new GrabidelovBlue(x, y) {
					@Override
					public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
						
						playerTouch(this, pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
	         
						return true;
					}
	        };
			} else if (MyConstants.playerOrder[playerOrderCounter].equals("green")) {
				retEntity = new GrabidelovGreen(x, y) {
					@Override
					public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
						
						playerTouch(this, pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
	         
						return true;
					}
	        };
			}
			
			playerOrderCounter++;
		}
		return retEntity;
	}
	
	public void playerTouch(GrabidelovEntity player, final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		int eventaction = pSceneTouchEvent.getAction(); 
		
        switch (eventaction) {
           case TouchEvent.ACTION_DOWN:
        	   	MainGameScreen.this.lastX = player.getCenterX();
        	   	MainGameScreen.this.lastY = player.getCenterY();
        	    break;
           case TouchEvent.ACTION_MOVE: {
        	   player.setPosition(pSceneTouchEvent.getX() - player.getWidth() / 2, pSceneTouchEvent.getY() - player.getHeight() / 2);
        	    
        	    /* Create a line between the last position and current position of the player */
        	    if (MainGameScreen.this.dragLine != null) {
        	    	MainGameScreen.this.mMainGameScene.detachChild(MainGameScreen.this.dragLine);
        	    }

        	    	MainGameScreen.this.dragLine = new Line ((float)MainGameScreen.this.lastX, (float)MainGameScreen.this.lastY, 
        	    			(float)player.getCenterX(), (float)player.getCenterY());
        	    	MainGameScreen.this.dragLine.setColor(0f, 0f, 0.5f); /* Set the color of the line to blue */
        	    	
            	    MainGameScreen.this.mMainGameScene.attachChild(MainGameScreen.this.dragLine);
        	    
    	        break;}
           case TouchEvent.ACTION_UP:
        	    /* Shoot the player */
        	   player.accelerate((MainGameScreen.this.lastX - player.getCenterX()) * MyConstants.SHOOT_FACTOR, 
        	    		(MainGameScreen.this.lastY - player.getCenterY()) * MyConstants.SHOOT_FACTOR);
        	    MainGameScreen.this.gEngine.addGrabidelovEntity(player); // add the player to the grabidelov engine
        	    MainGameScreen.this.mMainGameScene.detachChild(MainGameScreen.this.dragLine);
                break;
        }
	}
}
