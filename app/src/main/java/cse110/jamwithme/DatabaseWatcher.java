package cse110.jamwithme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Storm Quark on 11/11/2016.
 */

public class DatabaseWatcher {
    Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    //String userkey;

    public DatabaseWatcher(Context c) {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        mContext = c;
    }

    /** Update data by giving in the "key" (where to find user info in database) and which view
     * on the layout to put the new data to.
     */
    private void updateTextDataBy(String key, final int tview) {

        myRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Activity a = (Activity)mContext;
                if(dataSnapshot.exists()) {
                    String newval = (String) dataSnapshot.getValue();
                    TextView viewval = (TextView) a.findViewById(tview);
                    viewval.setText(newval);
                }
                else
                    badData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void updateData(String[] keys, final int[] r_id) {
        CharSequence error_msg = "Error: not enough data to " +
                "update or not enough matching section for data.";
        FirebaseUser user = mAuth.getCurrentUser();

        badUser(user);

        //Get key to the user node in database
        String key = "users/" + user.getUid();

        //quick error check that provided keys have matching r_id
        if(keys.length != r_id.length) {
            Toast.makeText(mContext, error_msg, Toast.LENGTH_LONG).show();
        }

        //for each provided thing, update
        for(int i = 0; i < keys.length; i++) {
            updateTextDataBy(key+"/"+keys[i], r_id[i]);
        }
    }

    public void updateRating(final int r_id) {
        FirebaseUser user = mAuth.getCurrentUser();
        badUser(user);


        //Get key to the user node in database
        String key = "users/" + user.getUid() + "/rating";

        myRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Activity a = (Activity)mContext;
                if(dataSnapshot.exists()) {
                    float newval = Float.valueOf(dataSnapshot.getValue().toString());
                    RatingBar viewR = (RatingBar) a.findViewById(r_id);
                    viewR.setRating(newval);
                }
                else
                    badData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void updateUserProfile() {
        UserLocation ul = new UserLocation(mContext, mAuth, myRef);

        String[] elems = {"personalBio", "name"};
        final int[] info = {R.id.eTBiography, R.id.eTName};

        updateData(elems, info);
        updateRating(R.id.ratingBar);
        saveDataBy("location", ul.getLongLat());
    }

    /** Save data by giving in the "key" (where to find user info in database) and which view
     * on the layout to pull the new data from.
     */
    public void saveDataBy(String key, final int tview) {
        Activity a = (Activity)mContext;
        TextView viewval = (TextView) a.findViewById(tview);
        String newInput = viewval.getText().toString();

        myRef.child(key).setValue(newInput);
        /*myRef.child("users").child(uid).child("username").setValue(findViewById(R.id.Profile_Username));*/
    }

    public void saveDataBy(String key, Object newval) {
        key = mAuth.getCurrentUser().getUid() + key;
        myRef.child(key).setValue(newval);
    }

    public void saveRating(String key, float newval) {
        key = "users/" + mAuth.getCurrentUser().getUid() + "/" + key;
        myRef.child(key).setValue(newval);
    }

    public void saveData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        badUser(user);

        //Get key to the user node in database
        String key = "users/" + user.getUid();

        //update current view of user to database
        saveDataBy(key+"/name", R.id.eTName);
        saveDataBy(key+"/personalBio", R.id.eTBiography);

        Activity a = (Activity)mContext;
        RatingBar currRat = (RatingBar) a.findViewById(R.id.ratingBar);
        float cR = currRat.getRating();
        saveRating("rating", cR);
    }

    public void saveData(String[] keys, final int[] r_id) {
        FirebaseUser user = mAuth.getCurrentUser();
        badUser(user);

        //Get key to the user node in database
        String key = "users/" + user.getUid();

        //quick error check that provided keys have matching r_id
        if (keys.length != r_id.length) {
            Toast.makeText(mContext, "Error matching user id to database", Toast.LENGTH_LONG).show();
        }

        //for each provided thing, update
        for (int i = 0; i < keys.length; i++) {
            saveDataBy(key + "/" + keys[i], r_id[i]);
        }
    }

    private void badUser(FirebaseUser user) {
        //if no user is logged in, go to login page
        if (user == null) {
            System.out.println("USER IS NULL!!!!\n");
            mContext.startActivity(new Intent(mContext, logina_ctivity.class));
        }
    }

    private void badData() {
        System.out.println("USER IS NULL!!!!\n");
        mContext.startActivity(new Intent(mContext, logina_ctivity.class));
    }

    public void deleteUserFromDatabase() {
        FirebaseUser user = mAuth.getCurrentUser();
        badUser(user);

        //Get key to the user node in database
        String key = "users/" + user.getUid();

        myRef.child(key).removeValue();
    }

    /*public boolean failedDatabase(String key) {
        myRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("") )
                    return False;
                return True;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }*/
}
