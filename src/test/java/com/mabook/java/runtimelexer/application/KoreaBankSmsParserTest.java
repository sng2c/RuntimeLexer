package com.mabook.java.runtimelexer.application;

import com.mabook.java.runtimelexer.*;
import com.mabook.java.runtimelexer.Lexer.NotExpectedTokenException;
import com.mabook.java.runtimelexer.Rule.OnMatchListener;
import com.mabook.java.runtimelexer.RuleSet.State;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class KoreaBankSmsParserTest {

	@Test
	public void test() {
		OnMatchListener listener = new OnMatchListener() {
			@Override
			public MatchResult onMatch(Lexer lexer, MatchResult result) {
				if( result.getRule() != Lexer.AUTOSKIP ) {
					System.out.println(result.getRule().getName() + ":" + result.getTokenString());
				}
				return result;
			}
		};
		KoreaBankSmsParser.parse("국민은행 2000원",listener);
		System.out.println("");
		KoreaBankSmsParser.parse("[Web발신]\n" +
				"하나체크카드(2*7*)김*승님 04/05 12:00 USD16.47/해외승인/ GOOGLE *SQUARE ENIX", listener);
		System.out.println("");
		KoreaBankSmsParser.parse("[Web발신]\n" +
				"하나(6*1*)김*승님 04/05 21:13 페리카나 일시불/17,000원/누적1,387,626원", listener);
	}
}
