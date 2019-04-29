package net.mgsx.ld44.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;

import net.mgsx.ld44.assets.GameAssets;
import net.mgsx.ld44.audio.GameAudio;

public class GridActor extends Actor {
	int mode = 0;
	public GridActor() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if(GameAudio.i.isJustBar(4, 0)){
			mode = (mode+1)%4; // MathUtils.random(1);
		}
		float max = .3f;
		float min = .1f;
		if(mode == 0){
			float f = GameAudio.i.getTimeNomalized(4);
			batch.setColor(1f,0f,1f,MathUtils.lerp(max, min, f));
		}else if(mode == 1){
			float f = GameAudio.i.getTimeNomalized(2f);
			batch.setColor(0f,1f,1f,MathUtils.lerp(max, min, f));
		}else if(mode == 2){
			float f = GameAudio.i.getTimeNomalized(4f);
			batch.setColor(1f,0f,0f,MathUtils.lerp(max, min, f));
		}else{
			float f = GameAudio.i.getTimeNomalized(1);
			batch.setColor(.5f,.5f,.5f,MathUtils.lerp(max, min, f));
			
		}
		
		
		
		
		float dx = getStage().getCamera().position.x/1300f;
		float dy = getStage().getCamera().position.y/1300f;
		batch.draw(GameAssets.i.grid1, 
				(getStage().getCamera().position.x - getStage().getWidth()) ,  
				getStage().getCamera().position.y - getStage().getHeight(), 
				
				getStage().getWidth() * 2 , 
				getStage().getHeight() * 2, 
				
				dx, dy, 
				dx + 4, dy + 2);
	}
}
