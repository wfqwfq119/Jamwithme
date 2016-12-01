package cse110.jamwithme;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RegisterClick {

    @Rule
    public ActivityTestRule<Splash> mActivityTestRule = new ActivityTestRule<>(Splash.class);

    /** Given on login screen, person can click to register instead of log in */
    @Test
    public void registerClick() {
        //Given on login splash screen
        FirebaseAuth.getInstance().signOut();   //Log out

        try {
            Thread.sleep(5000);

            ViewInteraction appCompatTextView = onView(
                    allOf(withId(R.id.Login_tv), withText("Register Here"),
                            withParent(allOf(withId(R.id.activity_logina_ctivity),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatTextView.perform(click());
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

}
