package net.mgsx.ld44.audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

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
	
	private static final class SfxPlay{
		public Sound sound;
		public float barPrecision;
		public SfxPlay(Sound sound, float barPrecision) {
			super();
			this.sound = sound;
			this.barPrecision = barPrecision;
		}
		
	}
	private Array<SfxPlay> scheduledSfx = new Array<SfxPlay>();

	public GameAudio() {
	}
	
	public void update(float deltaTime) {
		
		// XXX
		sounds.soundMasterVolume = .5f;
		
		musics.update(deltaTime);
		sounds.update(deltaTime);
		
		if(currentMusic != null){
			lastPosition = currentPosition;
			currentPosition = currentMusic.getPosition(); 
		}
		for(int i=0 ; i<scheduledSfx.size ; ){
			SfxPlay sfx = scheduledSfx.get(i);
			if(isJustBar(sfx.barPrecision,  - 0.41f)){ // TODO -45 phase game dependents (jump, etc)
				sounds.play(sfx.sound);
				scheduledSfx.removeIndex(i);
			}else{
				i++;
			}
		}
	}
	
	public boolean isJustBar(float bar, float offset) {
		int quantizedPos = MathUtils.floor((currentPosition + offset) * (bpm / 60) / bar);
		int lastQuantizedPos = MathUtils.floor((lastPosition + offset) * (bpm / 60) / bar);
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
	public void playGrabCoinSync(int type, float barPrecision) {
		scheduledSfx.add(new SfxPlay(GameAssets.i.sfxSplashs.random(), barPrecision));
		
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

	public float getBarDuration(float barLength) {
		return barLength / (bpm / 60f);
	}

	public float getNextBarTimeRemain(float barPrecision) {
		int quantizedPos = MathUtils.floor(currentPosition * (bpm / 60) / barPrecision);
		float nextTime = (quantizedPos + 1) * barPrecision / (bpm / 60);
		return nextTime - currentPosition;
	}
	


	
	
	
}	
