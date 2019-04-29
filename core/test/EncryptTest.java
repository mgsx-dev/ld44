import org.junit.Assert;
import org.junit.Test;

import net.mgsx.ld44.model.MetaGame;

public class EncryptTest {
	
	private void test(int score){
		String enc = MetaGame.encodeScore(score);
		int dec = MetaGame.decodeScore(enc);
		System.out.println(score);
		System.out.println(enc);
		Assert.assertEquals(score, dec);
	}
	
	@Test
	public void test(){
		test(345);
		test(3546000);
		test(Integer.MAX_VALUE); // TODO max 2.000.000.000 2147483647
	}
}
