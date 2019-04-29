package net.mgsx.ld44.actors;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

import net.mgsx.ld44.assets.GameAssets;
import net.mgsx.ld44.audio.GameAudio;
import net.mgsx.ld44.model.CasinoResult;
import net.mgsx.ld44.model.GameRules;
import net.mgsx.ld44.scenes.CurvesScene;
import net.mgsx.ld44.utils.QuickGdx;

public class CashMachineActor extends Group
{
	private Image body;
	public int radius;
	public boolean used = false;
	public boolean alive = true;

	public CashMachineActor() {
		body = QuickGdx.image(GameAssets.i.hero, 10 * 64, 7 * 64, 64, 64);
		addActor(body);
		body.setPosition(0, 0, Align.center);
	}
	
	@Override
	public void act(float delta) {
		radius = 24 * 2;
		
		super.act(delta);
//		body.setY((MathUtils.sin(GameAudio.i.timeToRad() / .5f)*.5f+.5f) * 64 * 2 + 32);
//		body.setScale((MathUtils.sin(GameAudio.i.timeToRad() / 1f)*.5f+.5f) * .5f + 1);
//		body.setY((MathUtils.sin(GameAudio.i.timeToRad() / 4f)) * 64 * 4 + 0);
//		body.setScale(2);
//		body.setOrigin(Align.center);
	}

	public float centerX() {
		return getX() + body.getX(Align.center);
	}

	public float centerY() {
		return getY() + body.getY(Align.center);
	}

	public void playCasino(final CurvesScene scene, HeroActor hero, Action endAction) {
		if(used) return;
		used = true;
		float delay = 0;
		for(CoinActor coin : hero.coins){
			coin.toFront();
			coin.head = null;
			coin.addAction(Actions.sequence(
					Actions.delay(delay),
					Actions.moveTo((getX() + coin.getX())/2, (getY() + coin.getY())/2 + 228, .3f, Interpolation.pow2),
					Actions.moveTo(getX() + 128, getY() + 128, .3f, Interpolation.pow2InInverse),
					Actions.removeActor()
					));
			delay += .2f;
		}
		hero.tail = hero;
		hero.coins.clear();
		
		
		body.remove();
		body = QuickGdx.image(GameAssets.i.hero, 6 * 64, 10 * 64, 5 * 64, 6 * 64);
		addActor(body);
		body.setScale(1f/6f);
		body.setOrigin(0,0);
		
		
		final CashColumnActor col1 = new CashColumnActor();
		final CashColumnActor col2 = new CashColumnActor();
		final CashColumnActor col3 = new CashColumnActor();
		addActor(col1);
		addActor(col2);
		addActor(col3);
		col1.setPosition(64, 64 * 2);
		col2.setPosition(64 * 2, 64 * 2);
		col3.setPosition(64 * 3, 64 * 2);
		col1.setVisible(false);
		col2.setVisible(false);
		col3.setVisible(false);
		
		// TODO rules
		final CasinoResult r = GameRules.genCasino(hero.type);
		
		scene.addScore(GameRules.scoreForCasino(r));
		
		GameAudio.i.softMuteMusic(true);
		
		//body.setPosition(0, 0, Align.center);
		body.addAction(Actions.sequence(
				Actions.scaleTo(1, 1, 1f, Interpolation.sine), 
				Actions.delay(delay),
				
				Actions.run(new Runnable() {
					
					@Override
					public void run() {
						col1.setVisible(true);
						col2.setVisible(true);
						col3.setVisible(true);
					
					}
				}),
				
				new Action() {
					@Override
					public boolean act(float delta) {
						col1.stopTo(r.combi1);
						return col1.finished();
					}
				},
				
				new Action() {
					@Override
					public boolean act(float delta) {
						col2.stopTo(r.combi2);
						return col2.finished();
					}
				},
				
				new Action() {
					@Override
					public boolean act(float delta) {
						col3.stopTo(r.combi3);
						return col3.finished();
					}
				},
				
				Actions.delay(2f), // TODO rolling
				
				// TODO spawn bonus points
				Actions.run(new Runnable() {
					@Override
					public void run() {
						spawnBonus(scene, r.bonusType, r.bonusCount);
					}
				}),
				
				Actions.run(new Runnable() {
					
					@Override
					public void run() {
						col1.setVisible(false);
						col2.setVisible(false);
						col3.setVisible(false);
					
						GameAudio.i.softMuteMusic(false);
					}
				}),
				
				Actions.scaleTo(0, 0, 1f),
				
				
				endAction, 
				Actions.removeActor(this)));
	}

	protected void spawnBonus(CurvesScene scene, int combiResultType, int combiResultCount) {
		
		for(int i=0 ; i<combiResultCount ; i++){
			scene.spawnBonus(scene.heroCurveTime + scene.getCurveTime(GameAudio.i.getBarDuration(.25f) * (i + 1 - 16)), combiResultType);
		}
	}
}
