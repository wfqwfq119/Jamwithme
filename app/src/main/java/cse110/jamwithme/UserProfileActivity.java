package cse110.jamwithme;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.TextView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
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

        //TODO update according to database saved
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        //String key = "users/" + mAuth.getCurrentUser().getUid();

        DatabaseWatcher d = new DatabaseWatcher(this);
        d.updateUserProfile();

        //cameraButton();
        add_Instr = (Button) findViewById(R.id.Profile_add_button);
        add_Instr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfileActivity.this,InstrumentSelect.class));
            }
        });

    }
    //try to create menu
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.log_out:
                mAuth.signOut();
                startActivity(new Intent(this,logina_ctivity.class));
                break;
            case R.id.action_settings:
                break;
            case R.id.navi_disprofile:
                startActivity(new Intent(this,ProfileDisplay.class));
                break;
            case R.id.navi_message:
                startActivity(new Intent(this,messagerAct.class));
                break;
            case R.id.delete_acct:
                Toast.makeText(this, "Please verify account!", Toast.LENGTH_SHORT)
                        .show();
                try{
                    startActivity(new Intent(this, DeleteAccountActivity.class));
                }catch(Exception e) {
                    e.printStackTrace();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
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
        final DatabaseWatcher d = new DatabaseWatcher(this);
        saveB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.saveData();
                try {
                    startActivity(new Intent(UserProfileActivity.this, ProfileDisplay.class));
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
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


