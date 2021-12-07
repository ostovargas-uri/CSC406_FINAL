import processing.core.PGraphics;

public class Plane extends GraphicObject {
	private float width, height;
	private float hWidth, hHeight;
	
	public Plane(float x, float y, float z)
	{
		super();
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.width = 100;		//	default
		this.height = 100;	//	default
		
		hWidth = width/2;
		hHeight = height/2;
		
		xAngle = 0;
		yAngle = 0;
		zAngle = 0;
		
		img = null;
	}

	@Override
	public void draw(PGraphics buffer, RenderMode mode) {
		super.draw(buffer, mode);	//	decide between drawing or picking
		
		buffer.pushMatrix();
		
		buffer.translate(x, y, z);
		buffer.rotateX(xAngle);
		buffer.rotateY(yAngle);
		buffer.rotateZ(zAngle);
		
		buffer.scale(scale);
		buffer.strokeWeight(strokeScale);
		
		buffer.textureMode(NORMAL);
		
		buffer.beginShape();
		if ((mode == RenderMode.TEXTURED || mode == RenderMode.LIGHTING) && img != null)
			buffer.texture(img);
		buffer.vertex(-hWidth, -hHeight, 0, 0, 0);
		buffer.vertex(+hWidth, -hHeight, 0, 1, 0);
		buffer.vertex(+hWidth, +hHeight, 0, 1, 1);
		buffer.vertex(-hWidth, +hHeight, 0, 0, 1);
		buffer.endShape(CLOSE);
		
		buffer.popMatrix();
	}
}
