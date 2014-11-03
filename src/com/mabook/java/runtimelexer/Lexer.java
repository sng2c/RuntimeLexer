package com.mabook.java.runtimelexer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

import com.mabook.java.runtimelexer.Rule.OnMatchListener;
import com.mabook.java.runtimelexer.RuleSet.Context;
import com.mabook.java.runtimelexer.RuleSet.Flag;
import com.mabook.java.runtimelexer.RuleSet.State;

public class Lexer {
	public static class NotExpectedTokenException extends RuntimeException {
		private static final long serialVersionUID = 448875726851395716L;
		private String message;

		public NotExpectedTokenException(CharSequence text) {
			super();
			if (text.length() > 10) {
				this.message = "\n\"" + text.subSequence(0, 10)
						+ "...\n ~ <- Not Expected";
			} else {
				this.message = "\n\"" + text + "\"\n ~ <- Not Expected";
			}
		}

		@Override
		public String getMessage() {
			return message;
		}
	}

	public static Rule AUTOSKIP = new Rule("AUTOSKIP");

	private RuleSet ruleSet;

	private OnMatchListener listener = null;
	
	public Lexer(RuleSet ruleSet) {
		this.setRuleSet(ruleSet);
	}
	
	public Lexer(RuleSet ruleSet, OnMatchListener listener) {
		this.setRuleSet(ruleSet);
		this.setListener(listener);
	}
	
	boolean stateChanged = true;
	private ArrayList<State> stateStack = new ArrayList<State>();
	private HashSet<Flag> flagSet = new HashSet<Flag>();

	public Set<Flag> getFlags() {
		return this.flagSet;
	}

	public void reset() {
		stateChanged = true;
		this.stateStack.clear();
		this.stateStack.add(RuleSet.DEFAULT);
		this.flagSet.clear();
	}

	public void pushState(State state) {
		stateChanged = true;
		this.stateStack.add(state);
	}

	public State popState() {
		stateChanged = true;
		return this.stateStack.remove(this.stateStack.size() - 1);
	}

	public State getState() {
		return this.stateStack.get(this.stateStack.size() - 1);
	}

	public List<State> getStateStack() {
		return this.stateStack;
	}

	public void setFlag(Flag flag) {
		stateChanged = true;
		this.flagSet.add(flag);
	}

	public void resetFlag(Flag flag) {
		stateChanged = true;
		this.flagSet.remove(flag);
	}

	ArrayList<Rule> curRules = new ArrayList<Rule>();

	public List<Rule> getCurrentRules() {
		if (stateChanged) {
			curRules.clear();
			ArrayList<Context> contexts = new ArrayList<RuleSet.Context>();

			contexts.add(this.getState());
			contexts.addAll(this.getFlags());

			Collections.sort(contexts);

			for (Context ctx : contexts) {
				List<Rule> rules = getRuleSet().getRules(ctx);
				if (rules != null)
					curRules.addAll(rules);
			}
			stateChanged = false;
		}
		return curRules;
	}

	public void lex(CharSequence text) {
		lex2(text, this.listener);
	}
	
	public void lex(CharSequence text, OnMatchListener listener) {
		reset();
		while (text.length() != 0) {
			List<Rule> rules = getCurrentRules();
			boolean matched = false;

			CharSequence subText = text;
			for (Rule rule : rules) {
				ArrayList<Token> tokens = rule.getTokens();
				StringBuilder sb = new StringBuilder();

				boolean subMatched = true;
				for (Token t : tokens) {
					Matcher matcher = t.getPattern().matcher(subText);
					if (matcher.find()) {
						sb.append(matcher.group());
						subText = subText.subSequence(matcher.end(),
								subText.length());
					} else {
						subMatched = false;
						subText = text;
						break;
					}
				}

				if (subMatched) {
					matched = true;
					OnMatchListener ruleListener = rule.getListener();
					MatchResult result = new MatchResult(rule, sb);
					if (ruleListener != null) {
						result = ruleListener.onMatch(this, result);
					}
					if (listener != null) {
						result = listener.onMatch(this, result);
					}
					text = subText;
					break;
				}

			}

			if (!matched) {
				if (ruleSet.useAutoSkip()) {
					if (listener != null) {
						MatchResult result = new MatchResult(AUTOSKIP,
								subText.subSequence(0, 1));
						result = listener.onMatch(this, result);
					}
					text = subText.subSequence(1, subText.length());
				} else {
					throw new NotExpectedTokenException(text);
				}
			}
		}
	}

	
	public static class Bench{
		long count = 0;
		long last = -1;
		long sum = 0;
		String name = null;
		public Bench(String name){
			this.name = name;
			last = System.currentTimeMillis();
		}
		
		public void begin(){
			last = System.currentTimeMillis();
		}
		public void end(){
			sum += System.currentTimeMillis() - last;
			count++;
		}
		
		@Override
		public String toString(){
			if(count == 0){
				return null;
			}
			return String.format("[%s] total: %dmsec, avr: %fmsec, count: %d", this.name, sum, (double)sum / (double)count, count);
		}
	}
	
	
	public void lex2(CharSequence text, OnMatchListener listener) {
		reset();
		
		while (text.length() != 0) {
			List<Rule> rules = getCurrentRules();
			boolean matched = false;

			CharSequence subText = text;
			for (Rule rule : rules) {
				ArrayList<Token> tokens = rule.getTokens();
				StringBuilder sb = new StringBuilder();

				
				boolean subMatched = true;
				for (Token t : tokens) {
					Matcher matcher = t.getPattern().matcher(subText);
					
					if (matcher.find()) {
						sb.append(matcher.group());
						subText = subText.subSequence(matcher.end(),
								subText.length());
					} else {
						subMatched = false;
						subText = text;
						break;
					}
				}

				if (subMatched) {
					matched = true;
					OnMatchListener ruleListener = rule.getListener();
					MatchResult result = new MatchResult(rule, sb);
					if (ruleListener != null) {
						result = ruleListener.onMatch(this, result);
					}
					if (listener != null) {
						result = listener.onMatch(this, result);
					}
					text = subText;
					break;
				}

			}
			if (!matched) {
				if (ruleSet.useAutoSkip()) {
					if (listener != null) {
						MatchResult result = new MatchResult(AUTOSKIP,
								subText.subSequence(0, 1));
						result = listener.onMatch(this, result);
					}
					text = subText.subSequence(1, subText.length());
				} else {
					throw new NotExpectedTokenException(text);
				}
			}
		}
	}
	
	public RuleSet getRuleSet() {
		return ruleSet;
	}

	public void setRuleSet(RuleSet ruleSet) {
		this.ruleSet = ruleSet;
	}

	public OnMatchListener getListener() {
		return listener;
	}

	public void setListener(OnMatchListener listener) {
		this.listener = listener;
	}

}
