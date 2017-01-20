package com.xpminecraft.compiler.scanner.client;


import com.xpminecraft.compiler.scanner.Scanner;
import com.xpminecraft.compiler.scanner.TokenType;
import com.xpminecraft.compiler.scanner.tokens.CaseInsensitiveKeywordTokenType;
import com.xpminecraft.compiler.scanner.tokens.CounterTokenType;
import dk.brics.automaton.RegExp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args)
    {
        if (args.length == 0)
            System.exit(0);
        File file = new File(args[0]);
        try(InputStreamReader in = new InputStreamReader(new FileInputStream(file)))
        {
            StringBuffer buffer = new StringBuffer();
            for(int c;(c = in.read()) != -1;)
            {
                buffer.append((char)c);
            }
            System.out.println(buffer.toString());
            //Visualizer visualizer = new Visualizer();
            Scanner scanner = new Scanner(buffer.toString());
            List<TokenType> tokenTypes = new LinkedList<>();
            tokenTypes.add(new CounterTokenType("library name",new RegExp("\\<[^>]*.h\\>")));
            tokenTypes.add(new CounterTokenType("comment",new RegExp("/\\*([^*]|\\*+[^*/])*\\*+/|//[^(\n|\r\n)]*")));
            tokenTypes.add(new CounterTokenType("operator",new RegExp("+|-|*|/|%|^|++|--|&|\\||=")));
//
//                    tokenTypes.add(new CounterTokenType("reserved keywords",
//                new RegExp("include|main|char|int|float|if|" +
//                        "else|elseif|for|while|do|return|switch|case|printf|scanf")));
            tokenTypes.add(new CaseInsensitiveKeywordTokenType());
            tokenTypes.add(new CounterTokenType("pointer", new RegExp("*[a-zA-Z]+[_0-9a-zA-Z]*")));
            tokenTypes.add(new CounterTokenType("printed token",new RegExp("\\\"[^\\\"]*\\\"")));
            tokenTypes.add(new TokenType("blank",new RegExp("\n|\r\n|\t")));
            tokenTypes.add(new CounterTokenType("puntuation",new RegExp("\\#|,|;|:|\\\"|'")));
            tokenTypes.add(new CounterTokenType("constant",new RegExp("-?[0-9]+\\.?[0-9]*")));
            tokenTypes.add(new CounterTokenType("address",new RegExp("&[a-zA-Z]+[_0-9a-zA-Z]*")));
            tokenTypes.add(new CounterTokenType("format specifier",new RegExp("%d|%f|%c|\\\\\\*")));

            tokenTypes.add(new CounterTokenType("identifier",new RegExp("[a-zA-Z]+[_0-9a-zA-Z]*")));
            tokenTypes.add(new CounterTokenType("bracket",new RegExp("\\(|\\)|\\[|\\]|\\{|\\}")));
            tokenTypes.add(new CounterTokenType("comparator",new RegExp("==|\\<|\\>|\\<=|\\>=|!=")));

            tokenTypes.forEach((type) -> scanner.addToken(type));
            // visualizer.updateAutomata(scanner.getAutomata());
            // visualizer.drawAutomata();
            scanner.scan();
//            tokenTypes.forEach((t) -> {
//                if(t instanceof CounterTokenType)
//                    System.out.println(t);
//            });
            int sum = 0;
            for(TokenType t : tokenTypes)
            {
                if(t instanceof CounterTokenType)
                {
                    sum += ((CounterTokenType) t).getCount();
                    System.out.println(t);
                }
            }
            System.out.println("Total :" + sum);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
