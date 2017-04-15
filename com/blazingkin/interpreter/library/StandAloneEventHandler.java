package com.blazingkin.interpreter.library;

import java.util.Scanner;

import com.blazingkin.interpreter.Interpreter;

public class StandAloneEventHandler implements BlzEventHandler {
	Scanner s = new Scanner(System.in);
	@Override
	public void print(String contents) {
		System.out.print(contents);		
	}

	@Override
	public void exitProgram(String exitMessage) {
		if (Interpreter.logging){
			System.out.println("Exiting: "+exitMessage);
		}
		System.exit(0);
	}

	@Override
	public String getInput() {
		return s.nextLine();
	}

}
