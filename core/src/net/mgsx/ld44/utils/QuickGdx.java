package net.mgsx.ld44.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

/**
 * Some quick helpers 
 */
public class QuickGdx {

	private static final Vector2 va = new Vector2();
	private static final Vector2 vb = new Vector2();
	private static final Vector2 vc = new Vector2();
	
	public static Animation<TextureRegion> textureRegionAnimation(Skin skin, String basename){
		return textureRegionAnimation(skin.getAtlas(), basename);
	}

	public static Animation<TextureRegion> textureRegionAnimation(TextureAtlas atlas, String basename) {
		Array<AtlasRegion> regions = atlas.findRegions(basename);
		Animation<TextureRegion> animation = new Animation<TextureRegion>(1f, regions);
		return animation;
	}

	public static Vector2 deltaVector(Actor a, Actor b) {
		return va.set(a.getX() - b.getX(), a.getY() - b.getY());
	}

	public static void lerp(Actor actor, Vector2 delta, float time) {
		vb.set(actor.getX(), actor.getY()).lerp(delta, time);
		actor.setPosition(vb.x, vb.y);
	}

	public static void follow(Actor a, Actor b, float speed, float min) {
		va.set(a.getX(), a.getY());
		vb.set(b.getX(), b.getY());
		// ab vector
		vc.set(vb).sub(va);
		if(vc.len() < min){
			vc.setLength(min);
		}
		va.mulAdd(vc, speed);
		a.setPosition(va.x, va.y);
	}

	public static Animation<TextureRegion> animation(Texture texture, int x, int y, int tileWidth, int tileHeight, int deltaX, int deltaY, int count) {
		Array<TextureRegion> frames = new Array<TextureRegion>();
		for(int i=0 ; i<count ; i++){
			frames.add(new TextureRegion(texture, x + i * deltaX, y + i * deltaY, tileWidth, tileHeight));
		}
		Animation<TextureRegion> a = new Animation<TextureRegion>(1f, frames);
		return a;
	}
}
