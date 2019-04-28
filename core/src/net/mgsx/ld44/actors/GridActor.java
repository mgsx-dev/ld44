package net.mgsx.ld44.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import net.mgsx.ld44.assets.GameAssets;

public class GridActor extends Actor {
	private float dx;
	
	public GridActor() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void act(float delta) {
		dx += delta * 0f;
		super.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(1,1,1,.2f);
		dx = getStage().getCamera().position.x/1000f;
		batch.draw(GameAssets.i.grid1, 
				(getStage().getCamera().position.x - getStage().getWidth()) ,  
				getStage().getCamera().position.y - getStage().getHeight(), 
				
				getStage().getWidth() * 2, 
				getStage().getHeight() * 2, 
				
				dx, 0, 
				dx + 2, 16);
	}
}
