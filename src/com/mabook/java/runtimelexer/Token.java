package com.mabook.java.runtimelexer;

import java.util.regex.Pattern;

public class Token {
	private String name;
	private Pattern pattern;

	public Token(String name, String pattern) {
		this.name = name;
		this.setPattern(Pattern.compile("^(?s)" + pattern));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Token(" + this.name + ")";
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}
}