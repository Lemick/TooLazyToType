package file;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class ResourceFile {

	private File file;
	
	public ResourceFile(String resourceName) {
		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource(resourceName);
		file = new File(resource.getPath()); 
	}

	public String getContent() throws IOException {
		return FileUtils.readFileToString(file);
	}
	
	public List<String> getLines() {
		try {
			return FileUtils.readLines(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
