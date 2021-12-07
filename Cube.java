import processing.core.PGraphics;

public class Cube extends GraphicObject {
	private float width, height, length;
	private float hWidth, hHeight, hLength;
	
	public Cube(float x, float y, float z)
	{
		super();
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.width = 100;
		this.height = 100;
		this.length = 100;
		
		hWidth = width/2;
		hHeight = height/2;
		hLength = length/2;
		
		xAngle = 0;
		yAngle = 0;
		zAngle = 0;
		
		img = null;
	}
	
	public void draw(PGraphics buffer, RenderMode mode) {
		super.draw(buffer, mode);
		
		buffer.pushMatrix();
		
		buffer.scale(scale);
		buffer.strokeWeight(strokeScale);
		
		buffer.translate(x, y, z);
		buffer.rotateX(xAngle);
		buffer.rotateY(yAngle);
		buffer.rotateZ(zAngle);
		
		buffer.textureMode(NORMAL);
		
		//	front face
		buffer.beginShape();
		buffer.vertex(-hWidth, -hHeight, +hLength, 0, 0);
		buffer.vertex(+hWidth, -hHeight, +hLength, 1, 0);
		buffer.vertex(+hWidth, +hHeight, +hLength, 1, 1);
		buffer.vertex(-hWidth, +hHeight, +hLength, 0, 1);
		buffer.endShape(CLOSE);
		
		//	left face
		buffer.beginShape();
		buffer.vertex(-hWidth, -hHeight, -hLength, 0, 0);
		buffer.vertex(-hWidth, -hHeight, +hLength, 1, 0);
		buffer.vertex(-hWidth, +hHeight, +hLength, 1, 1);
		buffer.vertex(-hWidth, +hHeight, -hLength, 0, 1);
		buffer.endShape(CLOSE);
		
		//	back face
		buffer.beginShape();
		buffer.vertex(+hWidth, -hHeight, -hLength, 0, 0);
		buffer.vertex(-hWidth, -hHeight, -hLength, 1, 0);
		buffer.vertex(-hWidth, +hHeight, -hLength, 1, 1);
		buffer.vertex(+hWidth, +hHeight, -hLength, 0, 1);
		buffer.endShape(CLOSE);
		
		//	right face
		buffer.beginShape();
		buffer.vertex(+hWidth, -hHeight, +hLength, 0, 0);
		buffer.vertex(+hWidth, -hHeight, -hLength, 1, 0);
		buffer.vertex(+hWidth, +hHeight, -hLength, 1, 1);
		buffer.vertex(+hWidth, +hHeight, +hLength, 0, 1);
		buffer.endShape(CLOSE);
		
		//	top face
		buffer.beginShape();
		buffer.vertex(-hWidth, -hHeight, -hLength, 0, 0);
		buffer.vertex(+hWidth, -hHeight, -hLength, 1, 0);
		buffer.vertex(+hWidth, -hHeight, +hLength, 1, 1);
		buffer.vertex(-hWidth, -hHeight, +hLength, 0, 1);
		buffer.endShape(CLOSE);
		
		//	bottom face
		buffer.beginShape();
		buffer.vertex(+hWidth, +hHeight, -hLength, 0, 0);
		buffer.vertex(-hWidth, +hHeight, -hLength, 1, 0);
		buffer.vertex(-hWidth, +hHeight, +hLength, 1, 1);
		buffer.vertex(+hWidth, +hHeight, +hLength, 0, 1);
		buffer.endShape(CLOSE);
		
		buffer.popMatrix();
	}
}
