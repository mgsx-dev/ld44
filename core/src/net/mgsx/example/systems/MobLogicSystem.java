package net.mgsx.example.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.mgsx.ecs.KitSystem;
import net.mgsx.ecs.Priority;
import net.mgsx.ecs.reflection.Asset;
import net.mgsx.ecs.reflection.Auto;
import net.mgsx.example.components.CMob;
import net.mgsx.example.model.GameWorld;

public class MobLogicSystem extends KitSystem
{
	@Asset("mob")
	public TextureRegion mobRegion;
	
	@Auto
	public GameWorld world;
	
	public MobLogicSystem() {
		super(Family.all(CMob.class).get(), Priority.LOGIC);
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		CMob mob = CMob.components.get(entity);
		// TODO world.smth.dst(mob.position)
	}

}
