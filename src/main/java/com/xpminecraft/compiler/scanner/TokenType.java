package com.xpminecraft.compiler.scanner;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;

/**
 * Created by Minecraft on 2017/1/16.
 */
public  class TokenType {
    private String name;
    private RegExp regExp;

    public TokenType(String name,RegExp regExp)
    {
        this.name = name;
        this.regExp = regExp;
    }
    public Automaton getAutomaton()
    {
        return regExp.toAutomaton();
    }

    public String getName()
    {
        return name;
    }
}
