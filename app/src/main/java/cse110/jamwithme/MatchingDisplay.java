package cse110.jamwithme;

/**
 * Created by Storm Quark on 11/21/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
    //private StorageReference storage;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Intent prev_intent;
    //private String updated;

    ListView matches;
    static ArrayList<String> userlist;
    static ArrayList<String> userlistname;
    static ArrayList<String> friends;
    ArrayAdapter<String> userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        //prev_intent = getIntent();
        //updated = prev_intent.getStringExtra("updated");

        userlist = new ArrayList<String>();
        //userlistname = new ArrayList<String>();
        friends = new ArrayList<String>();

        //Pull friend list
        DatabaseReference fref = myRef.child("users").child(mAuth.getCurrentUser().getUid()).child
                ("friends");

        fref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                friends.add(dataSnapshot.getKey().toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
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
                } else {
                    userlist.add(newuserkey);
                    userAdapter.notifyDataSetChanged();
                }
            }

            //Remove users from matches once they exit query
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

            //Once query is done adjusting, initialize all user names from the user id's noted
            @Override
            public void onGeoQueryReady() {
                userlistname = new ArrayList<String>();
                for (String s : userlist) {
                    userRef.child(s).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String ds = dataSnapshot.getValue().toString();
                                userlistname.add(ds);
                            } else {
                                //userlistname.add("Failed User");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    //Update the list view with the names
                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Toast.makeText(getBaseContext(), "Error retrieving geoquery", Toast.LENGTH_SHORT).show();
            }
        });

        //If click a person, go to display their profile for a "match"
        matches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userAdapter.notifyDataSetChanged();
                Intent query = new Intent(MatchingDisplay.this, MatchQuery.class);
                String pos = Integer.toString(position);
                query.putExtra("position", pos);

                startActivity(query);
            }
        });
    }
}