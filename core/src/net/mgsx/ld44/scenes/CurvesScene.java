package net.mgsx.ld44.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import net.mgsx.ld44.actors.CashMachineActor;
import net.mgsx.ld44.actors.ClockActor;
import net.mgsx.ld44.actors.CoinActor;
import net.mgsx.ld44.actors.CurveActor;
import net.mgsx.ld44.actors.GridActor;
import net.mgsx.ld44.actors.HUDActor;
import net.mgsx.ld44.actors.HeroActor;
import net.mgsx.ld44.actors.PigActor;
import net.mgsx.ld44.assets.GameAssets;
import net.mgsx.ld44.audio.GameAudio;
import net.mgsx.ld44.model.CurrencyCurve;
import net.mgsx.ld44.model.CurrencyGame;
import net.mgsx.ld44.model.GameRules;
import net.mgsx.ld44.model.MetaGame;
import net.mgsx.ld44.screens.GameScreen;
import net.mgsx.ld44.utils.Scene;

public class CurvesScene extends Group implements Scene{

	private ShapeRenderer shapeRenderer;
	public final Vector2 worldCenter = new Vector2(0,0);
	private float stepSize = 400;
	public HeroActor hero;
	private static final Vector2 point = new Vector2();
	private float t;
	
	private Array<Actor> entities = new Array<Actor>();
	
	public float heroCurveTime;
	private float worldSpeed = 200;

	private final static Array<CoinActor> nextCoins = new Array<CoinActor>();
	private final static Array<CoinActor> toRemoveCoins = new Array<CoinActor>();
	
	private static final float LOCK_FOR_FUSION = 2;
	private static final float LOCK_FOR_HERO_TRANSFORM = 8;
	
	private static final int MAX_QUEUE = 5;
	
	private float lockBar = 0;
	private ClockActor clockActor;
	private HUDActor hud;
	private Label label;
	
	// TODO plae and check bonus and other entities ...
	
	public CurvesScene() {
		
		addActor(clockActor = new ClockActor());
		
		addActor(new GridActor());
		
		shapeRenderer = new ShapeRenderer();
		
		setTransform(false);
		
		MetaGame.i.game = new CurrencyGame();
		
		Color[] colors = new Color[]{Color.RED, Color.ORANGE, Color.GREEN};
		
		for(int i=0 ; i<3 ; i++){
			CurrencyCurve c = new CurrencyCurve().set(i, colors[i%colors.length]);
			for(int j=0 ; j<c.controlPoints.length ; j++){
				c.add(point.set(j * stepSize, 100));
			}
			MetaGame.i.game.curves.add(c);
			addActor(new CurveActor(shapeRenderer, c));
		}
		worldCenter.x = CurrencyCurve.BUFFER_SIZE * stepSize;
		
		hero = new HeroActor(0);
		hero.curve = MetaGame.i.game.curves.first();
		addActor(hero);
		
		GameAudio.i.playMusicGame2();
		
		addActor(hud = new HUDActor());
	}
	
	// TODO when gen points : f (time) + last points position
	private void genPoint() {
		for(CurrencyCurve c : MetaGame.i.game.curves){
			
			float freq = .1f;
			c.add(point.set(worldCenter.x,
					Math.max(-10, 
							((.5f + .5f * MathUtils.sinDeg(worldCenter.x * freq + (1 + c.index) * 60f)) * 500 + 
							(MathUtils.random() - .6f) * 1300 +
							c.index * 100) * 1 + 100
					)));
			
			// genBonus();
		}
	}
	
	private float worldToCurveTime(float worldX){
		int worldStep = (int)(worldX / stepSize);
		float worlRemain = worldX / stepSize - worldStep;
		
//		hero.setX(MathUtils.lerp(a.x, b.x, t));
//		hero.setY(MathUtils.lerp(a.y, b.y, t));

		// TODO 1.4f ?? len or average seg len ? or derivative
		float t2 = 1.43f*(CurrencyCurve.BUFFER_SIZE/4 + worlRemain) / (float)CurrencyCurve.BUFFER_SIZE;
		// float t2 = t;
		return t2;
	}
	
	public float getCurveTime(float time) {
		return worldToCurveTime(time * worldSpeed) + .01f;
	}
	
