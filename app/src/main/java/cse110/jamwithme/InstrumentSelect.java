package cse110.jamwithme;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InstrumentSelect extends AppCompatActivity {
    private Intent prev_intent;
    private String prev_activ;
    ListView instr_view;
    Intent instr_selected;
    ArrayAdapter<String> instr_adapter;
    ArrayList<String> items_list = new ArrayList<String>(); // available instruments
    ArrayList<String> select_list = new ArrayList<String>(); // instruments selected by user
    int count = 0;
    String User_Uid;
    DatabaseReference mChildRef;

    Button bNext3;

    /*
    * Description: this method is called to set up the activity when it starts,
    * which includes displaying the instruments users can choose
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prev_intent = getIntent();
        prev_activ = prev_intent.getStringExtra("activity");
        setContentView(R.layout.activity_instrument_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        instr_view = (ListView)findViewById(R.id.Instr_List);
        instr_selected = new Intent(this,experience.class);
        // list of instruments users can choose
        items_list.add("Keyboard");
        items_list.add("Piano");
        items_list.add(("Recorder"));
        items_list.add("Classical Guitar");
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
            /*
             * Description: This method keeps track of the instruments that the user selects
             */
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
                        //instr_selected.putExtra("instrs",select_list.toString());
                        User_Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        mChildRef = FirebaseDatabase.getInstance().getReference("/users/"+User_Uid + "/Instruments");
                        for(String i : select_list){
                            mChildRef.push().setValue(i);
                        }
                        count = 0;
                        mode.finish();
                        //startActivity(instr_selected);
                        return true;
                    case R.id.remove_id:
                        User_Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        mChildRef = FirebaseDatabase.getInstance().getReference("/users/"+User_Uid + "/Instruments");
                        for(int i = 0; i < select_list.size(); i++){
                            final String words = select_list.get(i);
                            Query delete_Query = mChildRef.orderByValue().equalTo(words);
                            delete_Query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot my_Snapshot: dataSnapshot.getChildren()){
                                        if(my_Snapshot.getValue().equals(words)){
                                            my_Snapshot.getRef().removeValue();
                                            Toast.makeText(InstrumentSelect.this,words+" removed!",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        count = 0;
                        mode.finish();
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
        });

        nextPage3(); // MAYA and NANCY
    }

    /*
     * Description: Goes to the appropriate activity page based on which activity it was previously
     *              called from.
     */
    public void nextPage3() {
        bNext3 = (Button) findViewById(R.id.bNext3);
        final DatabaseWatcher d = new DatabaseWatcher(this);

        // define the action taken when the user clicks the next button
        bNext3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prev_activ.equals("UserProfileActivity")) {
                    startActivity(new Intent(InstrumentSelect.this, UserProfileActivity.class));
                }
                else if (prev_activ.equals("add_jams_activity")) {
                    startActivity(new Intent(InstrumentSelect.this, experience.class));
                }
            }
        });
    }

}
