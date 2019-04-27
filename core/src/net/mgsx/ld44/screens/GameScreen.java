package net.mgsx.ld44.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;

import net.mgsx.ld44.assets.GameAssets;
import net.mgsx.ld44.gfx.BlurNode;
import net.mgsx.ld44.gfx.PostProcessingStack;
import net.mgsx.ld44.gfx.ThresholdNode;
import net.mgsx.ld44.scenes.CurvesScene;
import net.mgsx.ld44.utils.PixelPerfectViewport;
import net.mgsx.ld44.utils.Scene;
import net.mgsx.ld44.utils.UIScreen;

public class GameScreen extends UIScreen
{
	public static final float WORLD_WIDTH = 600 * 2;
	public static final float WORLD_HEIGHT = 300 * 2;

	private Scene currentScene;
	
	PostProcessingStack postProcessing;
	
	public GameScreen() {
		super(new PixelPerfectViewport(WORLD_WIDTH, WORLD_HEIGHT), GameAssets.i.skin);
		
		postProcessing = new PostProcessingStack();
		postProcessing.pipeline.add(new ThresholdNode().set(10f));
		postProcessing.pipeline.add(new BlurNode());
		
//		Image img;
//		stage.addActor(img = new Image(GameAssets.i.skin, "white"));
//		img.setScaling(Scaling.stretch);
//		img.setFillParent(true);
//		img.setColor(.2f, .15f, .15f, 1);
//		
//		directTransition(new MenuScene());
//		
//		stage.addListener(new ChangeListener() {
//			@Override
//			public void changed(ChangeEvent event, Actor actor) {
//				if(event instanceof SceneOverEvent){
//					directTransition(((SceneOverEvent) event).nextScene);
//				}
//			}
//		});
		
		directTransition(new CurvesScene());
	}
	
	@Override
	public void render(float delta) {
		postProcessing.enabled = false;
		postProcessing.begin();
		stage.getViewport().apply();
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

}
