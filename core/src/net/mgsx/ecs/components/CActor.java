package net.mgsx.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class CActor implements Component {
	
	public final static ComponentMapper<CActor> components = ComponentMapper.getFor(CActor.class);
	
	public Actor actor;
}
