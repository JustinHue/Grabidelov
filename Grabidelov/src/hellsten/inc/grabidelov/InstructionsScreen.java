package hellsten.inc.grabidelov;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;

public class InstructionsScreen extends BaseGameActivity {

	// ===========================================================
	// Constants
	// ===========================================================

	/* Defines instructions screen width and height */
	private static final int CAMERA_WIDTH = 1024;
	private static final int CAMERA_HEIGHT = 614;
	
	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mInstructionsCamera;
	private Texture mInstructionTexture;
	private TextureRegion mInstructionTextureRegion;

	private Texture mBackTexture;
	private TextureRegion mBackTextureRegion;
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public Engine onLoadEngine() {

		this.mInstructionsCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
				this.mInstructionsCamera));
		
	}

	@Override
	public void onLoadResources() {
		
		/* Set the texture set up in the gfx directory */
		TextureRegionFactory.setAssetBasePath("gfx/");
		
		/* Set up the instructions texture */
		this.mInstructionTexture = new Texture(1024, 1024,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mInstructionTextureRegion = TextureRegionFactory.createFromAsset(
				this.mInstructionTexture, this, "game/instructions.jpg", 0, 0);

		/* Set up the back button texture */
		this.mBackTexture = new Texture(1024, 1024,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBackTextureRegion = TextureRegionFactory.createFromAsset(
				this.mBackTexture, this, "game/go_back.png", 0, 0);
		
		/* Load the textures to the texture manager */
		this.mEngine.getTextureManager().loadTexture(this.mInstructionTexture);
		this.mEngine.getTextureManager().loadTexture(this.mBackTexture);
		
	}

	@Override
	public Scene onLoadScene() {
		
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene(1);

		final Sprite instructions = new Sprite(0, 0, this.mInstructionTextureRegion);
    
		final Sprite backButton = new Sprite(800, 25, this.mBackTextureRegion)  {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				
				/* Go back to main menu */
				finish();
				
				return true;
			}
		}; 
		
		scene.attachChild(instructions);
		scene.attachChild(backButton);
		
		scene.registerTouchArea(backButton);
		scene.setTouchAreaBindingEnabled(true);
		
		return scene;
		
	}

	@Override
	public void onLoadComplete() {

	}
	
	
}
