package cse110.jamwithme;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URI;


class MP3_filter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        return (name.endsWith(".mp3"));
    }
}


public class add_jams_activity extends AppCompatActivity {

    ListView arr_jams;
    ArrayAdapter<String> adapter;
    String [] jams = {"PLAY Jam1", "PLAY Jam2", "PLAY Jam3", "PLAY Jam4", "PLAY Jam5"};


    private MediaPlayer jam_mp = new MediaPlayer();
    private static final int AUDIO_INTENT = 1;
    private StorageReference store_aud;
    private Button upload_aud;
    private Button stop_jam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jams_activity);

        store_aud = FirebaseStorage.getInstance().getReference();
        upload_aud = (Button)findViewById(R.id.upload) ;
        stop_jam = (Button)findViewById(R.id.stop_audio);

        // arr_jams[] displayed when 'ADD JAM' button is on
        arr_jams = (ListView)findViewById(R.id.jams_list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, jams);
        arr_jams.setAdapter(adapter);

        // Single audio file can be uploaded from device when "UPLOAD JAM" button is on
        upload_aud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sel_sound = new Intent(Intent.ACTION_GET_CONTENT);
                sel_sound.setType("audio/*");
                startActivityForResult(sel_sound, AUDIO_INTENT);
            }
        });


        // Enable media player when user clicks on particular sound ref //TODO
        arr_jams.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                    Toast.makeText(add_jams_activity.this, "Position ONE", Toast.LENGTH_LONG).show();
                }

                else if (position == 1) {
                    Toast.makeText(add_jams_activity.this, "Position TWO", Toast.LENGTH_LONG).show();
                }

                else if (position == 2) {
                    Toast.makeText(add_jams_activity.this, "Position THREE", Toast.LENGTH_LONG).show();
                }

                else if (position == 3) {
                    Toast.makeText(add_jams_activity.this, "Position FOUR", Toast.LENGTH_LONG).show();
                }

                else if (position  == 4){
                    Toast.makeText(add_jams_activity.this, "Position FIVE", Toast.LENGTH_LONG).show();
                }
                //adapterView.getItemAtPosition(position);
                //Intent play_jam = new Intent(//Get )
            } //TODO
        }); //TODO

        // Stop Button Functionality: Media player suspends when this button is on
        Button stop_jam = (Button)findViewById(R.id.stop_audio);
        stop_jam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jam_mp.stop();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == AUDIO_INTENT && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            StorageReference file_path = store_aud.child("MyJams").child(uri.getLastPathSegment());
            file_path.putFile(uri).addOnSuccessListener(
                                   new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(add_jams_activity.this,
                                   "Upload Successful", Toast.LENGTH_LONG).show();

                }
            });
        }
    }
}
