package cse110.jamwithme;


import android.support.annotation.NonNull;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.firebase.geofire.GeoLocation;
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

import org.junit.Before;
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
public class LoginWithValidAccount {

    @Rule
    public ActivityTestRule<Splash> mActivityTestRule = new ActivityTestRule<>(Splash.class);

    @Test
    public void loginWithValidAccount() {
        //Given user exists
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        final FirebaseAuth fa = FirebaseAuth.getInstance();

        //If user exists, sign in
        myRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot uid : dataSnapshot.getChildren()) {
                    String name = uid.child("name").getValue().toString();
                    if(name.equals("Espresso Test")) {
                        fa.signInWithEmailAndPassword("espressotest@ex.com", "password")
                                .addOnCompleteListener
                                        (new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                            }
                                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });

        //If isn't found, create account
        fa.createUserWithEmailAndPassword("espressotest@ex.com", "password").addOnCompleteListener
                (new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            if (user == null) {
                                System.out.println("Failed to log in");
                            }

                            //Create user object and place into database
                            User newUser = new User();
                            newUser.setName("Espresso Test");
                            newUser.setUid(user.getUid());
                            newUser.setLocation(new GeoLocation(0,0));

                            mDatabase.child("users").child(user.getUid()).setValue(newUser);
                        }
                    }
                });

        try {
            Thread.sleep(5000);

            ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.Login_name),
                        withParent(allOf(withId(R.id.activity_logina_ctivity),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
            appCompatEditText.perform(replaceText("espressotest@ex.com"), closeSoftKeyboard());

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
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
