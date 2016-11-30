package cse110.jamwithme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
    private static CharSequence error_msg = "Error: not enough data to " +
            "update or not enough matching section for data.";
    private static boolean retval;

    /**Create databaseWatcher object which contains references to needed details*/
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
                //data must exist inside the database at given key
                if(dataSnapshot.exists()) {
                    //Update given textview by the value at the key
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

    /** Check for current user existing and then update */
    public void updateData(String[] keys, final int[] r_id) {
        FirebaseUser user = mAuth.getCurrentUser();
        badUser(user);

        updateOtherUserTextData(user.getUid(), keys, r_id);
    }

    /** Update input users' text data according to userid */
    public void updateOtherUserTextData(String userid, String[] keys, final int[] r_id) {
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

    /** Update rating for current user after checking if they exist */
    public void updateRating(final int r_id) {
        FirebaseUser user = mAuth.getCurrentUser();
        badUser(user);

        updateRating(user.getUid(), r_id);
    }

    /** Update rating for input user */
    public void updateRating(String uid, final int r_id) {
        //Get key to the user node in database
        String key = "users/" + uid + "/rating";

        myRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Activity a = (Activity)mContext;
                //If data exists in database, update the rating bar
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
        //Update user location info
        UserLocation ul = new UserLocation(mContext, mAuth, myRef);

        //Update textviews of personal bio and name sections
        String[] elems = {"personalBio", "name"};
        final int[] info = {R.id.eTBiography, R.id.eTName};
        updateData(elems, info);

        updateRating(R.id.ratingBar);   //Update rating
        myRef.child(mAuth.getCurrentUser().getUid() + "/location").setValue(ul.getLongLat()); //Save location info
    }

    /** Update profile to reflect database info from given uid*/
    public void updateUserProfile(String uid) {
        String[] elems = {"personalBio", "name"};
        final int[] info = {R.id.eTBiography, R.id.eTName};

        updateOtherUserTextData(uid, elems, info);
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

    /** Save basic object into a user's account via provided key */
    public void saveDataBy(String key, Object newval) {
        key = "users/" + mAuth.getCurrentUser().getUid() + "/" + key;
        myRef.child(key).setValue(newval);
    }

    /** Save current user's rating */
    public void saveRating(int r_id) {
        saveRating(mAuth.getCurrentUser().getUid(), r_id);
    }

    /** Save input user's rating */
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

        // name and personalBio
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

    public boolean badUser(final String uid) {
        retval = false;

        myRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(uid)) {
                    retval = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

        System.out.println("user " + uid + " exists: " + retval);
        return retval;
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

        //remove location from geofire query
        GeoFire gf = new GeoFire(myRef.child("geofire"));
        gf.removeLocation(userstring);

        myRef.child("geofire/" + userstring).removeValue();
        myRef.child(userstring + "location").removeValue();
    }
}
