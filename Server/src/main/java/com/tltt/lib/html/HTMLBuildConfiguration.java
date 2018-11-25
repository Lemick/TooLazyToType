package com.tltt.lib.html;

import java.io.IOException;

public class HTMLBuildConfiguration {
	
	private String urlQuery;
	private boolean hasToRemoveAccent;
	private boolean hasToCleanMetaCode;
	private int nbSublinkstoExtract;


	public HTMLBuildConfiguration(String urlQuery) {
		this.urlQuery = urlQuery;
		hasToRemoveAccent = false;
		hasToCleanMetaCode = false;
		nbSublinkstoExtract = 0;
	}
	
	public HTMLBuildConfiguration(HTMLBuildConfiguration configToCopy) {
		this.urlQuery = configToCopy.urlQuery;
		hasToRemoveAccent = configToCopy.hasToRemoveAccent;
		hasToCleanMetaCode = configToCopy.hasToCleanMetaCode;
		nbSublinkstoExtract = configToCopy.nbSublinkstoExtract;
	}
	
	public HTMLBuildConfiguration subLinksToExplore(int sublinkstoExtract) {
		if (sublinkstoExtract < 0)
			throw new IllegalArgumentException();
	
		this.nbSublinkstoExtract = sublinkstoExtract;
		return this;
	}

	public HTMLBuildConfiguration urlQuery(String urlQuery) {
		this.urlQuery = urlQuery;
		return this;
	}

	public HTMLBuildConfiguration removeAccents(boolean hasToRemoveAccent) {
		this.hasToRemoveAccent = hasToRemoveAccent;
		return this;
	}

	public HTMLBuildConfiguration cleanMetaCode(boolean hasToCleanMetaCode) {
		this.hasToCleanMetaCode = hasToCleanMetaCode;
		return this;
	}

	public String getUrlQuery() {
		return urlQuery;
	}

	public boolean hasToRemoveAccent() {
		return hasToRemoveAccent;
	}

	public boolean hasToCleanMetaCode() {
		return hasToCleanMetaCode;
	}

	public int getNbSublinkstoExtract() {
		return nbSublinkstoExtract;
	}

}
