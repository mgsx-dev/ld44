package net.mgsx.example;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import net.mgsx.example.screens.ExampleGameScreen;

public class GameExample extends Game {
	
	
	@Override
	public void create () {
		// TODO create
		setScreen(new ExampleGameScreen());
	}

	@Override
	public void render () {
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// TODO draw
		super.render();
	}
	
}
