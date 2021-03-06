package smartserver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.*;

import java.util.List;
import java.util.StringTokenizer;

import org.junit.Test;

import com.tltt.lib.text.normalizer.Tokenizer;

public class TestTokenizer {

	@Test
	public void testRetrieveDelimiters() {
		List<String> actual;
		Tokenizer tokenizer;
		
		tokenizer = new Tokenizer("Ceci est plutôt intéréssant");
		actual = tokenizer.getDelimiters();
		assertThat(actual, contains(" "," "," "));
		
		tokenizer = new Tokenizer("l'espérance est bonne");
		actual = tokenizer.getDelimiters();
		assertThat(actual, contains("'"," "," "));		
	}
	
	@Test
	public void testRetrieveData() {
		List<String> actual;
		Tokenizer tokenizer;
		
		tokenizer = new Tokenizer("Ceci est plutôt intéréssant");
		actual = tokenizer.getData();
		assertThat(actual, contains("Ceci","est","plutôt","intéréssant"));
		
		tokenizer = new Tokenizer("l'espérance est bonne");
		actual = tokenizer.getData();
		assertThat(actual, contains("l","espérance","est", "bonne"));		
	}
	
	@Test
	public void testJoinString() {
		Tokenizer tokenizer;
		String actual;
		
		tokenizer = new Tokenizer("");
		actual = tokenizer.joinTokensWithDelimiters();
		assertEquals(actual, "");
		
		tokenizer = new Tokenizer("42");
		actual = tokenizer.joinTokensWithDelimiters();
		assertEquals(actual, "42");
		
		tokenizer = new Tokenizer("hey l'ami");
		actual = tokenizer.joinTokensWithDelimiters();
		assertEquals(actual, "hey l'ami");
	}
	
	@Test
	public void testRemoveLeadingWord() {
		Tokenizer tokenizer;
		String actual;
		tokenizer = new Tokenizer("Ceci est l'art");
		
		tokenizer.removeCurrentWord();
		actual = tokenizer.joinTokensWithDelimiters();
		assertEquals(actual, "est l'art");
		
		tokenizer.removeCurrentWord();
		actual = tokenizer.joinTokensWithDelimiters();
		assertEquals(actual, "l'art");
		
		tokenizer.removeCurrentWord();
		actual = tokenizer.joinTokensWithDelimiters();
		assertEquals(actual, "art");
		
		tokenizer.removeCurrentWord();
		actual = tokenizer.joinTokensWithDelimiters();
		assertEquals(actual, "");
	}
	
	@Test
	public void testSize() {
		Tokenizer tokenizer;
		int actual;
		tokenizer = new Tokenizer("Ceci est l'art");
		
		actual = tokenizer.wordsCount();
		assertEquals(4, actual);
		
		tokenizer.removeCurrentWord();
		actual = tokenizer.wordsCount();
		assertEquals(3, actual);
		
		tokenizer.removeCurrentWord();
		actual = tokenizer.wordsCount();
		assertEquals(2, actual);		
		
		tokenizer.removeCurrentWord();
		actual = tokenizer.wordsCount();
		assertEquals(1, actual);
		
		tokenizer.removeCurrentWord();
		actual = tokenizer.wordsCount();
		assertEquals(0, actual);
	}
}
