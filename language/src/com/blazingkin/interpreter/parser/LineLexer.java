package com.blazingkin.interpreter.parser;

import java.util.ArrayList;

import com.blazingkin.interpreter.expressionabstraction.Operator;

public class LineLexer {

    public static char translateEscape(char in){
        switch(in){
            case 'n':
            return '\n';
            case 't':
            return '\t';
            case 'b':
            return '\b';
            case 'r':
            return '\r';
            default:
            return in; 
        }
    }

    public static boolean isIdentStart(char c){
        return Character.isAlphabetic(c) || c == '-' || c == '_' || c == ':';
    }

    public static boolean isIdentNonStart(char c){
        return Character.isAlphabetic(c) || Character.isDigit(c) || c == '_' || c == ':' || c == '!' || c == '?';
    }

    public static ArrayList<Token> lexLine(String line) throws SyntaxException{
        StringBuilder buildingMeta = new StringBuilder();
        ArrayList<Token> tokens = new ArrayList<Token>();
        for (int i = 0; i < line.length(); i++){
            switch (line.charAt(i)) {
                case '(':
                    tokens.add(new Token(Operator.parensOpen));
                break;
                case ')':
                    tokens.add(new Token(Operator.parensClose));
                break;
                case '[':
                    tokens.add(new Token(Operator.sqBracketOpen));
                break;
                case ']':
                    tokens.add(new Token(Operator.sqBracketClose));
                break;
                case '{':
                    i++;
                    boolean foundEndBracket = false;
                    for (; i < line.length(); i++){
                        if (line.charAt(i) == '}'){
                            foundEndBracket = true;
                            break;
                        }
                        buildingMeta.append(line.charAt(i));
                    }
                    if (!foundEndBracket) {
                        throw new SyntaxException("Closing } not found");
                    }
                    tokens.add(new Token(Operator.environmentVariableLookup, buildingMeta.toString()));
                    buildingMeta = new StringBuilder();
                break;
                case '<':
                    // Check for <=
                    if (i + 1 < line.length() && line.charAt(i + 1) == '='){
                        i++;
                        tokens.add(new Token(Operator.LessThanEqual));
                    }else{
                        tokens.add(new Token(Operator.LessThan));
                    }
                break;
                case '>':
                    // Check for >=
                    if (i + 1 < line.length() && line.charAt(i + 1) == '='){
                        i++;
                        tokens.add(new Token(Operator.GreaterThanEqual));
                    }else{
                        tokens.add(new Token(Operator.GreaterThan));
                    }
                break;
                case ';':
                    tokens.add(new Token(Operator.ExpressionDelimit));
                break;
                case '|':
                    // Ensure that it is ||
                    if (i + 1 < line.length() && line.charAt(i + 1) == '|'){
                        i++;
                        tokens.add(new Token(Operator.LogicalOr));
                    }else{
                        throw new SyntaxException("Unexpected | in line: "+line);
                    }
                break;
                case '&':
                    // Ensure that it is &&
                    if (i + 1 < line.length() && line.charAt(i + 1) == '&'){
                        i++;
                        tokens.add(new Token(Operator.LogicalAnd));
                    }else{
                        throw new SyntaxException("Unexpected & in line: "+line);
                    }
                break;
                case '=':
                    if (i + 1 < line.length()){
                        char nextChar = line.charAt(i + 1);
                        if (nextChar == '='){
                            i++;
                            tokens.add(new Token(Operator.Comparison));
                            continue;
                        }else if (nextChar == '<'){
                            i++;
                            tokens.add(new Token(Operator.LessThanEqual));
                            continue;
                        }else if (nextChar == '>'){
                            i++;
                            tokens.add(new Token(Operator.GreaterThanEqual));
                            continue;
                        }else if (nextChar == '!'){
                            i++;
                            tokens.add(new Token(Operator.NotEqual));
                            continue;                            
                        }else if (nextChar == '~'){
                            i++;
                            tokens.add(new Token(Operator.ApproximateComparison));
                            continue;
                        }
                    }
                    // This is the default case, every other = will run continue
                    tokens.add(new Token(Operator.Assignment));
                break;
                case '~':
                    if (i + 1 < line.length() && line.charAt(i + 1) == '='){
                        i++;
                        tokens.add(new Token(Operator.ApproximateComparison));
                    }else{
                        throw new SyntaxException("Unexpected ~ in line: "+line);
                    }
                break;
                case '*':
                    if (i + 1 < line.length() && line.charAt(i + 1) == '*'){
                        i++;
                        tokens.add(new Token(Operator.Exponentiation));
                    }else{
                        tokens.add(new Token(Operator.Multiplication));
                    }
                break;
                case '/':
                    tokens.add(new Token(Operator.Division));
                break;
                case '%':
                    tokens.add(new Token(Operator.Modulus));
                break;
                case '_':
                    if (i + 1 < line.length() && line.charAt(i + 1) == '_'){
                        i++;
                        tokens.add(new Token(Operator.Logarithm));
                    }else{
                        throw new SyntaxException("Unexpected _ in line: "+line);
                    }
                break;
                case '!':
                    if (i + 1 < line.length() && line.charAt(i + 1) == '='){
                        i++;
                        tokens.add(new Token(Operator.NotEqual));
                    }else{
                        tokens.add(new Token(Operator.Exclam));
                    }
                break;
                case '+':
                    if (i + 1 < line.length() && line.charAt(i + 1) == '+'){
                        i++;
                        tokens.add(new Token(Operator.Increment));
                    }else{
                        tokens.add(new Token(Operator.Addition));
                    }
                break;
                case '-':
                    if (i + 1 < line.length() && line.charAt(i + 1) == '-'){
                        i++;
                        tokens.add(new Token(Operator.Decrement));
                    }else if (i + 1 < line.length() && line.charAt(i + 1) == '>'){
                        i++;
                        tokens.add(new Token(Operator.Lambda));
                    }else{
                        tokens.add(new Token(Operator.Subtraction));
                    }
                break;
                case '.':
                    tokens.add(new Token(Operator.DotOperator));
                break;
                case ',':
                    tokens.add(new Token(Operator.CommaDelimit));
                break;
                case '"':
                    boolean done = false;
                    boolean foundEnd = false;
                    while (!done){
                        i++;
                        if (i >= line.length()){
                            done = true;
                            continue;
                        } // We didn't find the end
                        char lookingAt = line.charAt(i);
                        if (lookingAt == '"'){
                            done = true;
                            foundEnd = true;
                        }else if (lookingAt == '\\'){
                            i++;
                            if (i < line.length()) {
                                // Translate this
                                buildingMeta.append(translateEscape(line.charAt(i)));
                            }
                        }else{
                            buildingMeta.append(lookingAt);
                        }
                    }
                    tokens.add(new Token(Operator.String, buildingMeta.toString()));
                    if (!foundEnd) {
                        throw new SyntaxException("String not closed on line: "+line);
                    }
                    buildingMeta = new StringBuilder();
                break;
                default:
                    char lookingAt = line.charAt(i);
                    if (Character.isDigit(lookingAt)){
                        buildingMeta.append(lookingAt);
                        while (i + 1 < line.length() && Character.isDigit(line.charAt(i + 1))){
                            buildingMeta.append(line.charAt(++i));
                        }
                        tokens.add(new Token(Operator.Number, buildingMeta.toString()));
                        buildingMeta = new StringBuilder();
                    }else if (isIdentStart(lookingAt)) {
                        buildingMeta.append(lookingAt);
                        while (i + 1 < line.length() && isIdentNonStart(line.charAt(i + 1))) {
                            buildingMeta.append(line.charAt(++i));
                        }
                        tokens.add(new Token(Operator.Ident, buildingMeta.toString()));
                        buildingMeta = new StringBuilder();
                    }else if (Character.isWhitespace(lookingAt)){

                    }else{
                        throw new SyntaxException("Unexpected character "+lookingAt+" in line: "+line);
                    }
            }
        }
        return tokens;
    }


}