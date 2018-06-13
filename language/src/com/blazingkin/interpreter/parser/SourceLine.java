package com.blazingkin.interpreter.parser;

public class SourceLine {
    public SourceLine(String line, int lineNumber){
        this.line = line;
        this.lineNumber = lineNumber;
    }
    public String line;
    public int lineNumber;

    public boolean equals(Object other){
        if (!(other instanceof SourceLine)){
            return false;
        }
        SourceLine otherLine = (SourceLine) other;
        return this.lineNumber == otherLine.lineNumber && this.line.equals(otherLine.line);
    }

}