	@Override
	public void act(float delta) {
		
		float moveDelta = delta;
		if(lockBar > 0){
			if(GameAudio.i.isJustBar(lockBar, 0)){
				lockBar = 0;
			}else{
				moveDelta = 0;
			}
		}else if(lockBar < 0){
			moveDelta = 0;
		}
		
		worldSpeed = 500;
		
		// TODO Spawn bonus logic
//		if(GameAudio.i.isJustBar(4)){
//			spawnBonus(heroCurveTime + getCurveTime(GameAudio.i.getNextBarDelay(0f)));
//			spawnBonus(heroCurveTime + getCurveTime(GameAudio.i.getNextBarDelay(.5f)));
//			spawnBonus(heroCurveTime + getCurveTime(GameAudio.i.getNextBarDelay(1)));
//			spawnBonus(heroCurveTime + getCurveTime(GameAudio.i.getNextBarDelay(1.5f)));
//			spawnBonus(heroCurveTime + getCurveTime(GameAudio.i.getNextBarDelay(2f)));
//		}
		
		Camera cam = getStage().getCamera();
		
		// TODO split in different method r class
		if(lockBar <= 0){
			
			if(GameAudio.i.lastEvents.size > 0){
				spawnBonus(heroCurveTime + getCurveTime(GameAudio.i.getBarDuration(1f)), null);
			}
			
			if(GameAudio.i.isJustBar(1, 0)){
				// XXX spawnBonus(heroCurveTime + getCurveTime(GameAudio.i.getBarDuration(0f)));
			}
			
			for(CurrencyCurve c : MetaGame.i.game.curves){
				c.update();
			}
			
			
			float lastX = worldCenter.x;
			worldCenter.x += moveDelta * worldSpeed;
			if((int)(lastX / stepSize) != (int)(worldCenter.x / stepSize)){
				genPoint();
			}
			
			
			float t2 = worldToCurveTime(worldCenter.x);
			float curY = point.y;
			
			heroCurveTime = t2;

			hero.curve.getPosition(point, t2);
			hero.setX(point.x);
			
			if(hero.jumpHeight > 0 && hero.baseY + hero.jumpHeight > point.y || hero.jumpVel > 0){
				hero.setY(hero.baseY + hero.jumpHeight);
				
				// test other curves above
				for(CurrencyCurve c2 : MetaGame.i.game.curves){
					c2.getPosition(point, t2);
					if(true){
						if(point.y < hero.baseY + hero.jumpHeight && point.y > curY){
							hero.curve = c2;
							hero.jumpHeight += -point.y + hero.baseY;
							hero.baseY = point.y;
							curY = point.y;
							
						}
					}
				}
				
			}else{
				hero.jumpVel = 0;
				hero.jumpHeight = 0;
				hero.setY(hero.baseY = point.y);
			}
			
			
			cam.position.x = hero.getX(Align.center) + GameScreen.WORLD_WIDTH/4;
			cam.position.y = Math.max(GameScreen.WORLD_HEIGHT/2, MathUtils.lerp(cam.position.y, hero.getY(Align.center), .1f));
			
			// XXX cam zoom debug
			float expectedoom = MathUtils.lerp(2f, 1f, hero.type / 15f);
			((OrthographicCamera)cam).zoom = MathUtils.lerp(((OrthographicCamera)cam).zoom, expectedoom, delta * .5f);
			
			while(hero.coins.size > MAX_QUEUE){
				CoinActor coin = hero.coins.pop();
				coin.addAction(
						Actions.sequence(
								Actions.parallel(
										Actions.scaleBy(0.5f, 0.5f, 1f, Interpolation.pow2), 
										Actions.alpha(0, 1f)), 
								Actions.removeActor()
								)
						);
				updateHeroTail(this, hero);
				// XXX addScore(GameRules.scoreForUnqueue(coin));
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.Z)){
				if(hero.coins.size > 0){
					hero.coins.removeIndex(0).remove();
					updateHeroTail(this, hero);
				}
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.E)){
				if(hero.coins.size > 0){
					hero.coins.add(hero.coins.removeIndex(0));
					updateHeroTail(this, hero);
				}
			}
			
			
			if(Gdx.input.isKeyJustPressed(Input.Keys.S) && hero.jump<=0){
				// FIXME a little buggy ... fall at down level
				hero.setY(hero.baseY = hero.baseY - 50);
				hero.jumpVel = -4;
				hero.jump = 1;
				for(CurrencyCurve c2 : MetaGame.i.game.curves){
					if(c2 == hero.curve) continue;
					c2.getPosition(point, t2);
					if(true){
						if(point.y < hero.baseY + hero.jumpHeight && point.y > curY){
							hero.curve = c2;
							hero.jumpHeight += -point.y + hero.baseY;
							hero.baseY = point.y;
							curY = point.y;
						}
					}
				}
				hero.setY(hero.baseY = hero.baseY - 50);
			}
			
