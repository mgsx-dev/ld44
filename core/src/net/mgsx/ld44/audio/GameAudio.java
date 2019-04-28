package net.mgsx.ld44.audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;

import net.mgsx.ld44.assets.GameAssets;

/**
 * Game specific audio logic  
 */
public class GameAudio {
	public static GameAudio i;
	
	public final GameMusic musics = new GameMusic();
	public final GameSound sounds = new GameSound();

	private Music currentMusic;
	private float lastPosition;
	private float currentPosition;
	private float bpm;

	public GameAudio() {
	}
	
	public void update(float deltaTime) {
		musics.update(deltaTime);
		sounds.update(deltaTime);
		
		if(currentMusic != null){
			lastPosition = currentPosition;
			currentPosition = currentMusic.getPosition() - 0.45f; // TODO -45 phase game dependents (jump, etc)
		}
	}
	
	public boolean isJustBar(float bar) {
		int quantizedPos = MathUtils.floor(currentPosition * (bpm / 60) / bar);
		int lastQuantizedPos = MathUtils.floor(lastPosition * (bpm / 60) / bar);
		return quantizedPos > lastQuantizedPos;
	}
	
	public void playMusicIntro(){
		musics.replaceMusics(GameAssets.i.introMusic, true);
	}
	
	public void playJump(){
		sounds.play(GameAssets.i.sfxJump);
	}

	public void playGrabCoin(int type) {
		
		//sounds.play(GameAssets.i.sfxCoin1);
		sounds.play(GameAssets.i.sfxSplashs.random());
	}

	public void playMusicGame(Music music) {
		lastPosition = 0;
		currentPosition = 0;
		currentMusic = music;
		musics.replaceMusics(music, true);
	}
	
	public void playMusicGame1() {
		bpm = 180;
		playMusicGame(GameAssets.i.music1);
	}
	public void playMusicGame2() {
		bpm = 148;
		playMusicGame(GameAssets.i.music2);
	}

	public float getNextBarDelay(float barLength) {
		return barLength / (bpm / 60f);
	}


	
	// Put here all game specific music and sfx events
	
	
}	
