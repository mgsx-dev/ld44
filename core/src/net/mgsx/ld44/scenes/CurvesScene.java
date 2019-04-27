package net.mgsx.ld44.scenes;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;

import net.mgsx.ld44.actors.CurveActor;
import net.mgsx.ld44.actors.HeroActor;
import net.mgsx.ld44.model.CurrencyCurve;
import net.mgsx.ld44.model.CurrencyGame;
import net.mgsx.ld44.screens.GameScreen;
import net.mgsx.ld44.utils.Scene;

public class CurvesScene extends Group implements Scene{

	private CurrencyGame game;
	private ShapeRenderer shapeRenderer;
	public final Vector2 worldCenter = new Vector2();
	private float stepSize = 60 * 2;
	private HeroActor hero;
	private static final Vector2 point = new Vector2();
	private float t;

	public CurvesScene() {
		
		shapeRenderer = new ShapeRenderer();
		
		setTransform(false);
		
		game = new CurrencyGame();
		
		Color[] colors = new Color[]{Color.RED, Color.ORANGE, Color.YELLOW};
		
		for(int i=0 ; i<3 ; i++){
			CurrencyCurve c = new CurrencyCurve().set(i, colors[i%colors.length]);
			for(int j=0 ; j<c.controlPoints.length ; j++){
				c.add(point.set(i * stepSize, c.index * 200 + MathUtils.random(10)));
			}
			game.curves.add(c);
			addActor(new CurveActor(shapeRenderer, c));
		}
		worldCenter.x = CurrencyCurve.BUFFER_SIZE * stepSize;
		
		hero = new HeroActor();
		hero.curve = game.curves.first();
		addActor(hero);
	}
	
	@Override
	public void act(float delta) {
		
		for(CurrencyCurve c : game.curves){
			c.update();
		}
		
		stepSize = 400;
		
		float lastX = worldCenter.x;
		worldCenter.x += delta * stepSize;
		Camera cam = getStage().getCamera();
		if((int)(lastX / stepSize) != (int)(worldCenter.x / stepSize)){
			for(CurrencyCurve c : game.curves){
				
				// basis in screen
				// c.add(point.set(worldCenter.x, MathUtils.random(260) - 20 ));
				
				// big random
				// c.add(point.set(worldCenter.x, MathUtils.random(560) - 100 ));
				
				// TODO accumulate new wave or use perlin ...
				
				float freq = .1f;
				c.add(point.set(worldCenter.x,
						Math.max(-10, 
								(.5f + .5f * MathUtils.sinDeg(worldCenter.x * freq + (1 + c.index) * 60f)) * 500 + 
								(MathUtils.random() - .6f) * 1300 +
								c.index * 100
						)));
			}
			t -= 1;
		}
		t = (worldCenter.x / stepSize) % 1f; // * game.curves.first().getNormal(point, t).len();
		
		CurrencyCurve c = hero.curve;
		Vector2 a = c.controlPoints[c.controlPointsLength-2];
		Vector2 b = c.controlPoints[c.controlPointsLength-1];
		
		int worldStep = (int)(worldCenter.x / stepSize);
		float worlRemain = worldCenter.x / stepSize - worldStep;
		
//		hero.setX(MathUtils.lerp(a.x, b.x, t));
//		hero.setY(MathUtils.lerp(a.y, b.y, t));

		// TODO 1.4f ?? len or average seg len ? or derivative
		float t2 = 1.4f*(c.controlPointsLength/4 + worlRemain) / (float)c.controlPointsLength;
		float curY = point.y;
		
		c.getPosition(point, t2);
		hero.setX(point.x);
		
		if(hero.jumpHeight > 0 && hero.baseY + hero.jumpHeight > point.y || hero.jumpVel > 0){
			hero.setY(hero.baseY + hero.jumpHeight);
			
			// test other curves above
			for(CurrencyCurve c2 : game.curves){
				c2.getPosition(point, t2);
				if(true){
					if(point.y < hero.baseY + hero.jumpHeight && point.y > curY){
						hero.curve = c2;
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
		
		super.act(delta);
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
