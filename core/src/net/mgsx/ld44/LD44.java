package net.mgsx.ld44;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import net.mgsx.ld44.assets.GameAssets;
import net.mgsx.ld44.audio.GameAudio;
import net.mgsx.ld44.screens.GameScreen;

public class LD44 extends Game {
	
	public static final boolean debug = true; 
	public static final int SCREEN_WIDTH = 600;
	public static final int SCREEN_HEIGHT = 300;

	public boolean paused = false; 
	
	@Override
	public void create () {
		
		GameAssets.i = new GameAssets();
		GameAssets.i.finish();
		
		GameAudio.i = new GameAudio();
		
		setScreen(new GameScreen());
	}

	@Override
	public void render () {
		float deltaTime = Gdx.graphics.getDeltaTime();
		if(debug && Gdx.input.isKeyJustPressed(Input.Keys.P)){
			paused = !paused;
			if(paused) deltaTime = 0;
		}
		GameAudio.i.update(deltaTime);
		if (screen != null) screen.render(deltaTime);
	}
	
}
