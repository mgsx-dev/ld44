package net.mgsx.ld44.scenes;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import net.mgsx.ld44.actors.AnimUtil;
import net.mgsx.ld44.assets.GameAssets;
import net.mgsx.ld44.screens.GameScreen;
import net.mgsx.ld44.utils.Scene;
import net.mgsx.ld44.utils.SceneOverEvent;

public class TitleScene extends Group implements Scene
{
	
	public TitleScene() {
		
		setSize(GameScreen.WORLD_WIDTH, GameScreen.WORLD_HEIGHT);
		
		Image bg;
		addActor(bg = new Image(GameAssets.i.textureTitle));
		
		Skin skin = GameAssets.i.skin;
		Table root = new Table(skin);
		TextButton bt;
		root.add(bt = new TextButton("START", skin)).padBottom(40);
//		SelectBox<String> box = new SelectBox<String>(skin);
//		box.setItems("Alpha", "Beta", "Gamma", "Delta");
//		root.add(box);
		addActor(root);
		root.setFillParent(true);
		
		AnimUtil.animateButton(bt);
		
		bt.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				exit();
			}
		});
	}
	
	protected void exit() {
		addAction(Actions.sequence(
				Actions.moveBy(-getWidth(), 0, .5f, Interpolation.swingIn),
				Actions.run(new Runnable() {
					@Override
					public void run() {
						fire(new SceneOverEvent(new MenuScene()));
					}
				})
				)
			);
	}

	@Override
	public void begin() {
		setX(-getWidth());
		start();
	}

	@Override
	public void start() {
		addAction(Actions.sequence(Actions.moveTo(0, 0, .5f, Interpolation.swingOut)));
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
