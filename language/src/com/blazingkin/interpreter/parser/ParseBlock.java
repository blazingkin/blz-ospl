package com.blazingkin.interpreter.parser;

import java.util.ArrayList;
import java.util.stream.Stream;

public class ParseBlock {

    private String header;
    private ArrayList<Either<String, ParseBlock>> lines;

    public ParseBlock(String header){
        this.header = header;
        lines = new ArrayList<Either<String, ParseBlock>>();
    }

    public ParseBlock(String header, ArrayList<Either<String, ParseBlock>> lines) {
        this.header = header;
        this.lines = lines;
    }

    public void addLine(Either<String, ParseBlock> line){
        lines.add(line);
    }

    public String getHeader(){
        return header;
    }

    public ArrayList<Either<String, ParseBlock>> getLines(){
        return lines;
    }

    public Stream<Either<String, ParseBlock>> streamLines(){
        return lines.stream();
    }

}