package net.mgsx.ld44.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import net.mgsx.ld44.assets.GameAssets;
import net.mgsx.ld44.scenes.MenuScene;
import net.mgsx.ld44.utils.PixelPerfectViewport;
import net.mgsx.ld44.utils.Scene;
import net.mgsx.ld44.utils.SceneOverEvent;
import net.mgsx.ld44.utils.UIScreen;

public class GameScreen extends UIScreen
{
	public static final float WORLD_WIDTH = 320;
	public static final float WORLD_HEIGHT = 240;

	private Scene currentScene;
	
	public GameScreen() {
		super(new PixelPerfectViewport(WORLD_WIDTH, WORLD_HEIGHT), GameAssets.i.skin);
		
		directTransition(new MenuScene());
		
		stage.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(event instanceof SceneOverEvent){
					directTransition(((SceneOverEvent) event).nextScene);
				}
			}
		});
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
