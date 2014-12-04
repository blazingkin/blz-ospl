package com.blazingkin.interpreter.executor.listener;

import java.util.ArrayList;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.InstructionExecutor;

public class Listener implements InstructionExecutor {

	public void run(String[] args){
		listeners.add(new Listener(args[1], getListenerType(args[0])));
	}
	
	public ListenerTypes getListenerType(String s){
		for (int i = 0; i < ListenerTypes.values().length; i++){
			if (s.equals(ListenerTypes.values()[i].name)){
				return ListenerTypes.values()[i];
			}
		}
		
		
		Interpreter.throwError("Could Not Find Listener Type: "+s);
		return null;
	}
	
	public static ArrayList<Listener> listeners = new ArrayList<Listener>();
	
	public static synchronized void fireEvent(ListenerTypes lt){
		for (int i = 0; i < listeners.size(); i++){
			if (listeners.get(i).listenerType.equals(lt)){
				Executor.eventsToBeHandled.add(Executor.functionLines.get(listeners.get(i).eventHandler).lineNumber+1);
			}
		}
	}
	
	public String eventHandler;
	public ListenerTypes listenerType;
	public Listener(){
		
	}
	public Listener(String eventHandler, ListenerTypes lt){
		this.eventHandler = eventHandler;
		listenerType = lt;
	}
	
}
