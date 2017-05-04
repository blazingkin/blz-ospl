package com.blazingkin.interpreter.executor.output.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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
import com.blazingkin.interpreter.variables.VariableTypes;

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
	public static ArrayList<TextLabel> textLabels = new ArrayList<TextLabel>();
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
	                	//bufferGraphics.clearRect(0, 0, GraphicsExecutor.jf.getWidth(), GraphicsExecutor.jf.getHeight());
	                	GraphicsExecutor.polygons.clear();
	                	GraphicsExecutor.textLabels.clear();
	                	GraphicsExecutor.clearFlag = false;
	                	clearing = false;
	                }
	        	}catch(Exception e){}
	        	}
	        	
	        	@Override
	            public void paintComponent(Graphics g) {
	            	super.paintComponent(g);
	        		System.out.println("called");
	            	update(g);
	            	if (System.currentTimeMillis() - Executor.getTimeStarted() > 1000){
	            		GraphicsExecutor.lastFPS = Executor.getFrames();
	            		Executor.setFrames(0);
	            		Executor.setTimeStarted(System.currentTimeMillis());
	            	}
	            	Executor.setFrames(Executor.getFrames() + 1);
	               try{

	               bufferGraphics.clearRect(0, 0, GraphicsExecutor.jf.getWidth(), GraphicsExecutor.jf.getHeight());
	               g.clearRect(0, 0, GraphicsExecutor.jf.getWidth(), GraphicsExecutor.jf.getHeight());
	               //System.out.println(System.currentTimeMillis()+" before");
	                for (Polygon p: GraphicsExecutor.polygons){
		                bufferGraphics.setColor(p.color);
		                bufferGraphics.fillPolygon(p.xPoints, p.yPoints, p.xPoints.length);
	                }
	                //System.out.println(System.currentTimeMillis()+" after");
	                for (TextLabel tl: GraphicsExecutor.textLabels){
	                	System.out.println("Drawing" +tl.text);
	                	bufferGraphics.setColor(Color.black);
	                	bufferGraphics.drawString(tl.text, tl.start.x, tl.start.y);
	                }
	                while(clearing);

	                g.drawImage(offScreen, 0, 0, this);
	               }catch(Exception e){
	            	   e.printStackTrace();
	               }


	            }
	        };
	        
	        
	        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

	            @SuppressWarnings("static-access")
				@Override
	            public boolean dispatchKeyEvent(KeyEvent ke) {
	                    switch (ke.getID()) {
	                    case KeyEvent.KEY_PRESSED:
	                    	String[] args = new String[1];
	                    	args[0] = ke.getKeyChar()+"";
	                    	if (ke.getKeyCode() == ke.VK_ENTER){
	                    		String[] ffff = {"\n"};
	                    		Listener.fireEvent(ListenerTypes.KeyboardKeyDown, ffff);
	                    		break;
	                    	}
	                    	if (ke.getKeyCode() == ke.VK_SHIFT){
	                    		String[] ffff = {"SHIFT"};
	                    		Listener.fireEvent(ListenerTypes.KeyboardKeyDown, ffff);
	                    		break;
	                    	}
	                    	if (ke.getKeyCode() == ke.VK_CAPS_LOCK){
	                    		String[] ffff = {"CAPS_LOCK"};
	                    		Listener.fireEvent(ListenerTypes.KeyboardKeyDown, ffff);
	                    		break;
	                    	}
	                    	if (ke.getKeyCode() == ke.VK_TAB){
	                    		String[] ffff = {"\t"};
	                    		Listener.fireEvent(ListenerTypes.KeyboardKeyDown, ffff);
	                    		break;
	                    	}
	                    	if (ke.getKeyCode() == ke.VK_ALT){
	                    		String[] ffff = {"ALT"};
	                    		Listener.fireEvent(ListenerTypes.KeyboardKeyDown, ffff);
	                    		break;
	                    	}
	                    	if (ke.getKeyCode() == ke.VK_BACK_SPACE){
	                    		String[] ffff = {"BACKSPACE"};
	                    		Listener.fireEvent(ListenerTypes.KeyboardKeyDown, ffff);
	                    		break;
	                    	}
	                    	if (ke.getKeyCode() == ke.VK_CONTROL){
	                    		String[] ffff = {"CONTROL"};
	                    		Listener.fireEvent(ListenerTypes.KeyboardKeyDown, ffff);
	                    		break;
	                    	}
	                    	Listener.fireEvent(ListenerTypes.KeyboardKeyDown, args);
	                        break;

	                    case KeyEvent.KEY_RELEASED:
	                    	String[] argds = new String[1];
	                    	argds[0] = ke.getKeyChar()+"";
	                    	if (ke.getKeyCode() == ke.VK_ENTER){
	                    		String[] ffff = {"\n"};
	                    		Listener.fireEvent(ListenerTypes.KeyboardKeyUp, ffff);
	                    		break;
	                    	}
	                    	if (ke.getKeyCode() == ke.VK_SHIFT){
	                    		String[] ffff = {"SHIFT"};
	                    		Listener.fireEvent(ListenerTypes.KeyboardKeyUp, ffff);
	                    		break;
	                    	}
	                    	if (ke.getKeyCode() == ke.VK_CAPS_LOCK){
	                    		String[] ffff = {"CAPS_LOCK"};
	                    		Listener.fireEvent(ListenerTypes.KeyboardKeyUp, ffff);
	                    		break;
	                    	}
	                    	if (ke.getKeyCode() == ke.VK_TAB){
	                    		String[] ffff = {"\t"};
	                    		Listener.fireEvent(ListenerTypes.KeyboardKeyUp, ffff);
	                    		break;
	                    	}
	                    	if (ke.getKeyCode() == ke.VK_ALT){
	                    		String[] ffff = {"ALT"};
	                    		Listener.fireEvent(ListenerTypes.KeyboardKeyUp, ffff);
	                    		break;
	                    	}
	                    	if (ke.getKeyCode() == ke.VK_BACK_SPACE){
	                    		String[] ffff = {"BACKSPACE"};
	                    		Listener.fireEvent(ListenerTypes.KeyboardKeyUp, ffff);
	                    		break;
	                    	}
	                    	if (ke.getKeyCode() == ke.VK_CONTROL){
	                    		String[] ffff = {"CONTROL"};
	                    		Listener.fireEvent(ListenerTypes.KeyboardKeyUp, ffff);
	                    		break;
	                    	}
	                    	Listener.fireEvent(ListenerTypes.KeyboardKeyUp, argds);
	                        break;
	                    }
	                    return false;
	            }
	        });
	        jf.addMouseMotionListener(
	        		new MouseMotionListener() {

						public void mouseDragged(MouseEvent arg0) {
							Listener.fireEvent(ListenerTypes.MouseMotion);
						}

						public void mouseMoved(MouseEvent arg0) {
							Listener.fireEvent(ListenerTypes.MouseMotion);
						}
	        			
	        			
	        		}
	        		
	        		);
	        
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
			
		case drawText:
			int px = (int) Variable.getValue(args[0]).value;
			int py = (int) Variable.getValue(args[1]).value;
			String buS = "";
			for (int i = 2; i < args.length; i++){
				buS = buS + Variable.getValue(args[i]).value;
				if (i != args.length-1){
					buS = buS + " ";
				}
			}
			TextLabel tl = new TextLabel(new Point(px,py), buS);
			textLabels.add(tl);
			jf.repaint();
			break;
		case setSize:
			if (args[0].contains("[]")){
				String s = args[0].replace("[", "").replace("]", "");
				dimX = (Integer) Variable.getValueOfArray(s, 0).value;
				dimY = (Integer) Variable.getValueOfArray(s, 1).value;
				jf.setSize(dimX,dimY);
				break;
			}
			if (Variable.getValue(args[0]).type == VariableTypes.Integer && Variable.getValue(args[1]).type == VariableTypes.Integer){
				dimX = (int) Variable.getValue(args[0]).value;
				dimY = (int) Variable.getValue(args[1]).value;;
				jf.setSize(dimX, dimY);
			}
			break;
			
		case clearLast:
			if (polygons.size() > 0){
			polygons.remove(polygons.size()-1);
			}
			break;
		case draw:
			String s = (String)Variable.getValue(args[0]).value;
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
					HashMap<Integer, Value> lst = Variable.getArray(str);
					for (int y = 0; y < lst.size(); y+=2){
						points.add(new Point( (Integer) ((Value) lst.values().toArray()[y]).value, (Integer) ((Value) lst.values().toArray()[y+1]).value));
					}
				}else{
					points.add(new Point((int) Variable.getValue(args[i]).value, (int) Variable.getValue(args[i+1]).value));
					i++;
				}
			}
			Point[] p = new Point[points.size()];
			for (int i = 0; i < points.size(); i++){
				p[i] = points.get(i);
			}
			polygons.add(new Polygon(c,p));
			jf.paintComponents(jf.getGraphics());
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
				String str = (String) Variable.getValue(args[0]).value;
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
