import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

public abstract class GraphicObject implements PConstants {
	protected static PApplet app;
	protected static int count = 0;
	protected int br, bg, bb;
	
	protected int id;
	protected float x, y, z;
	protected float xAngle, yAngle, zAngle, scale, strokeScale;
	protected boolean isSelected;
	protected PImage img;
	
	public GraphicObject()
	{
		//	color picking
		id = count++;
		br = (id & 0x00FF0000) >> 16;
		bg = (id & 0x0000FF00) >>  8;
		bb = (id & 0x000000FF) >>  0;
		
		isSelected = false;
		
		scale = 1;
		strokeScale = 1;
	}
	
	public static void decrement()
	{
		count--;
	}
	
	public void move(float dx1, float dx2, float dy)
	{
		x -= dx1;
		z -= dx2;
		
		y -= dy;
	}
	
	public void scale(float s)
	{
		scale *= 1+s;
		strokeScale = 1 / scale;
	}
	
	public void select()
	{
		isSelected = true;
	}
	
	public void deSelect()
	{
		isSelected = false;
	}
	
	public void setTexture(String path)
	{
		img = app.loadImage(path);
	}

	
	public int getID()
	{
		return id;
	}
	
	public void draw(PGraphics buffer, RenderMode mode)
	{
		switch (mode)
		{
			case WIREFRAMED:
				buffer.noFill();
				buffer.stroke(255);
				break;
			case SHADED:
				buffer.fill(127);
				buffer.stroke(255);
				break;
			case TEXTURED:
				buffer.fill(127);
				buffer.noStroke();
				break;
			case LIGHTING:
				buffer.fill(127);
				buffer.noStroke();
				buffer.lights();
				break;
			case PICK:
				buffer.fill(br, bg, bb);
				buffer.noStroke();
				break;
				
			default:
				break;
		}
		
		if (isSelected)
		{
			buffer.stroke(255, 0, 0);
		}
	}
}
