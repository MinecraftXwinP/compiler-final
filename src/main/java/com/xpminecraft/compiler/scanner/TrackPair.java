package com.xpminecraft.compiler.scanner;

import dk.brics.automaton.RunAutomaton;

public class TrackPair {
    private int current;
    private int previous;
    private boolean error = false;
    private TokenType tokenType;
    private RunAutomaton automaton;
    public TrackPair(TokenType tokenType)
    {
        this.tokenType = tokenType;
        this.automaton = tokenType.getAutomaton();
        setState(automaton.getInitialState());
    }
    public void setState(int i)
    {
        previous = current;
        current = i;
        if(i == -1)
            error = true;
    }
    public void step(char label)
    {
        if(isError()) return;
        setState(automaton.step(current,label));
    }

    public boolean isCurrentAccept()
    {
        return !isError() && automaton.isAccept(current);
    }

    public boolean isPreviousAccept()
    {
        return automaton.isAccept(previous);
    }

    public boolean isError()
    {
        return error;
    }

    public TokenType getTokenType()
    {
        return tokenType;
    }
}
