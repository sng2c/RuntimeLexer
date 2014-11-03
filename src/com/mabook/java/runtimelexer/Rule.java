package com.mabook.java.runtimelexer;

import java.util.ArrayList;

public class Rule {
	public static interface OnMatchListener {
		MatchResult onMatch(Lexer lexer, MatchResult result);
	}

	private ArrayList<Token> tokens = new ArrayList<Token>();
	private OnMatchListener listener;
	private String name;

	public Rule(String name, Object... tokens) {
		this.setName(name);
		for (Object obj : tokens) {
			if (obj instanceof Token) {
				this.tokens.add((Token) obj);
			} else if (obj instanceof OnMatchListener) {
				this.listener = (OnMatchListener) obj;
			} else if (obj instanceof String) {
				this.tokens.add(new Token("Pattern(\"" + (String) obj + "\")",
						(String) obj));
			}
		}
	}

	public OnMatchListener getListener() {
		return listener;
	}

	public void setListener(OnMatchListener listener) {
		this.listener = listener;
	}

	public ArrayList<Token> getTokens() {
		return tokens;
	}

	public void setTokens(ArrayList<Token> tokens) {
		this.tokens = tokens;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		for (Token t : tokens) {
			if (sb.length() == 0) {
				sb.append(t);
			} else {
				sb.append("+").append(t);
			}

		}
		return "Rule(" + this.name + " " + sb.toString() + ")";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}