			if(Gdx.input.isKeyJustPressed(Input.Keys.G)){
				PigActor pig = new PigActor();
				addActor(pig);
				hero.curve.getPosition(point, heroCurveTime + getCurveTime(GameAudio.i.getBarDuration(1f)));
				pig.setPosition(point.x, point.y);
				entities.add(pig);
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.H)){
				CashMachineActor mob = new CashMachineActor();
				addActor(mob);
				hero.curve.getPosition(point, heroCurveTime + getCurveTime(GameAudio.i.getBarDuration(1f)));
				mob.setPosition(point.x, point.y);
				entities.add(mob);
			}
		}
		
		
		
		for(Actor e : entities){
			if(e.getX() < getStage().getCamera().position.x - getStage().getWidth()){
				e.remove();
			}
			
			float hx = hero.getX();
			float hy = hero.getY();
			float ex = e.getX();
			float ey = e.getY();
			if(e instanceof CoinActor){
				if(point.set(ex - hx, ey - hy).len() < 64f - 20){ // tODO radius
					CoinActor coin = (CoinActor) e;
					if(coin.head == null){
						hero.addCoin(coin);
						updateHeroTail(this, hero);
						
						GameAudio.i.playGrabCoinSync(coin.type, .5f);
						coin.addAction(Actions.sequence(
								Actions.scaleTo(2, 2, .1f, Interpolation.sine),
								Actions.scaleTo(1, 1, .5f, Interpolation.elasticOut)
								));
						addScore(GameRules.scoreForEnqueue(coin));
					}
				}
				// e.remove();
			}else if(e instanceof PigActor){
				PigActor pig = (PigActor)e;
				if(point.set(pig.centerX() - hx, pig.centerY() - hy).len() < pig.radius + 32){ // tODO radius
					e.setOrigin(Align.center);
					e.addAction(Actions.sequence(
							Actions.parallel(
									Actions.scaleBy(2, 2, .3f),
									Actions.moveBy(10, 100, 1f, Interpolation.pow3InInverse)
									),
							Actions.removeActor()
							));
					
					if(hero.coins.size > 0){
						while(hero.coins.size>0) {
							hero.coins.removeIndex(0).remove();
						}
					}else{
						if(hero.type>0){
							hero.setType(hero.type-1);
						}else{
							// TODO die !
						}
					}
					lockBar = 4;
					updateHeroTail(this, hero);
				}
			}else if(e instanceof CashMachineActor){
				CashMachineActor mob = (CashMachineActor)e;
				if(point.set(mob.centerX() - hx, mob.centerY() - hy).len() < mob.radius + 32){ // tODO radius
					
					
					
					// lock all 
					lockBar = -1; // TODO infinite
					
					mob.playCasino(this, hero, Actions.run(new Runnable() {
						@Override
						public void run() {
							lockBar = 1;
						}
					}));
					updateHeroTail(this, hero);
				}
			}
		}
		for(int i=0 ; i<entities.size ; ) if(entities.get(i).getParent() == null) entities.removeIndex(i); else i++;
		
		super.act(delta);
	}

	



	public void addScore(int points) {
		MetaGame.i.game.score += points;
//		if(label != null && label.getParent() != null){
//			return;
//		}
		label = new Label(String.valueOf(points), GameAssets.i.skin);
		label.setFontScale(2);
		label.getColor().a = 1f;
		getStage().addActor(label);
		label.setPosition(hero.getX(Align.right) + 50, hero.getY(Align.top) + 100, Align.center);
		label.addAction(Actions.sequence(
				Actions.parallel(
						Actions.moveBy(0, 200f, 1f),
						Actions.alpha(0, 1f, Interpolation.pow2)
						),
				Actions.removeActor()
				));
		
	}

	public void spawnBonus(float curveTime, Integer type) {
		// TODO not specially hero curve
		
		hero.curve.getPosition(point, curveTime);
		
		if(type == null){
			type = GameRules.randomCoinTypeFor(hero.type);
		}
		
		CoinActor coinActor = new CoinActor(type); // TODO not random sequence
		
		// TODO séparer dans un swpawner de bonus : some predefined seqs !
		
		// spawn actor with rythm
		coinActor.setScale(4);
		coinActor.getColor().a = 0f;
		addActor(coinActor);
		
		coinActor.addAction(Actions.sequence(syncAction(1f), Actions.parallel(
				Actions.scaleTo(1, 1, .2f, Interpolation.pow3InInverse),
				Actions.alpha(1, .05f, Interpolation.linear))));
		
		coinActor.setPosition(point.x, point.y + 1 * 32f);
		
		
		
		entities.add(coinActor);
	}

	private Action syncAction(float barPrecision) {
		return Actions.delay(GameAudio.i.getNextBarTimeRemain(barPrecision));
	}

	private void genBonus() {
		hero.curve.getPosition(point, heroCurveTime + GameAudio.i.getBarDuration(4) / stepSize);
		CoinActor coinActor = new CoinActor(MathUtils.random(2));
		addActor(coinActor);
		coinActor.setPosition(point.x, point.y + 1 * 32f);
		
		entities.add(coinActor);

	}

	public void updateHeroTail(Group container, HeroActor hero)
	{
		boolean enabled = true;
		if(!enabled) return;
		
		
		
		// TODO why other transform leave hero head ????? maybe tail : hero tail ??? :grim:
		
		toRemoveCoins.clear();
		nextCoins.clear();
		int ctype = -1;
		int count = 0;
		int groupSizeForType = -1;
		
		for(int i=hero.coins.size-1 ; i>=0 ; i--){
			CoinActor coin = hero.coins.get(i);
			if(count == 0){
				ctype = coin.type;
				count = 1;
				groupSizeForType = GameRules.groupSizeForCoinType(ctype);
				// Fake : groupSizeForType = ctype % 3 == 2 ? 2 : 2;
				toRemoveCoins.add(coin);
			}else{
				if(coin.type == ctype){
					count++;
					toRemoveCoins.add(coin);
					if(count >= groupSizeForType){
						
						CoinActor newCoin = new CoinActor(GameRules.coinTransformType(ctype));
						newCoin.setPosition(toRemoveCoins.first().getX(), toRemoveCoins.first().getY());
						container.addActor(newCoin);
						
						newCoin.addAction(Actions.sequence(
								Actions.scaleTo(3, 3, .3f, Interpolation.sine),
								Actions.scaleTo(1, 1, .5f, Interpolation.elasticOut)
								));
						
						addScore(GameRules.scoreForFusion(newCoin));
						
						lockBar = LOCK_FOR_FUSION;
						
						// nextCoins.add(newCoin);
						while(toRemoveCoins.size > 0){
							toRemoveCoins.pop().remove();
						}
						count = 1;
						ctype = newCoin.type;
						toRemoveCoins.add(newCoin);
						//i++;
					}
				}else{
					while(toRemoveCoins.size > 0){
						nextCoins.add(toRemoveCoins.pop());
					}
					count = 0;
					i++;
				}
			}
		}
		// check hero
		if(hero.type == ctype && count + 1 >= GameRules.groupSizeForCoinType(ctype)){
			while(toRemoveCoins.size > 0){
				toRemoveCoins.pop().remove();
			}
			addScore(GameRules.scoreForHeroTransform(hero.type));
			
			hero.setType(GameRules.coinTransformType(ctype));
			
			lockBar = LOCK_FOR_HERO_TRANSFORM;
		}
		
		
		nextCoins.addAll(toRemoveCoins);
		
		
		toRemoveCoins.clear();
		hero.coins.clear();
		
		nextCoins.reverse();
		if(nextCoins.size > 0) nextCoins.get(0).head = hero;
		for(int i=1 ; i<nextCoins.size ; i++){
			nextCoins.get(i).head = nextCoins.get(i-1);
			
		}
		
		hero.coins.addAll(nextCoins);
		nextCoins.clear();
		
		hero.tail = hero.coins.size > 0 ? hero.coins.first() : hero;
		
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
