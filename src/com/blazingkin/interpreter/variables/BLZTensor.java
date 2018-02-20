package com.blazingkin.interpreter.variables;

import java.util.HashMap;

import com.blazingkin.interpreter.Interpreter;

public class BLZTensor<E> {
	//The key to this HashMap is the index of the value in the tensors as a string
	//e.g. I want element 1,2 in my rank 2 tensor. The key is "1,2,"
	private HashMap<String, E> tensor = new HashMap<String, E>();
	private int rank;
	private int[] sizes;
	public BLZTensor(int length){
		rank = length;
		sizes = new int[rank];
		for (int i = 0; i < sizes.length; i++){
			sizes[i] = -1;
		}
	}
	
	public BLZTensor(int[] sizes){
		rank = sizes.length;
		this.sizes = sizes.clone();
	}
	
	/*public BLZTensor(int[] sizes, E initialValue){
		rank = sizes.length;
		this.sizes = sizes.clone();
	}
	TODO Implement initial values
	*/
	
	public E getTensorEntry(int[] location){
		if (location.length != rank){
			Interpreter.throwError("Wrong number of tensor indices");
		}
		String buildingString = "";
		for (int i : location){
			buildingString += i+",";
		}
		return tensor.get(buildingString);
	}
	
	public void setTensorValue(int[] location, E obj){
		if (location.length != rank){
			Interpreter.throwError("Wrong number of tensor indices");
		}
		String buildingString = "";
		for (int i : location){
			buildingString += i+",";
		}
		tensor.put(buildingString, obj);
	}
	
	public static BLZTensor<Object> tensorProduct(BLZTensor<Object> t1, BLZTensor<Object> t2){
		//TODO implement tensorProduct
		return new BLZTensor<Object>(0);
	}
	
	public int getRank(){
		return rank;
	}
	
	public int[] getSizes(){
		return sizes;
	}
}
