package smartserver;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import org.hamcrest.collection.IsMapContaining;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.tltt.lib.file.ResourceFile;
import com.tltt.lib.question.Answer;
import com.tltt.lib.question.Question;
import com.tltt.lib.text.NoPredictionException;
import com.tltt.lib.text.OccurencesSearcher;

public class TestOccurencesSearcher {

	public String testFileBR;
	public String testFileArcEnCiel;
	public String testFileRaccourci;
	public String testFileVanGogh;
	public String testFileVictorHugo;

	@Mock
	Question mockQuestion;

	@Before
	public void initFile() throws IOException {
		MockitoAnnotations.initMocks(this);
		Mockito.when(mockQuestion.getTitle()).thenReturn("Quelle est la capitable de la France?");
		Mockito.when(mockQuestion.getAnswers()).thenReturn(new ArrayList<Answer>());

		testFileBR = new ResourceFile("QuestionBattleRoyal.html").getContent();
		testFileArcEnCiel = new ResourceFile("QuestionMytheArcEnCiel.html").getContent();
		testFileRaccourci = new ResourceFile("QuestionRaccourciClavierAnnule.html").getContent();
		testFileVanGogh = new ResourceFile("QuestionVanGogh.html").getContent();
		testFileVictorHugo = new ResourceFile("QuestionVictorHugo.html").getContent();
	}

	@Test
	public void mostOccurenceBR() throws NoPredictionException {
		Question question = new Question();
		question.setTitle("A quelle famille de jeux vidéo PUBG et H1Z1 appartiennent-ils ?");
		Answer expected = new Answer("C", "Battle Royale");

		question.getAnswers().add(new Answer("A", "FPS"));
		question.getAnswers().add(new Answer("B", "RPG"));
		question.getAnswers().add(expected);
		question.getAnswers().add(new Answer("D", "Plateforme"));

		OccurencesSearcher occurencesSearcher = new OccurencesSearcher(question, testFileBR);
		Answer actual = occurencesSearcher.predictAnswer();
		assertEquals(expected, actual);
	}

	@Test
	public void mostOccurenceArc() throws NoPredictionException {
		Question question = new Question();
		question.setTitle("D'aprés le mythe, que trouve-t-on aux pieds des arcs-en-ciels?");
		Answer expected = new Answer("A", "Un trésor");

		question.getAnswers().add(new Answer("B", "De l'eau"));
		question.getAnswers().add(new Answer("C", "Un projecteur"));
		question.getAnswers().add(expected);
		question.getAnswers().add(new Answer("D", "Le nouveau gourvenement"));

		OccurencesSearcher occurencesSearcher = new OccurencesSearcher(question, testFileArcEnCiel);
		Answer actual = occurencesSearcher.predictAnswer();
		assertEquals(expected, actual);
	}

	@Test
	public void mostOccurenceRacc() throws NoPredictionException {
		Question question = new Question();
		question.setTitle("Sur PC, que faut-il taper sur son clavier pour annuler une action ?");
		Answer expected = new Answer("B", "Ctrl + Z");

		question.getAnswers().add(expected);
		question.getAnswers().add(new Answer("C", "Ctrl + A"));
		question.getAnswers().add(new Answer("A", "Ctrl + C"));
		question.getAnswers().add(new Answer("D", "Ctrl + V"));

		OccurencesSearcher occurencesSearcher = new OccurencesSearcher(question, testFileRaccourci);
		Answer actual = occurencesSearcher.predictAnswer();
		assertEquals(expected, actual);
	}

