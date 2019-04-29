package net.mgsx.ld44.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import net.mgsx.ld44.assets.GameAssets;
import net.mgsx.ld44.midi.MidiEvent;
import net.mgsx.ld44.midi.MidiSequence;
import net.mgsx.ld44.midi.MidiTrack;

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
	
	public final Array<MidiEvent> lastEvents = new Array<MidiEvent>();
	
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

	private MidiSequence midiSequence;

	public GameAudio() {
	}
	
	public void update(float deltaTime) {
		
		// XXX
		sounds.soundMasterVolume = 1f;
		
		
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
		//System.out.println((currentPosition * (bpm/60)));
		
		// update midiSequence events
		lastEvents.clear();
		if(midiSequence != null){
			for(MidiTrack track : midiSequence.tracks){
				for(MidiEvent e : track.events){
					if(e.status == 144){ // note on
						float rate = 1f / (midiSequence.scale * 1);
						// beats * 4
						if(e.time*rate < currentPosition * (bpm/60f) && e.time * rate >= lastPosition * (bpm/60f)){
//							System.out.println(e.time);
//							System.out.println(currentPosition);
							lastEvents.add(e);
							// GameAudio.i.playGrabCoin(0);
						}
					}
				}
			}
		}
	}
	
	public boolean isJustBar(float bar, float offset) {
		int quantizedPos = MathUtils.floor((currentPosition + offset) * (bpm / 60) / bar);
		int lastQuantizedPos = MathUtils.floor((lastPosition + offset) * (bpm / 60) / bar);
		return quantizedPos > lastQuantizedPos;
	}
	
	
	public void playMusicGame(Music music) {
		lastPosition = 0;
		currentPosition = 0;
		currentMusic = music;
		musics.replaceMusics(music, true);
	}
	
	public void playMusicGame1() {
		bpm = 180;
		playMusicGame(GameAssets.i.musicB);
		midiSequence = new Json().fromJson(MidiSequence.class, Gdx.files.internal("music/midi lead track B.json"));
	}
	public void playMusicGame2() {
		bpm = 148;
		playMusicGame(GameAssets.i.musicD);
		midiSequence = new Json().fromJson(MidiSequence.class, Gdx.files.internal("music/midi lead track D.json"));
	}
	public void playMusicGame3() {
		bpm = 148;
		playMusicGame(GameAssets.i.musicF);
		midiSequence = new Json().fromJson(MidiSequence.class, Gdx.files.internal("music/midi lead track F.json"));
	}
	public void playMusicIntro(){
		bpm = 148; // TODO ?
		playMusicGame(GameAssets.i.musicIntro);
		midiSequence = null; // TODO new Json().fromJson(MidiSequence.class, Gdx.files.internal("music/midi lead track F.json"));
	}

	public float getBarDuration(float barLength) {
		return barLength / (bpm / 60f);
	}

	public float getNextBarTimeRemain(float barPrecision) {
		int quantizedPos = MathUtils.floor(currentPosition * (bpm / 60) / barPrecision);
		float nextTime = (quantizedPos + 1) * barPrecision / (bpm / 60);
		return nextTime - currentPosition;
	}

	public float timeToRad() {
		return (currentPosition * (bpm/60f)) * MathUtils.PI;
	}

	public float getTimeNomalized(float barPrecision) {
		return (currentPosition * (bpm/60f) / barPrecision) % 1f;
	}

	public float getMusicTime() {
		return currentPosition;
	}

	public void playJump(){
		sounds.play(GameAssets.i.sfxJump);
	}

	public void playGrabCoin(int type) {
		// TODO type
		sounds.play(GameAssets.i.sfxCoinShort);
		// sounds.play(GameAssets.i.sfxSplashs.random());
	}
	public void playGrabCoinSync(int type, float barPrecision) {
		// scheduledSfx.add(new SfxPlay(GameAssets.i.sfxSplashs.random(), barPrecision));
		scheduledSfx.add(new SfxPlay(GameAssets.i.sfxCoin, .25f));
		// sounds.play(GameAssets.i.sfxCoin);
	}
	
	public void playMachine(){
		sounds.play(GameAssets.i.sfxMachine);
	}

	public void playPigCollision() {
		// TODO ?
		sounds.play(GameAssets.i.sfxCoinLong);
	}

	public void playCoinRight() {
		sounds.play(GameAssets.i.sfxCoinShort);
	}

	public void playCoinLeft() {
		sounds.play(GameAssets.i.sfxCoinShort);
	}

	public void softMuteMusic(boolean muted) {
		currentMusic.setVolume((muted ? .5f : 1) * musics.musicsVolume);
	}
	


	
	
	
}	
