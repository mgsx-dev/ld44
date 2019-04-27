package net.mgsx.ld44.gfx;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;

public interface Processor {
	
	public static final Array<FrameBuffer> fboStack = new Array<FrameBuffer>();
	
	public static final Matrix4 matrix = new Matrix4();
	
	public void bind();
	public void process(Batch batch);
}
