package com.blazingkin.interpreter.executor.output.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.blazingkin.interpreter.Interpreter;
import com.blazingkin.interpreter.executor.Executor;
import com.blazingkin.interpreter.executor.InstructionExecutor;
import com.blazingkin.interpreter.executor.listener.Listener;
import com.blazingkin.interpreter.executor.listener.ListenerTypes;
import com.blazingkin.interpreter.variables.Value;
import com.blazingkin.interpreter.variables.Variable;

public class GraphicsExecutor implements InstructionExecutor {
	public static JFrame jf;
	public final GraphicsTask task;
	public static int lastFPS;
	public static boolean clearing = false;
	public GraphicsExecutor(GraphicsTask gt){
		task = gt;
	}
	
	public static Graphics bufferGraphics;
	public static Image offScreen;
	
	public static ArrayList<Polygon> polygons = new ArrayList<Polygon>();
	public static boolean clearFlag = true;
	public void run(String args[]){
		switch(task){
		case init:
			jf = new JFrame();
			jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			System.setProperty("red", "0xFF0000");
			System.setProperty("blue", "0x140CA3");
			System.setProperty("green", "0x0CA35F");
			System.setProperty("yellow", "0xEEB111");
			System.setProperty("black", "0xFFFFFF");
			System.setProperty("white", "0x000000");
			System.setProperty("orange", "0xF9FB07");
			System.setProperty("purple", "0x650D7F");
			System.setProperty("brown", "0x482F0B");

	        @SuppressWarnings("serial")
			JPanel panel = new JPanel() { 
	        	private boolean initialized = false;
	        	public void init(){
	        		offScreen = createImage(GraphicsExecutor.jf.getWidth(), GraphicsExecutor.jf.getHeight());
	        		bufferGraphics = offScreen.getGraphics();
	        	}
	        	
	        	public void update(Graphics g){
	        		if (!initialized){
	        			init();
	        			initialized = true;
	        		}
	        	try{
	        		if (GraphicsExecutor.clearFlag){
	        			clearing = true;
	                	bufferGraphics.clearRect(0, 0, GraphicsExecutor.jf.getWidth(), GraphicsExecutor.jf.getHeight());
	                	GraphicsExecutor.polygons.clear();
	                	GraphicsExecutor.clearFlag = false;
	                	clearing = false;
	                }
	        	}catch(Exception e){}
	        	//paint(g);
	        	}
	        	
	            public void paintComponent(Graphics g) {
	            	
	            	update(g);
	            	if (System.currentTimeMillis() - Executor.timeStarted > 1000){
	            		GraphicsExecutor.lastFPS = Executor.frames;
	            		Executor.frames = 0;
	            		Executor.timeStarted = System.currentTimeMillis();
	            	}
	            	Executor.frames++;
	               try{

	            	super.paintComponent(g);	               
	                for (Polygon p: GraphicsExecutor.polygons){
		                bufferGraphics.setColor(p.color);
		                bufferGraphics.fillPolygon(p.xPoints, p.yPoints, p.xPoints.length);
	                }
	                while(clearing);
	                g.drawImage(offScreen, 0, 0, this);
	               }catch(Exception e){}


	            }
	        };
	        
	        
	        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

	            @Override
	            public boolean dispatchKeyEvent(KeyEvent ke) {
	                    switch (ke.getID()) {
	                    case KeyEvent.KEY_PRESSED:
	                    	Listener.fireEvent(ListenerTypes.KeyboardKeyDown);
	                        break;

	                    case KeyEvent.KEY_RELEASED:
	                    	Listener.fireEvent(ListenerTypes.KeyboardKeyUp);
	                        break;
	                    }
	                    return false;
	            }
	        });
	        jf.addMouseListener(
	        	new MouseListener() {

					@Override
					public void mouseClicked(MouseEvent arg0) {
						if (arg0.getButton() == 1){
							Listener.fireEvent(ListenerTypes.MouseOneClick);
						}
						if (arg0.getButton() == 3){
							Listener.fireEvent(ListenerTypes.MouseTwoClick);	
						}
						
					}

					@Override
					public void mouseEntered(MouseEvent arg0) {
						Listener.fireEvent(ListenerTypes.MouseEntered);
					}

					@Override
					public void mouseExited(MouseEvent arg0) {
						Listener.fireEvent(ListenerTypes.MouseExited);
					}

					@Override
					public void mousePressed(MouseEvent arg0) {
						if (arg0.getButton() == 1){
							Listener.fireEvent(ListenerTypes.MouseDown);
						}
						if (arg0.getButton() == 3){
							Listener.fireEvent(ListenerTypes.MouseTwoDown);
						}
					}

					@Override
					public void mouseReleased(MouseEvent arg0) {
						if (arg0.getButton() == 1){
							Listener.fireEvent(ListenerTypes.MouseUp);
						}
						if (arg0.getButton() == 3){
							Listener.fireEvent(ListenerTypes.MouseTwoUp);	
						}
					}
	        		
	        		
	        		
	        		
	        		
	        		
	        	}
	        		
	        		
	        		
	        		
	        		
	        );
	        
	        
	        
