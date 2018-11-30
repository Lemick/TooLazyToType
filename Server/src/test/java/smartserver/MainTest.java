package smartserver;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.tltt.lib.QueryNavigator;
import com.tltt.lib.html.HTMLBuildConfiguration;
import com.tltt.lib.html.HTMLExtractor;
import com.tltt.lib.question.Answer;
import com.tltt.lib.question.Question;

public class MainTest {
	
	public static void main(String[] args) throws InterruptedException, IOException, URISyntaxException {
		//testDlJsoup("https://www.google.com/search?q=Parmi+ces+romans%2C+lequel+n%27a+pas+%C3%A9t%C3%A9+%C3%A9crit+par+Victor+Hugo+%3F");
	
		
		Question question = new Question();
		question.setTitle("Parmi ces romans, lequel n'a pas été écrit par Victor Hugo ?");
		List<Answer> answers = new ArrayList<Answer>();

		/**
		answers.add(new Answer("A", "notre-dame de paris"));
		answers.add(new Answer("B", "miserables"));
		answers.add(new Answer("C", "bonheur des dames"));
		answers.add(new Answer("D", "jour d'un condamne"));
**/
		
		answers.add(new Answer("A", "Notre-Dame de Paris"));
		answers.add(new Answer("B", "Les Misérables"));
		answers.add(new Answer("C", "Au Bonheur des Dames"));
		answers.add(new Answer("D", "Le Dernier Jour d'un Condamné"));
		
		
		question.setAnswers(answers);
		
		testOccurences(question);
		
		
	}
	
	private static void testDlJsoup(String url) throws IOException {
		HTMLBuildConfiguration config = new HTMLBuildConfiguration(url).cleanMetaCode(true).removeAccents(true);
		String html = new HTMLExtractor(config).build();
		FileUtils.writeStringToFile(new File("test.txt"), html);
	}
	
	private static void testOccurences(Question question) throws IOException {
		QueryNavigator navigator = new QueryNavigator(question);
		navigator.publishReport(null);
		
	}
}
