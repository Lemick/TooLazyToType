package com.tltt.lib.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class ResourceFile {

	private File file;
	
	public File getFile() {
		return file;
	}

	public ResourceFile(String resourceName) {
		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource(resourceName);
		file = new File(resource.getPath()); 
	}

	public String getContent() {
		try {
			return FileUtils.readFileToString(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public InputStream getInputStream() {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
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
