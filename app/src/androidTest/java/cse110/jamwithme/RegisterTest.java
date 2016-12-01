package cse110.jamwithme;


import android.support.annotation.NonNull;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
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
public class RegisterTest {

    @Rule
    public ActivityTestRule<logina_ctivity> mActivityTestRule = new ActivityTestRule<>(logina_ctivity.class);

    @Test
    public void registerTest() {
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
                    allOf(withId(R.id.Register_password), withText("password"),
                            withParent(allOf(withId(R.id.activity_register),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatEditText4.perform(pressImeActionButton());

            ViewInteraction appCompatButton = onView(
                    allOf(withId(R.id.Re_button), withText("Sign Up"),
                            withParent(allOf(withId(R.id.activity_register),
                                    withParent(withId(android.R.id.content)))),
                            isDisplayed()));
            appCompatButton.perform(click());
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