	        jf.add(panel);

			break;
			
			
		case setSize:
			if (args[0].contains("[]")){
				String s = args[0].replace("[", "").replace("]", "");
				dimX = (Integer) Variable.getValueOfArray(s+"[0]").value;
				dimY = (Integer) Variable.getValueOfArray(s+"[1]").value;
				jf.setSize(dimX,dimY);
				break;
			}
			if (Variable.isInteger(Variable.parseString(args[0])) && Variable.isInteger(Variable.parseString(args[1]))){
				dimX = Integer.parseInt(Variable.parseString(args[0]));
				dimY = Integer.parseInt(Variable.parseString(args[1]));
				jf.setSize(dimX, dimY);
			}
			break;
			
		case clearLast:
			if (polygons.size() > 0){
			polygons.remove(polygons.size()-1);
			}
			break;
		case draw:
			String s = Variable.parseString(args[0]);
			final Color c;
			Color co;
			try{

				int decimal = Integer.parseInt(s, 16);
				if (s.length() != 6 && !s.toLowerCase().contains("a|b|c|d|e|f")){
					decimal = Integer.parseInt(s);
				}
				co = new Color(decimal / 65536, (decimal / 256)%256, decimal %256);

			}catch(Exception e){
				
				co = Color.getColor(s.toLowerCase(),Color.black);
				if (s.toLowerCase().equals("random")){
					co = new Color((int)(Math.random()*16777215));
				}
			}
			c = co;
			ArrayList<Point> points = new ArrayList<Point>();
			for (int i = 1; i < args.length; i++){
				if (args[i].contains("[]")){
					String str = args[i].replace("[]", "");
					HashMap<Integer, Value> lst = Variable.lists.get(str);
					for (int y = 0; y < lst.size(); y+=2){
						points.add(new Point( (Integer) ((Value) lst.values().toArray()[y]).value, (Integer) ((Value) lst.values().toArray()[y+1]).value));
					}
				}else{
					points.add(new Point(Integer.parseInt(Variable.parseString(args[i])), Integer.parseInt(Variable.parseString(args[i+1]))));
					i++;
				}
			}
			Point[] p = new Point[points.size()];
			for (int i = 0; i < points.size(); i++){
				p[i] = points.get(i);
			}
			polygons.add(new Polygon(c,p));
			jf.repaint();
			break;
			
			
		case close:
			jf.setVisible(false);
			break;
			
			
		case clear:
			GraphicsExecutor.clearFlag = true;
			break;
			
			
		case setProperty:
			GraphicsProperty gp = null;
			try{
				String str = Variable.parseString(args[0]);
				for (int i = 0; i < GraphicsProperty.values().length; i++){
					if (str.equals(GraphicsProperty.values()[i].name)){
						gp = GraphicsProperty.values()[i];
					}
				}
				if (gp == null){
					throw new Exception();
				}
			}catch(Exception e){Interpreter.throwError("Invalid Graphics Property: "+args[0]);}
			switch(gp){
			case borderless:
				jf.setUndecorated(true);
				break;
			case bordered:
				jf.setUndecorated(false);
				break;
			case fullscreen:
				jf.setUndecorated(true);
				dimX = jf.getX();
				dimY = jf.getY();
				jf.setSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width,java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
				break;
			case windowed:
				jf.setUndecorated(false);
				jf.setSize(dimX, dimY);
				break;
			case visible:
				jf.setVisible(true);
				jf.toFront();
				jf.repaint();
				break;
			case invisible:
				jf.setVisible(false);
				break;
				default:
					break;
			}
			break;
			
			
		default:
			break;
			
			
		}


	}
	private int dimX, dimY;
	


}
