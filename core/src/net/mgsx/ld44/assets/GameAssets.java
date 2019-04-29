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

	public Sound sfxJump;

	public Texture hero;
	
	public TextureRegion heroBodyRegion;

	public Sound sfxSplash1;
	public Sound sfxSplash2;
	public Sound sfxSplash3;
	
	public Music musicB;
	public Music musicD;
	public Music musicF;

	public Array<Sound> sfxSplashs = new Array<Sound>();

	public Sound sfxCoinShort;
	public Sound sfxCoinLong;

	public Texture grid1;

	public Music musicIntro;

	public Sound sfxMachine;

	public Sound sfxCoin;
	
	
	public GameAssets() {
		assetManager = new AssetManager();
	}

	public void finish() {
		assetManager.finishLoading();
		
		// load directly direct
		skin = new Skin(Gdx.files.internal("skins/game-skin.json"));
		
		hero = new Texture("hero.png");
		hero.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		
		heroBodyRegion = new TextureRegion(hero, 0, 0, 64, 64);;
		
		sfxSplash1 = Gdx.audio.newSound(Gdx.files.internal("sfx/crash1.wav"));
		sfxSplash2 = Gdx.audio.newSound(Gdx.files.internal("sfx/crash2.wav"));
		sfxSplash3 = Gdx.audio.newSound(Gdx.files.internal("sfx/crash3.wav"));
		sfxMachine = Gdx.audio.newSound(Gdx.files.internal("sfx/machinesous.wav"));
		sfxSplash2 = Gdx.audio.newSound(Gdx.files.internal("sfx/powerup.wav"));
		
		sfxCoin = Gdx.audio.newSound(Gdx.files.internal("sfx/piece.wav"));
		sfxCoinShort = Gdx.audio.newSound(Gdx.files.internal("sfx/piece court.wav"));
		sfxCoinLong = Gdx.audio.newSound(Gdx.files.internal("sfx/piece long.wav"));
		
		sfxSplashs.add(sfxSplash1);
		sfxSplashs.add(sfxSplash2);
		sfxSplashs.add(sfxSplash3);
		
		musicIntro = Gdx.audio.newMusic(Gdx.files.internal("music/LD44 intro.mp3"));
		musicB = Gdx.audio.newMusic(Gdx.files.internal("music/LD44 track B.mp3"));
		musicD = Gdx.audio.newMusic(Gdx.files.internal("music/LD44 track D.mp3"));
		musicF = Gdx.audio.newMusic(Gdx.files.internal("music/LD44 track F.mp3"));
		
		grid1 = new Texture("grid1.png");
		grid1.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	}
	
}
