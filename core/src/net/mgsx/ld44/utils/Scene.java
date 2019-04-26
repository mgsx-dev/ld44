package net.mgsx.ld44.utils;


/**
 * If this scene is an Actor, it's ensure to be in stage when all method are called,
 * that is it can call getStage().
 * 
 * @author mgsx
 *
 */
public interface Scene 
{
	/**
	 * begin current scene : prepare during transition.
	 * scene is ensured to not be visible at this point.
	 */
	public void begin();
	
	/** 
	 * start current scene : prepare after transition : spawning interactive element.
	 * scene is ensured to be full visible at this point.
	 */
	public void start();

	/** 
	 * stop scene : preparing before transitions : removing any top stage elements
	 * scene still full visible at this point.
	 */
	public void stop();
	
	/**
	 * en scene : free some resources if any.
	 * scene is ensured to not be visible at this point (aka dispose).
	 */
	public void end();
}
