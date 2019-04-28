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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import net.mgsx.ld44.actors.CoinActor;
import net.mgsx.ld44.actors.CurveActor;
import net.mgsx.ld44.actors.GridActor;
import net.mgsx.ld44.actors.HeroActor;
import net.mgsx.ld44.audio.GameAudio;
import net.mgsx.ld44.model.CurrencyCurve;
import net.mgsx.ld44.model.CurrencyGame;
import net.mgsx.ld44.model.GameRules;
import net.mgsx.ld44.screens.GameScreen;
import net.mgsx.ld44.utils.Scene;

public class CurvesScene extends Group implements Scene{

	private CurrencyGame game;
	private ShapeRenderer shapeRenderer;
	public final Vector2 worldCenter = new Vector2(0,0);
	private float stepSize = 400;
	private HeroActor hero;
	private static final Vector2 point = new Vector2();
	private float t;
	
	private Array<Actor> entities = new Array<Actor>();
	
	private float heroCurveTime;
	private float worldSpeed = 200;

	private final static Array<CoinActor> nextCoins = new Array<CoinActor>();
	private final static Array<CoinActor> toRemoveCoins = new Array<CoinActor>();
	
	private static final float LOCK_FOR_FUSION = 2;
	private static final float LOCK_FOR_HERO_TRANSFORM = 8;
	
	private static final int MAX_QUEUE = 5;
	
	private float lockBar = 0;
	
	// TODO plae and check bonus and other entities ...
	
	public CurvesScene() {
		
		addActor(new GridActor());
		
		shapeRenderer = new ShapeRenderer();
		
		setTransform(false);
		
		game = new CurrencyGame();
		
		Color[] colors = new Color[]{Color.RED, Color.ORANGE, Color.GREEN};
		
		for(int i=0 ; i<3 ; i++){
			CurrencyCurve c = new CurrencyCurve().set(i, colors[i%colors.length]);
			for(int j=0 ; j<c.controlPoints.length ; j++){
				c.add(point.set(j * stepSize, 100));
			}
			game.curves.add(c);
			addActor(new CurveActor(shapeRenderer, c));
		}
		worldCenter.x = CurrencyCurve.BUFFER_SIZE * stepSize;
		
		hero = new HeroActor(0);
		hero.curve = game.curves.first();
		addActor(hero);
		
		GameAudio.i.playMusicGame1();
	}
	
	// TODO when gen points : f (time) + last points position
	private void genPoint() {
		for(CurrencyCurve c : game.curves){
			
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
	
	private float getCurveTime(float time) {
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
			
			if(GameAudio.i.isJustBar(1, 0)){
				spawnBonus(heroCurveTime + getCurveTime(GameAudio.i.getBarDuration(0f)));
			}
			
			for(CurrencyCurve c : game.curves){
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
				for(CurrencyCurve c2 : game.curves){
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
			((OrthographicCamera)cam).zoom = 1.5f;
			
			while(hero.coins.size > MAX_QUEUE){
				hero.coins.pop().addAction(
						Actions.sequence(
								Actions.parallel(
										Actions.scaleBy(0.5f, 0.5f, 1f, Interpolation.pow2), 
										Actions.alpha(0, 1f)), 
								Actions.removeActor()
								)
						);
				updateHeroTail(this, hero);
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
		}
		
		
		
		for(Actor e : entities){
			if(e.getParent() == null){
				
			}
			
			float hx = hero.getX();
			float hy = hero.getY();
			float ex = e.getX();
			float ey = e.getY();
			if(point.set(ex - hx, ey - hy).len() < 64f - 20){ // tODO radius
				if(e instanceof CoinActor){
					CoinActor coin = (CoinActor) e;
					if(coin.head == null){
						hero.addCoin(coin);
						updateHeroTail(this, hero);
						
						GameAudio.i.playGrabCoinSync(coin.type, .5f);
						coin.addAction(Actions.sequence(
								Actions.scaleTo(2, 2, .1f, Interpolation.sine),
								Actions.scaleTo(1, 1, .5f, Interpolation.elasticOut)
								));
					}
					
					// e.remove();
				}
			}
		}
		for(int i=0 ; i<entities.size ; ) if(entities.get(i).getParent() == null) entities.removeIndex(i); else i++;
		
		super.act(delta);
	}

	


	private void spawnBonus(float curveTime) {
		// TODO not specially hero curve
		
		hero.curve.getPosition(point, curveTime);
		
		
		CoinActor coinActor = new CoinActor(GameRules.randomCoinTypeFor(hero.type)); // TODO not random sequence
		
		// TODO séparer dans un swpawner de bonus : some predefined seqs !
		
		// spawn actor with rythm
		coinActor.setScale(4);
		coinActor.getColor().a = 0f;
		addActor(coinActor);
		
		coinActor.addAction(Actions.sequence(syncAction(1f), Actions.parallel(
				Actions.scaleTo(1, 1, .5f, Interpolation.pow3InInverse),
				Actions.alpha(1, .2f, Interpolation.linear))));
		
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
		
		
		
		// TODO handle head (hero) transform
		
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
						
						lockBar = LOCK_FOR_FUSION;
						
						//nextCoins.add(newCoin);
						while(toRemoveCoins.size > 0){
							toRemoveCoins.pop().remove();
						}
						count = 1;
						ctype = newCoin.type;
						toRemoveCoins.add(newCoin);
						i++;
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
		
		hero.tail = hero.coins.size > 0 ? hero.coins.peek() : hero;
		
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
