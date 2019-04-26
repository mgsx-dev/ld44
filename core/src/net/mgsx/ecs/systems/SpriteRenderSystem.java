package net.mgsx.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import net.mgsx.ecs.EntityGroup;
import net.mgsx.ecs.KitSystem;
import net.mgsx.ecs.components.CSprite;
import net.mgsx.ecs.reflection.Auto;

public class SpriteRenderSystem extends KitSystem
{
	@Auto public Batch batch;
	
	@Auto public Camera camera;
	
	private EntityGroup sprites = new EntityGroup(CSprite.class);
	
	public SpriteRenderSystem(/*Batch batch,*/ int priority) {
		super(Family.all(CSprite.class).get(), priority);
		// this.batch = batch;
	}

	@Override
	public void update(float deltaTime) {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
//		for(Entity entity : sprites.get(getEngine())){
//			Sprite sprite = CSprite.components.get(entity).sprite;
//			sprite.draw(batch);
//		}
		super.update(deltaTime);
		batch.end();
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Sprite sprite = CSprite.components.get(entity).sprite;
		sprite.draw(batch);
	}

}
