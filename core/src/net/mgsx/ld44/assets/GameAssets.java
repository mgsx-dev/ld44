package net.mgsx.ld44.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

public class GameAssets {
	public static GameAssets i;
	
	private AssetManager assetManager;

	public Skin skin;

	public Music introMusic;

	public Sound sfxJump;

	public Texture hero;
	
	public TextureRegion heroBodyRegion;

	public Sound sfxSplash1;
	public Sound sfxSplash2;
	public Sound sfxSplash3;
	
	public Music music1;
	public Music music2;

	public Array<Sound> sfxSplashs = new Array<Sound>();

	public Sound sfxCoin1;
	public Sound sfxCoin2;

	public Texture grid1;
	
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
		
		heroBodyRegion = new TextureRegion(hero, 0, 0, 64, 64);;
		
		sfxSplash1 = Gdx.audio.newSound(Gdx.files.internal("sfx/crash1.wav"));
		sfxSplash2 = Gdx.audio.newSound(Gdx.files.internal("sfx/crash2.wav"));
		sfxSplash3 = Gdx.audio.newSound(Gdx.files.internal("sfx/crash3.wav"));
		
		sfxCoin1 = Gdx.audio.newSound(Gdx.files.internal("sfx/piece1 court.wav"));
		sfxCoin2 = Gdx.audio.newSound(Gdx.files.internal("sfx/piece1 long.wav"));
		
		
		sfxSplashs.add(sfxSplash1);
		sfxSplashs.add(sfxSplash2);
		sfxSplashs.add(sfxSplash3);
		
		music1 = Gdx.audio.newMusic(Gdx.files.internal("music/LD44 track B.0.mp3"));
		music2 = Gdx.audio.newMusic(Gdx.files.internal("music/LD44 track D.0.mp3"));
		
		grid1 = new Texture("grid1.png");
		grid1.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	}
	
}
