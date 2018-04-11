package com.blazingkin.interpreter.parser;

import java.util.ArrayList;

public class SplitStream<T>{

    private int index = 0;
    private Either<T[], ArrayList<T>> source;
    public SplitStream(T[] arr){
        source = Either.left(arr);
    }
    public SplitStream(ArrayList<T> arr){
        source = Either.right(arr);
    }

    public boolean hasNext(){
        if (source.isLeft()){
            T[] left = source.getLeft().get();
            return index < left.length;
        }else{
            ArrayList<T> right = source.getRight().get();
            return index < right.size();
        }
    }

    public T next(){
        if (source.isLeft()){
            T[] left = source.getLeft().get();
            return left[index++];
        }else{
            ArrayList<T> right = source.getRight().get();
            return right.get(index++);
        }
    }

}