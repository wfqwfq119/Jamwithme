package cse110.jamwithme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class add_jams_activity extends AppCompatActivity {

    ListView arr_jams;
    ArrayAdapter<String> adapter;
    String [] jams = {"Jam1", "Jam2", "Jam3", "Jam4", "Jam5"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jams_activity);
        arr_jams = (ListView)findViewById(R.id.jams_view);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, jams);
        arr_jams.setAdapter(adapter);
    }
}
