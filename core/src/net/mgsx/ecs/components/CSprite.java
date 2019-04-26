package net.mgsx.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class CSprite implements Component
{
	
	public final static ComponentMapper<CSprite> components = ComponentMapper.getFor(CSprite.class);
	
	public final Sprite sprite = new Sprite();
}
