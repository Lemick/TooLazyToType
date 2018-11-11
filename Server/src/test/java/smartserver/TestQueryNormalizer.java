package smartserver;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import text.normalizer.QueryNormalizer;

public class TestQueryNormalizer {

	@Test
	public void removeLeadingStopWords() {
		QueryNormalizer queryNormalizer = new QueryNormalizer();
		String actual;
		
		actual = queryNormalizer.removeLeadingStopWords("Le foc");
		assertEquals("foc", actual);
		
		actual = queryNormalizer.removeLeadingStopWords("le foc");
		assertEquals("foc", actual);
		
		actual = queryNormalizer.removeLeadingStopWords("Foc");
		assertEquals("foc", actual);
		
		actual = queryNormalizer.removeLeadingStopWords("l'éclair de lumière");
		assertEquals("éclair de lumière", actual);
		
		actual = queryNormalizer.removeLeadingStopWords("de l'air");
		assertEquals("air", actual);
	}
	
	@Test
	public void removeDoubleQuotes() {
		String actual;
		
		actual = QueryNormalizer.removeDoubleQuotes("\"Qui a dit \"\"Eh bien : dansez maintenant.\"\" ?\"");
		assertEquals("Qui a dit Eh bien : dansez maintenant. ?", actual);

	}
	
}
