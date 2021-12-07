import java.awt.event.KeyEvent;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.event.MouseEvent;
import processing.opengl.PGraphics3D;

/**
 * Graphics Application
 * @author ostovargas
 *
 */
public class GraphicsApplication extends PApplet implements ApplicationConstants {
	private PGraphics buffer;
	private RenderMode renderMode;
	private ArrayList<GraphicObject> objects;	//	for object selection
	private ArrayList<Plane> planes;
	private ArrayList<Cube> cubes;
	private GraphicObject selectedObject;
	
	private TransformMode mode;
	
	//	camera controls
	private boolean altPressed;
	private float eyeX, eyeY, eyeZ;
	private float centerX, centerY, centerZ;
	private float tumbleTheta, tumblePhi;
	private float panTheta;
	private float distance;
	
	public void settings()
	{
		size(WIDTH, HEIGHT, P3D);
	}
	
	public void setup()
	{
		GraphicObject.app = this;
		renderMode = RenderMode.SHADED;
		
		objects = new ArrayList<GraphicObject>();
		planes = new ArrayList<Plane>();
		cubes = new ArrayList<Cube>();
		
//		planes.add(new Plane(HALF_WIDTH, HALF_HEIGHT, 0));
//		planes.get(0).setTexture("Earl_Knx_Graphic.fcad0B.png");
//		objects.add(planes.get(0));
		
//		cubes.add(new Cube(0, 0, 0));
//		objects.add(cubes.get(0));
		
		selectedObject = null;
		
		centerX = HALF_WIDTH;
		centerY = HALF_HEIGHT;
		centerZ = 0;
		
		tumbleTheta = QUARTER_PI;
		panTheta = HALF_PI + tumbleTheta;
		tumblePhi = THIRD_PI;
		distance = 1000;
		
		callibrateCamera();
		
		altPressed = false;
		
		mode = TransformMode.TRANSLATE;
		
		//	https://forum.processing.org/one/topic/peasycam-and-picking-library.html
		//	color picking
		buffer = createGraphics(width, height, P3D);
		//
	}
	
	public void createPlane()
	{
		Plane plane = new Plane(HALF_WIDTH, HALF_HEIGHT, 0);
		plane.setTexture("Earl_Knx_Graphic.fcad0B.png");
		planes.add(plane);
		
		objects.add(plane);
	}
	
	public void createCube()
	{
		Cube cube = new Cube(HALF_WIDTH, HALF_HEIGHT, 0);
		cubes.add(cube);
		
		objects.add(cube);
	}
	
	public void mousePressed()
	{
		//	do not color pick while controlling camera
		if (!altPressed)
		{
			//	color buffer picking
			buffer.beginDraw();
			
			buffer.background(255);
			buffer.noStroke();
			buffer.setMatrix(((PGraphics3D)g).camera);	//	?
			
			drawBuffer();
			
			buffer.endDraw();
			
			int color = buffer.get(mouseX, mouseY);
			
			int r = (int)buffer.red(color);
			int g = (int)buffer.green(color);
			int b = (int)buffer.blue(color);
			
			int id = (r << 16) + (g << 8) + b;
			
			if (mouseButton == LEFT)
			{
				if (id < objects.size())
				{
					if (selectedObject != null)
						selectedObject.deSelect();
					
					selectedObject = objects.get(id);
					objects.get(id).select();
				}
				else if (selectedObject != null)
				{
					selectedObject.deSelect();
					selectedObject = null;
				}
			}
		}
	}
	
	public void mouseDragged()
	{
		float dx = pmouseX - mouseX;
		float dy = pmouseY - mouseY;
		
		switch (mouseButton)
		{
			//	tumble
			case LEFT:
				if (altPressed)
					tumbleCamera(dx, dy);
				else if (selectedObject != null)
					transformObject(dx, dy);
				break;
			//	pan
			case CENTER:
				if (altPressed)
					panCamera(dx, dy);
				break;
			//	dolly
			case RIGHT:
				if (altPressed)
					dollyCamera(dy);
				break;
				
			default:
				break;
		}
	}
	
	public void mouseReleased()
	{
		;
	}
	
	public void mouseWheel(MouseEvent event) {
		  float e = event.getCount();
		  dollyCamera(e*10);
		}
	
	public void keyPressed()
	{
		switch (keyCode)
		{
			//	camera controls
			case KeyEvent.VK_ALT:
				altPressed = true;
				break;
				
			default:
				break;
		}
	}
	
