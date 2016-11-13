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

        DatabaseWatcher d = new DatabaseWatcher(this);

        d.updateData();
        //cameraButton();
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
