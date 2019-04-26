package net.mgsx.ld44.audio;

import com.badlogic.gdx.audio.Sound;

/**
 * Sounds mixer utility 
 */
public class GameSound {

	public float soundMasterVolume = 1f;

	public void play(Sound sound) {
		sound.play(soundMasterVolume);
	}

	public void update(float deltaTime) {
		// optional
	}

}
