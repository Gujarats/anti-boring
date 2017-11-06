package controller.tebakanManager.hint;

import org.junit.Test;

import antiboring.game.controller.tebakanManager.hint.KeyboardProcessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by gujarat on 04/11/17.
 */

public class KeyboardProcessorTest {

    @Test
    public void testGetNewChar(){
        String input = "SOMETHING";
        String result = KeyboardProcessor.getInstance().getNewChar(input);

        if (result.contains(input) && result.length()==16 && !result.contains(" ")){
            assertTrue(true);
        }else{
            assertTrue(false);
        }
    }

    @Test
    public void getUniqueChar(){
        String input = "Wkwk xoxo";
        String expected = "wkxo";
        String result = KeyboardProcessor.getInstance().getUniqueChar(input);
        assertEquals(expected,result);
    }

    @Test
    public void TestRepeatedDelete(){
        String sourceHint = "L  _  _ _ _ ";
        String result = KeyboardProcessor.getInstance().deleteKeyboardNew(sourceHint);
        result = KeyboardProcessor.getInstance().deleteKeyboardNew(result);
        result = KeyboardProcessor.getInstance().deleteKeyboardNew(result);

        assertEquals(result,sourceHint);
    }
}
