package hellsten.inc.grabidelov;

import java.io.IOException;
import java.util.Random;

import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;

import android.content.Intent;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Toast;

public class GameControlScreen extends BaseGameActivity implements
			IOnMenuItemClickListener {

	// ===========================================================
	// Constants
	// ===========================================================

	private static final int SHOOT_DELAY = 500;
	
	/* Defines splash screen width and height */
	private static final int CAMERA_WIDTH = 1024;
	private static final int CAMERA_HEIGHT = 614;
	
	/* Define menu items */
	private static final int MENU_PLAY = 0;
	private static final int MENU_INSTRUCTIONS = MENU_PLAY + 1;
	private static final int MENU_LEAVE = MENU_INSTRUCTIONS + 1;
	
	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mGameControlCamera;
	private Scene mMainScene;
	private Engine mEngine;
	
	private GrabidelovEngine gEngine;
	
	private Handler mGameControlHandler;
	
	protected MenuScene mStaticMenuScene;
	
	/* Create the gravity layer entity */
	private Entity gravityLayer;
	
	/* Game Control Screen textures */
	
	private Texture mPlayTexture;
	private TextureRegion mPlayTextureRegion;
	
	private Texture mInstructionsTexture;
	private TextureRegion mInstructionsTextureRegion;

	private Texture mLeaveTexture;
	private TextureRegion mLeaveTextureRegion;
	
	/* Game Textures (put these as static variables in the classes later ) */
	private Texture mSunTexture;
	private Texture mRedTexture;
	private Texture mBlueTexture;
	private Texture mGreenTexture;
	
	private boolean popupDisplayed;

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public Engine onLoadEngine() {
		
		this.mGameControlHandler = new Handler();
		this.mGameControlCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		this.mEngine = new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
				this.mGameControlCamera).setNeedsSound(true).setNeedsMusic(true));
		
		/* Set the Grabidelov Engine */
		this.gEngine = new GrabidelovEngine(this.mEngine, this.mGameControlCamera, gravityLayer);
		this.gEngine.setBoundaryFields(-1000, 2000, 1500, -1000);
		this.gEngine.setOnEntityCollisionListener(new GrabidelovEngine.OnEntityCollisionListener() {
			
			@Override
			public void collision(GrabidelovEntity e1, GrabidelovEntity e2) {
				
				/* Only remove the entity if it is hitting a sun */
				if (e1.getClassId() == GrabidelovSun.CLASS_ID && e2.getClassId() != GrabidelovSun.CLASS_ID) {
					gravityLayer.detachChild(e2);
					e2.destroy();
					Resources.projectileCollisionSound.play();
				} else if (e2.getClassId() == GrabidelovSun.CLASS_ID && e1.getClassId() != GrabidelovSun.CLASS_ID) {
					gravityLayer.detachChild(e1);
					e1.destroy();
					Resources.projectileCollisionSound.play();
				} 
				
				/* If they are both suns get rid of them both */
				if (e1.getClassId() == GrabidelovSun.CLASS_ID && e2.getClassId() == GrabidelovSun.CLASS_ID) {
					gravityLayer.detachChild(e1);
					e1.destroy();				
					gravityLayer.detachChild(e2);
					e2.destroy();
					Resources.sunCollisionSound.play();
				}
				
			}
			
		});
		
		this.gEngine.setOnBoundaryCollisionListener(new GrabidelovEngine.OnBoundaryCollisionListener() {
			
			@Override
			public void collision(int side, GrabidelovEntity e) {
				gravityLayer.detachChild(e);
				e.destroy();
			}
			
		});
		
		return this.mEngine;
		
	}

	@Override
	public void onLoadResources() {
		
		/* Import sound/music files */
		SoundFactory.setAssetBasePath("mfx/");
		try {
			Resources.projectileCollisionSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "projectile_collision.wav");
			Resources.sunCollisionSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "sun_collision.wav");
		} catch (final IOException e) {
			Debug.e(e);
		}
		
		MusicFactory.setAssetBasePath("mfx/");
		try {
			Resources.menuTheme = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), GameControlScreen.this, "orbit.mp3");
			Resources.menuTheme.setLooping(true);
		} catch (final IOException e) {
			Debug.e(e);
		}
		
		/* Set the texture set up in the gfx directory */
		TextureRegionFactory.setAssetBasePath("gfx/");
		
		/* Set the play texture */
		this.mPlayTexture = new Texture(256, 128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mPlayTextureRegion = TextureRegionFactory.createFromAsset(
				this.mPlayTexture, this, "menu/play.png", 0, 0);

		/* Set the instructions texture */
		this.mInstructionsTexture = new Texture(1024, 128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mInstructionsTextureRegion = TextureRegionFactory.createFromAsset(
				this.mInstructionsTexture, this, "menu/instructions.png", 0, 0);

		/* Set the leave texture */
		this.mLeaveTexture = new Texture(256, 128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mLeaveTextureRegion = TextureRegionFactory.createFromAsset(
				this.mLeaveTexture, this, "menu/leave.png", 0, 0);
		
		
		/* Set the texture set up in the gfx directory */
		TextureRegionFactory.setAssetBasePath("gfx/");
		
		/* Set the play texture */
		this.mPlayTexture = new Texture(256, 128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mPlayTextureRegion = TextureRegionFactory.createFromAsset(
				this.mPlayTexture, this, "menu/play.png", 0, 0);

		/* Set the instructions texture */
		this.mInstructionsTexture = new Texture(1024, 128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mInstructionsTextureRegion = TextureRegionFactory.createFromAsset(
				this.mInstructionsTexture, this, "menu/instructions.png", 0, 0);

		/* Set the leave texture */
		this.mLeaveTexture = new Texture(256, 128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mLeaveTextureRegion = TextureRegionFactory.createFromAsset(
				this.mLeaveTexture, this, "menu/leave.png", 0, 0);
	
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
		
		/* Load the game textures in to the texture manager */
		this.mEngine.getTextureManager().loadTexture(this.mSunTexture);	
		this.mEngine.getTextureManager().loadTexture(this.mRedTexture);	
		this.mEngine.getTextureManager().loadTexture(this.mBlueTexture);	
		this.mEngine.getTextureManager().loadTexture(this.mGreenTexture);	
		
		/* Load other textures to the texture manager */
		this.mEngine.getTextureManager().loadTexture(this.mInstructionsTexture);
		this.mEngine.getTextureManager().loadTexture(this.mPlayTexture);		
		this.mEngine.getTextureManager().loadTexture(this.mLeaveTexture);		
			
		
	}

	@Override
	public Scene onLoadScene() {
		
		/* Play the main theme */
		Resources.menuTheme.play();
		
		this.createStaticMenuScene();
		
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.mMainScene = new Scene(1);
		this.mMainScene.setBackground(new ColorBackground(0.8f, 0.8f, 0.9f)); // set the scene background to a light light grey
		
		
		/* Create the gravity layer entity */
		this.gravityLayer = new Entity();
		
		/* Add gravity layer to the scene */
		this.mMainScene.attachChild(this.gravityLayer);

		
		/* Add menu to the scene */
		this.mMainScene.setChildScene(mStaticMenuScene);
		
		// Set up the scene game loop
		this.mMainScene.registerUpdateHandler(new IUpdateHandler() {                    
            public void reset() {        
            }             
            public void onUpdate(float pSecondsElapsed) {         
            	
            	//======================================================================
            	// Run the Grabidelov Engine
            	//======================================================================
	            gEngine.run(); // run the grabidelov engine

            }
        });
        
		return this.mMainScene;
		
	}

	@Override
	public void onLoadComplete() {	
		
		/* Start the gravity layer handler*/
		mGameControlHandler.postDelayed(mShootLaunch, SHOOT_DELAY);
		
	}

	/* This launches the game control screen when it is exectued */
	private Runnable mShootLaunch = new Runnable() {
		
		public void run() {
			
			   runOnUpdateThread(new Runnable() {
	
				   @Override
				   public void run() {
					
					   if (gEngine.getListSize() < MyConstants.MAX_GAME_CONTROL_SCREEN_SHOTS) {
						   // Randomly create grabidelov entity and shoot it anywhere using grabidelov entity.
						   GrabidelovEntity entity = createRandomGrabidelovEntity();
						   // Add entity to gravity layer
						   GameControlScreen.this.gravityLayer.attachChild(entity);
							   	        	
						   gEngine.shootEntityFromAnyBoundary(entity, 
							     MyConstants.DEFAULT_MIN_SHOOT_SPEED, MyConstants.DEFAULT_MAX_SHOOT_SPEED);
					   }
					   
				   }
				   
			   });

        	mGameControlHandler.postDelayed(mShootLaunch, SHOOT_DELAY);
		}
		
	};
	
	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		if (pKeyCode == KeyEvent.KEYCODE_MENU
				&& pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			if (popupDisplayed) {
				/* Remove the menu and reset it. */
				mMainScene.setChildScene(mStaticMenuScene);
				popupDisplayed = false;
			} else {
				/* Attach the menu. */
				popupDisplayed = true;
			}
			return true;
		} else {
			return super.onKeyDown(pKeyCode, pEvent);
		}
	}
	

	@Override
	public boolean onMenuItemClicked(final MenuScene pMenuScene,
			final IMenuItem pMenuItem, final float pMenuItemLocalX,
			final float pMenuItemLocalY) {
		System.err.println("Menu Item Pressed");
		switch (pMenuItem.getID()) {
		case MENU_PLAY:

			/* Start the main game screen */
			Intent mainGameScreenIntent = new Intent(GameControlScreen.this,
					MainGameScreen.class);
			GameControlScreen.this.startActivity(mainGameScreenIntent);
			
			Resources.menuTheme.stop();
			
			return true;
		case MENU_LEAVE:
			/* End Activity. */
			this.finish();
			return true;
		case MENU_INSTRUCTIONS:
			
			Intent insturctionScreenIntent = new Intent(GameControlScreen.this,
					InstructionsScreen.class);
			GameControlScreen.this.startActivity(insturctionScreenIntent);
			
			/* Reset the Game Control Screen */
			runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					gEngine.getGrabidelovEntities().clear();
					while (gravityLayer.getChildCount() > 0) {
						gravityLayer.detachChild(gravityLayer.getChild(gravityLayer.getChildCount() - 1));
					}
				  }
			});
			   
			return true;
		default:
			return false;
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected void createStaticMenuScene() {
		
		this.mStaticMenuScene = new MenuScene(this.mGameControlCamera);
		
		final SpriteMenuItem playMenuItem = new SpriteMenuItem(MENU_PLAY, mPlayTextureRegion);
		this.mStaticMenuScene.addMenuItem(playMenuItem);

		final SpriteMenuItem instructionsMenuItem = new SpriteMenuItem(MENU_INSTRUCTIONS, mInstructionsTextureRegion);
		this.mStaticMenuScene.addMenuItem(instructionsMenuItem);
		
		final SpriteMenuItem leaveMenuItem = new SpriteMenuItem(MENU_LEAVE, mLeaveTextureRegion);
		this.mStaticMenuScene.addMenuItem(leaveMenuItem);
		
		this.mStaticMenuScene.buildAnimations();
		this.mStaticMenuScene.setBackgroundEnabled(false);
		this.mStaticMenuScene.setOnMenuItemClickListener(this);
		
	}

	protected GrabidelovEntity createRandomGrabidelovEntity() {
		
		Random randomGenerator = new Random();
		int randomEntityType = randomGenerator.nextInt(MyConstants.NUMBER_OF_ENTITY_TYPES);
		
		GrabidelovEntity entity = null;
		
		switch (randomEntityType) {
		case MyConstants.TYPE_SUN:
			entity = new GrabidelovSun();
			break;
		case MyConstants.TYPE_GREEN:
			entity = new GrabidelovGreen();
			break;
		case MyConstants.TYPE_RED:
			entity = new GrabidelovRed();
			break;
		case MyConstants.TYPE_BLUE:
			entity = new GrabidelovBlue();
			break;
		default:
			entity = new GrabidelovSun();
			break;
		}
		
		return entity;
		
		
	}
	
}
