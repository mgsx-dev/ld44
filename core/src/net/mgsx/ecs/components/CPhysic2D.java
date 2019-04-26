package net.mgsx.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Vector2;

public class CPhysic2D implements Component
{
	
	public final static ComponentMapper<CPhysic2D> components = ComponentMapper.getFor(CPhysic2D.class);
	
	/** optional implementation specific data : Shape, Box2D Body, etc. may be null */
	public Object userObject;
	
	public final Vector2 force = new Vector2();
	public final Vector2 velocity = new Vector2();
	public final Vector2 position = new Vector2();
	
	// TODO damping / friction / bounce ... at least damping ?
}
