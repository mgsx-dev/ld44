package net.mgsx.ld44.scenes;

import com.badlogic.gdx.Gdx;
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
import net.mgsx.ld44.actors.GameOverActor;
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
import net.mgsx.ld44.utils.UniControl;

public class CurvesScene extends Group implements Scene{

	private ShapeRenderer shapeRenderer;
	public final Vector2 worldCenter = new Vector2(0,0);
	private float stepSize = 500;
	public HeroActor hero;
	private static final Vector2 point = new Vector2();
	private float t;
	
	private Array<Actor> entities = new Array<Actor>();
	
	public float heroCurveTime;
	private float worldSpeed = 200;

	private final static Array<CoinActor> nextCoins = new Array<CoinActor>();
	private final static Array<CoinActor> toRemoveCoins = new Array<CoinActor>();
	
	private static final float LOCK_FOR_FUSION = 0;
	private static final float LOCK_FOR_HERO_TRANSFORM = 8;
	
	private static final int MAX_QUEUE = 5;
	private static final float TIME_RATE_FALLDOWN = 1f;
	private static final float PIG_COLLISION_BAR = 0;
	private static final float CHANGE_MIDI_NOMIDI_PERIOD = 4; // every 10 seconds
	
	private float noMidiSpeed;
	
	private float lockBar = 0;
	private ClockActor clockActor;
	private HUDActor hud;
	private Label label;
	private GameScreen gameScreen;
	
	private float curveAmplitude = 1f;
	private float curveFrequency = .1f;
	
	private float cachMachineTime = 0;
	private float pigTime = 0;
	
	private float curveChangeTimeout;
	private int spawnCurveIndex;
	
	public CurvesScene(GameScreen gameScreen, int musicIndex) {
		this.gameScreen = gameScreen;
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
		
		GameAudio.i.playMusicGame(musicIndex);
		
		addActor(hud = new HUDActor());
	}
	
