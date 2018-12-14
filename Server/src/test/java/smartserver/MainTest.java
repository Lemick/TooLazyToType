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
		testDlJsoup("https://www.google.com/search?q=Quel+est+en+2018+la+part+des+taxes+dans+le+prix+du+carburant+automobile+%3F");
	
		
		Question question = new Question();
		question.setTitle("Quel est en 2018 la part des taxes dans le prix du carburant automobile ?");
		List<Answer> answers = new ArrayList<Answer>();

		/**
		answers.add(new Answer("A", "notre-dame de paris"));
		answers.add(new Answer("B", "miserables"));
		answers.add(new Answer("C", "bonheur des dames"));
		answers.add(new Answer("D", "jour d'un condamne"));
**/
		
		answers.add(new Answer("A", "50%"));
		answers.add(new Answer("B", "60%"));
		answers.add(new Answer("C", "70%"));
		answers.add(new Answer("D", "80%"));
		
		
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
