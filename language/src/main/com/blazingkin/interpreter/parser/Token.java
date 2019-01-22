package com.blazingkin.interpreter.parser;

import com.blazingkin.interpreter.expressionabstraction.Operator;

public class Token {
    public Operator op;
    public String meta = "";
    public Token(Operator op){
        this.op = op;
    }
    public Token(Operator op, String meta){
        this.op = op;
        this.meta = meta;
    }

    public String toString(){
        return op.toString() + " (" + meta + ")";
    }
    public boolean equals(Object other){
        if (other instanceof Token){
            Token oth = (Token) other;
            return this.op.equals(oth.op) && this.meta.equals(oth.meta);
        }
        return false;
    }
}