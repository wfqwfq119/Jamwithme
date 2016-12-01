package cse110.jamwithme;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class FullScaleScenarioTest {

    @Rule
    public ActivityTestRule<Splash> mActivityTestRule = new ActivityTestRule<>(Splash.class);

    /** Given user does not exist and no one is logged in
     * When I register for an account
     * Then I can navigate to see my profile and other peoples' profiles through matching location
     **/
    @Test
    public void fullScaleScenarioTest() {
        final FirebaseAuth fa = FirebaseAuth.getInstance();
        //Given espressotest@ex.com account doesn't exist and not logged in
        FirebaseAuth.getInstance().signOut();   //Log out

        //Check if user exists
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot uid : dataSnapshot.getChildren()) {
                    String name = uid.child("name").getValue().toString();
                    if(name.equals("Espresso Test")) {
                        final String userstring = uid.getKey().toString();
                        //delete account

                        fa.signInWithEmailAndPassword("espressotest@ex.com", "testtest").addOnCompleteListener
                                (new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                        }
                                        else {
                                            FirebaseUser fu = fa.getCurrentUser();
                                            if(fu != null)
                                            {
                                                fu.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) { }
                                                    }
                                                });
                                                myRef.child("users/" + userstring).removeValue();

                                                //remove location from geofire query
                                                GeoFire gf = new GeoFire(myRef.child("geofire"));
                                                gf.removeLocation(userstring);

                                                myRef.child("geofire/" + userstring).removeValue();
                                                myRef.child(userstring + "location").removeValue();
                                            }


                                        }
                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

        /** Begin Test after moment of sleep*/
        try {
            Thread.sleep(5000);

            ViewInteraction appCompatEditText = onView(
                    allOf(withId(R.id.Login_name),
                            withParent(allOf(withId(R.id.activity_logina_ctivity),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText.perform(click());

            pressBack();

            /** Test Register */
            ViewInteraction appCompatTextView = onView(
                    allOf(withId(R.id.Login_tv), withText("Register Here"),
                            withParent(allOf(withId(R.id.activity_logina_ctivity),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatTextView.perform(click());

            ViewInteraction appCompatEditText2 = onView(
                    allOf(withId(R.id.Re_Username),
                            withParent(allOf(withId(R.id.activity_register),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText2.perform(click());

            ViewInteraction appCompatEditText3 = onView(
                    allOf(withId(R.id.Re_Username),
                            withParent(allOf(withId(R.id.activity_register),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText3.perform(replaceText("E"), closeSoftKeyboard());

            ViewInteraction appCompatEditText4 = onView(
                    allOf(withId(R.id.Re_Username), withText("E"),
                            withParent(allOf(withId(R.id.activity_register),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText4.perform(click());

            ViewInteraction reg = onView(withId(R.id.activity_register));
            reg.check(matches(isDisplayed()));

            ViewInteraction appCompatEditText5 = onView(
                    allOf(withId(R.id.Re_Username),
                            withParent(allOf(withId(R.id.activity_register),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText5.perform(replaceText("Espresso Test"), closeSoftKeyboard());

            ViewInteraction appCompatEditText6 = onView(
                    allOf(withId(R.id.Register_Email),
                            withParent(allOf(withId(R.id.activity_register),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText6.perform(replaceText("espressotest@ex.com"), closeSoftKeyboard());

            ViewInteraction appCompatEditText7 = onView(
                    allOf(withId(R.id.Register_password),
                            withParent(allOf(withId(R.id.activity_register),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText7.perform(replaceText("testtest"), closeSoftKeyboard());

            ViewInteraction appCompatEditText8 = onView(
                    allOf(withId(R.id.Register_password_confirm),
                            withParent(allOf(withId(R.id.activity_register),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText8.perform(replaceText("testtest"), closeSoftKeyboard());

            ViewInteraction appCompatEditText9 = onView(
                    allOf(withId(R.id.Register_password_confirm), withText("testtest"),
                            withParent(allOf(withId(R.id.activity_register),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText9.perform(pressImeActionButton());

            ViewInteraction appCompatButton = onView(
                    allOf(withId(R.id.Re_button), withText("Sign Up"),
                            withParent(allOf(withId(R.id.activity_register),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatButton.perform(click());

            /** Test Camera Register Page */
            ViewInteraction cam = onView(withId(R.id.activity_camera));
            cam.check(matches(isDisplayed()));

            ViewInteraction appCompatButton2 = onView(
                    allOf(withId(R.id.bNext), withText("Next"),
                            withParent(allOf(withId(R.id.activity_camera),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatButton2.perform(click());

            /** Test Jam Register Page */
            ViewInteraction jam = onView(withId(R.id.activity_add_jams_activity));
            jam.check(matches(isDisplayed()));

            ViewInteraction appCompatButton3 = onView(
                    allOf(withId(R.id.bNext2), withText("Next"),
                            withParent(allOf(withId(R.id.activity_add_jams_activity),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatButton3.perform(click(longClick()));

            /*ViewInteraction actionMenuItemView = onView(
                    allOf(withId(R.id.add_id), withContentDescription("Add"), isDisplayed()));
            actionMenuItemView.perform(click());*/

            ViewInteraction appCompatButton4 = onView(
                    allOf(withId(R.id.bNext3), withText("Next"), isDisplayed()));
            appCompatButton4.perform(click());

            /** Test Experience Register Page */
            ViewInteraction expreg = onView(withId(R.id.activity_experience));
            expreg.check(matches(isDisplayed()));

            ViewInteraction appCompatButton5 = onView(
                    allOf(withId(R.id.bNext4), withText("Next"),
                            withParent(allOf(withId(R.id.activity_experience),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatButton5.perform(click());

            /** Test Biography Registration Page */
            ViewInteraction bioreg = onView(withId(R.id.activity_biography));
            bioreg.check(matches(isDisplayed()));

            ViewInteraction appCompatEditText11 = onView(
                    allOf(withId(R.id.eTBiography),
                            withParent(allOf(withId(R.id.activity_biography),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText11.perform(click());

            ViewInteraction appCompatEditText12 = onView(
                    allOf(withId(R.id.eTBiography),
                            withParent(allOf(withId(R.id.activity_biography),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText12.perform(replaceText("4"), closeSoftKeyboard());

            ViewInteraction appCompatEditText13 = onView(
                    allOf(withId(R.id.eTBiography),
                            withParent(allOf(withId(R.id.activity_biography),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText13.perform(click());

            ViewInteraction appCompatEditText14 = onView(
                    allOf(withId(R.id.eTBiography),
                            withParent(allOf(withId(R.id.activity_biography),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText14.perform(replaceText("4espresso is hard qq"), closeSoftKeyboard());

            ViewInteraction appCompatButton6 = onView(
                    allOf(withId(R.id.bNext5), withText("Next"),
                            withParent(allOf(withId(R.id.activity_biography),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatButton6.perform(click());

            /** Test Final Registration Page */
            ViewInteraction userprof = onView(withId(R.id.activity_user_profile));
            userprof.check(matches(isDisplayed()));

            //check personalBio
            onView(withId(R.id.eTBiography)).check(matches(withText("4espresso is hard qq")));

            onView(withId(R.id.eTName)).check(matches(withText("Espresso Test")));

            /*ViewInteraction textView = onView(
                    allOf(withId(R.id.tvInstruments), withText("Recorder, "),
                            childAtPosition(
                                    allOf(withId(R.id.activity_user_profile),
                                            childAtPosition(
                                                    withId(android.R.id.content),
                                                    0)),
                                    7),
                            isDisplayed()));
            textView.check(matches(withText("Recorder, ")));*/

            ViewInteraction appCompatButton7 = onView(
                    allOf(withId(R.id.save_button), withText("Save"),
                            withParent(allOf(withId(R.id.activity_user_profile),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatButton7.perform(click());

            /** Test Profile Display */
            ViewInteraction profdisp = onView(withId(R.id.activity_display_profile));
            profdisp.check(matches(isDisplayed()));

            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

            /** Test Friend Display */
            ViewInteraction appCompatTextView3 = onView(
                    allOf(withId(R.id.title), withText("Friend"), isDisplayed()));
            appCompatTextView3.perform(click());

            ViewInteraction fr = onView(withId(R.id.activity_friend_list));
            fr.check(matches(isDisplayed()));

            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

            /** Test Matching List Display */
            ViewInteraction appCompatTextView4 = onView(
                    allOf(withId(R.id.title), withText("Match Up"), isDisplayed()));
            appCompatTextView4.perform(click());

            ViewInteraction match = onView(withId(R.id.activity_matches));
            match.check(matches(isDisplayed()));

            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

            ViewInteraction appCompatTextView5 = onView(
                    allOf(withId(R.id.title), withText("Match Up"), isDisplayed()));
            appCompatTextView5.perform(click());

            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

            /** Test Log out */
            ViewInteraction appCompatTextView6 = onView(
                    allOf(withId(R.id.title), withText("Log out"), isDisplayed()));
            appCompatTextView6.perform(click());

            ViewInteraction login = onView(withId(R.id.activity_logina_ctivity));
            login.check(matches(isDisplayed()));

            /** Test Log in */
            ViewInteraction appCompatEditText15 = onView(
                    allOf(withId(R.id.Login_name),
                            withParent(allOf(withId(R.id.activity_logina_ctivity),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText15.perform(click());

            ViewInteraction appCompatEditText16 = onView(
                    allOf(withId(R.id.Login_name),
                            withParent(allOf(withId(R.id.activity_logina_ctivity),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText16.perform(replaceText("espressotest@ex.com"), closeSoftKeyboard());

            ViewInteraction appCompatEditText17 = onView(
                    allOf(withId(R.id.Login_pass),
                            withParent(allOf(withId(R.id.activity_logina_ctivity),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText17.perform(replaceText("testtest"), closeSoftKeyboard());

            ViewInteraction appCompatEditText18 = onView(
                    allOf(withId(R.id.Login_pass), withText("testtest"),
                            withParent(allOf(withId(R.id.activity_logina_ctivity),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText18.perform(pressImeActionButton());

            ViewInteraction appCompatButton8 = onView(
                    allOf(withId(R.id.Login_botton), withText("Login"),
                            withParent(allOf(withId(R.id.activity_logina_ctivity),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatButton8.perform(click());

            /** Test Prof Display Again */
            ViewInteraction logindisplay = onView(
                    allOf(withId(R.id.title), withText("Friend"), isDisplayed()));

            ViewInteraction logindisp = onView(withId(R.id.activity_display_profile));
            logindisplay.check(matches(isDisplayed()));

            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
            /*ViewInteraction afterlogoutlogin = onView(withId(R.id.activity_display_profile));
            afterlogoutlogin.check(matches(isDisplayed()));

            onView(withId(R.id.eTBiography)).check(matches(withText("4espresso is hard qq")));
            onView(withId(R.id.eTName)).check(matches(withText("Espresso Test")));*/

            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

            /** Test Delete Account */
            ViewInteraction appCompatTextView7 = onView(
                    allOf(withId(R.id.title), withText("Delete Account"), isDisplayed()));
            appCompatTextView7.perform(click());

            ViewInteraction del = onView(withId(R.id.activity_delete));
            del.check(matches(isDisplayed()));

            ViewInteraction appCompatEditText19 = onView(
                    allOf(withId(R.id.Login_email),
                            withParent(allOf(withId(R.id.activity_delete),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText19.perform(click());

            ViewInteraction appCompatEditText20 = onView(
                    allOf(withId(R.id.Login_email),
                            withParent(allOf(withId(R.id.activity_delete),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText20.perform(replaceText("espressotest@ex.com"), closeSoftKeyboard());

            ViewInteraction appCompatEditText21 = onView(
                    allOf(withId(R.id.Login_email), withText("espressotest@ex.com"),
                            withParent(allOf(withId(R.id.activity_delete),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText21.perform(pressImeActionButton());

            ViewInteraction appCompatEditText22 = onView(
                    allOf(withId(R.id.Login_pass),
                            withParent(allOf(withId(R.id.activity_delete),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText22.perform(replaceText("testtest"), closeSoftKeyboard());

            ViewInteraction appCompatEditText23 = onView(
                    allOf(withId(R.id.Login_pass), withText("testtest"),
                            withParent(allOf(withId(R.id.activity_delete),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText23.perform(pressImeActionButton());

            ViewInteraction appCompatButton9 = onView(
                    allOf(withId(R.id.delete_button), withText("Delete Account"),
                            withParent(allOf(withId(R.id.activity_delete),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatButton9.perform(click());

            login.check(matches(isDisplayed()));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
