package cse110.jamwithme;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginAndModifyProfile {

    @Rule
    public ActivityTestRule<Splash> mActivityTestRule = new ActivityTestRule<>(Splash.class);

    @Test
    public void loginAndModifyProfile() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.Login_name),
                        withParent(allOf(withId(R.id.activity_logina_ctivity),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("name@test.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.Login_pass),
                        withParent(allOf(withId(R.id.activity_logina_ctivity),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("password"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.Login_pass), withText("password"),
                        withParent(allOf(withId(R.id.activity_logina_ctivity),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatEditText3.perform(pressImeActionButton());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.Login_botton), withText("Login"),
                        withParent(allOf(withId(R.id.activity_logina_ctivity),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.Profile_add_button), withText("Add Instruments"),
                        withParent(allOf(withId(R.id.activity_user_profile),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.add_id), withContentDescription("add"), isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.bNext3), withText("NEXT"), isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.eTBiography),
                        withParent(allOf(withId(R.id.activity_user_profile),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("Modify Personal Bio"), closeSoftKeyboard());

                ViewInteraction appCompatButton4 = onView(
                        allOf(withId(R.id.save_button), withText("Save"),
                                withParent(allOf(withId(R.id.activity_user_profile),
                                        withParent(withId(android.R.id.content)))),
                                isDisplayed()));
        appCompatButton4.perform(click());

    }

}
