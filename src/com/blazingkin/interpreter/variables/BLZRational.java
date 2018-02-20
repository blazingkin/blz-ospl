package com.blazingkin.interpreter.variables;

import java.math.BigInteger;

public class BLZRational  {
	public BigInteger num, den;
	public BLZRational(BigInteger num, BigInteger den){
		if (num.equals(BigInteger.ZERO)){	// So we don't get a division by 0 error
			this.num = BigInteger.ZERO;
			this.den = BigInteger.ONE;
			return;
		}
		BigInteger gcm = num.gcd(den);
		int sign = num.signum() / den.signum();
		this.num = num.divide(gcm).abs().multiply(BigInteger.valueOf(sign));
		this.den = den.divide(gcm).abs();
	}
	
	public boolean equals(Object other){
		if (other instanceof BLZRational){
			BLZRational oth = (BLZRational) other;
			return this.num.equals(oth.num) && this.den.equals(oth.den);
		}
		return false;
	}
	
	public String toString(){
		return num+"/"+den;
	}
	
	public static BLZRational multiply(BLZRational a, BLZRational b){
		return new BLZRational(a.num.multiply(b.num), a.den.multiply(b.den));
	}
	
	public BLZRational multiply(BLZRational other){
		return multiply(this, other);
	}
	
	public static BLZRational add(BLZRational a, BLZRational b){
		return new BLZRational((a.num.multiply(b.den)).add(b.num.multiply(a.den)), a.den.multiply(b.den));
	}
	
	public BLZRational subtract(BLZRational other){
		return subtract(this, other);
	}
	
	public static BLZRational subtract(BLZRational a, BLZRational b){
		return new BLZRational((a.num.multiply(b.den)).subtract(b.num.multiply(a.den)), a.den.multiply(b.den));		
	}
	
	public BLZRational add(BLZRational other){
		return add(this, other);
	}
	
	
	
}
