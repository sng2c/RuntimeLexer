package com.mabook.java.runtimelexer;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Assert;



import com.mabook.java.runtimelexer.Lexer.NotExpectedTokenException;
import com.mabook.java.runtimelexer.Rule.OnMatchListener;
import com.mabook.java.runtimelexer.RuleSet.State;

public class LexerTest {

	@Test
	public void testLexSimple() {
		RuleSet ruleSet = new RuleSet();
		ruleSet.appendRule(new Rule("WS", "\\s+"));
		ruleSet.appendRule(new Rule("NUMBER", "\\d+"));
		ruleSet.appendRule(new Rule("ALPHA", "(?i)[a-z]+"));
		
		OnMatchListener listener = new OnMatchListener() {
			int count = 0;
			@Override
			public MatchResult onMatch(Lexer lexer, MatchResult result) {
				switch (this.count) {
				case 0:
					assertEquals("ALPHA", result.getRule().getName());
					assertEquals("ABC",result.getTokenString().toString());
					break;
				case 1:
					assertEquals("WS", result.getRule().getName());
					assertEquals(" ",result.getTokenString().toString());
					break;
				case 2:
					assertEquals("NUMBER", result.getRule().getName());
					assertEquals("123",result.getTokenString().toString());
					break;
				case 3:
					assertEquals("WS", result.getRule().getName());
					assertEquals(" ",result.getTokenString().toString());
					break;
				case 4:
					assertEquals("AUTOSKIP", result.getRule().getName());
					assertEquals("!",result.getTokenString().toString());
					break;
				case 5:
					assertEquals("AUTOSKIP", result.getRule().getName());
					assertEquals("@",result.getTokenString().toString());
					break;
				case 6:
					assertEquals("AUTOSKIP", result.getRule().getName());
					assertEquals("#",result.getTokenString().toString());
					break;
				default:
					break;
				}
				this.count++;
				return result;
			}
		};
		
		Lexer lex = new Lexer(ruleSet);
		lex.lex("ABC 123 !@#",listener);
	}

	
	@Test
	public void testLexSimple2() {
		RuleSet ruleSet = new RuleSet(false);
		ruleSet.appendRule(new Rule("WS", "\\s+"));
		ruleSet.appendRule(new Rule("NUMBER", "\\d+"));
		ruleSet.appendRule(new Rule("ALPHA", "(?i)[a-z]+"));
		
		OnMatchListener listener = new OnMatchListener() {
			int count = 0;
			@Override
			public MatchResult onMatch(Lexer lexer, MatchResult result) {
				switch (this.count) {
				case 0:
					assertEquals("ALPHA", result.getRule().getName());
					break;
				case 1:
					assertEquals("WS", result.getRule().getName());
					break;
				case 2:
					assertEquals("NUMBER", result.getRule().getName());
					break;
				case 3:
					assertEquals("WS", result.getRule().getName());
					break;
				case 4:
					assertEquals("AUTOSKIP", result.getRule().getName());
					break;
				default:
					break;
				}
				this.count++;
				return result;
			}
		};
		
		Lexer lex = new Lexer(ruleSet);
		try{
			lex.lex("ABC 123 !@#",listener);
		}
		catch(NotExpectedTokenException e){
			assertTrue("Got Exception", true);
		}
	}
	
	@Test
	public void testLexSimple3() {
		final State STATE_VALUE = new State("VALUE");
		RuleSet ruleSet = new RuleSet(false);
		ruleSet.appendRule(new Rule("FIELD", new Token("FIELD","(?i)([a-z0-9_]+):\\s*")));
		ruleSet.appendRule(STATE_VALUE, new Rule("VALUE", new Token("VALUE","([^\\n]+)\\n?")));
		
		
		OnMatchListener listener = new OnMatchListener() {
			String expected[] = {"NAME","KHS","AGE","36"};
			int cnt=0;
			@Override
			public MatchResult onMatch(Lexer lexer, MatchResult result) {
				System.out.println(lexer);
				if( "FIELD".equals(result.getRule().getName()) ){
					lexer.pushState(STATE_VALUE);
				}
				else if( "VALUE".equals(result.getRule().getName()) ){
					lexer.popState();
				}
				assertEquals(expected[cnt], result.getTokenMatchers().get(0).getMatcher().group(1));
				cnt++;
				return result;
			}
		};
		
		Lexer lex = new Lexer(ruleSet);
		lex.lex("NAME: KHS\nAGE: 36\n",listener);
	}
}
