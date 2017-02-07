package com.blazingkin.interpreter.variables;

import java.util.Stack;

public class BLZInt {
	private byte[] value; //Most significant byte is in value[0]
	private boolean negative = false;
	public BLZInt(int startingValue){
		value = convertIntToByteArray(startingValue);
		negative = startingValue<0;
	}
	
	public BLZInt(BLZInt otherInt){
		value = otherInt.getValue();
		negative = otherInt.isNegative();
	}
	
	public BLZInt(byte[] val, boolean neg){
		value = val;
		negative = neg;
	}
	
	public static byte[] convertIntToByteArray(int in){
		Stack<Byte> byteArr = new Stack<Byte>();
		while (in > 0){
			byteArr.push((byte)(in % 256));
			in = in >> 8;
		}
		byte arr[] = new byte[byteArr.size()];
		int i = 0;
		while (byteArr.size() > 0){
			arr[i++] = byteArr.pop();
		}
		return arr;
	}
	
	public static int convertBLZIntToJavaInt(BLZInt bi){
		int val = 0;
		for (int i = 0; i < bi.getValue().length; i++){
			val *= 256;
			val += bi.getValue()[i];
		}
		return bi.isNegative()?-1*val:val;
	}
	
	public static BLZInt addBLZInt(BLZInt int1, BLZInt int2){
		if (int1.isNegative() == int2.isNegative()){
			int carry = 0;
			Stack<Integer> in1 = new Stack<Integer>();	//These have top of the stack as least signifigant bit
			Stack<Integer> in2 = new Stack<Integer>();
			for (byte b : int1.value){
				in1.add((int)b);
			}
			for (byte b : int2.value){
				in2.add((int)b);
			}
			Stack<Byte> out = new Stack<Byte>();
			while (!in1.isEmpty() || !in2.isEmpty() || carry != 0){
				int a1 = in1.isEmpty()?0:in1.pop();
				int a2 = in2.isEmpty()?0:in2.pop();
				int sum = a1 + a2 + carry;
				carry = sum - (sum%256);
				out.push((byte)(sum%256));
			}
			byte[] newVal = new byte[out.size()];
			int inc = 0;
			while (out.size() > 0){
				newVal[inc++] = out.pop();
			}
			return new BLZInt(newVal, int1.isNegative());
		}else{
			//TODO handle negative case
			return new BLZInt(convertBLZIntToJavaInt(int1) + convertBLZIntToJavaInt(int2));
		}
	}
	
	public int compareInt(BLZInt otherInt){	//Returns 1 if current int is bigger, 0 if they are the same, -1 if current int is smaller
		if (!negative && otherInt.negative){
			return 1;
		}else if(negative && !otherInt.negative){
			return -1;
		}
		if (value.length > otherInt.getValue().length){
			return negative?-1:1;
		}else if(otherInt.getValue().length > value.length){
			return negative?1:-1;
		}
		for (int i = 0; i < value.length; i++){
			if (value[i] > otherInt.getValue()[i]){
				
			}
		}
		return 0;
	}
	
	public static boolean isBLZInt(String in){
		for (char c : in.toCharArray()){
			if (c != '0' && c != '1' && c != '2' && c != '3' && c != '4'
				&& c != '5' && c != '6' && c!='7' && c != '8' && c != '9'){
				return false;
			}
		}
		return true;
	}
	
	public static BLZInt parseInt(String in){
		return new BLZInt(0); //TODO implement parseInt
	}
	
	public boolean isNegative(){
		return negative;
	}
	public byte[] getValue(){
		return value;
	}


	
}
