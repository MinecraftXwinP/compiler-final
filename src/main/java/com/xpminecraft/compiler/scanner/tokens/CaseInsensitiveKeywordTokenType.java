package com.xpminecraft.compiler.scanner.tokens;

import dk.brics.automaton.Automaton;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Minecraft on 2017/1/17.
 */
public class CaseInsensitiveKeywordTokenType extends CounterTokenType{
    public static final String[] KEYWORDS = {
      "include","main","char","int","float","if","else","elseif","for","while",
            "do","return","switch","case","printf","scanf"
    };
    public CaseInsensitiveKeywordTokenType()
    {
        super("reserved keyword",null);
    }
    public Automaton getAutomaton()
    {
        List<Automaton> keywords = new LinkedList<>();
        for(int i=0;i < KEYWORDS.length;i++)
        {
            keywords.add(makeCaseInsensitive(KEYWORDS[i]));
        }
        return Automaton.union(keywords);
    }

    public static Automaton makeCaseInsensitive(String name)
    {
        List<Automaton> list = new LinkedList<>();
        for(int i=0;i < name.length();i++)
        {
            char c = name.charAt(i);
            List<Automaton> charAutomata = new LinkedList<>();
            charAutomata.add(Automaton.makeChar(Character.toLowerCase(c)));
            charAutomata.add(Automaton.makeChar(Character.toUpperCase(c)));
            list.add(Automaton.union(charAutomata));
        }
        return Automaton.concatenate(list);
    }
}
