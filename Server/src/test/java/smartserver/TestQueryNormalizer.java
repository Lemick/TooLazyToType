package smartserver;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.tltt.lib.text.normalizer.QueryNormalizer;

public class TestQueryNormalizer {

	@Test
	public void removeLeadingStopWords() {
		QueryNormalizer queryNormalizer = QueryNormalizer.getInstance();
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
		
		actual = queryNormalizer.removeLeadingStopWords("de la");
		assertEquals("la", actual);
		
		actual = queryNormalizer.removeLeadingStopWords("");
		assertEquals("", actual);
	}
	
	@Test
	public void removeStopWords() {
		QueryNormalizer queryNormalizer = QueryNormalizer.getInstance();
		String actual;
		
		actual = queryNormalizer.removeAllStopWords("Le foc");
		assertEquals("foc", actual);
		
		actual = queryNormalizer.removeAllStopWords("le foc");
		assertEquals("foc", actual);
		
		actual = queryNormalizer.removeAllStopWords("Foc");
		assertEquals("foc", actual);
		
		actual = queryNormalizer.removeAllStopWords("éclair de lumière");
		assertEquals("éclair lumière", actual);
		
		actual = queryNormalizer.removeAllStopWords("de l'air de la");
		assertEquals("air ", actual);
		
		actual = queryNormalizer.removeAllStopWords("Sot-l'y-laisse");
		assertEquals("sot-l'y-laisse", actual);
		
		actual = queryNormalizer.removeAllStopWords("de la");
		assertEquals("", actual);
		
		actual = queryNormalizer.removeAllStopWords("");
		assertEquals("", actual);
		
		actual = queryNormalizer.removeAllStopWords("Quel réseau social propose désormais des émissions de divertissement en partenariat avec des médias, \"les show\" ?");
		assertEquals("réseau social propose émissions divertissement partenariat médias, \"les show\" ?", actual);
	}
	
	@Test
	public void containsTopicalSubjects() {
		QueryNormalizer queryNormalizer = QueryNormalizer.getInstance();
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
		
		actual = QueryNormalizer.getInstance().removeDoubleQuotes("\"Qui a dit \"\"Eh bien : dansez maintenant.\"\" ?\"");
		assertEquals("Qui a dit Eh bien : dansez maintenant. ?", actual);

	}
	
	@Test
	public void testRemoveLeadingStopWordsNeverEmpty() {
		String actual;
		
		actual = QueryNormalizer.getInstance().removeLeadingStopWords("Elle et Lui");
		assertEquals("lui", actual);

		actual = QueryNormalizer.getInstance().removeLeadingStopWords("Lui");
		assertEquals("lui", actual);
	}
	
}
