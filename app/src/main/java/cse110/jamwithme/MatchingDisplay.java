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

public class MatchingDisplay extends CreateMenu {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private StorageReference storage;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Intent prev_intent;
    //rivate String updated;

    ListView matches;
    static ArrayList<String> userlist;
    static ArrayList<String> userlistname;
    static ArrayList<String> friends;
    static int indx;
    ArrayAdapter<String> userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        prev_intent = getIntent();

        userlist = new ArrayList<String>();
        userlistname = new ArrayList<String>();
        friends = new ArrayList<String>();

        //Pull friend list
        DatabaseReference fref = myRef.child("users").child(mAuth.getCurrentUser().getUid()).child
                ("friends");

        final Context c = this;

        fref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot ds: dataSnapshot.getChildren()) {
                        friends.add(ds.getKey().toString());
                    }
                }
                else {
                    //Toast.makeText(c, "Data fref doesn't exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        final DatabaseReference userRef = myRef.child("users");

        //Get current user location
        UserLocation ul = new UserLocation(this, mAuth, myRef);

        double rad = 5;    //kilometers

        GeoFire findUsers = new GeoFire(myRef.child("geofire"));
        matches = (ListView) findViewById(R.id.Matches_List);

        //Set list view
        userAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                userlistname);
        matches.setAdapter(userAdapter);

        //Start query
        GeoQuery query = findUsers.queryAtLocation(ul.getLongLat(), rad);
        query.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String newuserkey, GeoLocation location) {
                user = mAuth.getCurrentUser();
                String userUID = user.getUid();

                // Prevent users from adding themselves to their matches
                if (userUID.equals(newuserkey) || friends.contains(newuserkey)) {
                    System.out.println("Friends: ");
                    for (String f : friends) {
                        System.out.println(f);
                    }
                }
                else {
                    userlist.add(newuserkey);

                    //get username
                    userRef.child(newuserkey).child("name").addListenerForSingleValueEvent(new
                                                                                                 ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String ds = dataSnapshot.getValue().toString();
                                //userlistname.add(dataSnapshot.getValue().toString());
                                userlistname.add(ds);

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
                            //userlistname.remove("Failed User");
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
}