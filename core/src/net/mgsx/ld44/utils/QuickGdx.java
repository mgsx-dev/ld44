package net.mgsx.ld44.utils;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

/**
 * Some quick helpers 
 */
public class QuickGdx {

	public static Animation<TextureRegion> textureRegionAnimation(Skin skin, String basename){
		return textureRegionAnimation(skin.getAtlas(), basename);
	}

	public static Animation<TextureRegion> textureRegionAnimation(TextureAtlas atlas, String basename) {
		Array<AtlasRegion> regions = atlas.findRegions(basename);
		Animation<TextureRegion> animation = new Animation<TextureRegion>(1f, regions);
		return animation;
	}
}
