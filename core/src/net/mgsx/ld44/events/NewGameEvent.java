package net.mgsx.ld44.events;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

public class NewGameEvent extends ChangeEvent {
	public int musicIndex;

	public NewGameEvent(int musicIndex) {
		super();
		this.musicIndex = musicIndex;
	}
	
}
