package com.mabook.java.runtimelexer.application;

import com.mabook.java.runtimelexer.*;

/**
 * Created by sng2c on 15. 4. 7..
 */
public class KoreaBankSmsParser {

    static RuleSet ruleSet;
    static Lexer lexer;
    static {
        ruleSet = new RuleSet();
        ruleSet.appendRule(new Rule("HEADER","\\[Web발신\\]"));
        ruleSet.appendRule(new Rule("HEADER","\\(Web발신\\)"));
        ruleSet.appendRule(new Rule("MONEY_WON_TOTAL","누적\\s*([\\d,]+)원"));
        ruleSet.appendRule(new Rule("MONEY","([\\d,]+)원"));
        ruleSet.appendRule(new Rule("MONEY","USD([\\d,\\.]+)"));
        ruleSet.appendRule(new Rule("CARD","\\S+\\([\\d\\*]{4}\\)"));
        ruleSet.appendRule(new Rule("BANK","\\S+은행"));
        ruleSet.appendRule(new Rule("CARD","\\S+카드"));
        ruleSet.appendRule(new Rule("DATE","\\d\\d/\\d\\d"));
        ruleSet.appendRule(new Rule("TIME","\\d\\d:\\d\\d"));
        ruleSet.appendRule(new Rule("WORD","[^/\\s]+"));
        lexer = new Lexer(ruleSet);
    }

    public static void parse(String sms, Rule.OnMatchListener listener){
        lexer.lex(sms, listener);
    }
}
