package cse110.jamwithme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class UserProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public Button add_jams;

    // Give 'add_jams' button functionality
    public void init() {
        add_jams = (Button)findViewById(R.id.add_my_jams);
        add_jams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent access_jams = new Intent(UserProfileActivity.this,
                                                add_jams_activity.class);
                startActivity(access_jams);
            }
        });
    }

    //Update page according to database info
    private void updateData() {
        ProgressDialog Re_Pro = new ProgressDialog(UserProfileActivity.this); //TODO
        FirebaseUser user = mAuth.getCurrentUser();

        //If no user is logged in, go to login page
        if (user == null) {
            startActivity(new Intent(UserProfileActivity.this,logina_ctivity.class));
        }

        String key = "users/" + user.getUid();

        myRef.child(key + "/name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.getValue();
                TextView username = (TextView) findViewById(R.id.Profile_Username);
                username.setText(name);
                // do your stuff here with value
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*myRef.child("users").child(uid).child("username").setValue(findViewById(R.id.Profile_Username));*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        init();

        //TODO update according to database saved
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        updateData();
    }
}


