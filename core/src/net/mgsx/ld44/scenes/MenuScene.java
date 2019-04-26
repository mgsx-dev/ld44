package net.mgsx.ld44.scenes;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import net.mgsx.ld44.assets.GameAssets;
import net.mgsx.ld44.utils.SceneOverEvent;

public class MenuScene extends Table
{

	public MenuScene() {
		super(GameAssets.i.skin);
		setFillParent(true);
		TextButton btPlay = new TextButton("Start", getSkin());
		add(btPlay);
		btPlay.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				play();
			}
		});
	}
	
	public void play() {
		addAction(Actions.sequence(
				Actions.moveBy(getWidth(), 0, 1f, Interpolation.sine),
				Actions.run(new Runnable() {
					@Override
					public void run() {
						fire(new SceneOverEvent(new GameScene()));
					}
				})
				)
			);
	}

}
