package smartserver;

import static org.junit.Assert.*;

import org.junit.Test;

import com.tltt.lib.URLGenerator;
import com.tltt.lib.dto.QuidQuestionDTO;

public class TestURLGenerator {

	@Test
	public void testBuild() {
		String actual;
		QuidQuestionDTO quidQuestionDTO = new QuidQuestionDTO();
		URLGenerator urlGenerator;
		
		quidQuestionDTO.setQuestionEntitled("Aujourd'hui, quel est le meilleur groupe au monde ?");
		urlGenerator = new URLGenerator(quidQuestionDTO);
		actual = urlGenerator.getUrlQuery();
		assertEquals(actual, "https://www.google.com/search?q=Aujourd%27hui%2C+quel+est+le+meilleur+groupe+au+monde+%3F&tbm=nws");
		
		quidQuestionDTO.setQuestionEntitled("Qui a inventé le moteur ?");
		urlGenerator = new URLGenerator(quidQuestionDTO);
		actual = urlGenerator.getUrlQuery();
		assertEquals(actual, "https://www.google.com/search?q=Qui+a+invent%C3%A9+le+moteur+%3F");
	}
}
