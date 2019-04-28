package net.mgsx.ld44.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import net.mgsx.ld44.assets.GameAssets;

public class GridActor extends Actor {
	
	public GridActor() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(.9f,.5f,1,.2f);
		float dx = getStage().getCamera().position.x/300f;
		float dy = getStage().getCamera().position.y/300f;
		batch.draw(GameAssets.i.grid1, 
				(getStage().getCamera().position.x - getStage().getWidth()) ,  
				getStage().getCamera().position.y - getStage().getHeight(), 
				
				getStage().getWidth() * 2, 
				getStage().getHeight() * 2 + getStage().getCamera().position.y/2f, 
				
				dx, dy, 
				dx + 4, dy + 4);
	}
}
