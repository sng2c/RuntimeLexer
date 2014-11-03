package com.mabook.java.runtimelexer;

public class MatchResult {

	private Rule rule = null;
	private CharSequence tokenString = null;

	public MatchResult(Rule rule, CharSequence tokenString) {
		this.setRule(rule);
		this.tokenString = tokenString;
	}

	public CharSequence getTokenString() {
		return tokenString;
	}

	public void setTokenString(CharSequence tokenString) {
		this.tokenString = tokenString;
	}

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

}