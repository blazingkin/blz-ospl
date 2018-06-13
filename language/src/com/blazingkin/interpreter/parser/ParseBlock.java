package com.blazingkin.interpreter.parser;

import java.util.ArrayList;
import java.util.stream.Stream;

public class ParseBlock {

    private String header;
    private ArrayList<Either<SourceLine, ParseBlock>> lines;

    public ParseBlock(String header){
        this.header = header;
        lines = new ArrayList<Either<SourceLine, ParseBlock>>();
    }

    public ParseBlock(String header, ArrayList<Either<SourceLine, ParseBlock>> lines) {
        this.header = header;
        this.lines = lines;
    }

    public void addLine(Either<SourceLine, ParseBlock> line){
        lines.add(line);
    }

    public String getHeader(){
        return header;
    }

    public ArrayList<Either<SourceLine, ParseBlock>> getLines(){
        return lines;
    }

    public Stream<Either<SourceLine, ParseBlock>> streamLines(){
        return lines.stream();
    }

}