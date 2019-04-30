package net.mgsx.ld44.midi;

import com.badlogic.gdx.utils.Array;

public class MidiTrack {
	public Array<MidiEvent> events = new Array<MidiEvent>();
	public transient int lastIndex;
}