	public void keyReleased()
	{
		switch (keyCode)
		{
			//	shading controls
			case KeyEvent.VK_1:
				renderMode = RenderMode.WIREFRAMED;
				break;
			case KeyEvent.VK_2:
				renderMode = RenderMode.SHADED;
				break;
			case KeyEvent.VK_3:
				renderMode = RenderMode.TEXTURED;
				break;
			case KeyEvent.VK_4:
				renderMode = RenderMode.LIGHTING;
				break;
				
			//	camera controls
			case KeyEvent.VK_ALT:
				altPressed = false;
				break;
				
			case KeyEvent.VK_W:
				System.out.println("TRANSLATE");
				mode = TransformMode.TRANSLATE;
				break;
			case KeyEvent.VK_E:
				System.out.println("ROTATE");
				mode = TransformMode.ROTATE;
				break;
			case KeyEvent.VK_R:
				System.out.println("SCALE");
				mode = TransformMode.SCALE;
				break;
				
			case KeyEvent.VK_O:
				createPlane();
				break;
			
			case KeyEvent.VK_P:
				createCube();
				break;
				
			case KeyEvent.VK_X:
				planes.remove(selectedObject);
				cubes.remove(selectedObject);
				objects.remove(selectedObject);
				GraphicObject.decrement();
				break;
				
			default:
				break;
		}
	}
	
	private void transformObject(float dx, float dy)
	{
		switch (mode)
		{
			case TRANSLATE:
				float sin_dx = sin(panTheta) * dx;
				float cos_dx = cos(panTheta) * dx;
				selectedObject.move(sin_dx, cos_dx, dy);
				break;
				
			case ROTATE:
				break;
				
			case SCALE:
				float s = sqrt(dx*dx + dy*dy)*0.01f;
				selectedObject.scale(s);
				break;
				
			default:
				break;
		}
	}
	
	private void tumbleCamera(float dth, float dph)
	{
		dth *= 0.01f;
		tumbleTheta += dth;
		panTheta += dth;			//	theta for panning
		tumblePhi += dph*0.01f;
		
		if (tumblePhi < 0.001f)
			tumblePhi = 0.001f;
		else if (tumblePhi > PI-0.001f)
			tumblePhi = PI-0.001f;
		
		callibrateCamera();
	}
	
	private void panCamera(float dx, float dy)
	{
		eyeY += dy;
		centerY += dy;
		
		float sin_dx = sin(panTheta) * dx;
		float cos_dx = cos(panTheta) * dx;
		
		eyeX += sin_dx;
		centerX += sin_dx;
		eyeZ += cos_dx;
		centerZ += cos_dx;
	}
	
	private void dollyCamera(float dy)
	{
		distance += dy;
		if (distance < 100)
			distance = 100;
		
		callibrateCamera();
	}
	
	private void callibrateCamera()
	{
		eyeX = centerX + distance * sin(tumblePhi) * sin(tumbleTheta);
		eyeY = centerY + distance * -cos(tumblePhi);
		eyeZ = centerZ + distance * sin(tumblePhi) * cos(tumbleTheta);
	}
	
	public void draw()
	{
		background(176, 196, 222);
		
		//	draw grid
		pushMatrix();
		translate(HALF_WIDTH-GRID_HALF_WIDTH, HALF_HEIGHT, -GRID_HALF_HEIGHT);
		rotateX(HALF_PI);
		stroke(0);
		beginShape(LINES);
		for (int x = 0; x <= GRID_WIDTH; x += 50)
		{
		    vertex(x, 0.0f);
		    vertex(x, GRID_WIDTH);
		}
		for (int y = 0; y <= GRID_HEIGHT; y += 50)
		{
		    vertex(0.0f, y);
		    vertex(GRID_HEIGHT, y);
		}
		endShape();
		popMatrix();
		
		//	cursor
		pushMatrix();
		noStroke();
		fill(255, 165, 0);
		translate(centerX, centerY, centerZ);
		sphere(1);
		popMatrix();
		
		camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, 0, 1, 0);
	
		/*
		 *	draw objects on SCENE
		 */
		for (Plane plane : planes)
			plane.draw(g, renderMode);
		
		for (Cube cube : cubes)
			cube.draw(g, renderMode);
	}
	
	private void drawBuffer()
	{
		for (Plane plane : planes)
			plane.draw(buffer, RenderMode.PICK);
		
		for (Cube cube : cubes)
			cube.draw(buffer, RenderMode.PICK);
	}
	
	public static void main(String[] argv)
	{
		PApplet.main("GraphicsApplication");
	}
}
