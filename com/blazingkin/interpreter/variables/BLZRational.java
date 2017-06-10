package com.blazingkin.interpreter.variables;

public class BLZRational  {
	public long num, den;
	public BLZRational(long num, long den){
		if (num == 0){	// So we don't get a division by 0 error
			this.num = 0;
			this.den = 1;
			return;
		}
		long gcm = Math.abs(gcm(num, den));
		long sign = num * den / (Math.abs(num) * Math.abs(den));
		this.num = sign * Math.abs(num) / gcm;
		this.den = Math.abs(den) / gcm;
	}
	
	public static long gcm(long a, long b) {
	    return b == 0 ? a : gcm(b, a % b); // Not bad for one line of code :)
	    // Notice how this was pulled directly from Stack Overflow ;)
	}
	
	public boolean equals(Object other){
		if (other instanceof BLZRational){
			BLZRational oth = (BLZRational) other;
			return this.num == oth.num && this.den == oth.den;
		}
		return false;
	}
	
	public String toString(){
		return num+"/"+den;
	}
	
	public static BLZRational multiply(BLZRational a, BLZRational b){
		return new BLZRational(a.num * b.num, a.den * b.den);
	}
	
	public BLZRational multiply(BLZRational other){
		return multiply(this, other);
	}
	
	public static BLZRational add(BLZRational a, BLZRational b){
		return new BLZRational((a.num * b.den) + (b.num * a.den), a.den * b.den);
	}
	
	public BLZRational add(BLZRational other){
		return add(this, other);
	}
	
	
	
}
