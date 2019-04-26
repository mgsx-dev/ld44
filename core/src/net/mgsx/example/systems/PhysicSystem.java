package net.mgsx.example.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Vector2;

import net.mgsx.ecs.components.CPhysic2D;
import net.mgsx.ecs.reflection.Auto;
import net.mgsx.ecs.systems.Physic2DSystemBase;

public class PhysicSystem extends Physic2DSystemBase
{
	@Auto
	public TiledMap map;
	
	private Polyline background;

	private Vector2 start = new Vector2();

	private Vector2 end = new Vector2();

	private Vector2 displacement = new Vector2();
	
	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		background = ((PolylineMapObject)map.getLayers().get("collisions").getObjects().get("background")).getPolyline();
	}
	
	@Override
	protected void applyForces(Entity entity, CPhysic2D physics, float deltaTime) {
		physics.force.add(0, -9.7f);
	}
	
	@Override
	protected void applyVelocity(Entity entity, CPhysic2D physics, float deltaTime) {
		super.applyVelocity(entity, physics, deltaTime);
		
		float[] vertices = background.getTransformedVertices();
		for(int i=0 ; i<vertices.length-2 ; i+=2){
			float d = Intersector.intersectSegmentCircleDisplace(start.set(vertices[i], vertices[i+1]), end.set(vertices[i+2], vertices[i+3]), physics.position, 10f, displacement);
			if(!Float.isInfinite(d)){
				// TODO clamp in some way ...
			}
		}
	}
}
