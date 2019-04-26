package net.mgsx.example.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import net.mgsx.ecs.Priority;
import net.mgsx.ecs.components.CPhysic2D;
import net.mgsx.example.components.CPlayer;

public class InputSystem extends EntitySystem
{

	public InputSystem() {
		super(Priority.INPUT);
	}
	
	@Override
	public void update(float deltaTime) 
	{
		ImmutableArray<Entity> players = getEngine().getEntitiesFor(Family.all(CPlayer.class).get());
		if(players.size() > 0){
			Entity entity = players.first();
			CPlayer player = CPlayer.components.get(entity);
			player.dx = 0;
			if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
				player.dx = -1;
			}
			CPhysic2D physic = CPhysic2D.components.get(entity);
			if(physic != null){
				if(player.dx == 0 && player.dy == 0){
					physic.force.setZero();
				}else{
					physic.force.set(player.dx, player.dy).nor().scl(100f);
				}
				
			}
			
			// TODO
		}
	}

}
