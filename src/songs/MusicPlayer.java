package songs;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;

/*
 * Music Player
 *
 * This instructor-provided file implements the graphical user interface (GUI)
 * for the Music Player program and allows you to test the behavior
 * of your Song class.
 */

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;


public class MusicPlayer implements ActionListener, StdAudio.AudioEventListener {

	// instance variables
	private Song song;
	private boolean playing; // whether a song is currently playing
	private boolean isStopped = false;
	public JFrame frame;
	private JFileChooser fileChooser;
	private JTextField tempoText;
	private JSlider currentTimeSlider;
	private JButton load;
	private JButton play;
	private JButton pause;
	private JButton stop;
	private JButton reverse;
	private JButton octaveUp;
	private JButton octaveDown;
	private JButton changeDuration;
	private JPanel panel1;
	private JPanel panel2;
	private JPanel panel3;
	private JPanel panel4;
	private JPanel panel5;
	private JPanel panel6;


	//these are the two labels that indicate time
	// to the right of the slider
	private JLabel currentTimeLabel;
	private JLabel totalTimeLabel;
	private JLabel tempLabel;
	private JLabel currentPlaying;
	private JLabel octave;
	private JLabel currentTempo;
	private JLabel statusLabel; 
	private String octaveLevel = "Regular";



	/*
	 * Creates the music player GUI window and graphical components.
	 */
	/**
	 * This is the constructor of the Music Player
	 */
	public MusicPlayer() {
		song = null;
		createComponents();
		doLayout();
		StdAudio.addAudioEventListener(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	/*
	 * Called when the user interacts with graphical components, such as
	 * clicking on a button.
	 */
	/**
	 * This is the actionPerformed method which gives different instructions based on the "listeners"
	 */
	public void actionPerformed(ActionEvent event) {
		String cmd = event.getActionCommand();
		if (cmd.equals("Play")) {
			if(song!=null){
				statusLabel.setText("Current Song: " + song.getTitle()+" by "+song.getArtist());
				if(StdAudio.isPaused()){
					StdAudio.setPaused(false);
					playing = true;
					isStopped = false;
					doEnabling();

				}else{
					playSong();
					playing=true;
					StdAudio.setPaused(false);
					isStopped = false;
					doEnabling();
				}

			}
			else{
				statusLabel.setText("The song can't be played by this player!");
				playing = false;
				fileChooser.setSelectedFile(null);
			}
		} else if (cmd.equals("Pause")) {
			StdAudio.setPaused(true);
			playing = false;
			doEnabling();

		} else if (cmd == "Stop") {
			StdAudio.setMute(true);
			StdAudio.setPaused(false);
			playing = false;
			isStopped = true;
			doEnabling();
		} else if (cmd == "Load") {
			try {
				loadFile();
			} catch (IOException ioe) {
				System.out.println("not able to load from the file");
			}
		} else if (cmd == "Reverse") {
			//TODO - fill this 
			song.reverse();
		} else if (cmd == "Up") {
			//TODO - fill this

			if(song.octaveUp()){
				if (octaveLevel.equals("Regular")){
					octaveLevel = "1";
				}
				else if (Integer.valueOf(octaveLevel)==-1){
					octaveLevel = "Regular";
				}
				else{
					int a = Integer.valueOf(octaveLevel);
					a ++;
					octaveLevel = String.valueOf(a);
				}
				if(octaveLevel.equals("Regular")){
					octave.setText(octaveLevel+" Octave");
				}
				else if(Integer.valueOf(octaveLevel)>0){
					octave.setText("+"+octaveLevel+" Octave");
				}
				else{
					octave.setText(octaveLevel+" Octave");
				}
			}
			else{
				JOptionPane.showMessageDialog(null,"The highest octave of a note is 10!","Maximum Octave Error",JOptionPane.WARNING_MESSAGE);
			}


		} else if (cmd == "Down") {
			//TODO - fill this
			if(song.octaveDown()){
				if (octaveLevel.equals("Regular")){
					octaveLevel = "-1";
				}
				else if(Integer.valueOf(octaveLevel)==1){
					octaveLevel = "Regular";
				}
				else{
					int a = Integer.valueOf(octaveLevel);
					a --;
					octaveLevel = String.valueOf(a);
				}
				if(octaveLevel.equals("Regular")){
					octave.setText(octaveLevel+" Octave");
				}
				else if(Integer.valueOf(octaveLevel)>0){
					octave.setText("+"+octaveLevel+" Octave");
				}
				else{
					octave.setText(octaveLevel+" Octave");
				}
			}
			else{
				JOptionPane.showMessageDialog(null,"The lowest octave of a note is 1!","Minimum Octave Error",JOptionPane.WARNING_MESSAGE);
			}
		} else if (cmd == "Change Tempo") {
			//TODO - fill this
			try{
				String ratio;
				//ratio=Double.parseDouble(tempoText.getText());
				JOptionPane inputTempo = new JOptionPane();
				inputTempo.setSize(new Dimension(800,400));
				ratio = inputTempo.showInputDialog(frame, "Please enter a number to change the tempo.\n (for instance: 2.0 for doubling the tempo)","1.0");
				double r = Double.parseDouble(ratio);
				if(r>0){
					song.changeTempo(r);
					String s1 = String.valueOf(r*Double.parseDouble(currentTempo.getText()));
					currentTempo.setText(s1);
					updateTotalTime();
					setCurrentTime(currentTimeSlider.getValue()/500.00*song.getTotalDuration());

				}
				else if(r<=0){
					JOptionPane.showMessageDialog(null,"The input must be a positive number!","Invalid Input",JOptionPane.WARNING_MESSAGE);
				}
				else{
					throw new Exception();
				}
			}
			// This indicates that the user didn't give any input which means tempo is not changed
			catch(NullPointerException e){}
			catch (Exception e){
				JOptionPane.showMessageDialog(null,"The input is invalid. Please try again!","Invalid Input!",JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	/*
	 * Called when audio events occur in the StdAudio library. We use this to
	 * set the displayed current time in the slider.
	 */
	/**
	 * This method is called when audio is played
	 */
	public void onAudioEvent(StdAudio.AudioEvent event) {
		// update current time
		if (event.getType() == StdAudio.AudioEvent.Type.PLAY
				|| event.getType() == StdAudio.AudioEvent.Type.STOP) {
			setCurrentTime(getCurrentTime() + event.getDuration());
		}
	}

	/*
	 * Sets up the graphical components in the window and event listeners.
	 */
	/**
	 * This method creates all the components in UI
	 */
	private void createComponents() {
		//TODO - create all your components here.
		// note that you should have already defined your components as instance variables.
		frame = new JFrame("Music Player by Guoda Liang and Yiwei Guo");
		frame.setSize(800, 400);
		fileChooser = new JFileChooser();
		statusLabel = new JLabel("Welcome to the Music Player!",JLabel.CENTER);
		statusLabel.setFont(new Font("Comic", Font.BOLD, 22));


		currentTimeLabel = new JLabel();
		currentTimeSlider = new JSlider(JSlider.HORIZONTAL, 0, 500, 0);
		currentTimeSlider.setPreferredSize(new Dimension(500, 30));
		currentTimeSlider.setMajorTickSpacing(100);
		currentTimeSlider.setMinorTickSpacing(50);
		currentTimeSlider.setPaintTicks(true);

		play = new JButton("Play");
		play.setPreferredSize(new Dimension(90,40));
		play.setBorder(BorderFactory.createLineBorder(Color.GREEN));
		play.setToolTipText("Click to play the song.");
		play.addActionListener(this);

		pause = new JButton("Pause");
		pause.setPreferredSize(new Dimension(90,40));
		pause.setBorder(BorderFactory.createLineBorder(Color.GREEN));
		pause.setToolTipText("Click to pause the song.");
		pause.addActionListener(this);

		stop = new JButton("Stop");
		stop.setPreferredSize(new Dimension(90,40));
		stop.setBorder(BorderFactory.createLineBorder(Color.GREEN));
		stop.setToolTipText("Click to stop the song.");
		stop.addActionListener(this);

		load = new JButton("Load");
		load.setPreferredSize(new Dimension(90,40));
		load.setBorder(BorderFactory.createLineBorder(Color.GREEN));
		load.setToolTipText("Click to load a song.");
		load.addActionListener(this);

		reverse = new JButton("Reverse");
		reverse.setPreferredSize(new Dimension(120,25));
		reverse.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		reverse.setToolTipText("Click to reverse the song.");
		reverse.addActionListener(this);

		tempLabel = new JLabel("Tempo");
		changeDuration = new JButton("Change Tempo");
		changeDuration.setPreferredSize(new Dimension(120,25));
		changeDuration.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		changeDuration.addActionListener(this);

		octaveUp = new JButton("Up");
		octaveUp.setPreferredSize(new Dimension(80,25));
		octaveUp.setMinimumSize(new Dimension(80,25));
		octaveUp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		octaveUp.setToolTipText("Click to increase the octave by one.");
		octaveUp.addActionListener(this);

		octaveDown = new JButton("Down");
		octaveDown.setPreferredSize(new Dimension(80,25));
		octaveDown.setMinimumSize(new Dimension(80,25));
		octaveDown.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		octaveDown.setToolTipText("Click to decrease the octave by one.");
		octaveDown.addActionListener(this);



		currentTimeLabel = new JLabel("0.00/");
		currentTimeLabel.setFont(new Font("Comic", Font.BOLD,14));

		totalTimeLabel = new JLabel("0.00sec");
		totalTimeLabel.setFont(new Font("Comic", Font.BOLD,14));

		currentTempo = new JLabel("1.0",JLabel.CENTER);
		currentTempo.setFont(new Font("Serif",Font.BOLD,15));

		octave = new JLabel(octaveLevel +" Octave");
		octave.setPreferredSize(new Dimension(120,30));
		octave.setFont(new Font("Serif",Font.BOLD,15));

		doEnabling();
	}


	/*
	 * Performs layout of the components within the graphical window. 
	 * Also make the window a certain size and put it in the center of the screen.
	 */
	/**
	 * This method arranges the positions of different components
	 */
	private void doLayout() {
		//TODO - figure out how to layout the components
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));


		panel1 = new JPanel();
		panel2 = new JPanel();
		panel3 = new JPanel();
		panel4 = new JPanel();

		//line0.setLayout(new BoxLayout(line0, BoxLayout.Y_AXIS));
		panel1.setLayout(new FlowLayout());
		statusLabel.setPreferredSize(new Dimension(800, 30));
		panel1.add(statusLabel);
		panel1.add(new JLabel(""));
		panel1.setPreferredSize(new Dimension(800, 20));

		panel2.add(new JLabel("                           "));
		panel2.add(currentTimeSlider);
		panel2.add(new JLabel(" "));
		panel2.add(currentTimeLabel);
		panel2.add(totalTimeLabel);
		panel2.setPreferredSize(new Dimension(800, 5));

		panel3.add(play);
		panel3.add(new JLabel("  "));
		panel3.add(pause);
		panel3.add(new JLabel("  "));
		panel3.add(stop);
		panel3.add(new JLabel("  "));
		panel3.add(load);
		panel3.add(new JLabel("    "));
		panel3.setPreferredSize(new Dimension(800, 0));

		panel4.add(new JLabel("                                           "));
		JPanel p41 = new JPanel();
		p41.setPreferredSize(new Dimension(120,80));
		p41.add(reverse);
		p41.add(Box.createRigidArea(new Dimension(120,5)));
		p41.add(changeDuration);
		panel4.add(p41);
		JPanel p40 = new JPanel();
		p40.setLayout(new BorderLayout());
		p40.setPreferredSize(new Dimension(150,60));
		JLabel currentT = new JLabel("     Current Tempo:");
		currentT.setFont(new Font("Serif",Font.BOLD,15));
		p40.add(currentT,BorderLayout.NORTH);
		p40.add(currentTempo,BorderLayout.CENTER);
		currentTempo.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel4.add(p40);
		panel4.add(new JLabel(""));
		JPanel p42 = new JPanel();
		p42.setLayout(new FlowLayout());
		p42.setPreferredSize(new Dimension(80,75));
		p42.add(octaveUp);
		p42.add(Box.createRigidArea(new Dimension(80,5)));
		p42.add(octaveDown);
		panel4.add(p42);
		panel4.add(new JLabel("    "));
		panel4.add(octave);
		panel4.setPreferredSize(new Dimension(750, 20));




		frame.add(Box.createRigidArea(new Dimension(500,30)));
		frame.add(panel1);
		frame.add(panel2);
		frame.add(panel3);
		frame.add(panel4);
		frame.setMinimumSize(new Dimension(800, 450));
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
	}



	/*
	 * Sets whether every button, slider, spinner, etc. should be currently
	 * enabled, based on the current state of whether a song has been loaded and
	 * whether or not it is currently playing. This is done to prevent the user
	 * from doing actions at inappropriate times such as clicking play while the
	 * song is already playing, etc.
	 */

	/**
	 * This method refreshes the the usability of different buttons based on different situations
	 */
	private void doEnabling() {
		//TODO - figure out which buttons need to enabled
		if(isStopped){
			play.setEnabled(true);
			pause.setEnabled(false);
			stop.setEnabled(false);
			load.setEnabled(true);
			reverse.setEnabled(true);
			octaveUp.setEnabled(true);
			octaveDown.setEnabled(true);
			changeDuration.setEnabled(true);
		}
		else{
			if(playing){
				play.setEnabled(false);
				pause.setEnabled(true);
				stop.setEnabled(true);
				load.setEnabled(false);
				reverse.setEnabled(false);
				octaveUp.setEnabled(false);
				octaveDown.setEnabled(false);
				changeDuration.setEnabled(false);	
			}
			else {

				if (fileChooser.getSelectedFile()!=null) {
					play.setEnabled(true);
					pause.setEnabled(false);
					stop.setEnabled(true);
					load.setEnabled(false);
					reverse.setEnabled(true);
					octaveUp.setEnabled(true);
					octaveDown.setEnabled(true);
					changeDuration.setEnabled(true);
				}
				else {
					play.setEnabled(false);
					pause.setEnabled(false);
					stop.setEnabled(false);
					load.setEnabled(true);
					reverse.setEnabled(false);
					octaveUp.setEnabled(false);
					octaveDown.setEnabled(false);
					changeDuration.setEnabled(false);
				}
			}
		}
	}







	/*
	 * Returns the estimated current time within the overall song, in seconds.
	 */
	/**
	 * This method returns the current time
	 * @return
	 */
	private double getCurrentTime() {
		String timeStr = currentTimeLabel.getText();
		timeStr = timeStr.replace(" /", "");
		try {
			return Double.parseDouble(timeStr);
		} catch (NumberFormatException nfe) {
			return 0.0;
		}
	}

	/*
	 * Pops up a file-choosing window for the user to select a song file to be
	 * loaded. If the user chooses a file, a Song object is created and used
	 * to represent that song.
	 */
	/**
	 * This method load the file from the computer and throws an exception if file is unaccessible
	 * @throws IOException
	 */
	private void loadFile() throws IOException {
		if (fileChooser.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File selected = fileChooser.getSelectedFile();
		if (selected == null) {
			return;
		}
		statusLabel.setText("File Loaded: " + selected.getName());
		String filename = selected.getAbsolutePath();
		System.out.println("Loading song from " + selected.getName() + " ...");

		//TODO - create a song from the file
		song = new Song(filename);

		currentTempo.setText("1.0");

		setCurrentTime(0.0);
		octaveLevel = "Regular";
		octave.setText(octaveLevel+" Octave");
		updateTotalTime();
		System.out.println("Loading complete.");
		System.out.println("Song: " + song);
		doEnabling();
	}

	/*
	 * Initiates the playing of the current song in a separate thread (so
	 * that it does not lock up the GUI). 
	 * You do not need to change this method.
	 * It will not compile until you make your Song class.
	 */
	/**
	 * This is the given method for playing the song
	 */
	private void playSong() {
		if (song != null) {
			setCurrentTime(0.0);
			Thread playThread = new Thread(new Runnable() {
				public void run() {
					StdAudio.setMute(false);
					setPlaying(true);
					doEnabling();
					String title = song.getTitle();
					String artist = song.getArtist();
					double duration = song.getTotalDuration();
					System.out.println("Playing \"" + title + "\", by "
							+ artist + " (" + duration + " sec)");
					song.play();
					System.out.println("Playing complete.");
					setPlaying(false);
					doEnabling();
				}
			});
			playThread.start();
		}
	}

	/*
	 * Sets the current time display slider/label to show the given time in
	 * seconds. Bounded to the song's total duration as reported by the song.
	 */
	/** 
	 * This method sets the current time
	 * @param time input current time
	 */
	private void setCurrentTime(double time) {
		double total = song.getTotalDuration();
		time = Math.max(0, Math.min(total, time));
		currentTimeLabel.setText(String.format("%07.2f /", time));
		currentTimeSlider.setValue((int) (500 * time / total));
	}

	/*
	 * Updates the total time label on the screen to the current total duration.
	 */
	/**
	 * This method is used to update the total time of the song whenever loading a new file
	 */
	private void updateTotalTime() {
		//TODO - fill this
		totalTimeLabel.setText(String.format("%07.2f", song.getTotalDuration())+" sec");
	}

	/*
	 * The following are all useless getters and setters that I created just in case I might need them
	 */
	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public JButton getLoad() {
		return load;
	}

	public void setLoad(JButton load) {
		this.load = load;
	}

	public JButton getPlay() {
		return play;
	}

	public void setPlay(JButton play) {
		this.play = play;
	}

	public JButton getStop() {
		return stop;
	}

	public void setStop(JButton stop) {
		this.stop = stop;
	}

	public JButton getOctaveUp() {
		return octaveUp;
	}

	public void setOctaveUp(JButton octaveUp) {
		this.octaveUp = octaveUp;
	}

	public JButton getReverse() {
		return reverse;
	}

	public void setReverse(JButton reverse) {
		this.reverse = reverse;
	}

	public JButton getOctaveDown() {
		return octaveDown;
	}

	public void setOctaveDown(JButton octaveDown) {
		this.octaveDown = octaveDown;
	}

	public JButton getChangeDuration() {
		return changeDuration;
	}

	public void setChangeDuration(JButton changeDuration) {
		this.changeDuration = changeDuration;
	}

	public JLabel getStatusLabel() {
		return statusLabel;
	}

	public void setStatusLabel(JLabel statusLabel) {
		this.statusLabel = statusLabel;
	}

	public JLabel getTotalTimeLabel() {
		return totalTimeLabel;
	}

	public void setTotalTimeLabel(JLabel totalTimeLabel) {
		this.totalTimeLabel = totalTimeLabel;
	}

	public JSlider getCurrentTimeSlider() {
		return currentTimeSlider;
	}

	public void setCurrentTimeSlider(JSlider currentTimeSlider) {
		this.currentTimeSlider = currentTimeSlider;
	}

	public JPanel getPanel3() {
		return panel3;
	}

	public void setPanel3(JPanel panel3) {
		this.panel3 = panel3;
	}

	public JPanel getPanel4() {
		return panel4;
	}

	public void setPanel4(JPanel panel4) {
		this.panel4 = panel4;
	}
}
