package net.mgsx.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.mgsx.ecs.KitSystem;
import net.mgsx.ecs.components.CAnimated;
import net.mgsx.ecs.components.CSprite;
import net.mgsx.ecs.reflection.Asset;

public class PlayerAnimationSystem extends KitSystem
{
	@Asset("player-idle")
	public Animation<TextureRegion> playerIdle;
	
	
	public PlayerAnimationSystem(Family family, int priority) {
		super(family, priority);
	}
	
	@Override
	protected void entityAdded(Entity entity) {
		entity
		.add(component(CAnimated.class).set(playerIdle, 0))
		.add(component(CSprite.class));
	}
}
