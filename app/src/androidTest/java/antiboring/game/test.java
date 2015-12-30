//package santana.tebaktebakan;
//
///**
// * Created by Gujarat Santana on 31/08/15.
// */
//
//import android.support.test.rule.ActivityTestRule;
//import android.support.test.runner.AndroidJUnit4;
//import android.test.suitebuilder.annotation.LargeTest;
//
//import org.junit.Rule;
//import org.junit.runner.RunWith;
//
//import santana.tebaktebakan.activity.AnswerTempTebakanActivity;
//
//
///**
// * Basic tests showcasing simple view matchers and actions like {@link android.support.test.espresso.matcher.ViewMatchers#withId},
// * {@link android.support.test.espresso.action.ViewActions#click} and {@link android.support.test.espresso.action.ViewActions#typeText}.
// * <p>
// * Note that there is no need to tell Espresso that a view is in a different {@link android.app.Activity}.
// */
//@RunWith(AndroidJUnit4.class)
//@LargeTest
//public class test {
//
//    public static final String STRING_TO_BE_TYPED = "Espresso";
//
//    /**
//     * A JUnit {@link org.junit.Rule @Rule} to launch your activity under test. This is a replacement
//     * for {@link android.test.ActivityInstrumentationTestCase2}.
//     * <p>
//     * Rules are interceptors which are executed for each test method and will run before
//     * any of your setup code in the {@link org.junit.Before @Before} method.
//     * <p>
//     * {@link android.support.test.rule.ActivityTestRule} will create and launch of the activity for you and also expose
//     * the activity under test. To get a reference to the activity you can use
//     * the {@link android.support.test.rule.ActivityTestRule#getActivity()} method.
//     */
//    @Rule
//    public ActivityTestRule<AnswerTempTebakanActivity> mActivityRule = new ActivityTestRule<>(
//            AnswerTempTebakanActivity.class);
//
//    @Test
//    public void changeText_sameActivity() {
//        // Type text and then press the button.
//        onView(withId(R.id.editTextUserInput))
//                .perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());
//        onView(withId(R.id.changeTextBt)).perform(click());
//
//        // Check that the text was changed.
//        onView(withId(R.id.textToBeChanged)).check(matches(withText(STRING_TO_BE_TYPED)));
//    }
//
//    @Test
//    public void changeText_newActivity() {
//        // Type text and then press the button.
//        onView(withId(R.id.editTextUserInput)).perform(typeText(STRING_TO_BE_TYPED),
//                closeSoftKeyboard());
//        onView(withId(R.id.activityChangeTextBtn)).perform(click());
//
//        // This view is in a different Activity, no need to tell Espresso.
//        onView(withId(R.id.show_text_view)).check(matches(withText(STRING_TO_BE_TYPED)));
//    }
//}
