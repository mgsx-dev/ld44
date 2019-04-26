package net.mgsx.ld44.scenes;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import net.mgsx.ld44.assets.GameAssets;
import net.mgsx.ld44.screens.GameScreen;
import net.mgsx.ld44.utils.Scene;

public class GameScene extends Group implements Scene
{
	
	public GameScene() {
		
		setSize(GameScreen.WORLD_WIDTH, GameScreen.WORLD_HEIGHT);
		
		Skin skin = GameAssets.i.skin;
		Table root = new Table(skin);
		root.add("Lusum Dare 44");
		root.add(new TextButton("Let's Jam! :-)", skin));
		SelectBox<String> box = new SelectBox<String>(skin);
		box.setItems("Alpha", "Beta", "Gamma", "Delta");
		root.add(box);
		addActor(root);
		root.setFillParent(true);
	}
	
	@Override
	public void begin() {
		setX(-getWidth());
		start();
	}

	@Override
	public void start() {
		addAction(Actions.sequence(Actions.moveTo(0, 0, 1f, Interpolation.sine)));
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
