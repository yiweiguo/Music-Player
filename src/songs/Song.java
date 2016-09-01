package songs;

//TODO: write this class

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Song {
	private String title;
	private String artist;
	private double totalDuration;
	private Note[] notes;
	private int noteLength;
	public HashMap unitTestForPlay;





	/**
	 * This constructor reads the file and constructs a song based on the content
	 * @param filename it is the filename String
	 * @throws FileNotFoundException  when such file does not exist, throw an error
	 */
	public Song(String filename) throws FileNotFoundException{
		File file = new File(filename);
		Scanner s1 = new Scanner(file);
		title = s1.nextLine();
		artist = s1.nextLine();
		noteLength = s1.nextInt();
		notes = new Note[noteLength];
		s1.nextLine();

		int counter = 0;
		while (s1.hasNextLine()){		

			double duration;
			String   pitch;
			int octave;
			String accidental;
			String repeat;

			duration = s1.nextDouble();
			pitch = s1.next();

			if(!pitch.equals("R")){
				octave =s1.nextInt();
				accidental  = s1.next();
				repeat =s1.next();	

				boolean isRepeat;
				if(repeat.equals("true")){
					isRepeat = true;
				}
				else{
					isRepeat = false;
				}

				Pitch pitch1 = Pitch.valueOf(pitch);
				Accidental accidental1 = Accidental.valueOf(accidental);

				Note newNote = new Note(duration, pitch1, octave,accidental1,isRepeat);
				notes[counter] = newNote;


				counter++;
			}
			else{
				repeat =s1.next();	

				boolean isRepeat;
				if(repeat.equals("true")){
					isRepeat = true;
				}
				else{
					isRepeat = false;
				}


				Note newNote = new Note(duration,isRepeat);
				notes[counter] = newNote;

				counter++;
			}

		}
		s1.close();
		totalDuration = getTotalDuration();

	}


	/**
	 * This is the setter for the note array
	 * @param notes  array containing the notes for the song
	 */
	public void setNotes(Note[] notes) {
		this.notes = notes;
	}

	/**
	 * This is the getter for the note array
	 * @return  returns an array of notes in the song
	 */
	public Note[] getNotes() {
		return notes;
	}


	/**
	 * This is the getter for song title
	 * @return the title of the song
	 */
	public String getTitle(){
		return title;
	}

	/** 
	 * This is the getter for the artist
	 * @return the name of the artist
	 */
	public String getArtist(){
		return artist;
	}

	/**
	 * This method calculates the total duration of the song
	 * If there is repeat section, the duration for that section will be doubled
	 * @return
	 */
	public double getTotalDuration(){
		double duration = 0;
		int counter = 0;
		for (Note note: notes){
			if (note.isRepeat()==true){
				counter=counter+1;
			}
			if (counter>0){
				duration += note.getDuration()*2;
			}
			else{
				duration += note.getDuration();
			}
			if (counter ==2){
				counter = 0;
			}
		}
		return duration;
	}

	/**
	 * This method essentially calls the play method for each note in the note array
	 * If there is a repeat section, those notes in the repeat section will be played twice
	 */
	public void play(){
		HashMap repeat = new HashMap<Integer, Integer>();
		int counter = 0;
		int start=0;
		int stop=0;
		for (int i =0; i < notes.length;i++){
			if (notes[i].isRepeat()==true && counter ==0){
				start = i;
				counter ++;
			}
			else if	(notes[i].isRepeat()==true&&counter==1){
				stop = i;
				counter ++;
			}
			else{
				continue;
			}
			if (counter >1){
				repeat.put(stop, start);
				counter = 0;
			}

		}

		unitTestForPlay = (HashMap<Integer, Integer>)repeat.clone();
		

		for (int i=0; i < notes.length ; i++){
			if (!repeat.containsKey(i)){
				notes[i].play();
			}
			else {
				notes[i].play();
				for (int j=(int) repeat.get(i); j<i+1;j++){
					notes[j].play();
				}
				repeat.remove(i);
			}
		}
	}

	/**
	 * This method decrease the octave of the song and returns true if the action is completed for all notes
	 * @return 
	 */
	public boolean octaveDown(){
		boolean isDown = true;
		for (Note note: notes){
			if(!note.getPitch().equals(Pitch.R)){
				if (note.getOctave()>1){
					note.setOctave(note.getOctave()-1);
				}
				else{
					isDown = false;
				}
			}
			else{
				continue;
			}
		}

		return isDown;
	}

	/**
	 * This method increase the octave of the song and returns true if the action is completed for all notes
	 * @return
	 */
	public boolean octaveUp(){
		boolean isUp = true;
		for (Note note: notes){
			if(!note.getPitch().equals(Pitch.R)){
				if (note.getOctave()<10){
					note.setOctave(note.getOctave()+1);
				}
				else{
					isUp = false;
				}
			}
			else{
				continue;
			}
		}
		return isUp;
	}

	/**
	 * This method changes the tempo of the song
	 * @param ratio the ratio of the tempo where 2 means the tempo doubles
	 */
	public void changeTempo(double ratio){
		for (Note note:notes){
			note.setDuration(note.getDuration()*ratio);
		}
		totalDuration = getTotalDuration();
	}

	/**
	 * This method reverses all the notes in the song
	 */
	public void reverse(){
		Note[] reverse = new Note[noteLength];
		for (int i = 0; i <noteLength; i++){
			reverse[i] = notes[noteLength-i-1];
		}
		notes = reverse;
	}

	/**
	 * This overrides the toString method
	 * What it does essentially is to output the notes of the song for testing
	 */
	public String toString(){
		String s ="";
		s += "The title of the song is \""+ title +"\".\n";
		s += "The artist of the song is "+ artist +".\n";
		s += "The duration of the song is "+ getTotalDuration() +"\n";
		for (Note note:notes){
			if (!note.getPitch().equals(Pitch.R)){
				s += note.getDuration() + " " + note.getPitch()+" " +
						note.getOctave()+" "+ note.getAccidental() +" "+note.isRepeat()+"\n";
			}
			else{
				s += note.getDuration() + " "+ note.getPitch()+" "+note.isRepeat() +"\n";
			}
		}
		return s;
	}

}
