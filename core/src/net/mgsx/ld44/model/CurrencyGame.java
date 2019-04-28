package net.mgsx.ld44.model;

import com.badlogic.gdx.utils.Array;

public class CurrencyGame {
	public final Array<CurrencyCurve> curves = new Array<CurrencyCurve>();
	public int score;
	public float time;
	public float totalTime = GameRules.levelDuration;
}
