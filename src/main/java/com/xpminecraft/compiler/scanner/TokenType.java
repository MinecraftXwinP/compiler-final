package com.xpminecraft.compiler.scanner;

import dk.brics.automaton.RegExp;
import dk.brics.automaton.RunAutomaton;

/**
 * Created by Minecraft on 2017/1/16.
 */
public  class TokenType {
    private String name;
    private RegExp regExp;

    public TokenType(String name,RegExp regExp)
    {
        this.regExp = regExp;
    }
    public RunAutomaton getAutomaton()
    {
        return new RunAutomaton(regExp.toAutomaton());
    }
}
