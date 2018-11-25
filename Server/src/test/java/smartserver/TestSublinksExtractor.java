package smartserver;

import static org.junit.Assert.*;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import com.tltt.lib.file.ResourceFile;
import com.tltt.lib.html.GoogleSublinkExtractor;

public class TestSublinksExtractor {

	public Document testFileBRDoc;

	@Before
	public void initFile() throws IOException {
		ResourceFile testFileBR = new ResourceFile("QuestionBattleRoyal.html");
		testFileBRDoc = Jsoup.parse(testFileBR.getContent());
	}

	@Test
	public void testGoogleSublinkExtractor() {
		GoogleSublinkExtractor googleSublinkExtractor = new GoogleSublinkExtractor(testFileBRDoc);
		String actual;
		
		actual = googleSublinkExtractor.getNextSublink();
		assertEquals("https://fr.wikipedia.org/wiki/PlayerUnknown%27s_Battlegrounds" , actual);
		
		actual = googleSublinkExtractor.getNextSublink();
		assertEquals("https://fr.wikipedia.org/wiki/Battle_royale_(jeu_vid%C3%A9o)" , actual);
	}
	
	@Test
	public void testGoogleSublinkExtractorEmpty() {
		GoogleSublinkExtractor googleSublinkExtractor = new GoogleSublinkExtractor(new Document(""));
		String actual;
		
		actual = googleSublinkExtractor.getNextSublink();
		assertEquals(null, actual);

	}
}
