package net.mgsx.example.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;

import net.mgsx.ecs.KitEngine;
import net.mgsx.example.components.CPlayer;
import net.mgsx.example.model.GameWorld;
import net.mgsx.example.systems.MobLogicSystem;

public class ExampleGameScreen extends ScreenAdapter
{
	private KitEngine engine;
	private AssetManager assetManager;
	private GameWorld game;
	private boolean pause;

	public ExampleGameScreen() {
		assetManager = new AssetManager();
		engine = new KitEngine(assetManager);
		
		game = new GameWorld();
		
		engine.addObject(game);
		
		
		engine.addSystem(new MobLogicSystem());
		
		// TODO add entities :
		
		// TODO is shared game better ? can only be one though!! and not changeable ! 
		// shared objects are for resources not state object or any game data.
		// passing through entity is not nice...
		// maybe 2 screens : 2 engines and then 2 different shared object make sense !!
		
		
//		engine.addEntity(game = engine.createEntity()
//		.add(engine.createComponent(CGame.class)));
		
		engine.addEntity(engine.createEntity()
		.add(engine.createComponent(CPlayer.class)));
	}
	
	@Override
	public void render(float delta) {
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
			pause = !pause;
		}
		engine.update(pause ? delta : 0);
	}
}
