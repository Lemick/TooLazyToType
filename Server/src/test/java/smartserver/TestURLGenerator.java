package smartserver;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.tltt.lib.URLGenerator;
import com.tltt.lib.question.Question;

public class TestURLGenerator {

	@Test
	public void testBuild() {
		String actual;
		Question question = new Question();
		URLGenerator urlGenerator;
		
		question.setTitle("Aujourd'hui, quel est le meilleur groupe au monde ?");
		urlGenerator = new URLGenerator(question);
		actual = urlGenerator.getUrlQuery();
		assertEquals(actual, "https://www.google.com/search?q=Aujourd%27hui%2C+quel+est+le+meilleur+groupe+au+monde+%3F&tbm=nws");
		
		question.setTitle("Qui a inventé le moteur ?");
		urlGenerator = new URLGenerator(question);
		actual = urlGenerator.getUrlQuery();
		assertEquals(actual, "https://www.google.com/search?q=Qui+a+invent%C3%A9+le+moteur+%3F");
	}
}
