package com.blazingkin.interpreter.executor.listener;

import java.util.ArrayList;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.instruction.InstructionExecutorStringArray;
import com.blazingkin.interpreter.variables.Value;

public class Listener implements InstructionExecutorStringArray {

	public Value run(String[] args){
		listeners.add(new Listener(args[1], getListenerType(args[0])));
		return Value.nil();
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
				Executor.getEventsToBeHandled().add(new Event(Executor.getMethodInCurrentProcess(listeners.get(i).eventHandler)));
			}
		}
	}
	
	public static synchronized void fireEvent(ListenerTypes lt, String[] arguments){
		for (int i = 0; i < listeners.size(); i++){
			if (listeners.get(i).listenerType.equals(lt)){
				Executor.getEventsToBeHandled().add(new Event(Executor.getMethodInCurrentProcess(listeners.get(i).eventHandler), arguments));
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
