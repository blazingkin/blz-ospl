package com.blazingkin.interpreter.parser;

import java.util.Optional;

/* Why is this not standard library java? */
public class Either<L, R> {
    private L left;
    private R right;
    private boolean isLeft;

    private Either(L left, R right, boolean isLeft){
        this.left = left;
        this.right = right;
        this.isLeft = isLeft;
    }

    public static <L,R> Either<L,R> left(L left){
        return new Either<L,R>(left, null, true);
    }

    public static <L,R> Either<L,R> right(R right){
        return new Either<L,R>(null, right, false);
    }

    public boolean isLeft() {
        return isLeft;
    }

    public boolean isRight(){
        return !isLeft;
    }

    public Optional<L> getLeft(){
        if (isLeft){
            return Optional.of(left);
        }
        return Optional.empty();
    }

    public Optional<R> getRight(){
        if (!isLeft){
            return Optional.of(right);
        }
        return Optional.empty();
    }

    public String toString() {
        String sideString = isLeft ? "L" : "R";
        Class type = isLeft ? left.getClass() : right.getClass();
        String value = isLeft ? left.toString() : right.toString();
        return "<Either " +  sideString + " " + type.getSimpleName()  + " " + value + ">";
    }


}