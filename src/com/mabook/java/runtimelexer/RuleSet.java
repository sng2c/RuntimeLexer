package com.mabook.java.runtimelexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RuleSet {

	public static class Context implements Comparable<Context> {
		public static float PRIORITY_HIGH = 0f;
		public static float PRIORITY_MEDIUM = 128f;
		public static float PRIORITY_LOW = 255f;

		private float priority = PRIORITY_MEDIUM;

		protected String name;

		public Context(String name) {
			this.name = name;
		}

		public Context(String name, float priority) {
			this.name = name;
			this.priority = priority;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "Context(" + this.name + ")";
		}

		@Override
		public int compareTo(Context o) {
			float diff = this.getPriority() - o.getPriority();
			if (diff > 0) {
				return 1;
			} else if (diff < 0) {
				return -1;
			} else {
				return 0;
			}
		}

		public float getPriority() {
			return priority;
		}

		public void setPriority(float priority) {
			this.priority = priority;
		}
	}

	public static class State extends Context {
		public State(String name) {
			super(name);
		}

		public State(String name, float priority) {
			super(name, priority);
		}

		@Override
		public String toString() {
			return "State(" + this.name + ")";
		}
	}

	public static class Flag extends Context {

		public Flag(String name) {
			super(name);
		}

		public Flag(String name, float priority) {
			super(name, priority);
		}

		@Override
		public String toString() {
			return "Flag(" + this.name + ")";
		}
	}

	public static State DEFAULT = new State("DEFAULT");

	private HashMap<Context, ArrayList<Rule>> ruleMap = new HashMap<Context, ArrayList<Rule>>();

	public RuleSet() {
	}

	public List<Rule> getRules(Context context) {
		return this.ruleMap.get(context);
	}

	public RuleSet appendRule(Context context, Rule rule) {
		if (!ruleMap.containsKey(context)) {
			ruleMap.put(context, new ArrayList<Rule>());
		}
		ruleMap.get(context).add(rule);
		return this;
	}

	public RuleSet appendRule(Rule rule) {
		return this.appendRule(DEFAULT, rule);
	}

}
