package cse110.jamwithme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
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
        FirebaseUser user = mAuth.getCurrentUser();
        badUser(user);

        updateOtherUserData(user.getUid(), keys, r_id);
    }

    /** Update other users' text data according to userid */
    public void updateOtherUserData(String userid, String[] keys, final int[] r_id) {
        CharSequence error_msg = "Error: not enough data to " +
                "update or not enough matching section for data.";

        //Get key to the user node in database
        String key = "users/" + userid;

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

        updateRating(user.getUid(), r_id);
    }

    public void updateRating(String uid, final int r_id) {
        //Get key to the user node in database
        String key = "users/" + uid + "/rating";

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

    /** Update profile to reflect database info */
    public void updateUserProfile() {
        UserLocation ul = new UserLocation(mContext, mAuth, myRef);

        String[] elems = {"personalBio", "name"};
        final int[] info = {R.id.eTBiography, R.id.eTName};

        updateData(elems, info);
        updateRating(R.id.ratingBar);
        saveDataBy("location", ul.getLongLat());
    }

    /** Update profile to reflect database info */
    public void updateUserProfile(String uid) {
        String[] elems = {"personalBio", "name"};
        final int[] info = {R.id.eTBiography, R.id.eTName};

        updateOtherUserData(uid, elems, info);
        updateRating(uid, R.id.ratingBar);
    }

    /** Save data by giving in the "key" (where to find user info in database) and which view
     * on the layout to pull the new data from.
     */
    public void saveDataBy(String key, final int tview) {
        Activity a = (Activity)mContext;
        TextView viewval = (TextView) a.findViewById(tview);
        String newInput = viewval.getText().toString();

        myRef.child(key).setValue(newInput);
    }

    public void saveDataBy(String key, Object newval) {
        key = mAuth.getCurrentUser().getUid() + key;
        myRef.child(key).setValue(newval);
    }

    public void saveRating(int r_id) {
        FirebaseUser u = mAuth.getCurrentUser();
        saveRating(u.getUid(), r_id);
    }

    public void saveRating(String user, final int r_id) {
        String key = "users/" + user + "/rating";

        Activity a = (Activity)mContext;
        RatingBar currRat = (RatingBar) a.findViewById(r_id);
        float cR = currRat.getRating();
        myRef.child(key).setValue(cR);
    }

    /** Save data for current user */
    public void saveData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        badUser(user);

        //Get key to the user node in database
        String key = "users/" + user.getUid();

        String[] k = {"name", "personalBio"};
        int[] r_id = {R.id.eTName, R.id.eTBiography};

        //update current view of user to database
        saveData(user.getUid(), k, r_id);
        saveRating(user.getUid(), R.id.ratingBar);
    }

    /** Save Data depending on input userid */
    public void saveData(String u, String[] keys, final int[] r_id) {
        //Get key to the user node in database
        String key = "users/" + u;

        //quick error check that provided keys have matching r_id
        if (keys.length != r_id.length) {
            Toast.makeText(mContext, "Error matching user id to database", Toast.LENGTH_LONG).show();
        }

        //for each provided thing, update
        for (int i = 0; i < keys.length; i++) {
            saveDataBy(key + "/" + keys[i], r_id[i]);
        }
    }

    /** Save Data depending on input userid */
    public void saveData(String[] keys, final int[] r_id) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        badUser(user);

        saveData(user.getUid(), keys, r_id);
    }

    /** Checks if user exists */
    private void badUser(FirebaseUser user) {
        //if no user is logged in, go to login page
        if (user == null) {
            badData();
        }
    }

    /** if there is bad data, start at login activity */
    private void badData() {
        System.out.println("USER IS NULL!!!!\n");
        mContext.startActivity(new Intent(mContext, logina_ctivity.class));
    }

    /** Delete user by clearing user's node from users as well as location references */
    public void deleteUserFromDatabase() {
        FirebaseUser user = mAuth.getCurrentUser();
        badUser(user);
        String userstring = user.getUid().toString();

        //Get key to the user node in database
        myRef.child("users/" + userstring).removeValue();
        myRef.child("geofire/" + userstring).removeValue();
        myRef.child(userstring + "location").removeValue();
    }
}
