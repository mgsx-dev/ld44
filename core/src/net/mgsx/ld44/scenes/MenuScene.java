package net.mgsx.ld44.scenes;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import net.mgsx.ld44.actors.AnimUtil;
import net.mgsx.ld44.assets.GameAssets;
import net.mgsx.ld44.audio.GameAudio;
import net.mgsx.ld44.events.NewGameEvent;
import net.mgsx.ld44.model.MetaGame;
import net.mgsx.ld44.utils.Scene;
import net.mgsx.ld44.utils.SceneOverEvent;

public class MenuScene extends Table implements Scene
{

	public MenuScene() {
		super(GameAssets.i.skin);
		setFillParent(true);
		
		Image bg;
		addActor(bg = new Image(GameAssets.i.textureTuto));
		
		GameAudio.i.playMusicIntro();
		
		Table menu = new Table(getSkin());
		menu.defaults().pad(10);
		menu.add("Best Score").row();
		menu.add(String.valueOf(MetaGame.i.bestScore)).row();
		
		for(int i=0 ; i<3 ; i++){
			TextButton btPlay = new TextButton("Play with\nmusic " + (i+1), getSkin());
			menu.add(btPlay).row();
			final int index = i;
			btPlay.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					play(index);
				}
			});
			AnimUtil.animateButton(btPlay);
		}
		add(menu).expand().top().right().padRight(150).padTop(25);
		
		TextButton btBack = new TextButton("back", getSkin());
		// AnimUtil.animateButton(btBack);
		addActor(btBack);
		btBack.validate();
		btBack.setPosition(450, 50);
		
		btBack.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				
				addAction(Actions.sequence(
						Actions.moveBy(getWidth(), 0, .5f, Interpolation.pow2),
						Actions.run(new Runnable() {
							@Override
							public void run() {
								fire(new SceneOverEvent(new TitleScene()));
							}
						}),
						Actions.removeActor()
						)
					);
			}
		});
	}
	
	public void play(final int n) {
		addAction(Actions.sequence(
				Actions.moveBy(getWidth(), 0, .5f, Interpolation.pow2),
				Actions.run(new Runnable() {
					@Override
					public void run() {
						fire(new NewGameEvent(n));
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
