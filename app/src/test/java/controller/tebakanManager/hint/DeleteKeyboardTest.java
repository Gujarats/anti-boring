package controller.tebakanManager.hint;

/**
 * Created by gujarat on 04/11/17.
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import antiboring.game.controller.tebakanManager.HintsUiManager;
import antiboring.game.controller.tebakanManager.hint.KeyboardProcessor;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class DeleteKeyboardTest {

    /*local variable */
    private String source;

    private String expected;

    @Parameters(name = "Test {index}: deletKeyboradNew({0})={2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "B R A V O", new int[] {0,1,2,3}, "B R A V _"},
                { "B R _ _ _ ",new int[] {}, "B _ _ _ _ "},
                {"C _ T _ _", new int[] {0,1,2,3,4 }, "C _ T _ _"}
        });
    }

    public DeleteKeyboardTest(String source, int[] index, String expected){
        this.source = source;
        for (int i:index) {
            KeyboardProcessor.getInstance().addIndexHint(i);
        }
        this.expected = expected;
    }

    @Test
    public void TestDeleteKeyboardOnce(){
        assertEquals(expected, KeyboardProcessor.getInstance().deleteKeyboardNew(source));
        KeyboardProcessor.getInstance().removeHintIndex();
    }

}
