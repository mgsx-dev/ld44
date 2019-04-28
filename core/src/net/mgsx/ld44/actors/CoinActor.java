package net.mgsx.ld44.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
	
	private Animation<TextureRegion> idleAnim;

	public CoinActor(int type) {
		this.type = type;
		TextureRegion tBody = new TextureRegion(GameAssets.i.hero, 0, type * 64, 64, 64);
		body = new Image(tBody);
		addActor(body);
		body.setOrigin(Align.center);
		body.setPosition(0, 0, Align.center);
		
		idleAnim = QuickGdx.animation(GameAssets.i.hero, 0, type * 64, 64, 64, 64, 0, 4);
	}
	
	@Override
	public void act(float delta) {
		
		
		time += delta * 10;
		
		
		((TextureRegionDrawable)body.getDrawable()).setRegion(idleAnim.getKeyFrame(time, true));
		if(head != null){
			((TextureRegionDrawable)body.getDrawable()).setRegion(idleAnim.getKeyFrame(0, true));

			QuickGdx.follow(this, head, 10f * delta, 5f); // radius + half radius
			body.setScale(1f);
		}else{
			//time += delta * 360f;
			//body.setScaleX(MathUtils.sinDeg(time));
			
			if(getX() + GameScreen.WORLD_WIDTH/2 < getStage().getCamera().position.x){
				remove();
			}
		}
		
		super.act(delta);
	}
}
