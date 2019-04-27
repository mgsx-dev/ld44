package net.mgsx.ld44.gfx;

import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class PingPongBuffer {

	private FrameBuffer front;
	private FrameBuffer back;

	public void setSize(int width, int height){
		front = new FrameBuffer(Format.RGBA8888, width, height, false);
		back = new FrameBuffer(Format.RGBA8888, width, height, false);
	}
	
	public void flip(){
		FrameBuffer fbo = front;
		front = back;
		back = fbo;
	}
	
	public FrameBuffer front(){
		return front;
	}
	public FrameBuffer back(){
		return back;
	}
	
}
