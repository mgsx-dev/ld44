package net.mgsx.ld44.audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

/**
 * Music mixer utility 
 */
public class GameMusic {
	private static class CrossFade{
		public float duration;
		public float time;
	}
	
	public float musicsVolume = 1f;
	
	private Array<Music> musics = new Array<Music>();
	private final CrossFade crossfade = new CrossFade();
	
	public void initialize() {
	}
	
	
	public void replaceMusics(Music music, boolean looping){
		while(musics.size > 0) musics.pop().stop();
		music.setLooping(looping);
		music.play();
		musics.add(music);
		
	}
	
	public void fadeToMusic(Music music, float duration, boolean looping){
		while(musics.size > 1) musics.removeIndex(0).stop();
		crossfade.duration = duration;
		crossfade.time = 0;
		music.setLooping(looping);
		music.play();
		musics.add(music);
	}
	
	public void play(Sound sound, float volume){
		sound.play(volume);
	}
	
	public void update(float deltaTime) {
		if(musics.size > 1){
			crossfade.time += deltaTime;
			if(crossfade.time > crossfade.duration){
				if(musics.size > 1)	musics.removeIndex(0).stop();
				musics.first().setVolume(musicsVolume);
			}else{
				float t = crossfade.time / crossfade.duration;
				musics.first().setVolume(musicsVolume * t);
				if(musics.size > 1) musics.peek().setVolume(musicsVolume * (1 - t));
			}
		}
	}
}
