package cse110.jamwithme;

/**
 * Created by Storm Quark on 11/21/2016.
 */

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MatchingDisplay extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private StorageReference storage;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    /*ListView matches;
    Intent instr_selected;
    ArrayAdapter<String> instr_adapter;
    ArrayList<String> select_list = new ArrayList<String>();
    int count = 0;*/
    ArrayList<String> userlist = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        //Get current user location
        UserLocation ul = new UserLocation(this, mAuth, myRef);

        double rad = 5;    //kilometers

        GeoFire findUsers = new GeoFire(myRef);

        //Start query
        GeoQuery query = findUsers.queryAtLocation(ul.getLongLat(), rad);
        query.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String newuserkey, GeoLocation location) {
                userlist.add(newuserkey);
                // additional code, like displaying a pin on the map
                // and adding Firebase listeners for this user
            }

            @Override
            public void onKeyExited(String newuserkey) {
                userlist.remove(newuserkey);
                // additional code, like removing a pin from the map
                // and removing any Firebase listener for this user
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
            }

            @Override
            public void onGeoQueryReady() {
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Toast.makeText(getBaseContext(), "Error retrieving geoquery", Toast.LENGTH_SHORT)
                        .show();
            }
        });


        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        instr_view = (ListView)findViewById(R.id.Instr_List);
        instr_selected = new Intent(this,experience.class);
        items_list.add("Keyboard");
        items_list.add("Piano");
        items_list.add(("Recorder"));
        items_list.add("Classical guitar");
        items_list.add("Drum kit");
        items_list.add("Electric Guitar");
        items_list.add("Violin");
        items_list.add("Percussion");
        items_list.add("Bass Guitar");
        instr_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items_list);
        instr_view.setAdapter(instr_adapter);
        instr_view.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        instr_view.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if(select_list.contains(items_list.get(position))){
                    mode.setTitle("instrument already selected");
                }
                else {
                    count = count + 1;
                    mode.setTitle(count + " items selected");
                    select_list.add(items_list.get(position));
                }

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.delete_button,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()){
                    case R.id.add_id:
                        instr_selected.putExtra("instrs",select_list.toString());
                        count = 0;
                        mode.finish();
                        //startActivity(instr_selected);
                        return true;
                    default:
                        return false;
                }
                //return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

}