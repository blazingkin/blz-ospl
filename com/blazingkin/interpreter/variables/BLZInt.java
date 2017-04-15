package com.blazingkin.interpreter.variables;

import java.util.Stack;

public class BLZInt {
	private byte[] value; //Most significant byte is in value[0]
	private boolean negative = false; //Since they are not fixed length, 2's compliment doesn't work
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
			byteArr.push((byte)(in % 256));	// Get a single byte
			in = in >> 8;	// Shift that byte out
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
				in1.push((int)b);
			}
			for (byte b : int2.value){
				in2.push((int)b);
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
			BLZInt greater = int1.compareInt(int2)>0?int1:int2;
			BLZInt lesser = int1.compareInt(int2)>0?int2:int1;
			Stack<Integer> gre = new Stack<Integer>();
			Stack<Integer> les = new Stack<Integer>();
			for (byte b : greater.value){
				gre.push((int)b);
			}
			for (byte b : lesser.value){
				les.push((int)b);
			}
			//Stack<Byte> out = new Stack<Byte>();
			
			return new BLZInt(convertBLZIntToJavaInt(int1) + convertBLZIntToJavaInt(int2));
		}
	}
	
	public static BLZInt mulBLZInt(BLZInt int1, BLZInt int2){
		//boolean neg = int1.isNegative() ^ int2.isNegative(); //XOR Them to get sign
		//TODO implement int multiplication
		return null;
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
				return negative?-1:1;
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
