package com.xpminecraft.compiler.scanner;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RunAutomaton;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scanner {
    private LinkedList<TokenType> tokenTypes;
    private String source;
    private int lastEnd = 0;
    public void scan() {
        // tokenType  <=>  automaton
        TokenType[] typesToScan = tokenTypes.toArray(new TokenType[0]);
        int[] previousStates;
        int[] states = new int[typesToScan.length];
        RunAutomaton[] automata = new RunAutomaton[typesToScan.length];
        // create automata & init start states
        for(int i=0;i < typesToScan.length;i++)
        {
            RunAutomaton automaton = typesToScan[i].getAutomaton();
            automata[i] = automaton;
        }
        //boolean notFound = false;
        while(lastEnd < source.length())
        {
            // init all start states
            for(int i=0;i < states.length;i++)
            {
                states[i] = automata[i].getInitialState();
            }
            // init previousStates
            previousStates = states;
            boolean advanced = true; // true if any automata change state
            for(int cursor=lastEnd;cursor < source.length() && advanced;cursor++)
            {
                char label = source.charAt(cursor);
                // update automata states
                advanced = false;
                for(int i=0;i < states.length;i++)
                {
                    // ignore error state
                    if(states[i] == -1)
                        continue;
                    int nextState = automata[i].step(states[i],label);
                    if(nextState != -1)
                    {
                        advanced = true;
                    }
                    states[i] = nextState;
                }
                // check match
                int matches = 0;
                for(int i=0;i < states.length;i++)
                {
                    if(states[i] != -1 && automata[i].isAccept(states[i]))
                    {
                        matches++;
                    }
                }

                if(matches > 1)     // multiple matches perform next state
                {
                    // update previousStates and perform next step
                    previousStates = states;
                } else if (matches == 0)
                {
                    // no matches this step,check whether there's a match previously.
                    for(int i=0;i < previousStates.length;i++)
                    {
                        if(previousStates[i] != -1 && automata[i].isAccept(previousStates[i]))
                        {
                            foundToken(source.substring(lastEnd,cursor));
                            lastEnd = cursor;   // advanced start point
                            break;
                        }
                    }
                    // previous state also no match,advance lastEnd
                    lastEnd = lastEnd + 1;
                } else
                {
                    System.out.println("FOUND: " + source.substring(lastEnd,cursor + 1));
                    foundToken(source.substring(lastEnd,cursor + 1));
                    lastEnd = cursor + 1;   // advanced start point
                    break;
                }
            }
        }

    }

    private void foundToken(String lexeme)
    {
        System.out.println("Found: " + lexeme);
    }



    public void addToken(TokenType tokenType)
    {
        tokenTypes.add(tokenType);
    }

    public Scanner(String source)
    {
        tokenTypes = new LinkedList<>();
        this.source = source;
    }
}
