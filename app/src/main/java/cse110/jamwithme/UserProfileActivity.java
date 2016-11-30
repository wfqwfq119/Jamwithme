package cse110.jamwithme;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import android.widget.EditText;


public class UserProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private StorageReference storage;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public Button add_jams;
    public Button saveB;
    private Button add_Instr;

    /*** CAMERA OBJECTS ***/
    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_CAMERA = 2;
    private ImageView imageView;
    private ImageButton camButton;
    private UsingCamera camObj;
    private static int picWidth = 50;
    private static int picHeight = 50;
    Uri img_uri;
    private ProgressDialog upl_progress;

    private TextView displayInstruments;
    private String instruments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        displayInstruments = (TextView)findViewById(R.id.tvInstruments);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        storage = FirebaseStorage.getInstance().getReference();
        storage.child("users/" + user.getUid() + "/myimg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(UserProfileActivity.this).load(uri).resize(picWidth, picHeight).into(imageView);
            }
        });

        storage.child("users/" + user.getUid() + "/myimg").getDownloadUrl().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                camObj.setDefaultPhoto(imageView, picWidth, picHeight);
            }
        });

        init();
        initSaveButton();

        //TODO update according to database saved
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        DatabaseWatcher d = new DatabaseWatcher(this);
        d.updateUserProfile();

        add_Instr = (Button) findViewById(R.id.Profile_add_button);
        add_Instr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(UserProfileActivity.this, InstrumentSelect.class);
                next.putExtra("activity", "UserProfileActivity");
                startActivity(next);
            }
        });

        /****** CAMERA - Nancy *****/
        camObj = new UsingCamera(this, "UserProfileActivity");
        imageView = (ImageView) findViewById(R.id.ivProfile);
        camButton = (ImageButton) findViewById(R.id.camButton);

        /*
        intent = getIntent();
        instruments = intent.getStringExtra("instrs");
        displayInstruments = (TextView) findViewById(R.id.tvInstruments);
        displayInstruments.setText(instruments);
        */
        //displayInstruments.setText(instrumentSelect.select_list.toString()); //TODO

        camObj.cameraButton(camButton);



        myRef.child("users/" + user.getUid() + "/Instruments").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getValue() == null) {
                    displayInstruments.setText("No Instruments selected");
                    return;
                }
                String my_list = dataSnapshot.getValue().toString();
                //System.out.println(my_list);
                String[] new_list = my_list.split(",");
                //System.out.println(new_list);
                for(String si : new_list){
                    if(si == null)
                        break;
                    //String[] check_list = si.split("=");
                    //System.out.println(check_list[0]);

                    if(instruments == null){
                        instruments = si;
                        instruments += ", ";
                    }
                    else if(instruments.toCharArray().length == new_list.length){
                        instruments += si;
                    }
                    else {
                        instruments += si;
                        instruments += ", ";
                    }
                }
                //System.out.println(instruments);
                displayInstruments.setText(instruments);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                /*if(dataSnapshot.getValue() == null) {
                    displayInstruments.setText("No Instruments selected");
                    return;
                }
                String my_list = dataSnapshot.getValue().toString();
                //System.out.println(my_list);
                String[] new_list = my_list.split(",");
                System.out.println(new_list);
                for(String si : new_list){
                    if(si == null)
                        break;
                    //String[] check_list = si.split("=");
                    //System.out.println(check_list[0]);

                    if(instruments == null){
                        instruments = si;
                        instruments += ", ";
                    }
                    else if(instruments.toCharArray().length == new_list.length){
                        instruments += si;
                    }
                    else {
                        instruments += si;
                        instruments += ", ";
                    }
                }
                //System.out.println(instruments);
                displayInstruments.setText(instruments);*/
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //InstrList(user, instruments);
        //System.out.println(instruments);

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
            case R.id.navi_friend:
                startActivity(new Intent(this,friend_list.class));
                break;
            case R.id.matching:
                Intent match = new Intent(this, MatchingDisplay.class);
                match.putExtra("updated", "false");
                startActivity(match);
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
                access_jams.putExtra("activity", "UserProfileActivity");
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
                    FirebaseUser user = mAuth.getCurrentUser();
                    Intent displayProf = new Intent(UserProfileActivity.this, ProfileDisplay.class);
                    displayProf.putExtra("userid", user.getUid());
                    startActivity(displayProf);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            img_uri = camObj.usingCamera(data, imageView, picWidth, picHeight);
        }
        if (requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
            img_uri = camObj.selectFromGallery(data, imageView, picWidth, picHeight);
        }
        camObj.upload_img(img_uri,storage, upl_progress);
    }

}


