require 'midilib/io/seqwriter'
require 'midilib/sequence'
require 'midilib/consts'

module MidiTools

	def self.export_midi(t)
		seq = MIDI::Sequence.new()	
		# Create a track to hold the notes. Add it to the sequence.
		track = MIDI::Track.new(seq)
		seq.tracks << track
		# Give the track a name and an instrument name (optional).
		track.name = 'New Track'
		track.instrument = MIDI::GM_PATCH_NAMES[0]
		quarter_note_length = seq.note_to_delta('quarter')
		midiPositionToMidiTrack(seq, track, trackToMidiPos(t), 0.25)
	end

	def self.trackToMidiPos(t)
		midiPositions = []
		t.markers.map { |marker|  
			midiPositions << marker.abs_beat
		}
		midiPositions
	end

	##
    # noteDuration is expressed as fraction of beat (quarter note = black)
    # see https://github.com/jimm/midilib#calculating-delta-times
	def self.midiPositionToMidiTrack(sequence, track, midiPositions, noteDuration)
		midiCursor = 0
		midiPositions.each do |midiPosition|
			positionAbsoluteInTicks = (midiPosition * sequence.note_to_delta('quarter')).to_i	
			delta = positionAbsoluteInTicks - midiCursor
			raise StudioException, "delta time beetween two markers is negative " unless delta >= 0
			track.events << MIDI::NoteOn.new(0, 52, 127, delta )
			midiCursor = positionAbsoluteInTicks
			durationInTicks = (noteDuration * sequence.note_to_delta('quarter')).to_i
  			track.events << MIDI::NoteOff.new(0, 52, 127, durationInTicks )
  			midiCursor += durationInTicks
		end
		track.recalc_times
		sequence
	end

	def self.write_midi(seq, name, dest)
		File.open("#{dest}/#{name}.mid", 'wb') { |file| seq.write(file) }
	end
end

# MidiTools.export_midi

midiFile, jsonFile = ARGV

jsonFile = "#{File.dirname(midiFile)}/#{File.basename(midiFile,'.*')}.json"



puts midiFile

require 'midilib/io/seqreader'
require 'json'

# Create a new, empty sequence.
seq = MIDI::Sequence.new()



# Read the contents of a MIDI file into the sequence.
File.open(midiFile, 'rb'){ | file |
    seq.read(file) { | track, num_tracks, i |
        # Print something when each track is read.
        puts "read track #{i} of #{num_tracks}"
        
    }
    data = {
    	scale: seq.length_to_delta(1), # one quater note in seconds
		tracks: []
	}
    seq.tracks.each do |track|
    	data[:tracks] << td = {
    		events: []
    	}
    	track.events.each do |event|
    		ed = {
    			time: event.time_from_start, 
				status: event.status, 
				type: event.class
			}
    		case event when MIDI::ChannelEvent
    			ed[:channel] = event.channel
    		end
     		case event when MIDI::NoteEvent
    			ed[:note] = event.note
    			ed[:velocity] = event.velocity
    		end
    		td[:events] << ed
    	end
    end
    File.write(jsonFile, JSON.pretty_generate(data))
}

