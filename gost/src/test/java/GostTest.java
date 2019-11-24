import task_of_crypto.Gost;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;

public class GostTest {

    @Test
   public void isTheStringCorrectlyPadded(){
        String testString = "Born to be kings. We are the princes of the universe.";
        Gost gost2814 = new Gost();
        String result  = gost2814.stringToRightLength(testString);
        System.out.println(result);
        Assert.assertEquals(result.length(),64);

    }

    @Test
    public void isTheKeyCorrectlyChanges(){
        String key  = "Отсюда естественноkjhfjksddddddddddfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffds" +
                "dfsdffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";
        String result = new Gost().toCorrectKeyWord(key);
        System.out.println(result);
        Assert.assertEquals(32, result.length());

    }

    @Test
    public void checkTheBlock(){
        String text  = "Born to be kings. We are the princes of the universe.";
        Gost gost = new Gost();
        text = gost.stringToRightLength(text);
        gost.cutStringIntoBlocks(text);
        System.out.println(Arrays.toString(gost.currentTextBlocks));
    }

    @Test
    public void checkSumMod2(){
        String test1 = "11011";
        String test2 = "100001";
        BigInteger value1 = new BigInteger(test1,2);
        BigInteger value2 = new BigInteger(test2,2);
        System.out.println(value1.add(value2).toString(2));
    }


}
