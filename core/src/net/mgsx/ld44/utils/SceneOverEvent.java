package net.mgsx.ld44.utils;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

public class SceneOverEvent extends ChangeEvent{
	public Actor nextScene;

	public SceneOverEvent(Actor nextScene) {
		super();
		this.nextScene = nextScene;
	}
	
}
