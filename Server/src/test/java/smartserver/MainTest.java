package smartserver;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;

import com.tltt.lib.QueryNavigator;
import com.tltt.lib.html.HTMLBuildConfiguration;
import com.tltt.lib.html.HTMLExtractor;
import com.tltt.lib.question.Question;

public class MainTest {
	
	public static void main(String[] args) throws InterruptedException, IOException, URISyntaxException {
		testDlJsoup("https://www.google.com/search?client=firefox-b-ab&q=racine");
	}
	
	private static void testDlJsoup(String url) throws IOException {
		HTMLBuildConfiguration config = new HTMLBuildConfiguration(url).cleanMetaCode(true).removeAccents(true).subLinksToExplore(1);
		String html = new HTMLExtractor(config).build();
		FileUtils.writeStringToFile(new File("test.txt"), html);
	}
	
	private static void testOccurences(Question question) throws IOException {
		QueryNavigator navigator = new QueryNavigator(question);
		navigator.publishReport(null);
	}
}
