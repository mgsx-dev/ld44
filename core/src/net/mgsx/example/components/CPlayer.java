package net.mgsx.example.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class CPlayer implements Component
{
	
	public final static ComponentMapper<CPlayer> components = ComponentMapper.getFor(CPlayer.class);
	public int dx, dy;
	
	
}
