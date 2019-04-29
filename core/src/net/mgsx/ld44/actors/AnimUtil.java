package net.mgsx.ld44.actors;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

public class AnimUtil {
	public static void animateButton(TextButton bt){
		bt.setTransform(true);
		bt.setOrigin(Align.center);
		bt.addAction(Actions.forever(Actions.sequence(
				Actions.scaleTo(1.1f, 1.1f, .5f, Interpolation.sine),
				Actions.scaleTo(1f, 1f, .5f, Interpolation.sine)
				)));
	}
}
