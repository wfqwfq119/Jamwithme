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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
//push it just push it
public class InstrumentSelect extends AppCompatActivity {
    ListView instr_view;
    Intent instr_selected;
    ArrayAdapter<String> instr_adapter;
    ArrayList<String> items_list = new ArrayList<String>();
    ArrayList<String> select_list = new ArrayList<String>();
    int count = 0;

    Button bNext3; // MAYA AND NANCY

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrument_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        });

        nextPage3(); // MAYA and NANCY
    }

    // MAYA AND NANCY
    public void nextPage3() {
        bNext3 = (Button) findViewById(R.id.bNext3);
        final DatabaseWatcher d = new DatabaseWatcher(this);
        bNext3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InstrumentSelect.this, experience.class));
            }
        } );
    }

}
