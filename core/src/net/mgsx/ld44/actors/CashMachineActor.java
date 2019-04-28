package net.mgsx.ld44.actors;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

import net.mgsx.ld44.assets.GameAssets;
import net.mgsx.ld44.audio.GameAudio;
import net.mgsx.ld44.utils.QuickGdx;

public class CashMachineActor extends Group
{
	private Image body;
	public int radius;

	public CashMachineActor() {
		body = QuickGdx.image(GameAssets.i.hero, 4 * 64, 0, 64, 64);
		addActor(body);
		body.setPosition(0, 0, Align.center);
	}
	
	@Override
	public void act(float delta) {
		radius = 24 * 2;
		
		super.act(delta);
//		body.setY((MathUtils.sin(GameAudio.i.timeToRad() / .5f)*.5f+.5f) * 64 * 2 + 32);
//		body.setScale((MathUtils.sin(GameAudio.i.timeToRad() / 1f)*.5f+.5f) * .5f + 1);
		body.setY((MathUtils.sin(GameAudio.i.timeToRad() / 4f)) * 64 * 4 + 0);
		body.setScale(2);
		body.setOrigin(Align.center);
	}

	public float centerX() {
		return getX() + body.getX(Align.center);
	}

	public float centerY() {
		return getY() + body.getY(Align.center);
	}
}
