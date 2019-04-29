package net.mgsx.ld44.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class BlurNode implements Processor{

	private ShaderProgram blurShader;
	
	private PingPongBuffer pingPong = new PingPongBuffer();

	private Vector2 uSize = new Vector2();
	
	public BlurNode() {
		pingPong.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	@Override
	public void bind() {
		fboStack.add(pingPong.back());
	}
	
	@Override
	public void process(Batch batch) {
		matrix.set(batch.getProjectionMatrix());
		if(blurShader == null){
			
			for(int k=0 ; k<14 ; k++){
				Array<Float> kernel = getKernel(k);
				System.out.println(kernel.size);
				System.out.println(kernel);
				System.out.println("kernel "+k);
			}
			
			Array<Float> kernel = getKernel(4);

			
			String fragSource = Gdx.files.internal("shaders/blur-frag.glsl").readString();
			
			String[] ss = fragSource.split("#START"); // TODO !!!!!!!!!!!!!!!!!!!
			String[] sss = ss[1].split("#END");
			
			String gen = "";
			
			boolean customKernel = false;
			
			if(customKernel){
				
				float rate = 1;
				gen += "color += texture2D(u_texture, vec2(tc.x, tc.y)) * " + rate + ";\n";
				float total = rate;
				int n = 8;
				for(int i=1 ; i<=n ; i++){
					int dx = i;
					int dy = i;
					rate *= .9f;
					total += rate * 1.4f;
					gen += "color += texture2D(u_texture, vec2(tc.x + dx * " + dx + ".0, tc.y + dy * " + dy + ".0)) * " + rate + ";\n";
					gen += "color += texture2D(u_texture, vec2(tc.x - dx * " + dx + ".0, tc.y - dy * " + dy + ".0)) * " + rate + ";\n";
				}
				gen += "color /= " + total + ";";
			}else{
				for(int i=0 ; i<kernel.size ; i++){
					float rate = kernel.get(i) * 1.5f;
					float dx = i - (kernel.size-1)/2f;
					float dy = i - (kernel.size-1)/2f;
					gen += "color += texture2D(u_texture, vec2(tc.x + dx * " + dx + ", tc.y + dy * " + dy + ")) * " + rate + ";\n";
					
				}
				
			}
			
			
			
			
			gen += "color.a += 1.0;\n";
			
			fragSource = ss[0] + gen + sss[1];
			
			blurShader = new ShaderProgram(Gdx.files.internal("shaders/blur-vert.glsl").readString(), 
					fragSource);
			
			if(!blurShader.isCompiled()) throw new GdxRuntimeException(blurShader.getLog());
		}
		
		batch.getProjectionMatrix().setToOrtho2D(0, 0, 1, 1);
		Gdx.gl.glClearColor(0,0,0, 0);
		for(int i=0 ; i<8 ; i++){
			Texture texture = pingPong.back().getColorBufferTexture();
			
			pingPong.front().begin();
			Gdx.gl.glClearColor(0, 0, 0, 0);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			batch.setShader(blurShader);
			batch.begin();
			blurShader.begin();
			blurShader.setUniformf("u_scale", uSize.set(2.5f, 0f).rotate(i * 90).scl(1f/texture.getWidth(), 1f/texture.getHeight()));
			batch.draw(texture, 0, 0, 1, 1, 0, 0, 1, 1);
			batch.end();
			pingPong.front().end();
			
			pingPong.flip();
		}
		
		fboStack.add(pingPong.back());
		
		batch.setShader(null);
		batch.getProjectionMatrix().set(matrix);
		
	}

	private static Array<Float> getKernel(int index){
		
		// pascal triangle : http://rastergrid.com/blog/2010/09/efficient-gaussian-blur-with-linear-sampling/
		Array<Float> prev = new Array<Float>();
		Array<Float> current = new Array<Float>();
		float factor = 1;
		for(int i=0 ; i<=index ; i++){
			// fill
			current.add(1f);
			for(int j=0; j<i ; j++){
				if(j<prev.size-1){
					current.add(prev.get(j) + prev.get(j+1));
				}else{
					current.add(1f);
				}
			}
			if(i<index){
				// swap
				Array<Float> tmp = prev;
				prev = current;
				current = tmp;
				current.clear();
				factor *= 2;
			}
		}
		// scale
		for(int i=0 ; i<current.size ; i++){
			float value = current.get(i) / factor;
			current.set(i, value);
		}
		// eliminate
		for(int i=0 ; i<current.size ; ){
			if(current.get(i) <2f / 255f){
				current.removeIndex(i);
			}else{
				i++;
			}
		}
		
		// normalize
		float sum = 0;
		for(int i=0 ; i<current.size ; i++){
			sum += current.get(i);
		}
		for(int i=0 ; i<current.size ; i++){
			float value = current.get(i) / sum;
			current.set(i, value);
		}
		return current;
	}

	@Override
	public void resize(int width, int height) {
		pingPong.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	

}
