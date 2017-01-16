package com.xpminecraft.compiler.scanner;


import dk.brics.automaton.RegExp;

import java.io.*;

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
            scanner.addToken(new TokenType("reserved keywords",new RegExp("include|for")));
            //scanner.addToken(new TokenType("library name",new RegExp("<.*>")));
            scanner.scan();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
