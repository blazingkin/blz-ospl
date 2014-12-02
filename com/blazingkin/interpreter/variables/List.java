package com.blazingkin.interpreter.variables;

import java.util.HashMap;

public class List {
		public HashMap<Integer, Value> lst = new HashMap<Integer, Value>();
		public void addElement(Value v){
			lst.put(lst.size(), v);
		}
		public void setElement(int index, Value v){
			lst.put(index, v);
		}
		public void removeElement(Value v){
			lst.remove(v);
		}
		public void removeElement(int n){
			lst.remove(n);
		}
		public Value get(int index){
			return lst.get(index);
		}
		
}
