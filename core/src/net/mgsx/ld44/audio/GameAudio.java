package net.mgsx.ld44.audio;

import net.mgsx.ld44.assets.GameAssets;

/**
 * Game specific audio logic  
 */
public class GameAudio {
	public static GameAudio i;
	
	public final GameMusic musics = new GameMusic();
	public final GameSound sounds = new GameSound();

	public GameAudio() {
	}
	
	public void update(float deltaTime) {
		musics.update(deltaTime);
		sounds.update(deltaTime);
	}
	
	public void playMusicIntro(){
		musics.replaceMusics(GameAssets.i.introMusic, true);
	}
	
	public void playJump(){
		sounds.play(GameAssets.i.sfxJump);
	}

	
	// Put here all game specific music and sfx events
	
}	
