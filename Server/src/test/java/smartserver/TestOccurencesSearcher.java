package smartserver;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.tltt.lib.dto.QuidAnswerDTO;
import com.tltt.lib.dto.QuidQuestionDTO;
import com.tltt.lib.file.ResourceFile;
import com.tltt.lib.text.NoPredictionException;
import com.tltt.lib.text.OccurencesSearcher;

public class TestOccurencesSearcher {

	public String testFileBR;
	public String testFileArcEnCiel;
	public String testFileRaccourci;
	public String testFileVanGogh;

	@Mock
	QuidQuestionDTO mockQuestionDTO;
	
	@Before
	public void initFile() throws IOException {
		MockitoAnnotations.initMocks(this);
		Mockito.when(mockQuestionDTO.getQuestionEntitled()).thenReturn("Quelle est la capitable de la France?");
		Mockito.when(mockQuestionDTO.getAnswers()).thenReturn(new ArrayList<QuidAnswerDTO>());
		
		testFileBR = new ResourceFile("QuestionBattleRoyal.html").getContent();
		testFileArcEnCiel = new ResourceFile("QuestionMytheArcEnCiel.html").getContent();
		testFileRaccourci = new ResourceFile("QuestionRaccourciClavierAnnule.html").getContent();
		testFileVanGogh = new ResourceFile("QuestionVanGogh.html").getContent();
	}

	@Test
	public void mostOccurenceBR() throws NoPredictionException {
		QuidQuestionDTO quidQuestionDTO = new QuidQuestionDTO();
		quidQuestionDTO.setQuestionEntitled("A quelle famille de jeux vidéo PUBG et H1Z1 appartiennent-ils ?");
		QuidAnswerDTO expected = new QuidAnswerDTO("C", "Battle Royale");

		quidQuestionDTO.getAnswers().add(new QuidAnswerDTO("A", "FPS"));
		quidQuestionDTO.getAnswers().add(new QuidAnswerDTO("B", "RPG"));
		quidQuestionDTO.getAnswers().add(expected);
		quidQuestionDTO.getAnswers().add(new QuidAnswerDTO("D", "Plateforme"));

		OccurencesSearcher occurencesSearcher = new OccurencesSearcher(quidQuestionDTO, testFileBR);
		QuidAnswerDTO actual = occurencesSearcher.getMostOccuredAnswer();
		assertEquals(expected, actual);
	}

	@Test
	public void mostOccurenceArc() throws NoPredictionException {
		QuidQuestionDTO quidQuestionDTO = new QuidQuestionDTO();
		quidQuestionDTO.setQuestionEntitled("D'aprés le mythe, que trouve-t-on aux pieds des arcs-en-ciels?");
		QuidAnswerDTO expected = new QuidAnswerDTO("A", "Un trésor");

		quidQuestionDTO.getAnswers().add(new QuidAnswerDTO("B", "De l'eau"));
		quidQuestionDTO.getAnswers().add(new QuidAnswerDTO("C", "Un projecteur"));
		quidQuestionDTO.getAnswers().add(expected);
		quidQuestionDTO.getAnswers().add(new QuidAnswerDTO("D", "Le nouveau gourvenement"));

		OccurencesSearcher occurencesSearcher = new OccurencesSearcher(quidQuestionDTO, testFileArcEnCiel);
		QuidAnswerDTO actual = occurencesSearcher.getMostOccuredAnswer();
		assertEquals(expected, actual);
	}

	@Test
	public void mostOccurenceRacc() throws NoPredictionException {
		QuidQuestionDTO quidQuestionDTO = new QuidQuestionDTO();
		quidQuestionDTO.setQuestionEntitled("Sur PC, que faut-il taper sur son clavier pour annuler une action ?");
		QuidAnswerDTO expected = new QuidAnswerDTO("B", "Ctrl + Z");

		quidQuestionDTO.getAnswers().add(expected);
		quidQuestionDTO.getAnswers().add(new QuidAnswerDTO("C", "Ctrl + A"));
		quidQuestionDTO.getAnswers().add(new QuidAnswerDTO("A", "Ctrl + C"));
		quidQuestionDTO.getAnswers().add(new QuidAnswerDTO("D", "Ctrl + V"));

		OccurencesSearcher occurencesSearcher = new OccurencesSearcher(quidQuestionDTO, testFileRaccourci);
		QuidAnswerDTO actual = occurencesSearcher.getMostOccuredAnswer();
		assertEquals(expected, actual);
	}

	@Test
	public void countWordsBR() {
		int actual;
		OccurencesSearcher occurencesSearcher = new OccurencesSearcher(mockQuestionDTO, testFileBR);

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
		OccurencesSearcher occurencesSearcher = new OccurencesSearcher(mockQuestionDTO, testFileArcEnCiel);

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
		OccurencesSearcher occurencesSearcher = new OccurencesSearcher(mockQuestionDTO, testFileRaccourci);

		actual = occurencesSearcher.countWord("Ctrl + Z");
		assertEquals(2, actual);

		actual = occurencesSearcher.countWord("Ctrl + A");
		assertEquals(0, actual);

		actual = occurencesSearcher.countWord("Ctrl + V");
		assertEquals(0, actual);
	}

	@Test
	public void testMaxMapBR() throws NoPredictionException {
		Map<QuidAnswerDTO, Integer> map = new HashMap<>();
		map.put(new QuidAnswerDTO("A", "Eau"), 8);
		map.put(new QuidAnswerDTO("B", "Feu"), 16);
		map.put(new QuidAnswerDTO("C", "Terre"), 3);
		map.put(new QuidAnswerDTO("D", "Air"), 0);
		QuidAnswerDTO result = OccurencesSearcher.getMostFrequentAnswer(map);
		assertEquals("B", result.getLabel());

		map = new HashMap<>();
		map.put(new QuidAnswerDTO("A", "AA"), 0);
		map.put(new QuidAnswerDTO("B", "AA"), 1);
		map.put(new QuidAnswerDTO("C", "BB"), 0);
		map.put(new QuidAnswerDTO("D", "BB"), 2);
		result = OccurencesSearcher.getMostFrequentAnswer(map);
		assertEquals("D", result.getLabel());

	}

}
