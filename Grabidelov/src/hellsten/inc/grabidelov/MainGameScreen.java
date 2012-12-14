package hellsten.inc.grabidelov;

import java.io.IOException;

import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.util.FPSLogger;
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

	private Camera mMainGameScreenCamera;
	private Engine mEngine;
	
	@Override
	public Engine onLoadEngine() {
		
		this.mMainGameScreenCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		
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
			Resources.projectileCollisionSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "projectile_collision.wav");
			Resources.sunCollisionSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "sun_collision.wav");
		} catch (final IOException e) {
			Debug.e(e);
		}
		
		MusicFactory.setAssetBasePath("mfx/");
		try {
			Resources.mainTheme = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), MainGameScreen.this, "Pulse.mp3");
			Resources.mainTheme.setLooping(true);
		} catch (final IOException e) {
			Debug.e(e);
		}
		
	}

	@Override
	public Scene onLoadScene() {
		
		this.mEngine.registerUpdateHandler(new FPSLogger());
		
		Resources.mainTheme.play();
		
		final Scene mMainGameScene = new Scene(1);
		mMainGameScene.setBackground(new ColorBackground(0.8f, 0.8f, 0.9f)); // set the scene background to a light light grey
		
		
		// Set up the main game scene loop
		mMainGameScene.registerUpdateHandler(new IUpdateHandler() {                    
            public void reset() {        
            }             
            public void onUpdate(float pSecondsElapsed) {         
            }
        });
		
		return mMainGameScene;
	}

	@Override
	public void onLoadComplete() {

		
	}

}
