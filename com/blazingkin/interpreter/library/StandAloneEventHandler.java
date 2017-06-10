package com.blazingkin.interpreter.library;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.blazingkin.interpreter.Interpreter;

public class StandAloneEventHandler implements BlzEventHandler {
	Scanner s = new Scanner(System.in);
	private static OutputStream out = new BufferedOutputStream(System.out);
	private static BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
	
	private static boolean flushScheduled = false;
	final Runnable flush = new Runnable() {
		public void run() { 
			try{
				writer.flush(); 
				flushScheduled = false;
			}catch(Exception e){
				
			}
		}
	};
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	final ScheduledFuture<?> beeperHandle =
		       scheduler.scheduleAtFixedRate(flush, 250, 250, TimeUnit.MILLISECONDS);
	
	@Override
	public void print(String contents) {
		try{
			writer.write(contents);
			if (!flushScheduled){
				
			}
			flushScheduled = true;
		}catch(Exception e){
			System.out.println(contents);
		}
	}
	
	


	@Override
	public void exitProgram(String exitMessage) {
		try {
			writer.close();
		} catch (IOException e) {
		}
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
