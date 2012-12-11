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

	private static final int CAMERA_WIDTH = 1024;
	private static final int CAMERA_HEIGHT = 614;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private Texture mTexture;
	private TextureRegion mSplashTextureRegion;
	private Handler mHandler;

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	public Engine onLoadEngine() {
		mHandler = new Handler();
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
				this.mCamera));
	}

	public void onLoadResources() {
		this.mTexture = new Texture(1024, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mSplashTextureRegion = TextureRegionFactory.createFromAsset(
				this.mTexture, this, "gfx/hellsten_inc.png", 0, 0);

		this.mEngine.getTextureManager().loadTexture(this.mTexture);
	}

	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene(1);

		/* Center the splash on the camera. */
		final int centerX = (CAMERA_WIDTH - this.mSplashTextureRegion
				.getWidth()) / 2;
		final int centerY = (CAMERA_HEIGHT - this.mSplashTextureRegion
				.getHeight()) / 2;

		/* Create the sprite and add it to the scene. */
		final Sprite splash = new Sprite(centerX, centerY,
				this.mSplashTextureRegion);
		scene.getLastChild().attachChild(splash);

		return scene;
	}

	public void onLoadComplete() {
		mHandler.postDelayed(mLaunchTask, 3000);
	}

	private Runnable mLaunchTask = new Runnable() {
		public void run() {
			Intent myIntent = new Intent(SplashScreen.this,
					GameControlScreen.class);
			SplashScreen.this.startActivity(myIntent);
		}
	};

}
