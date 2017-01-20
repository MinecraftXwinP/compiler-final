package com.xpminecraft.compiler.scanner;

import dk.brics.automaton.Automaton;

import java.util.LinkedList;
import java.util.List;

public class Scanner {
    private LinkedList<TokenType> tokenTypes;
    private String source;
    private int lastEnd = 0;
    public void scan() {
        // tokenType  <=>  automaton
        TokenType[] typesToScan = tokenTypes.toArray(new TokenType[0]);
        List<TrackPair> trackList;

        while(lastEnd < source.length())
        {
            trackList = new LinkedList<>();
            // init trackList
            for(int i=0;i < typesToScan.length;i++)
            {
                trackList.add(new TrackPair(typesToScan[i]));
            }
            boolean matched = false; // true if any automata change state
            for(int cursor=lastEnd;cursor < source.length();cursor++)
            {
                char label = source.charAt(cursor);
                //remove useless error pairs
                trackList.removeIf((pair)->pair.isError());
                // update automata states
                for(TrackPair pair : trackList)
                {
                    pair.step(label);
                }
                // check match
                int notError = 0;
                for(TrackPair pair : trackList)
                {
                    if(!pair.isError())
                        notError++;
                }
                // all error, check previous matches
                if(notError == 0)
                {
                    for(TrackPair pair : trackList)
                    {
                        if(pair.isPreviousAccept())
                        {
                            foundToken(pair,source.substring(lastEnd,cursor));
                            lastEnd = cursor;
                            matched = true;
                            break;
                        }
                    }
                }
            }
            // still no match , advanced start point
            if(!matched)
                lastEnd++;
        }

    }

    public List<Automaton> getAutomata()
    {
        List<Automaton> retAutomatonList = new LinkedList<>();
        tokenTypes.forEach((t) -> retAutomatonList.add(t.getAutomaton()));
        return retAutomatonList;
    }

    private void foundToken(TrackPair pair,String lexeme)
    {
        //System.out.println("Found: " + lexeme + " <" + pair.getTokenType().getName() + ">");
        if(pair.getTokenType() instanceof Action)
        {
            ((Action) pair.getTokenType()).accept(lexeme);
        }
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
