package net.mgsx.ld44.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class ThresholdNode implements Processor{

	public FrameBuffer fbo;

	private ShaderProgram blurShader;

	public float threshold = .9f;
	
	public ThresholdNode() {
		fbo = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
	}
	
	public ThresholdNode set(float threshold){
		this.threshold = threshold;
		return this;
	}
	
	@Override
	public void bind() {
		fboStack.add(fbo);
	}
	
	@Override
	public void process(Batch batch) {
		matrix.set(batch.getProjectionMatrix());
		if(blurShader == null){
			String fragSource = Gdx.files.internal("shaders/pixel-frag.glsl").readString();
			
			String[] hb = fragSource.split("#HEADER");
			
			String[] ss = hb[1].split("#START"); // TODO !!!!!!!!!!!!!!!!!!!
			String[] sss = ss[1].split("#END");
			
			String gen = "";
			
			gen += "vec4 tex = texture2D(u_texture, vec2(tc.x, tc.y));\n";
			// gen += "float lum = max(max(tex.r,tex.g),tex.b) + (tex.r+tex.g+tex.b)/3.0;\n";
			gen += "float lum = (tex.r+tex.g+tex.b)/3.0;\n";
			// gen += "if(lum > u_threshold) color = vec4(tex.rgb, 1.0);\n";
			gen += "color = vec4(pow(lum, u_threshold) * tex.rgb, 1.0);\n";
			
			// gen = "color = texture2D(u_texture, vec2(tc.x, tc.y));\n";
			
			String pre = "uniform float u_threshold;\n";
			
			fragSource = hb[0] + pre + ss[0] + gen + sss[1];
			
			blurShader = new ShaderProgram(Gdx.files.internal("shaders/simple-vert.glsl").readString(), 
					fragSource);
			
			if(!blurShader.isCompiled()) throw new GdxRuntimeException(blurShader.getLog());
		}
		
		batch.getProjectionMatrix().setToOrtho2D(0, 0, 1, 1);
		Gdx.gl.glClearColor(0,0,0, 0);
			Texture texture = fbo.getColorBufferTexture();
			
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			batch.setShader(blurShader);
			batch.begin();
			blurShader.begin();
			blurShader.setUniformf("u_threshold", threshold);
			batch.draw(texture, 0, 0, 1, 1, 0, 0, 1, 1);
			batch.end();
			
		
		
		batch.setShader(null);
		batch.getProjectionMatrix().set(matrix);
	}

	@Override
	public void resize(int width, int height) {
		if(fbo == null || fbo.getWidth() != width || fbo.getHeight() != height){
			if(fbo != null) fbo.dispose();
			fbo = new FrameBuffer(Format.RGBA8888, width, height, false);
		}
	}


}
