package com.mabook.java.runtimelexer;

import java.util.List;
import java.util.regex.Matcher;

public class MatchResult {

	private Rule rule = null;
	private CharSequence tokenString = null;
	private List<TokenMatcher> tokenMatchers = null;
	
	public static class TokenMatcher{
		private Token token;
		private Matcher matcher;
		public TokenMatcher(Token token, Matcher matcher){
			this.token = token;
			this.matcher = matcher;
		}
		public Token getToken() {
			return token;
		}
		public void setToken(Token token) {
			this.token = token;
		}
		public Matcher getMatcher() {
			return matcher;
		}
		public void setMatcher(Matcher matcher) {
			this.matcher = matcher;
		}
	}
	
	public MatchResult(Rule rule, CharSequence tokenString, List<TokenMatcher> tokenMatchers) {
		this.setRule(rule);
		this.tokenString = tokenString;
		this.setTokenMatchers(tokenMatchers);
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

	public List<TokenMatcher> getTokenMatchers() {
		return tokenMatchers;
	}

	public void setTokenMatchers(List<TokenMatcher> tokenMatchers) {
		this.tokenMatchers = tokenMatchers;
	}


}