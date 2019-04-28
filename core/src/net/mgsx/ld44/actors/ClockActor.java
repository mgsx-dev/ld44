package net.mgsx.ld44.actors;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import net.mgsx.ld44.assets.GameAssets;
import net.mgsx.ld44.audio.GameAudio;
import net.mgsx.ld44.model.MetaGame;
import net.mgsx.ld44.utils.QuickGdx;

public class ClockActor extends Group
{
	private Image back;
	private Image center;
	private Image hours;
	private Image minutes;

	private Array<Image> dots = new Array<Image>();
	private Label label;
	
	public ClockActor() {
		back = QuickGdx.image(GameAssets.i.hero, 10 * 64, 0 * 64, 6 * 64, 6 * 64);
		center = QuickGdx.image(GameAssets.i.hero, 10 * 64, 6 * 64, 1 * 64, 1 * 64);
		minutes = QuickGdx.image(GameAssets.i.hero, 11 * 64, 6 * 64, 5 * 64, 1 * 64);
		hours = QuickGdx.image(GameAssets.i.hero, 12 * 64, 7 * 64, 4 * 64, 1 * 64);
		label = new Label("00:00", GameAssets.i.skin);
		int N = 12;
		for(int i=0 ; i<N ; i++){
			Image dot = QuickGdx.image(GameAssets.i.hero, 10 * 64, 6 * 64, 1 * 64, 1 * 64);
			float angle = (float)i / (float)N * 360f;
			float r = 150;
			dot.setPosition(MathUtils.cosDeg(angle) * r, MathUtils.sinDeg(angle) * r, Align.center);
			addActor(dot);
			dots.add(dot);
		}
		
		addActor(back);
		addActor(hours);
		addActor(minutes);
		addActor(center);
		addActor(label);
		label.setPosition(0, 0, Align.center);
	}
	
	@Override
	public void act(float delta) {
		MetaGame.i.game.time += delta;
		
		int value = MathUtils.ceil((MetaGame.i.game.totalTime - MetaGame.i.game.time)*100*4);
		int h1 = (value/100)/60;
		int m1 = (value/100)%60;
		int s1 = value%100;
		label.setText(String.valueOf(h1 + ":" + m1 + ":" + s1));
		
		getColor().a = .3f;
		setScale(2);
		
		setPosition(getStage().getCamera().position.x, getStage().getCamera().position.y);
		
		hours.setOrigin(32, 32);
		minutes.setOrigin(32, 32);
		
		hours.setSize(150, 64);
		minutes.setSize(150, 64);
		
		float baseSize = 128;
		float boomSize = 2;
		float centerSize = MathUtils.lerp(center.getWidth(), baseSize, .03f);
		center.setSize(centerSize, centerSize);
		for(Image dot : dots){
			dot.setOrigin(Align.center);
			float s = MathUtils.lerp(dot.getScaleX(), 1, .05f);;
			dot.setScale(s, s);
		}
		
		if(GameAudio.i.lastEvents.size > 0){
			center.setSize(baseSize * boomSize, baseSize * boomSize);
			
		}
		if(GameAudio.i.isJustBar(1, 0)){
			dots.random().setScale(boomSize,boomSize);
			
		}
		
		back.setPosition(0, 0, Align.center);
		center.setPosition(0, 0, Align.center);
		hours.setPosition(-32, -32);
		minutes.setPosition(-32, -32);
		
		
		float dateTime = (MetaGame.i.game.time / MetaGame.i.game.totalTime) * 12; // 10 minute : 12h
		
		float h = dateTime / 12f;
		float m = dateTime % 12f;
		
		hours.setRotation(-360 * h);
		minutes.setRotation(-360 * m);
		
		super.act(delta);
	}
}
