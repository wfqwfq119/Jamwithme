package cse110.jamwithme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Created by Storm Quark on 11/12/2016.
 */

public class ProfileDisplay extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private StorageReference storage;

    public ImageButton edit_button;
    public Button play_myjam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_profile);
        //Toast.makeText(ProfileDisplay.this, "Display Profile", Toast.LENGTH_LONG).show();
        init();

        String[] elems = {"personalBio", "name"};
        final int[] info = {R.id.pbio, R.id.name};

        //update according to database saved
        mAuth = FirebaseAuth.getInstance();

        //TODO update according to database saved
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        TextView insrt_list = (TextView)findViewById(R.id.tvIntsrlist);
        //Bundle insrt_Bun = getIntent().getExtras();
        //String msg = insrt_Bun.getString("instrs");
        //insrt_list.setText(msg);
        DatabaseWatcher d = new DatabaseWatcher(this);
        d.updateData(elems, info);
    }

    // Give 'save' button functionality
    public void init() {
        edit_button = (ImageButton)findViewById(R.id.editProf);
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit_profile = new Intent(ProfileDisplay.this,
                        UserProfileActivity.class);
                startActivity(edit_profile);
            }
        });

        storage = FirebaseStorage.getInstance().getReference();

        play_myjam = (Button)findViewById(R.id.my_jams);
        play_myjam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storage.child("MyJam/54ea3a83-b745-4886-b163-ac999c1fd607.mp3").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        MediaPlayer mp = MediaPlayer.create(ProfileDisplay.this, uri);
                        mp.start();
                    }
                });
            }
        });
    }

}
