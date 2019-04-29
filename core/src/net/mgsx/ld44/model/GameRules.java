package net.mgsx.ld44.model;

import com.badlogic.gdx.math.MathUtils;

import net.mgsx.ld44.actors.CoinActor;
import net.mgsx.ld44.actors.HeroActor;
import net.mgsx.ld44.actors.PigActor;

public class GameRules {

	public static final float levelDuration = 60 * 3; // 180 s : 3 mn

	public static int groupSizeForCoinType(int cointType){
		if(cointType == 15) return 7; // XXX limit 15
		return cointType % 3 == 2 ? 2 : 5;
	}
	
	public static int coinTransformType(int coinType){
		if(coinType == 15) return 15; // XXX limit 15
		return (coinType%3 == 2 ? coinType+1 : coinType+2) % 16;
	}

	public static int randomCoinTypeFor(int type) {
		if(type == 15) return 15; // XXX limit 15
		int minType = Math.max(type-1, 0);
		int maxType = type+1;
//		maxType = type; // XXX
//		minType = type; // XXX
		return MathUtils.random(minType, maxType) % 16;
	}

	public static CasinoResult genCasino(int heroType) {
		
		
		
		CasinoResult r = new CasinoResult();
		float rnd1 = MathUtils.random();
		if(rnd1 < .33f){
			r.bonusType = 0;
			r.bonusCount = 1;
			r.combi1 = MathUtils.random(15);
			r.combi2 = (r.combi1 + MathUtils.random(7)) % 15;
			r.combi3 = (r.combi2 + MathUtils.random(7)) % 15;
		}else if(rnd1 < .75f){
			r.bonusType = MathUtils.clamp(heroType, 0, 15);
			r.bonusCount = 5;
			r.combi1 = r.bonusType;
			r.combi2 = r.combi1;
			r.combi3 = r.combi1;
		}else {
			r.bonusType = 15;
			r.bonusCount = 10;
			r.combi1 = r.bonusType;
			r.combi2 = r.combi1;
			r.combi3 = r.combi1;

		}
		return r;
	}

	public static int scoreForUnqueue(CoinActor coin) {
		return (coin.type + 1) * 10;
	}

	public static int scoreForEnqueue(CoinActor coin) {
		if(coin.type == 15) return 1000;
		return (coin.type + 1) * 10;
	}

	public static int scoreForHeroTransform(int type) {
		if(type + 1 == 15) return 10000;
		return (type + 1) * 1000;
	}

	public static int scoreForFusion(CoinActor newCoin) {
		return (newCoin.type + 1) * 100;
	}

	public static int scoreForCasino(CasinoResult r) {
		return r.bonusType * 50;
	}

	public static float pigPeriod(CurrencyGame game, HeroActor hero) {
		return MathUtils.lerp(5, 1, hero.type / 15f);
	}

	public static float cashMachinePeriod(CurrencyGame game, HeroActor hero) {
		return 10 + hero.type;
	}

	public static void onHeroVersusPig(HeroActor hero, PigActor pig) {
		if(hero.coins.size > 0){
			while(hero.coins.size>0) {
				hero.coins.removeIndex(0).remove();
			}
		}else{
		}
		if(hero.type>0){
			hero.setType(hero.type-1);
		}else{
			MetaGame.i.game.over = true;
		}
	}

	public static float newTempoBonusSpeed() {
		return (1 << MathUtils.random(0, 3)) / 2f; // 1 bar or 2 bars
	}

	public static boolean shouldLockWhenTransform(int type) {
		return false; // type < 15; // never lock
	}

	public static float getChangeCurvePeriod(int type) {
		return MathUtils.lerp(MathUtils.random(3f, 5f), MathUtils.random(1f, 2f), type/15f) ;
	}

	public static int pigType(HeroActor hero) {
		return MathUtils.random(2);
	}

	public static int pigLevel(HeroActor hero) {
		return hero.type/3 > 2 ? 1 : 0;
	}

	public static float getCurveAmplitude(HeroActor hero) {
		return MathUtils.lerp(.5f, 1.5f, MathUtils.clamp(hero.type/6f, 0, 1)) ;
	}

	public static float getCurveFrequency(HeroActor hero) {
		return MathUtils.lerp(.1f, .1f, hero.type/15f) ;
	}

	public static float getZoom(HeroActor hero) {
		float t = hero.type / 15f;
		float expectedoom = MathUtils.lerp(2f, 1.5f, t*t*t );
		return expectedoom; // expectedoom;
	}

}
