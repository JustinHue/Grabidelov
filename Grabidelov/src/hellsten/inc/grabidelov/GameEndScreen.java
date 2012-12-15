
/*
 * File Name: GameEndScreen.java
 * Author's Name: Justin Hellsten
 * Date: Decemeber 14, 2012
 * Purpose: This activity is to display the current and high scores, if the player lost or wins 
 * 
 */
package hellsten.inc.grabidelov;

/* Import files */
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.graphics.Color;

/* This class defines the end game screen which is used to display the current and high scores of the player, and
 * to tell them whether they lost or won.
 */
public class GameEndScreen extends BaseGameActivity {

	// ===========================================================
	// Constants
	// ===========================================================

	/* Defines splash screen width and height */
	private static final int CAMERA_WIDTH = 1024;
	private static final int CAMERA_HEIGHT = 614;

	
	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mGameEndScreenCamera;
	private Texture mGameEndScreenTexture;
	private TextureRegion mGameEndScreenTextureRegion;

	private Font font;
	private Texture fontTexture;
	
	private Texture mBackTexture;
	private TextureRegion mBackTextureRegion;
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public Engine onLoadEngine() {

		this.mGameEndScreenCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
				this.mGameEndScreenCamera));
		
	}

	@Override
	public void onLoadResources() {

		//===========================================================
		// Load Resources for the Splash Screen
		//===========================================================
		/* Load font texture */
		this.fontTexture = new Texture(512, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		FontFactory.setAssetBasePath("gfx/font/");
		this.font = FontFactory.createFromAsset(this.fontTexture, this,
				"flubber.ttf", 48, true, Color.RED);
		this.mEngine.getTextureManager().loadTexture(this.fontTexture);
		this.mEngine.getFontManager().loadFont(this.font);
		
		/* Set the texture set up in the gfx directory */
		TextureRegionFactory.setAssetBasePath("gfx/");
		
		/* Set up the background texture */
		this.mGameEndScreenTexture = new Texture(1024, 1024,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mGameEndScreenTextureRegion = TextureRegionFactory.createFromAsset(
				this.mGameEndScreenTexture, this, "game/background.jpg", 0, 0);

		/* Set up the back button texture */
		this.mBackTexture = new Texture(1024, 1024,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBackTextureRegion = TextureRegionFactory.createFromAsset(
				this.mBackTexture, this, "game/go_back.png", 0, 0);
		
		/* Load the textures to the texture manager */
		this.mEngine.getTextureManager().loadTexture(this.mGameEndScreenTexture);
		this.mEngine.getTextureManager().loadTexture(this.mBackTexture);
		
	}
	
	@Override
	public Scene onLoadScene() {

		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene(1);

		final Sprite goBackSprite = new Sprite(CAMERA_WIDTH / 2 - 200, 350, this.mBackTextureRegion) {
				@Override
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
					
					/* Go back to main menu */
					finish();
					
					return true;
				}
        }; 
        
		Text displayText;
		
		if (MyConstants.won) {
			displayText = new Text(CAMERA_WIDTH / 2 - 100, 100, font, "Congradulations you Won!");
		} else {
			displayText = new Text(CAMERA_WIDTH / 2 - 100, 100, font, "Opps, you lost!"); 
		}
		
		if (MyConstants.score > MyConstants.high_score) {
			MyConstants.high_score = MyConstants.score;
		}
		
		/* Create text for score and attach to scene */
		Text finalScoreText = new Text(CAMERA_WIDTH / 2 - 100, 250, font, "Final Score: " + MyConstants.score);
		Text highScoreText = new Text(CAMERA_WIDTH / 2 - 100, 300, font, "High Score: " + MyConstants.high_score);
		
		/* Add background to scene */
		final Sprite background = new Sprite(0, 0,
				this.mGameEndScreenTextureRegion);
		
		/* Add all the entities to the scene */
		scene.attachChild(background);
		scene.attachChild(finalScoreText);
		scene.attachChild(highScoreText);
		scene.attachChild(displayText);
		scene.attachChild(goBackSprite);
		
		scene.registerTouchArea(goBackSprite);
		scene.setTouchAreaBindingEnabled(true);
		
		MyConstants.score = 0;
		
		return scene;
		
	}

	@Override
	public void onLoadComplete() {
	}

}
