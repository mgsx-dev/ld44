package net.mgsx.ld44.model;

import com.badlogic.gdx.math.MathUtils;

public class GameRules {

	public static int groupSizeForCoinType(int cointType){
		return cointType % 3 == 2 ? 2 : 5;
	}
	
	public static int coinTransformType(int coinType){
		return coinType%3 == 2 ? coinType+1 : coinType+2;
	}

	public static int randomCoinTypeFor(int type) {
		int minType = Math.min(type-1, 0);
		int maxType = type+1;
//		maxType = type; // XXX
		minType = type; // XXX
		return MathUtils.random(minType, maxType);
	}
}
