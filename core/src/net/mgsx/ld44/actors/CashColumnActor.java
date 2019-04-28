package net.mgsx.ld44.actors;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import net.mgsx.ld44.assets.GameAssets;

public class CashColumnActor extends Image
{
	public float time;
	private Integer targetRank;

	public CashColumnActor() {
		super(new TextureRegion(GameAssets.i.hero, 0, 0, 64, 64));
	}
	
	@Override
	public void act(float delta) {
		if(targetRank != null){
			time+=delta * .3f;
			TextureRegion region = ((TextureRegionDrawable)getDrawable()).getRegion();
			if(time > 1){
				region.setV(targetRank / 16f);
				region.setV2((targetRank+1) / 16f);
			}else{
				region.setV(MathUtils.lerp(region.getV(), targetRank/ 16f, .1f));
				region.setV2(region.getV() + 1f / 16f);
			}
		}else{
			float d = delta * .5f;
			TextureRegion region = ((TextureRegionDrawable)getDrawable()).getRegion();
			region.setV(region.getV() - d);
			region.setV2(region.getV2() - d);
		}
		super.act(delta);
	}

	public void stopTo(int rank) {
		targetRank = rank;
	}

	public boolean finished() {
		return time < 1;
	}
	
}
