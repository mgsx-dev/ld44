package net.mgsx.ld44.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class PostProcessingStack {
	public final Color finalColor = new Color(1,1,1,1);
	public final Color emissiveColor = new Color(1,1,1,1);
	public final Color emissive2Color = new Color(1,1,1,1);
	public final Array<Processor> pipeline = new Array<Processor>();
	public Batch batch = new SpriteBatch();
	public boolean enabled = true;
	public void begin() {
		if(!enabled) return;
		for(int i=0 ; i<pipeline.size ; i++){
			pipeline.get(pipeline.size-1-i).bind();
		}
		//pipeline.first().bind();
		if(Processor.fboStack.size > 0){
			Processor.fboStack.peek().begin();
		}
	}

	public void end() {
		if(!enabled) return;
		for(int i=0 ; i<pipeline.size ; i++){
			if(Processor.fboStack.size > 0){
				Processor.fboStack.pop().end();
				if(Processor.fboStack.size > 0){
					Processor.fboStack.peek().begin();
				}
			}
			// if(i<pipeline.size-1) pipeline.get(i+1).bind();
			pipeline.get(i).process(batch);

		}
		if(Processor.fboStack.size > 0){
			// XXX mix !
			pipeline.first().bind();
			Texture texture = Processor.fboStack.pop().getColorBufferTexture();
			Texture texture2 = Processor.fboStack.pop().getColorBufferTexture();
			
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
			batch.getProjectionMatrix().setToOrtho2D(0, 0, 1, 1);
			
			batch.getProjectionMatrix().setToOrtho2D(0, 0, 1, 1);
			batch.enableBlending();
			batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
			batch.setColor(emissiveColor.r, emissiveColor.g, emissiveColor.b, emissiveColor.a);
			batch.begin();
			batch.draw(texture2, 0, 0, 1, 1, 0, 0, 1, 1);
			batch.end();
			
			batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			batch.begin();
			batch.setColor(finalColor.r, finalColor.g, finalColor.b, finalColor.a);
			batch.draw(texture, 0, 0, 1, 1, 0, 0, 1, 1);
			batch.end();
			
			
			if(emissive2Color.a > 0){
				
				batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
				batch.setColor(emissive2Color.r, emissive2Color.g, emissive2Color.b, emissive2Color.a);
				batch.begin();
				batch.draw(texture2, 0, 0, 1, 1, 0, 0, 1, 1);
				batch.end();
			}
			
			
			
			
			
			
			batch.setColor(Color.WHITE);
			batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			//batch.disableBlending();
			
		}
	}

	public void resize(int width, int height) {
		for(Processor p : pipeline){
			p.resize(width, height);
		}
	}

}
