package net.mgsx.ld44.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import net.mgsx.ld44.assets.GameAssets;
import net.mgsx.ld44.model.CurrencyCurve;

public class HeroActor extends Group
{
	
	public CurrencyCurve curve;
	
	public  float baseY = 0;
	private Image body;
	private float bodyRotation;
	private Image head;
	
	public float jump;
	public float jumpHeight, jumpVel, jumpAcc;
	
	public Actor tail;
	
	public final Array<CoinActor> coins = new Array<CoinActor>();

	public HeroActor() {
		body = new Image(GameAssets.i.heroBodyRegion);
		addActor(body);
		body.setOrigin(Align.center);
		
		TextureRegion heroHead = new TextureRegion(GameAssets.i.hero, 64 * 8, 0, 64, 64);
		head = new Image(heroHead);
		addActor(head);
		head.setOrigin(Align.center);

	}
	
	@Override
	public void act(float delta) {
		
		((OrthographicCamera)getStage().getCamera()).zoom = 1.5f;
		
		if(jump <= 0 && Gdx.input.isKeyJustPressed(Input.Keys.A)){
			jump = 1;
			jumpVel = 12;
		}
		if(jump > 0 && !Gdx.input.isKeyPressed(Input.Keys.A)){
			jumpVel -= delta * 70;
			if(jumpVel <0){
				// jumpVel /= 1.1f;
			}
		}else if(jump > 0){
			if(jumpVel < 0){
				jumpVel -= delta * 10;
			}else{
				jumpVel -= delta * 20;
			}
		}
		jumpVel = Math.max(jumpVel, -50);
		jumpHeight += jumpVel;
		if(jumpHeight < 0){
			jump = 0;
			jumpVel = 0;
			jumpHeight = 0;
		}
		
		float base = 32;
		
		head.setPosition(10, 10 + base, Align.center);
		
		body.setPosition(0, base, Align.center);
		bodyRotation -= delta * 800f;
		body.setRotation(bodyRotation);
		super.act(delta);
	}

	public void addCoin(CoinActor coin) {
		if(tail == null){
			tail = this;
		}
		coin.head = tail;
		tail = coin;
		coins.insert(0, coin);
	}

}
