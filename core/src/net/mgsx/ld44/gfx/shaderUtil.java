package net.mgsx.ld44.gfx;

public class shaderUtil {
	public static String formatFloat(float v){
		String s = String.valueOf(v);
		if(!s.contains(".")){
			s += ".0";
		}
		return s;
	}
}
