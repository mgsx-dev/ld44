import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.scenes.scene2d.Group;

import net.mgsx.ld44.actors.HeroActor;
import net.mgsx.ld44.assets.GameAssets;
import net.mgsx.ld44.scenes.CurvesScene;

public class ConversionTest {
	@BeforeClass
	public static void setupClass(){
		GameAssets.i = new GameAssets();
	}
	
	@Test
	public void test(){
		HeroActor hero = new HeroActor();
		CurvesScene.updateHeroTail(new Group(), hero);
	}
}
