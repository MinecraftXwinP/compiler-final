package com.xpminecraft.compiler.scanner;

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
                // update automata states
                /*
                for(int i=0;i < typesToScan.length;i++)
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
                */
                int matches = 0;
                TrackPair lastMatch = null;
                for(TrackPair pair : trackList)
                {
                    pair.step(label);
                    if(pair.isCurrentAccept())
                    {
                        matches++;
                        lastMatch = pair;
                    }

                }
                // check match
                if(matches == 1)
                {
                    foundToken(lastMatch,source.substring(lastEnd,cursor + 1));
                    matched = true;
                    lastEnd = cursor + 1;
                    break;
                } else if (matches == 0)
                {
                    //check whether previous run has match
                    for (TrackPair pair: trackList)
                    {
                        if(pair.isPreviousAccept())
                        {
                            foundToken(pair,source.substring(lastEnd,cursor));
                            matched = true;
                            lastEnd = cursor;
                            break;
                        }
                    }
                    // remove useless error pairs
                    trackList.removeIf((pair)->pair.isError());
                }
            }
            // still no match , advanced start point
            if(!matched)
                lastEnd++;
        }

    }

    private void foundToken(TrackPair pair,String lexeme)
    {
        System.out.println("Found: " + lexeme + " <" + pair.getTokenType().getName() + ">");
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
