package com.blazingkin.interpreter.executor.sourcestructures;
import java.util.Optional;

import com.blazingkin.interpreter.parser.SyntaxException;

public class ImportStatement {

    public final String packageName;
    public String packageVersion;
    public Optional<String> alias;

    public ImportStatement(String line) throws SyntaxException {
        String[] tokens = line.split("( |\t)+");

        // If there are no tokens
        if (tokens.length == 0 || (tokens.length == 1 && tokens[0].equals(""))){
            throw new SyntaxException("Import statement was empty!");
        }
        
        packageName = tokens[0];

        for (int i = 1; i < tokens.length; i += 2){
            String keyword = tokens[i].toLowerCase();
            // If we are about to get out of bounds, throw a syntax error
            if (i + 1 == tokens.length) {
                throw new SyntaxException("Invalid import statement. Wrong number of arguments");
            }
            switch(keyword){
                case "tagged":
                    packageVersion = tokens[i + 1];
                break;
                case "as":
                    alias = Optional.of(tokens[i + 1]);
                break;
            }
        }
    }

}