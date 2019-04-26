package net.mgsx.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CAnimated implements Component
{
	
	public final static ComponentMapper<CAnimated> components = ComponentMapper.getFor(CAnimated.class);
	
	public Animation<TextureRegion> animation;
	public float time;
	
	public CAnimated set(Animation<TextureRegion> animation, float time) {
		this.animation = animation;
		this.time = time;
		return this;
	}
}
