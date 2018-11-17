package smartserver;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.tltt.lib.text.normalizer.QueryNormalizer;

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
		
		actual = queryNormalizer.removeLeadingStopWords("Sot-l'y-laisse");
		assertEquals("sot-l'y-laisse", actual);
	}
	
	@Test
	public void containsTopicalSubjects() {
		QueryNormalizer queryNormalizer = new QueryNormalizer();
		boolean actual;
				
		actual = queryNormalizer.isTopicalSubjectSentence("Qui à eu ce mois-ci le césar du meilleur acteur ?");
		assertEquals(true, actual);
		
		actual = queryNormalizer.isTopicalSubjectSentence("Cette semaine, quelle ville à été decernée meilleure ville du monde ?");
		assertEquals(true, actual);
		
		actual = queryNormalizer.isTopicalSubjectSentence("Qui a été nommé homme de l'année aujourd'hui?");
		assertEquals(true, actual);
		
		actual = queryNormalizer.isTopicalSubjectSentence("Quel film sort bientôt au cinéma ?");
		assertEquals(true, actual);
		
		actual = queryNormalizer.isTopicalSubjectSentence("Qui est l'inventeur de l'ampoule ?");
		assertEquals(false, actual);
		

	}
	
	
	@Test
	public void removeDoubleQuotes() {
		String actual;
		
		actual = QueryNormalizer.removeDoubleQuotes("\"Qui a dit \"\"Eh bien : dansez maintenant.\"\" ?\"");
		assertEquals("Qui a dit Eh bien : dansez maintenant. ?", actual);

	}
	
}
