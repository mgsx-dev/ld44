package net.mgsx.ld44.model;

import com.badlogic.gdx.utils.Array;

public class CurrencyGame {
	public final Array<CurrencyCurve> curves = new Array<CurrencyCurve>();
	public int score;
	public float time;
	public float totalTime = GameRules.levelDuration;
	public boolean lockTime = false;
	public boolean over = false;
	public boolean fallOut = false;
	public void update(float delta) {
		if(!lockTime){
			time += delta;
		}
	}
}
