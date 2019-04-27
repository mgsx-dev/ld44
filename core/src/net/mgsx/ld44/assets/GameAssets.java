package net.mgsx.ld44.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameAssets {
	public static GameAssets i;
	
	private AssetManager assetManager;

	public Skin skin;

	public Music introMusic;

	public Sound sfxJump;

	public Texture hero;
	
	// TODO declare your assets
	
	
	public GameAssets() {
		assetManager = new AssetManager();
		
		// load your assets with asset manager (optional)
	}

	public void finish() {
		assetManager.finishLoading();
		
		// fetch assets (optional)
		
		// load directly (optional)
		skin = new Skin(Gdx.files.internal("skins/game-skin.json"));
		
		hero = new Texture("hero.png");
	}
	
}
