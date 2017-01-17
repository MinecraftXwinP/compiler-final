package com.xpminecraft.compiler.scanner.tokens;


import com.xpminecraft.compiler.scanner.Action;
import com.xpminecraft.compiler.scanner.TokenType;
import dk.brics.automaton.RegExp;

public class CounterTokenType extends TokenType implements Action<String> {
    private int count = 0;
    public CounterTokenType(String name, RegExp regExp)
    {
        super(name,regExp);
    }
    public void accept(String s) {
        System.out.printf("Token %s with length %d belongs to %s%n",s,s.length(),getName());
        count++;
    }
    public int getCount()
    {
        return count;
    }

    public String toString()
    {
        return String.format("%s : %d",getName(),getCount());
    }
}
