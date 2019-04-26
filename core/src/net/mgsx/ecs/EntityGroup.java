package net.mgsx.ecs;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

public class EntityGroup {
	public ImmutableArray<Entity> entities;
	public final Family family;
	
	public EntityGroup(Class<? extends Component>... components) {
		family = Family.all(components).get();
	}

	public ImmutableArray<Entity> get(Engine engine) {
		if(entities == null){
			entities = engine.getEntitiesFor(family);
		}
		return entities;
	}
}
