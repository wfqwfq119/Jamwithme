package cse110.jamwithme;


import android.support.annotation.NonNull;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.IdlingPolicy;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

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

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CreateAccountLogout {

    @Rule
    public ActivityTestRule<Splash> mActivityTestRule = new ActivityTestRule<>(Splash.class);

    /** Given account doesn't exist
     * When user registers
     * Then they can go to their profile and logout.
     */
    @Test
    public void createAccountLogout() {
        //Given the account doesn't exist
        FirebaseAuth.getInstance().signOut();   //Log out
        final FirebaseAuth fa = FirebaseAuth.getInstance();
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot uid : dataSnapshot.getChildren()) {
                    String name = uid.child("name").getValue().toString();
                    if(name.equals("Espresso Test")) {
                        final String userstring = uid.getKey().toString();
                        //delete account
                        fa.signInWithEmailAndPassword("espressotest@ex.com", "password")
                                .addOnCompleteListener
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
                                                        if (task.isSuccessful()) {
                                                        }
                                                        else {
                                                        }
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

        /** Test here */
        //Sleep and then begin test
        try {
            Thread.sleep(5000);

            ViewInteraction appCompatTextView = onView(
                    allOf(withId(R.id.Login_tv), withText("Register Here"),
                            withParent(allOf(withId(R.id.activity_logina_ctivity),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatTextView.perform(click());

            ViewInteraction appCompatEditText = onView(
                    allOf(withId(R.id.Re_Username),
                            withParent(allOf(withId(R.id.activity_register),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText.perform(replaceText("Espresso Test"), closeSoftKeyboard());

            ViewInteraction appCompatEditText2 = onView(
                    allOf(withId(R.id.Register_Email),
                            withParent(allOf(withId(R.id.activity_register),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText2.perform(replaceText("espressotest@ex.com"), closeSoftKeyboard());

            ViewInteraction appCompatEditText3 = onView(
                    allOf(withId(R.id.Register_password),
                            withParent(allOf(withId(R.id.activity_register),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText3.perform(replaceText("password"), closeSoftKeyboard());

            ViewInteraction appCompatEditText4 = onView(
                    allOf(withId(R.id.Register_password_confirm),
                            withParent(allOf(withId(R.id.activity_register),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText4.perform(replaceText("password"), closeSoftKeyboard());

            ViewInteraction appCompatEditText5 = onView(
                    allOf(withId(R.id.Register_password_confirm), withText("password"),
                            withParent(allOf(withId(R.id.activity_register),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText5.perform(pressImeActionButton());

            ViewInteraction appCompatButton = onView(
                    allOf(withId(R.id.Re_button), withText("Sign Up"),
                            withParent(allOf(withId(R.id.activity_register),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatButton.perform(click());

            ViewInteraction appCompatButton2 = onView(
                    allOf(withId(R.id.bNext), withText("Next"),
                            withParent(allOf(withId(R.id.activity_camera),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatButton2.perform(click());

            ViewInteraction appCompatButton3 = onView(
                    allOf(withId(R.id.bNext2), withText("Next"),
                            withParent(allOf(withId(R.id.activity_add_jams_activity),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatButton3.perform(click());

            ViewInteraction appCompatButton4 = onView(
                    allOf(withId(R.id.bNext3), withText("Next"), isDisplayed()));
            appCompatButton4.perform(click());

            ViewInteraction appCompatButton5 = onView(
                    allOf(withId(R.id.bNext4), withText("Next"),
                            withParent(allOf(withId(R.id.activity_experience),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatButton5.perform(click());

            ViewInteraction appCompatButton6 = onView(
                    allOf(withId(R.id.bNext5), withText("Next"),
                            withParent(allOf(withId(R.id.activity_biography),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatButton6.perform(click());

            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

            ViewInteraction appCompatTextView2 = onView(
                    allOf(withId(R.id.title), withText("Settings"), isDisplayed()));
            appCompatTextView2.perform(click());

            ViewInteraction appCompatTextView3 = onView(
                    allOf(withId(R.id.title), withText("Log out"), isDisplayed()));
            appCompatTextView3.perform(click());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