	@Test
	public void mostOccurenceVictorHugo() throws NoPredictionException {
		Question question = new Question();
		question.setTitle("Parmi ces romans, lequel n'a pas été écrit par Victor Hugo ?");

		Answer answerA = new Answer("A", "Notre-Dame de Paris");
		Answer answerB = new Answer("B", "Les Misérables");
		Answer answerC = new Answer("C", "Au Bonheur des Dames");
		Answer answerD = new Answer("D", "Le Dernier Jour d'un Condamné");
		question.setAnswers(Arrays.asList(answerA, answerB, answerC, answerD));
		
		OccurencesSearcher occurencesSearcher = new OccurencesSearcher(question, testFileVictorHugo);
		Map<Answer, Integer> mapAnswersOccurences = occurencesSearcher.getAnswersOccurences();

		assertThat(mapAnswersOccurences.size(), is(4));
		assertThat(mapAnswersOccurences, IsMapContaining.hasEntry(answerA, 2));
		assertThat(mapAnswersOccurences, IsMapContaining.hasEntry(answerB, 4));
		assertThat(mapAnswersOccurences, IsMapContaining.hasEntry(answerC, 0));
		assertThat(mapAnswersOccurences, IsMapContaining.hasEntry(answerD, 1));
	}

	@Test
	public void countWordsBR() {
		int actual;
		OccurencesSearcher occurencesSearcher = new OccurencesSearcher(mockQuestion, testFileBR);

		actual = occurencesSearcher.countWord("battle Royale");
		assertEquals(14, actual);

		actual = occurencesSearcher.countWord("BATTLE ROYALE");
		assertEquals(14, actual);

		actual = occurencesSearcher.countWord("battle royale");
		assertEquals(14, actual);

		actual = occurencesSearcher.countWord("FPS");
		assertEquals(1, actual);

		actual = occurencesSearcher.countWord("Celine Dion");
		assertEquals(0, actual);
	}

	@Test
	public void countWordsArc() {
		int actual;
		OccurencesSearcher occurencesSearcher = new OccurencesSearcher(mockQuestion, testFileArcEnCiel);

		actual = occurencesSearcher.countWord("tresor");
		assertEquals(4, actual);

		actual = occurencesSearcher.countWord("un tresor");
		assertEquals(3, actual);

		actual = occurencesSearcher.countWord("ceci non plus");
		assertEquals(0, actual);
	}

	@Test
	public void countWordsRacc() {
		int actual;
		OccurencesSearcher occurencesSearcher = new OccurencesSearcher(mockQuestion, testFileRaccourci);

		actual = occurencesSearcher.countWord("Ctrl + Z");
		assertEquals(2, actual);

		actual = occurencesSearcher.countWord("Ctrl + A");
		assertEquals(0, actual);

		actual = occurencesSearcher.countWord("Ctrl + V");
		assertEquals(0, actual);
	}

	@Test
	public void testMaxMapBR() throws NoPredictionException {
		OccurencesSearcher occurencesSearcher;

		occurencesSearcher = new OccurencesSearcher(mockQuestion, testFileRaccourci);
		occurencesSearcher.getAnswersOccurences().clear();
		occurencesSearcher.getAnswersOccurences().put(new Answer("A", "Eau"), 8);
		occurencesSearcher.getAnswersOccurences().put(new Answer("B", "Feu"), 16);
		occurencesSearcher.getAnswersOccurences().put(new Answer("C", "Terre"), 3);
		occurencesSearcher.getAnswersOccurences().put(new Answer("D", "Air"), 0);
		Answer result = occurencesSearcher.getMostFrequentAnswer();
		assertEquals("B", result.getId());

		occurencesSearcher = new OccurencesSearcher(mockQuestion, testFileRaccourci);
		occurencesSearcher.getAnswersOccurences().put(new Answer("A", "AA"), 0);
		occurencesSearcher.getAnswersOccurences().put(new Answer("B", "AA"), 1);
		occurencesSearcher.getAnswersOccurences().put(new Answer("C", "BB"), 0);
		occurencesSearcher.getAnswersOccurences().put(new Answer("D", "BB"), 2);
		result = occurencesSearcher.getMostFrequentAnswer();
		assertEquals("D", result.getId());

	}

}
