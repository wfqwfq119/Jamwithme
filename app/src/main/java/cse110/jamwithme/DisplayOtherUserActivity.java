package cse110.jamwithme;

/**
 * Created by Storm Quark on 11/21/2016.
 */

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DisplayOtherUserActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private StorageReference storage;

    private ImageView prof_pic;
    private ImageButton edit_button;
    private Button play_myjam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_profile);

        /*mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance().getReference();

        prof_pic = (ImageView)findViewById(R.id.profile_pic);
        final String key = "users/" + user.getUid();
        storage.child(key + "/myimg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) { Picasso.with(ProfileDisplay.this).load(uri).fit().into(prof_pic); }
        });

        play_myjam = (Button)findViewById(R.id.my_jams);
        play_myjam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storage.child(key + "/myjam").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        MediaPlayer mp = MediaPlayer.create(ProfileDisplay.this, uri);
                        mp.start();
                    }
                });
            }
        });

        Button admin_see_questions = (Button)findViewById(R.id.admin_new_questions);
    if ( !clause ) {
        admin_see_questions.setVisibility(View.GONE);
    }

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        TextView insrt_list = (TextView)findViewById(R.id.tvIntsrlist);
        DatabaseWatcher d = new DatabaseWatcher(this);
        //d.updateData(elems, info);

        d.updateUserProfile();*/
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

}
