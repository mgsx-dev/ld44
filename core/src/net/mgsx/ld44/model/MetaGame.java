package net.mgsx.ld44.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class MetaGame {
	public static MetaGame i = new MetaGame();
	public CurrencyGame game;
	public int bestScore;
	
	// TODO global session state like current GameLevel, global stats, etc.
	
	public void load() {
		bestScore = 0;
		String encScore = Gdx.app.getPreferences("coin-coin").getString("score", null);
		if(encScore != null){
			bestScore = decodeScore(encScore);
		}
	}
	
	public void save(){
		Preferences pref = Gdx.app.getPreferences("coin-coin");
		String encScore = encodeScore(bestScore);
		pref.putString("score", encScore);
		pref.flush();
	}
	
	
	public static int decodeScore(String encScore){
		String decScore = "";
		int sum = 0;
		for(int i=0 ; i<encScore.length()-1 ; i++){
			try{
				int code = Integer.parseInt(encScore.substring(i, i+1));
				sum += code;
				code = (code + 5) % 10;
				decScore += String.valueOf(code);
			}catch(NumberFormatException e){
				break;
			}
		}
		try{
			try{
				int cs = Integer.parseInt(encScore.substring(encScore.length()-1));
				cs = cs % 10;
				if(sum % 10 == cs){
					return Integer.parseInt(decScore);
				}
			}catch(NumberFormatException e){
			}
		}catch(NumberFormatException e){
		}
		return 0;
	}
	public static String encodeScore(int score){
		String decScore = String.valueOf(score);
		String encScore = "";
		int sum = 0;
		for(int i=0 ; i<decScore.length() ; i++){
			int code = Integer.parseInt(decScore.substring(i, i+1));
			code = (code + 5) % 10;
			sum += code;
			encScore += String.valueOf(code);
		}
		encScore += String.valueOf(sum % 10);
		return encScore;
	}
	
}
