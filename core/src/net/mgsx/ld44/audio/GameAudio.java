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
	private float rawPosition;
	private float lastRawPosition;
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
			lastRawPosition = rawPosition;
			rawPosition = currentMusic.getPosition(); 
		}
		lastPosition = currentPosition;
		currentPosition += deltaTime;
		if(Math.abs(currentPosition - rawPosition) > .2f){
			currentPosition = rawPosition;
			lastPosition = lastRawPosition;
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
			float rate = 1f / midiSequence.scale;
			for(MidiTrack track : midiSequence.tracks){
				if(currentPosition < lastPosition){
					track.lastIndex = 0;
				}
				for(int i=track.lastIndex ; i<track.events.size ; i++){
					MidiEvent e = track.events.get(i);
					track.lastIndex = i;
					if(e.time*rate < currentPosition * (bpm/60f)){
						if(e.status == 144){ // note on
							lastEvents.add(e);
						}
					}else{
						break;
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
	
	
	private void playMusicGame(Music music) {
		lastPosition = 0;
		currentPosition = 0;
		currentMusic = music;
		musics.replaceMusics(music, true);
	}
	
	public void playMusicGame(int index) {
		if(index == 0){
			playMusicGame1();
		}else if(index == 1){
			playMusicGame2();
		}else{
			playMusicGame3();
		}
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
		scheduledSfx.add(new SfxPlay(GameAssets.i.sfxCoins.random(), .25f));
		// sounds.play(GameAssets.i.sfxCoin);
	}
	
	public void playMachine(){
		sounds.play(GameAssets.i.sfxMachine);
	}

	public void playPigCollision() {
		sounds.play(GameAssets.i.sfxMalus);
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

	public void playFusion() {
		sounds.play(GameAssets.i.sfxBonus);
	}

	public void playHeroFusion() {
		sounds.play(GameAssets.i.sfxBonus);
	}
	


	
	
	
}	