	private void genPoint() {
		
		for(CurrencyCurve c : MetaGame.i.game.curves){
			
			float freq = curveFrequency; 
			float amplitude = curveAmplitude; 
			
			float height = Math.max(-10, 
					((.5f + .5f * MathUtils.sinDeg(worldCenter.x * freq + (1 + c.index) * 60f)) * 500 + 
					(MathUtils.random() - .6f) * 1300 +
					c.index * 100) * 1 + 100
			);
			
			height = height * amplitude;
			
			float timeRate =  MetaGame.i.game.time / MetaGame.i.game.totalTime;
			if(timeRate > TIME_RATE_FALLDOWN)
			{
				height = -1200;
			}
			
			c.add(point.set(worldCenter.x, height));
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
	
	private void lock(float n) {
		lockBar = n;
		gameScreen.postProcessing.emissive2Color.a = 1f;
	}
	
	private Actor gameOverActor = null;
	public boolean midiMode;
	private int noMidiBonusType;
	
	@Override
	public void act(float delta) {
		
		curveAmplitude = GameRules.getCurveAmplitude(hero);
		curveFrequency = GameRules.getCurveFrequency(hero);
		
		curveChangeTimeout -= delta;
		if(curveChangeTimeout < 0){
			curveChangeTimeout = GameRules.getChangeCurvePeriod(hero.type);
			spawnCurveIndex = MathUtils.random(2);
		}
		
		hud.toFront();
		
		MetaGame.i.game.update(delta);
		
		if(hero.getY(Align.center) < -600){
			MetaGame.i.game.over = true;
			MetaGame.i.game.fallOut = true;
		}
		
		if(MetaGame.i.game.over && gameOverActor == null){
			MetaGame.i.game.over = true;
			lock(-1);
			MetaGame.i.game.lockTime = true;
			
			hud.addActor(gameOverActor = new GameOverActor());
			
		}
		if(MetaGame.i.game.over){
			gameScreen.postProcessing.emissiveColor.a = MathUtils.lerp(gameScreen.postProcessing.emissiveColor.a, .5f, delta * 1.2f);
			gameScreen.postProcessing.emissive2Color.a = MathUtils.lerp(gameScreen.postProcessing.emissive2Color.a, 0, delta * 3f);
		}else{
			
			float targetAlpha = hero.type >= 15 ? 1f : hero.type==0 ? .6f : ( hero.type%2==1 ? 1 : .3f);
			gameScreen.postProcessing.emissiveColor.a = MathUtils.lerp(gameScreen.postProcessing.emissiveColor.a, Math.min(1f, targetAlpha), delta * 1.2f);
			
			// emi 2
			gameScreen.postProcessing.emissive2Color.a = MathUtils.lerp(gameScreen.postProcessing.emissive2Color.a, 0, delta * 3f);
		}
		

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
		
		
		
		worldSpeed = 500 + hero.type * 10;
		
		
		Camera cam = getStage().getCamera();
		
		// TODO split in different method r class
		if(lockBar == 0){
			
			cachMachineTime += delta;
			pigTime += delta;
			
			// change every 10s
			boolean newMode = MathUtils.sin(GameAudio.i.getMusicTime() * MathUtils.PI / CHANGE_MIDI_NOMIDI_PERIOD) > 0;
			
			newMode = true; // XXX fore midi only
//			cachMachineTime = 0; // XXX
//			pigTime = 0; // XXX
			
			if(newMode && !midiMode){
				noMidiSpeed = GameRules.newTempoBonusSpeed();
				noMidiBonusType = GameRules.randomCoinTypeFor(hero.type);
			}
			midiMode = newMode;
			
			
			if(!midiMode && GameAudio.i.isJustBar(noMidiSpeed, 0)){
//				if(cachMachineTime > 3){
//					cachMachineTime = 0;
//					spawnMachine(heroCurveTime + getCurveTime(GameAudio.i.getBarDuration(1f)), null);
//				}else if(pigTime > 2){
//					pigTime = 0;
//					spawnPig(heroCurveTime + getCurveTime(GameAudio.i.getBarDuration(1f)), null);
//				}else{
//					spawnBonus(heroCurveTime + getCurveTime(GameAudio.i.getBarDuration(1f)), null);
//				}
				
				spawnBonus(heroCurveTime + getCurveTime(GameAudio.i.getBarDuration(1f)), noMidiBonusType);
			}
					
					
			if(midiMode && GameAudio.i.lastEvents.size > 0){
				// choose between pig, cash or bonus :
				if(cachMachineTime > GameRules.cashMachinePeriod(MetaGame.i.game, hero)){
					cachMachineTime = 0;
					spawnMachine(heroCurveTime + getCurveTime(GameAudio.i.getBarDuration(1f)));
				}else if(pigTime > GameRules.pigPeriod(MetaGame.i.game, hero)){
					pigTime = 0;
					spawnPig(heroCurveTime + getCurveTime(GameAudio.i.getBarDuration(1f)));
				}else{
					spawnBonus(heroCurveTime + getCurveTime(GameAudio.i.getBarDuration(1f)), null);
				}
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
			
			float expectedoom = GameRules.getZoom(hero);
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
			
			// player controls
//			if(UniControl.isJustPressed(UniControl.LEFT)){
//				if(hero.coins.size > 0){
//					hero.coins.removeIndex(0).remove();
//					updateHeroTail(this, hero);
//				}
//			}
			
			if(UniControl.isJustPressed(UniControl.RIGHT)){
				if(hero.coins.size > 0){
					hero.coins.add(hero.coins.removeIndex(0));
					updateHeroTail(this, hero);
					GameAudio.i.playCoinRight();
				}
			}
			else if(UniControl.isJustPressed(UniControl.LEFT)){
				if(hero.coins.size > 0){
					hero.coins.insert(0, hero.coins.pop());
					updateHeroTail(this, hero);
					GameAudio.i.playCoinLeft();
				}
			}
			
			
			if(UniControl.isJustPressed(UniControl.DOWN) && hero.jump<=0){
				// FIXME a little buggy ... fall at down level
				
				// find curve just below current
				
				hero.setY(hero.baseY = hero.baseY);
				hero.jumpVel = -2;
				hero.jump = 1;
				float hMax = hero.curve.getPosition(point, t2).y;
				
				boolean found = false;
				for(CurrencyCurve c2 : MetaGame.i.game.curves){
					if(c2 == hero.curve) continue;
					c2.getPosition(point, t2);
					if(point.y < hMax){
						if(!found || point.y > hero.baseY){
							hero.curve = c2;
							hero.jumpHeight = 0; // += -point.y + hero.baseY - 32;
							hero.baseY = point.y;
							found = true;
						}
					}
				}
				hero.jumpHeight = hMax - hero.baseY - 1;
				hero.setY(hero.baseY);
			}
			
			// TODO !!!
			/*
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
			*/
		}
		
		
		if(lockBar == 0){
			for(Actor e : entities){
				if(e.getX() < getStage().getCamera().position.x - getStage().getWidth()){
					e.remove();
				}
				
				float hx = hero.getX();
				float hy = hero.getY();
				float ex = e.getX();
				float ey = e.getY();
				if(e instanceof CoinActor){
					CoinActor coin = (CoinActor) e;
					if(coin.head == null){
						if(point.set(ex - hx, ey - hy).len() < 64f - 20){ // tODO radius
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
					if(pig.alive){
						if(point.set(pig.centerX() - hx, pig.centerY() - hy).len() < pig.radius + 32){ // tODO radius
							pig.alive = false;
							e.setOrigin(Align.center);
							e.addAction(Actions.sequence(
									Actions.parallel(
											Actions.scaleBy(2, 2, .3f),
											Actions.moveBy(10, -500, 1f, Interpolation.pow3InInverse)
											),
									Actions.removeActor()
									));
							
							GameRules.onHeroVersusPig(hero, pig);
							lock(PIG_COLLISION_BAR);
							updateHeroTail(this, hero);
							
							GameAudio.i.playPigCollision();
							
							hero.setOrigin(Align.center);
							hero.addAction(
									Actions.sequence(
											Actions.scaleTo(2, 2, .3f, Interpolation.pow2), 
											Actions.scaleTo(1, 1, 1f, Interpolation.elasticOut)));
						}
					}
				}else if(e instanceof CashMachineActor){
					CashMachineActor mob = (CashMachineActor)e;
					if(mob.alive){
						if(point.set(mob.centerX() - hx, mob.centerY() - hy).len() < mob.radius + 32){ // tODO radius
							
							mob.alive = false;
							
							GameAudio.i.playMachine();
							
							// lock all 
							lock(-1); // TODO infinite
							
							mob.playCasino(this, hero, Actions.run(new Runnable() {
								@Override
								public void run() {
									lockBar = 1;
								}
							}));
							updateHeroTail(this, hero);
							mob.toFront();
							hero.toFront();
							
							hero.setOrigin(Align.center);
							hero.addAction(
									Actions.sequence(
											Actions.scaleTo(4, 4, 1f), 
											Actions.delay(3f),
											Actions.scaleTo(1, 1, 1f, Interpolation.elasticOut)));
							
						}
					}
				}
			}
			for(int i=0 ; i<entities.size ; ) if(entities.get(i).getParent() == null) entities.removeIndex(i); else i++;
		}
		
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
		
		MetaGame.i.game.curves.get(spawnCurveIndex).getPosition(point, curveTime);
		
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
	public void spawnPig(float curveTime) {
		
		hero.curve.getPosition(point, curveTime);
		
		PigActor a = new PigActor(GameRules.pigType(hero), GameRules.pigLevel(hero)); 
		
		
		// spawn actor with rythm
		a.getColor().a = 0f;
		addActor(a);
		
		a.addAction(Actions.sequence(syncAction(1f), Actions.parallel(
				Actions.scaleTo(1, 1, .2f, Interpolation.pow3InInverse),
				Actions.alpha(1, .05f, Interpolation.linear))));
		
		a.setPosition(point.x, point.y + 1 * 32f);
		
		
		
		entities.add(a);
	}
	public void spawnMachine(float curveTime) {
		
		hero.curve.getPosition(point, curveTime);
		
		CashMachineActor a = new CashMachineActor(); 
		
		// spawn actor with rythm
		a.getColor().a = 0f;
		addActor(a);
		
		a.addAction(Actions.sequence(syncAction(1f), Actions.parallel(
				Actions.scaleTo(1, 1, .2f, Interpolation.pow3InInverse),
				Actions.alpha(1, .05f, Interpolation.linear))));
		
		a.setPosition(point.x, point.y + 1 * 32f);
		
		
		
		entities.add(a);
	}
	

	private Action syncAction(float barPrecision) {
		return Actions.delay(GameAudio.i.getNextBarTimeRemain(barPrecision));
	}

	public void updateHeroTail(Group container, HeroActor hero)
	{
		boolean enabled = true;
		if(!enabled) return;
		
		boolean hasChanged = false;
		
		
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
						
						GameAudio.i.playFusion();
						
						lock(LOCK_FOR_FUSION);
						
						hasChanged = true;
						
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
			
			hero.addAction(Actions.sequence(
					Actions.scaleTo(3, 3, .5f),
					Actions.scaleTo(1, 1, 1f, Interpolation.elasticOut)));
			
			if(GameRules.shouldLockWhenTransform(hero.type)){
				lock(LOCK_FOR_HERO_TRANSFORM);
			}
			
			GameAudio.i.playHeroFusion();
			
			hasChanged = true;
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
		
		if(hasChanged){
			updateHeroTail(container, hero);
		}
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		gameScreen.postProcessing.enabled = true;
		gameScreen.postProcessing.emissiveColor.set(1, 1, 1, 0);
		gameScreen.postProcessing.emissive2Color.set(1, 1, 1, 0);
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		((OrthographicCamera)getStage().getCamera()).zoom = 1;
		getStage().getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		remove();
		gameScreen.postProcessing.enabled = false;
//		addAction(Actions.sequence(
//				Actions.moveBy(1000, 0, 1f),
//				Actions.removeActor()
//				));
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
