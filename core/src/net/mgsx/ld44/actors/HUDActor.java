package net.mgsx.ld44.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
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
	private Label labelTime;

	public HUDActor() {
		super(GameAssets.i.skin);
		setSize(GameScreen.WORLD_WIDTH, GameScreen.WORLD_HEIGHT);
		
		Table sTable = new Table(getSkin());
		
		
		sTable.defaults().width(600);
		sTable.add(labelScore = new Label("", getSkin())).padLeft(200);
		sTable.add(labelTime = new Label("12345", getSkin())).padLeft(200);
		
		add(sTable).expand().top().padTop(-150);
	}
	
	@Override
	public void act(float delta) {
		setX(getStage().getCamera().position.x - GameScreen.WORLD_WIDTH/2);
		setY(getStage().getCamera().position.y - GameScreen.WORLD_HEIGHT/2);
		labelScore.getColor().a = 1f;
		labelScore.setFontScale(2);
		labelScore.setText(String.valueOf(MetaGame.i.game.score));
		if(lastScore != MetaGame.i.game.score){
			labelScore.clearActions();
			labelScore.addAction(Actions.sequence(
					Actions.color(new Color(1f, 1f, 1f, 1), .3f),
					Actions.color(new Color(1f, 0.7f, 0f, 1), .3f)
					)
					);
		}
		// labelScore.setColor(1f,1f,1f,.3f);
		lastScore = MetaGame.i.game.score;
		
		
		int value = MathUtils.ceil((MetaGame.i.game.totalTime - MetaGame.i.game.time)*100*4);
		value = Math.max(value, 0);
		labelTime.setText(String.valueOf(value/100));
		labelTime.setColor(0, .5f, 1f, .5f);
		super.act(delta);
	}
}	
