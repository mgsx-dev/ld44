package net.mgsx.ld44.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import net.mgsx.ld44.assets.GameAssets;
import net.mgsx.ld44.events.BackMenuEvent;
import net.mgsx.ld44.model.MetaGame;

public class GameOverActor extends Table
{
	public GameOverActor() {
		super(GameAssets.i.skin);
		
		setBackground(getSkin().newDrawable("white", Color.DARK_GRAY));
		
		Table scoreTable = new Table(getSkin());
		
		boolean newRecord = MetaGame.i.game.score > MetaGame.i.bestScore;
		if(newRecord){
			MetaGame.i.bestScore = MetaGame.i.game.score;
			MetaGame.i.save();
		}
		
		scoreTable.defaults().pad(30);
		
		scoreTable.add("GAME OVER").row();
		
		if(MetaGame.i.game.time < MetaGame.i.game.totalTime){
			if(MetaGame.i.game.fallOut){
				scoreTable.add("Currency bankrupt").row();
			}else{
				scoreTable.add("Currency captured").row();
			}
		}else{
			scoreTable.add("Trading time over").row();
		}
		
		scoreTable.add("score: " + String.valueOf(MetaGame.i.game.score)).row();
		scoreTable.add("best: " + String.valueOf(MetaGame.i.bestScore)).row();
		if(newRecord){
			Label label = scoreTable.add("NEW RECORD!!!").getActor();
			label.addAction(Actions.forever(
					Actions.sequence(
							Actions.color(Color.YELLOW, .3f), 
							Actions.color(Color.GRAY, .3f)
							)
					)
				);
			scoreTable.row();
		}
		TextButton bt = new TextButton("Menu", getSkin());
		scoreTable.add(bt);
		AnimUtil.animateButton(bt);
		
		add(scoreTable).expand().center();
		
		setFillParent(true);
		
		bt.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				fire(new BackMenuEvent());
			}
		});
	}
	
}
