package com.xpminecraft.compiler.scanner;


import dk.brics.automaton.RegExp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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
            Scanner scanner = new Scanner(buffer.toString());
            scanner.addToken(new TokenType("puntuation",new RegExp("\\#")));
            scanner.addToken(new TokenType("reserved keywords",
                                new RegExp("include|main|char|int|float|if|" +
                                            "else|elseif|for|while|do|return|switch|case|printf|scanf")));
            // ==== PROBLEM
            scanner.addToken(new TokenType("pointer", new RegExp("*[a-zA-Z]+.*")));
            scanner.addToken(new TokenType("operator",new RegExp("+|-|*|/|%|^|++|--|&|\\||=")));
            scanner.addToken(new TokenType("library name",new RegExp("\\<.*\\>")));
            scanner.addToken(new TokenType("identifier",new RegExp("[a-zA-Z]+.+")));
            scanner.addToken(new TokenType("comment",new RegExp("/\\*.*/")));
            scanner.scan();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
