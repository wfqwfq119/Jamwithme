package cse110.jamwithme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Storm Quark on 11/12/2016.
 */

public class ProfileDisplay extends CreateMenu {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private StorageReference storage;

    private MediaPlayer mp;
    private ImageView imageView;
    private ImageButton edit_button;
    private Button play_myjam;
    private String userID;
    private TextView displayInstruments; // Nancy
    private String instruments;
    private static int width = 50;
    private static int height = 50;
    public UsingCamera camObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_profile);

        displayInstruments = (TextView)findViewById(R.id.tvInstruments);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance().getReference();

        //See who is being displayed by input uid, default being current user
        if(getIntent().hasExtra("userid")) {
            userID = getIntent().getStringExtra("userid");
        }
        else
            userID = user.getUid();

        final String key = "users/" + userID;

        //Set up profile picture
        imageView = (ImageView)findViewById(R.id.profile_pic);
        camObj = new UsingCamera(this, "UserProfileActivity");
        storage.child(key + "/myimg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
            //Picasso.with(ProfileDisplay.this).load(uri).fit().into(imageView);
            Picasso.with(ProfileDisplay.this).load(uri).resize(width, height).into(imageView);
                }
        });

        storage.child(key + "/myimg").getDownloadUrl().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            camObj.setDefaultPhoto(imageView, width, height);
            }
        });

        //Set up profile jam
        play_myjam = (Button)findViewById(R.id.my_jams);
        play_myjam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        storage.child(key + "/myjam").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        mp = MediaPlayer.create(ProfileDisplay.this, uri);
                        mp.start();
                    }
                });
            }
        });

        init(); //Initialize button

        //update according to database saved on the user's id
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        DatabaseWatcher d = new DatabaseWatcher(this);
        d.updateUserProfile(userID);

        myRef.child("users/" + user.getUid() + "/Instruments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null) {
                    displayInstruments.setText("No Instruments selected");
                    return;
                }
                String my_list = dataSnapshot.getValue().toString();
                String[] new_list = my_list.split(",");
                for(String s : new_list){
                    if(s == null)
                        break;
                    String[] check_list = s.split("=");
                    if(s.equals(new_list[0]))
                    {
                        instruments = check_list[1];
                        instruments += ",";
                    }
                    else if(s.equals(new_list[new_list.length-1]))
                    {
                        instruments += check_list[1];
                    }
                    else {
                        instruments += check_list[1];
                        instruments += ",";
                    }
                }
                //System.out.println(instruments);
                displayInstruments.setText(instruments);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    // Give 'edit' button functionality
    public void init() {
        FirebaseUser currU = mAuth.getCurrentUser();

        edit_button = (ImageButton)findViewById(R.id.editProf);
        if(userID.equals(currU.getUid())) {
            edit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent edit_profile = new Intent(ProfileDisplay.this,
                            UserProfileActivity.class);
                    startActivity(edit_profile);
                }
            });
        }
        else {
            edit_button.setVisibility(View.GONE);
        }
    }
}
