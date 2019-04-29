package net.mgsx.ld44.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import net.mgsx.ld44.assets.GameAssets;
import net.mgsx.ld44.events.BackMenuEvent;
import net.mgsx.ld44.events.NewGameEvent;
import net.mgsx.ld44.gfx.BlurNode;
import net.mgsx.ld44.gfx.PostProcessingStack;
import net.mgsx.ld44.gfx.ThresholdNode;
import net.mgsx.ld44.scenes.CurvesScene;
import net.mgsx.ld44.scenes.MenuScene;
import net.mgsx.ld44.scenes.TitleScene;
import net.mgsx.ld44.utils.Scene;
import net.mgsx.ld44.utils.SceneOverEvent;
import net.mgsx.ld44.utils.UIScreen;

public class GameScreen extends UIScreen
{
	public static final boolean DEBUG_GAME_SCENE = false;
	
	public static final float WORLD_WIDTH = 600 * 2;
	public static final float WORLD_HEIGHT = 300 * 2;

	private Scene currentScene;
	
	public PostProcessingStack postProcessing;
	
	public GameScreen() {
		// super(new PixelPerfectViewport(WORLD_WIDTH, WORLD_HEIGHT), GameAssets.i.skin);
		super(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT + 100), GameAssets.i.skin);
		
		postProcessing = new PostProcessingStack();
		postProcessing.pipeline.add(new ThresholdNode().set(.5f));
		postProcessing.pipeline.add(new BlurNode());
		postProcessing.enabled = false;
		
		if(DEBUG_GAME_SCENE){
			directTransition(new CurvesScene(this));
		}else{
			directTransition(new TitleScene());
		}
		
		stage.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(event instanceof BackMenuEvent){
					directTransition(new MenuScene());
				}else if(event instanceof NewGameEvent){
					directTransition(new CurvesScene(GameScreen.this));
				}else if(event instanceof SceneOverEvent){
					directTransition((((SceneOverEvent)event).nextScene));
				}
			}
		});
	}
	
	@Override
	public void render(float delta) {
		// postProcessing.enabled = true;
		postProcessing.begin();
//		Gdx.gl.glClearColor(1, 1, 1, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.getViewport().apply();
//		Gdx.gl.glClearColor(1, 1, 1, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render(delta);
		postProcessing.end();
	}

	protected void directTransition(Actor nextActor) {
		if(nextActor instanceof Scene){
			Scene nextScene = (Scene)nextActor;
			if(currentScene != null){
				currentScene.stop();
				currentScene.end();
			}
			currentScene = nextScene;
			nextScene.begin();
			nextScene.start();
		}
		stage.addActor(nextActor);
	}
	
	@Override
	public void resize(int width, int height) {
		postProcessing.resize(width, height);
		super.resize(width, height);
	}

}
