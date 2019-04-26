package net.mgsx.ecs;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.mgsx.ecs.systems.SpriteRenderSystem;

// TODO might not be usefull : screens are better or scene
public class ECSGame extends Game
{
	private KitEngine engine;
	private Batch batch;
	private Camera camera;
	
	@Override
	public void create() {
		engine = new KitEngine(new AssetManager());
		
		batch = new SpriteBatch();
		
		engine.setSharedData(Batch.class, batch);
		engine.setSharedData(SpriteBatch.class, batch);
		
		camera = new OrthographicCamera();
		engine.setSharedData(Camera.class, camera);
		engine.setSharedData(OrthographicCamera.class, camera);
		
		engine.addSystem(new SpriteRenderSystem(Priority.RENDER));
		
		setScreen(new ECSScreen(engine));
	}

}
