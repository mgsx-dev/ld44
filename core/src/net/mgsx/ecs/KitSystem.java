package net.mgsx.ecs;


import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class KitSystem extends IteratingSystem
{
	private final EntityListener listener = new EntityListener() {
		@Override
		public void entityAdded(Entity entity) {
			KitSystem.this.entityAdded(entity);
		}
		@Override
		public void entityRemoved(Entity entity) {
			KitSystem.this.entityRemoved(entity);
		}
	};
	
	public KitSystem(Family family, int priority) {
		super(family, priority);
	}
	
	protected void entityAdded(Entity entity) {
	}

	protected void entityRemoved(Entity entity) {
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
	}
	
	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		engine.addEntityListener(getFamily(), listener);
	}
	
	@Override
	public void removedFromEngine(Engine engine) {
		engine.removeEntityListener(listener);
		super.removedFromEngine(engine);
	}
	
	protected <T extends Component> T component(Class<T> type){
		return getEngine().createComponent(type);
	}

}
