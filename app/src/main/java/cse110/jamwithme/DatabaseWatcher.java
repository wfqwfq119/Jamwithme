package cse110.jamwithme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

    public DatabaseWatcher(Context c) {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        mContext = c;
    }

    /** Update data by giving in the "key" (where to find user info in database) and which view
     * on the layout to put the new data to.
     */
    private void updateDataBy(String key, final int tview) {

        myRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Activity a = (Activity)mContext;
                String newval = (String) dataSnapshot.getValue();
                TextView viewval = (TextView)a.findViewById(tview);
                viewval.setText(newval);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void updateData() {
        FirebaseUser user = mAuth.getCurrentUser();

        //If no user is logged in, go to login page
        if (user == null) {
            mContext.startActivity(new Intent(mContext, logina_ctivity.class));
        }

        //Get key to the user node in database
        String key = "users/" + user.getUid();
        //update name data of that user into the name view
        updateDataBy(key+"/name", R.id.eTName);

        /*myRef.child("users").child(uid).child("username").setValue(findViewById(R.id.Profile_Username));*/
    }

    public void updateData(String[] keys, final int[] r_id) {
        CharSequence error_msg = "Error: not enough data to " +
                "update or not enough matching section for data.";
        FirebaseUser user = mAuth.getCurrentUser();

        //If no user is logged in, go to login page
        if (user == null) {
            System.out.println("USER IS NULL!!!!\n");
            mContext.startActivity(new Intent(mContext, logina_ctivity.class));
        }

        //Get key to the user node in database
        String key = "users/" + user.getUid();

        //quick error check that provided keys have matching r_id
        if(keys.length != r_id.length) {
            Toast.makeText(mContext, error_msg, Toast.LENGTH_LONG).show();
        }

        //for each provided thing, update
        for(int i = 0; i < keys.length; i++) {
            updateDataBy(key+"/"+keys[i], r_id[i]);
        }
    }
}
