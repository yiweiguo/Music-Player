package songs;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SongTest {
	Song song;

	@Before
	public void setUp() throws Exception {
		song  = new Song("PopGoesTheWeasel.txt");
	}

	@Test
	public void testSong() {
		assertEquals("Pop Goes the Weasel",song.getTitle());
		assertEquals("Unknown",song.getArtist());
		assertEquals(9.8,song.getTotalDuration(),0.001);
		assertEquals(30,song.getNotes().length);
	}

	@Test
	public void testGetTitle() {
		assertEquals("Pop Goes the Weasel",song.getTitle());
	}

	@Test
	public void testGetArtist() {
		assertEquals("Unknown",song.getArtist());
	}

	@Test
	public void testGetTotalDuration() {
		assertEquals(9.8,song.getTotalDuration(),0.001);
	}

	@Test
	public void testOctaveDown() {
		song.octaveDown();
		assertEquals(3,song.getNotes()[0].getOctave());
		assertEquals(4,song.getNotes()[25].getOctave());
	}

	@Test
	public void testOctaveUp() {
		song.octaveUp();
		assertEquals(5,song.getNotes()[0].getOctave());
		assertEquals(6,song.getNotes()[25].getOctave());
	}

	@Test
	public void testChangeTempo() {
		song.changeTempo(0.5);
		assertEquals(0.1,song.getNotes()[0].getDuration(),0.001);
		assertEquals(0.2,song.getNotes()[1].getDuration(),0.001);
		assertEquals(4.9,song.getTotalDuration(),0.001);
		song.changeTempo(4);
		assertEquals(0.4,song.getNotes()[0].getDuration(),0.001);
		assertEquals(0.8,song.getNotes()[1].getDuration(),0.001);
		assertEquals(19.6,song.getTotalDuration(),0.001);
	}

	@Test
	public void testReverse() {
		Note note1 = song.getNotes()[0];
		Note note30 = song.getNotes()[29];
		song.reverse();
		assertEquals(song.getNotes()[29],note1);
		assertEquals(song.getNotes()[0],note30);
	}
	
	@Test
	public void testPlay(){
		song.getNotes()[2].setRepeat(true);
		song.getNotes()[5].setRepeat(true);
		song.play();
		assertTrue(song.unitTestForPlay.containsKey(5));
		assertTrue(song.unitTestForPlay.containsValue(2));
		assertEquals(2,song.unitTestForPlay.get(5));
	}

	
}
