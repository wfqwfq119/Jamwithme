package cse110.jamwithme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.EditText;


public class UserProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public Button add_jams;
    public Button saveB;
    private Button add_Instr;
    //private ImageView ivProfile;
    //private ImageButton camButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        init();
        initSaveButton();

        String[] elems = {"personalBio", "name"};
        final int[] info = {R.id.eTBiography, R.id.eTName};

        //TODO update according to database saved
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        //String key = "users/" + mAuth.getCurrentUser().getUid();

        DatabaseWatcher d = new DatabaseWatcher(this);

        d.updateData(elems, info);
        //Toast.makeText(this, "USER LOCATION", Toast.LENGTH_LONG).show();
        UserLocation ul = new UserLocation(this, mAuth, myRef);
        String[] s = {"location"};
        saveDataBy("location", ul.getLongLat());

        //locationT(myRef, key);

        //cameraButton();
        add_Instr = (Button) findViewById(R.id.Profile_add_button);
        add_Instr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfileActivity.this,InstrumentSelect.class));
            }
        });

    }

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

    // Allow saving
    public void initSaveButton() {
        saveB = (Button)findViewById(R.id.save_button);
        saveB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                try {
                    startActivity(new Intent(UserProfileActivity.this, ProfileDisplay.class));
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /** Save data by giving in the "key" (where to find user info in database) and which view
     * on the layout to pull the new data from.
     */
    private void saveDataBy(String key, final int tview) {
        myRef = FirebaseDatabase.getInstance().getReference();
        TextView viewval = (TextView) findViewById(tview);
        String newInput = viewval.getText().toString();

        myRef.child(key).setValue(newInput);
    }

    private void saveDataBy(String key, Object newval) {
        myRef = FirebaseDatabase.getInstance().getReference();
        key = mAuth.getCurrentUser().getUid() + key;
        myRef.child(key).setValue(newval);
    }

    //Save page to database
    private void saveData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //If no user is logged in, go to login page
        if (user == null) {
            startActivity(new Intent(UserProfileActivity.this, logina_ctivity.class));
        }

        //Get key to the user node in database
        String key = "users/" + user.getUid();

        //update current view of user to database
        saveDataBy(key+"/name", R.id.eTName);
        saveDataBy(key+"/personalBio", R.id.eTBiography);
    }

    private void saveData(String[] keys, final int[] r_id) {
        FirebaseUser user = mAuth.getCurrentUser();

        //If no user is logged in, go to login page
        if (user == null) {
            startActivity(new Intent(UserProfileActivity.this, logina_ctivity.class));
        }

        //Get key to the user node in database
        String key = "users/" + user.getUid();

        //quick error check that provided keys have matching r_id
        if(keys.length != r_id.length) {
            Toast.makeText(this, "Error matching user id to database", Toast.LENGTH_LONG).show();
        }

        //for each provided thing, update
        for(int i = 0; i < keys.length; i++) {
            saveDataBy(key+"/"+keys[i], r_id[i]);
        }
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


