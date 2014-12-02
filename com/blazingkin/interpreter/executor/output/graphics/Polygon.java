package com.blazingkin.interpreter.executor.output.graphics;

import java.awt.Color;

public class Polygon {
	public int[] xPoints;
	public int[] yPoints;
	public Color color;
	public Polygon(Color c, Point... po){
		xPoints = new int[po.length];
		yPoints = new int[po.length];
		int i = 0;
		for (Point p: po){
			xPoints[i] = p.x;
			yPoints[i] = p.y;
			i++;
		}
		color = c;
	}
	


}
