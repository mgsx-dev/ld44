package net.mgsx.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;

import net.mgsx.ecs.KitSystem;
import net.mgsx.ecs.Priority;
import net.mgsx.ecs.components.CPhysic2D;

public class Physic2DSystemBase extends KitSystem
{
	public Physic2DSystemBase() {
		super(Family.all(CPhysic2D.class).get(), Priority.PHYSIC);
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		CPhysic2D physics = CPhysic2D.components.get(entity);
		applyForces(entity, physics, deltaTime);
		applyVelocity(entity, physics, deltaTime);
		applyPosition(entity, physics, deltaTime);
	}

	protected void applyForces(Entity entity, CPhysic2D physics, float deltaTime) {
		// TODO if component has gravity, apply gravity, etc.
		physics.force.setZero();
	}
	
	protected void applyVelocity(Entity entity, CPhysic2D physics, float deltaTime) {
		// TODO limit to collisions eventually
		physics.velocity.mulAdd(physics.force, deltaTime);
	}
	
	private void applyPosition(Entity entity, CPhysic2D physics, float deltaTime) {
		// TODO clamp position eventually
		physics.position.mulAdd(physics.velocity, deltaTime);
	}
}
