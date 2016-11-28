package cse110.jamwithme;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
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

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MatchQuery extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference db_ref;
    private Intent prev_intent;
    private String str_idx;
    private String m_name;
    private int idx;
    private StorageReference storage;

    private ImageView prof_pic;
    private ImageButton Bdecline;
    private ImageButton Bplay_mp;
    private ImageButton Baccept;
    private MediaPlayer mp;
    private RatingBar viewR;
    private TextView usr_name;
    private TextView instruments;

    //public MatchQuery(ArrayList<String> next_matches) { matches = next_matches; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_query);
        mp = null;
        prev_intent = getIntent();
        str_idx = prev_intent.getStringExtra("position");
        idx = Integer.parseInt(str_idx);

        storage = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        db_ref = database.getReference();

        prof_pic = (ImageView)findViewById(R.id.ivProfile);
        Bdecline = (ImageButton)findViewById(R.id.bDecline);
        Bplay_mp = (ImageButton)findViewById(R.id.bPlay);
        Baccept = (ImageButton)findViewById(R.id.bAccept);
        viewR = (RatingBar)findViewById(R.id.exp_rating_bar);
        usr_name = (TextView)findViewById(R.id.tvName);
        instruments = (TextView)findViewById(R.id.tvInstr);

        determ_match();
    }

    public void displayMatch(String next_match) {
        String key = "users/" + next_match;

        // Setup profile picture
        storage.child(key + "/myimg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) { Picasso.with(MatchQuery.this).load(uri).fit().into(prof_pic); }
        });

        // Setup profile jam
        storage.child(key + "/myjam").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) { mp = MediaPlayer.create(MatchQuery.this, uri); }
        });

        // Setup name of next_match
        db_ref.child(key + "/name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    m_name = (String) dataSnapshot.getValue();
                    usr_name.setText(m_name);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        // Setup rating of next_match
        db_ref.child(key + "/rating").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    float newval = Float.valueOf(dataSnapshot.getValue().toString());
                    viewR.setRating(newval);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public void suspend_mp() {
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.stop();
                mp.release();
            }
        }
    }

    public void determ_match() {
        displayMatch(MatchingDisplay.userlist.get(idx));
        final Intent back = new Intent(MatchQuery.this, MatchingDisplay.class);
        back.putExtra("updated", "true");


        // Suspend mp if playing, then do nothing else
        Bdecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suspend_mp();
                MatchingDisplay.userlist.remove(idx);
                MatchingDisplay.userlistname.remove(idx);
                startActivity(back);
            }
        });

        // Play matched users' profile jam
        Bplay_mp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp != null) { mp.start(); }
                else { Toast.makeText(MatchQuery.this, "Nothing to play!", Toast.LENGTH_LONG); }
            }
        });

        // Suspend mp if playing, then add matched user to curr_user's friends list
        Baccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suspend_mp();
                friend_obj next_fr = new friend_obj(MatchingDisplay.userlist.get(idx), m_name);
                friend_list.friend_Array.add(next_fr);
                MatchingDisplay.userlist.remove(idx);
                MatchingDisplay.userlistname.remove(idx);
                startActivity(back);
            }
        });
    }
}
