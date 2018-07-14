package com.blazingkin.interpreter.executor.executionstack;

import java.util.ArrayDeque;
import java.util.HashMap;

import com.blazingkin.interpreter.BLZRuntimeException;
import com.blazingkin.interpreter.executor.sourcestructures.Process;
import com.blazingkin.interpreter.variables.Context;
import com.blazingkin.interpreter.variables.Value;

public class RuntimeStack {

	public static HashMap<Long, ArrayDeque<Process>> threadProcessStack = new HashMap<Long, ArrayDeque<Process>>();


	
	public static void push(Process process) throws BLZRuntimeException{
		ArrayDeque<Process> pStack = threadProcessStack.get(Thread.currentThread().getId());
		if (pStack == null) {
			threadProcessStack.put(Thread.currentThread().getId(), new ArrayDeque<Process>());
			pStack = threadProcessStack.get(Thread.currentThread().getId());
		}
		pStack.push(process);
		process.onBlockStart();
	}
	
	public static void pop(){
		ArrayDeque<Process> pStack = threadProcessStack.get(Thread.currentThread().getId());
		if (pStack == null) {
			threadProcessStack.put(Thread.currentThread().getId(), new ArrayDeque<Process>());
			return;
		}
		pStack.pop();
	}

	public static boolean isEmpty() {
		ArrayDeque<Process> pStack = threadProcessStack.get(Thread.currentThread().getId());
		if (pStack == null) {
			threadProcessStack.put(Thread.currentThread().getId(), new ArrayDeque<Process>());
			return true;
		}
		return pStack.isEmpty();
	}

	public static ArrayDeque<Process> getProcessStack() {
		ArrayDeque<Process> pStack = threadProcessStack.get(Thread.currentThread().getId());
		if (pStack == null) {
			threadProcessStack.put(Thread.currentThread().getId(), new ArrayDeque<Process>());
			pStack = threadProcessStack.get(Thread.currentThread().getId());
		}
		return pStack;
	}
	
	public static void cleanup(){
		threadProcessStack.clear();
	}
}
