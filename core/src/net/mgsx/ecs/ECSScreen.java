package net.mgsx.ecs;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ScreenAdapter;

public class ECSScreen extends ScreenAdapter {
	private Engine engine;

	public ECSScreen(Engine engine) {
		super();
		this.engine = engine;
	}
	
	@Override
	public void render(float delta) {
		engine.update(delta);
	}
	
}
