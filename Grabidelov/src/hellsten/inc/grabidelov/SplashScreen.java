package hellsten.inc.grabidelov;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.content.Intent;
import android.os.Handler;

public class SplashScreen extends BaseGameActivity {

	// ===========================================================
	// Constants
	// ===========================================================

	/* Defines splash screen width and height */
	private static final int CAMERA_WIDTH = 1024;
	private static final int CAMERA_HEIGHT = 614;
	
	private static final int SPLASH_DELAY = 3000;
	
	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mSplashCamera;
	private Texture mSplashTexture;
	private TextureRegion mSplashTextureRegion;
	private Handler mSplashHandler;

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public Engine onLoadEngine() {
		
		mSplashHandler = new Handler();
		this.mSplashCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
				this.mSplashCamera));
		
	}

	@Override
	public void onLoadResources() {
		
		//===========================================================
		// Load Game Resources
		//===========================================================

		
		
		//===========================================================
		// Load Resources for the Splash Screen
		//===========================================================
		/* Set the texture set up in the gfx directory */
		TextureRegionFactory.setAssetBasePath("gfx/");
		
		/* Set up the splash texture */
		this.mSplashTexture = new Texture(1024, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mSplashTextureRegion = TextureRegionFactory.createFromAsset(
				this.mSplashTexture, this, "hellsten_inc.png", 0, 0);

		/* Load the textures to the texture manager */
		this.mEngine.getTextureManager().loadTexture(this.mSplashTexture);
		
	}

	@Override
	public Scene onLoadScene() {
		
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene(1);

		/* Center the splash on the camera. */
		final int centerX = (CAMERA_WIDTH - this.mSplashTextureRegion
				.getWidth()) / 2;
		final int centerY = (CAMERA_HEIGHT - this.mSplashTextureRegion
				.getHeight()) / 2;

		/* Create the splash sprite and add it to the scene. */
		final Sprite splash = new Sprite(centerX, centerY,
				this.mSplashTextureRegion);
		
		/* Add all the entities to the scene */
		scene.getLastChild().attachChild(splash);
		
		return scene;
		
	}

	@Override
	public void onLoadComplete() {
		
		/* Start the splash launch after the delay */
		mSplashHandler.postDelayed(mLaunchSplash, SPLASH_DELAY);
		
	}
	
	/* This launches the game control screen when it is exectued */
	private Runnable mLaunchSplash = new Runnable() {
		
		public void run() {
			
			Intent myIntent = new Intent(SplashScreen.this,
					GameControlScreen.class);
			SplashScreen.this.startActivity(myIntent);
			
			SplashScreen.this.finish();
		}
		
	};
	

}
