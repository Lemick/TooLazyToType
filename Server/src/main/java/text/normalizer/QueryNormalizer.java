package text.normalizer;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import file.ResourceFile;

public class QueryNormalizer {

	private static final String STOP_WORDS_FILENAME = "normalizer/stop_words_fr.txt";

	private List<String> stopWords;

	public QueryNormalizer() {
		loadStopWordsFile();
	}

	private void loadStopWordsFile() {
		ResourceFile file = new ResourceFile(STOP_WORDS_FILENAME);
		stopWords = file.getLines();
	}

	public static String removeDiacritics(String text) {
		return Normalizer.normalize(text, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}

	public static String removeDoubleQuotes(String text) {
		return text.replace("\"", "");
	}
	
	public String removeLeadingStopWords(String text) {
		List<String> tokens = new ArrayList<String>(Arrays.asList(text.toLowerCase().split("\\s+|'")));
		Iterator<String> tokensIter = tokens.iterator();

		while (tokensIter.hasNext()) {
			String token = tokensIter.next();
			if (isStopWord(token))
				tokensIter.remove();
			else
				break;
		}

		String result = String.join(" ", tokens);
		return result;
	}

	public boolean isStopWord(String word) {
		word = word.toLowerCase();
		for (String stopWord : stopWords) {
			if (word.equals(stopWord))
				return true;
		}
		return false;
	}
}
