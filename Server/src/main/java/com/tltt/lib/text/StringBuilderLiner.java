package com.tltt.lib.text;

public class StringBuilderLiner {

	private StringBuilder sb;

	public StringBuilder getSb() {
		return sb;
	}

	public StringBuilderLiner() {
		sb = new StringBuilder();
	}

	public void append(String string) {
		sb.append(string);
	}

	public void appendLine(String string) {
		sb.append(string).append("\n");
	}

	public String toString() {
		return sb.toString();
	}
}
