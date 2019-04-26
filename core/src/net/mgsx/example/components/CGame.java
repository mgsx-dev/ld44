package net.mgsx.example.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class CGame implements Component
{
	
	public final static ComponentMapper<CGame> components = ComponentMapper.getFor(CGame.class);
	
	public int score;
	public int state;
	
	// TODO ... all game : typically one but could be more : renderer has to deal with multiple of it.
}
