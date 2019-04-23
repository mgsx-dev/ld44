package net.mgsx.ld44;

import java.awt.SplashScreen;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.mgsx.ld44.LD44;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
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
