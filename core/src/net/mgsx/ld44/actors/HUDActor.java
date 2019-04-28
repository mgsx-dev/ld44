package net.mgsx.ld44.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.mgsx.ld44.assets.GameAssets;
import net.mgsx.ld44.model.MetaGame;
import net.mgsx.ld44.screens.GameScreen;

public class HUDActor extends Table
{
	private Label labelScore;
	private int lastScore;

	public HUDActor() {
		super(GameAssets.i.skin);
		setSize(GameScreen.WORLD_WIDTH, GameScreen.WORLD_HEIGHT);
		add(labelScore = new Label("", getSkin())).expand().top();
	}
	
	@Override
	public void act(float delta) {
		setX(getStage().getCamera().position.x - GameScreen.WORLD_WIDTH/2);
		setY(getStage().getCamera().position.y - GameScreen.WORLD_HEIGHT/2);
		labelScore.getColor().a = .5f;
		labelScore.setFontScale(labelScore.getColor().a * 4);
		labelScore.setText(String.valueOf(MetaGame.i.game.score));
		if(lastScore != MetaGame.i.game.score){
			labelScore.clearActions();
			labelScore.addAction(Actions.sequence(
					Actions.color(new Color(1f, 0.7f, 0f, 1), .3f),
					Actions.color(new Color(1f, 1f, 1f, .5f), 1f)));
		}
		lastScore = MetaGame.i.game.score;
		super.act(delta);
	}
}	
