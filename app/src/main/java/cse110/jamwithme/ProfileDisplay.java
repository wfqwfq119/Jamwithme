package cse110.jamwithme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Storm Quark on 11/12/2016.
 */

public class ProfileDisplay extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public Button add_jams;
    //private ImageView ivProfile;
    //private ImageButton camButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_profile);

        //TODO update according to database saved
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        updateData();
        //cameraButton();
    }

    /* Update data by giving in the "key" (where to find user info in database) and which view
     * on the layout to put the new data to.
     */
    private void updateDataBy(String key, final int tview) {

        myRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String newval = (String) dataSnapshot.getValue();
                TextView viewval = (TextView) findViewById(tview);
                viewval.setText(newval);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //Update page according to database info
    private void updateData() {
        ProgressDialog Re_Pro = new ProgressDialog(UserProfileActivity.this); //TODO
        FirebaseUser user = mAuth.getCurrentUser();

        //If no user is logged in, go to login page
        if (user == null) {
            startActivity(new Intent(UserProfileActivity.this, logina_ctivity.class));
        }

        //Get key to the user node in database
        String key = "users/" + user.getUid();
        //update name data of that user into the name view
        updateDataBy(key+"/name", R.id.eTName);

        /*myRef.child("users").child(uid).child("username").setValue(findViewById(R.id.Profile_Username));*/
    }

    /** GIVE CAMERA BUTTON FUNCTIONALITY **/
    /*public void cameraButton() {
        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        camButton = (ImageButton) findViewById(R.id.iBTakePic);
        camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent image = new Intent(UserProfileActivity.this,
                        camera.class);
                startActivity(image);
            }
        });
    }*/

}
