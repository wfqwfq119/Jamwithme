package cse110.jamwithme;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.annotation.Target;
import java.net.URI;
import java.util.jar.*;


public class add_jams_activity extends AppCompatActivity {

    ListView arr_jams;
    ArrayAdapter<String> adapter;
    String[] jams = {"PLAY Jam1", "PLAY Jam2", "PLAY Jam3", "PLAY Jam4", "PLAY Jam5"};


    private MediaPlayer jam_mp = new MediaPlayer();
    private static final int AUDIO_INTENT = 1;
    private static final int REQ_READ_EXT_STORAGE = 2;

    private ProgressDialog upl_progress;
    private StorageReference myjams;
    private StorageReference store_aud;
    private Button upload_aud;
    private Button delete_aud;
    private ImageButton stop_jam;


    // Suspend media player if user returns to profile page
    @Override
    protected void onPause() {
        super.onPause();
        jam_mp.release();
    }

    // Setup layout: ListView with arr_jams[] elems, Buttons for Media Player,
    //               Buttons for profile customization
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jams_activity);


        // Always display arr_jams[] elements in the ListView
        arr_jams = (ListView) findViewById(R.id.jams_list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, jams);
        arr_jams.setAdapter(adapter);
        store_aud = FirebaseStorage.getInstance().getReferenceFromUrl("gs://jamwme-63b9e.appspot.com");

        upload_aud = (Button) findViewById(R.id.upload);
        delete_aud = (Button) findViewById(R.id.del_audio);
        upl_progress = new ProgressDialog(this);


        // 'STOP JAM' button is on --> Media player suspends
        stop_jam = (ImageButton) findViewById(R.id.stop_audio);
        stop_jam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jam_mp.stop();
            }
        });



        // 'UPLOAD JAM' button is on --> Single audio file can be uploaded from device
        upload_aud.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < 23) {
                    Toast.makeText(getApplicationContext(), "Setting > Apps > Jam With Me > Permissions > Storage", Toast.LENGTH_LONG).show();
                }
                else {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        Intent sel_sound = new Intent(Intent.ACTION_GET_CONTENT);
                        sel_sound.setType("audio/*");
                        startActivityForResult(sel_sound, AUDIO_INTENT);
                    }
                    else {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_READ_EXT_STORAGE);
                    }
                }
            }
        });




        // Populate listview with playable sound files
        arr_jams.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override //TODO
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO
            }
        }); //TODO
    }

    // Handle results for runtime permissions page (external storage)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent sel_sound = new Intent(Intent.ACTION_GET_CONTENT);
            sel_sound.setType("audio/*");
            startActivityForResult(sel_sound, AUDIO_INTENT);
        }
        else {
            Toast.makeText(this, "Cannot UPLOAD JAM without permission to external storage!", Toast.LENGTH_LONG).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    // Single sound file is chosen --> file uploaded to FireBase server
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUDIO_INTENT && resultCode == RESULT_OK) {
            upl_progress.setMessage("Uploading Audio...");
            upl_progress.show();

            Uri uri = data.getData();

            StorageReference file_path = store_aud.child("MyJams").child(uri.getLastPathSegment());
            file_path.putFile(uri).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            upl_progress.dismiss();
                            Uri download_uri = taskSnapshot.getDownloadUrl();

                            //TODO


                            Toast.makeText(add_jams_activity.this, "Upload Successful", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }


}

//REQUESTS FROM CUSTOMERS:

// -listview items are more toggle friendly (play/pause)

// -scenario: Given there are five sound files already uploaded, when the user tries
//            to upload another sound file, then a "error" text box will appear.


