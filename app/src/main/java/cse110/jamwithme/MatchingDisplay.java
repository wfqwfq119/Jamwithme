package cse110.jamwithme;

/**
 * Created by Storm Quark on 11/21/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MatchingDisplay extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private StorageReference storage;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Intent prev_intent;
    private String updated;

    ListView matches;
    static ArrayList<String> userlist = new ArrayList<String>();
    static ArrayList<String> userlistname = new ArrayList<String>();
    ArrayAdapter<String> userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        prev_intent = getIntent();
        updated = prev_intent.getStringExtra("updated");
        System.out.println("updated: " + updated + "\n");

        final Intent userFound = new Intent(this, ProfileDisplay.class);
        final DatabaseReference userRef = myRef.child("users");
        final Context c = this;

        //Get current user location
        UserLocation ul = new UserLocation(this, mAuth, myRef);

        double rad = 5;    //kilometers

        GeoFire findUsers = new GeoFire(myRef.child("geofire"));
        matches = (ListView) findViewById(R.id.Matches_List);

        //Set list view
        userAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                userlistname);
        matches.setAdapter(userAdapter);


        if (updated.equals("false")) {
            //Start query
            GeoQuery query = findUsers.queryAtLocation(ul.getLongLat(), rad);
            query.addGeoQueryEventListener(new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(String newuserkey, GeoLocation location) {
                    user = mAuth.getCurrentUser();
                    String userUID = user.getUid();

                    // Prevent users from adding themselves to their matches
                    if (userUID.equals(newuserkey)) {
                        return;
                    }

                    userlist.add(newuserkey);

                    //get username
                    userRef.child(newuserkey).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                userlistname.add(dataSnapshot.getValue().toString());
                                userAdapter.notifyDataSetChanged();
                            } else {
                                //userlistname.add("Failed User");
                                userAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    userAdapter.notifyDataSetChanged();
                }

                @Override
                public void onKeyExited(String newuserkey) {
                    userlist.remove(newuserkey);
                    userRef.child(newuserkey).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                userlistname.remove(dataSnapshot.getValue().toString());
                                userAdapter.notifyDataSetChanged();
                            } else {
                                userlistname.remove("Failed User");
                                userAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    userAdapter.notifyDataSetChanged();
                }

                @Override
                public void onKeyMoved(String key, GeoLocation location) {
                }

                @Override
                public void onGeoQueryReady() {
                }

                @Override
                public void onGeoQueryError(DatabaseError error) {
                    Toast.makeText(getBaseContext(), "Error retrieving geoquery", Toast.LENGTH_SHORT).show();
                }
            });
        }
        matches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent query = new Intent(MatchingDisplay.this, MatchQuery.class);
                String pos = Integer.toString(position);
                query.putExtra("position", pos);
                startActivity(query);
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
            case R.id.navi_friend:
                startActivity(new Intent(this,friend_list.class));
                break;
            case R.id.matching:
                Intent match = new Intent(this, MatchingDisplay.class);
                match.putExtra("updated", "false");
                startActivity(match);
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