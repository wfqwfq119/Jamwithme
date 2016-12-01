package cse110.jamwithme;


import android.support.annotation.NonNull;
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

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DeleteTest {

    @Rule
    public ActivityTestRule<DeleteAccountActivity> mActivityTestRule = new ActivityTestRule<>(DeleteAccountActivity
            .class);

    /** Given user exists and logged in
     * When the user goes to menu and clicks logout AND when they verify info
     * Then account is deleted
     */
    @Test
    public void clickDelete() {
        //Given user exists
        /*final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot uid : dataSnapshot.getChildren()) {
                    String name = uid.child("name").getValue().toString();
                    if(name.equals("Espresso Test")) {
                        final String userstring = uid.getKey().toString();

                        fa.signInWithEmailAndPassword("espressotest@ex.com", "testtest").addOnCompleteListener
                                (new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            System.exit(1);
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


        FirebaseAuth fa = FirebaseAuth.getInstance();
        fa.signInWithEmailAndPassword("espressotest@ex.com", "testtest").addOnCompleteListener
                (new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            System.exit(1);
                        }
                        else {
                            //FirebaseUser fu = fa.getCurrentUser();
                            if(fu != null)
                            {
                            }
                        }
                    }
                });*/

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
        appCompatEditText22.perform(replaceText("password"), closeSoftKeyboard());

        ViewInteraction appCompatEditText23 = onView(
                allOf(withId(R.id.Login_pass), withText("password"),
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

        ViewInteraction login = onView(withId(R.id.activity_logina_ctivity));
        login.check(matches(isDisplayed()));

    }

}
