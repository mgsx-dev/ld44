package net.mgsx.ld44.scenes;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import net.mgsx.ld44.assets.GameAssets;
import net.mgsx.ld44.events.NewGameEvent;
import net.mgsx.ld44.model.MetaGame;
import net.mgsx.ld44.utils.Scene;

public class MenuScene extends Table implements Scene
{

	public MenuScene() {
		super(GameAssets.i.skin);
		setFillParent(true);
		
		Table menu = new Table(getSkin());
		menu.defaults().pad(30);
		menu.add("Best Score").row();
		menu.add(String.valueOf(MetaGame.i.bestScore)).row();
		
		TextButton btPlay = new TextButton("Start", getSkin());
		menu.add(btPlay).row();
		btPlay.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				play();
			}
		});
		add(menu).expand().center();
	}
	
	public void play() {
		addAction(Actions.sequence(
				Actions.moveBy(getWidth(), 0, .5f, Interpolation.pow2),
				Actions.run(new Runnable() {
					@Override
					public void run() {
						fire(new NewGameEvent());
					}
				}),
				Actions.removeActor()
				)
			);
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		
	}

}
