package net.mgsx.ld44;

import java.awt.SplashScreen;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	private static final int SCREEN_RATE = 2;

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = LD44.SCREEN_WIDTH * SCREEN_RATE;
		config.height = LD44.SCREEN_HEIGHT * SCREEN_RATE;
		new LwjglApplication(new LD44(){
			@Override
			public void create() {
				SplashScreen splashScreen = SplashScreen.getSplashScreen();
				if(splashScreen != null){
					splashScreen.close();
				}
				super.create();
			}
		}, config);
	}
}
