package net.mgsx.ld44.actors;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

import net.mgsx.ld44.assets.GameAssets;
import net.mgsx.ld44.screens.GameScreen;
import net.mgsx.ld44.utils.QuickGdx;

public class CoinActor extends Group
{
	private Image body;
	private float time;
	public Actor head;
	
	public int type;

	public CoinActor(int type) {
		this.type = type;
		TextureRegion tBody = new TextureRegion(GameAssets.i.hero, 0, type * 64, 64, 64);
		body = new Image(tBody);
		addActor(body);
		body.setOrigin(Align.center);
		body.setPosition(0, 0, Align.center);
	}
	
	@Override
	public void act(float delta) {
		
		if(head != null){
			QuickGdx.follow(this, head, 10f * delta, 5f); // radius + half radius
			body.setScale(1f);
		}else{
			time += delta * 360f;
			body.setScaleX(MathUtils.sinDeg(time));
			
			if(getX() + GameScreen.WORLD_WIDTH/2 < getStage().getCamera().position.x){
				remove();
			}
		}
		
		super.act(delta);
	}
}
