package cse110.jamwithme;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;


public class add_jams_activity extends AppCompatActivity {

    private static final int AUDIO_INTENT = 1;
    private static final int REQ_READ_EXT_STORAGE = 2;
    private MediaPlayer mp;
    private Uri aud_uri;
    private ProgressDialog upl_progress;
    private FirebaseStorage storage;
    private StorageReference stor_ref;
    private FirebaseUser fb_usr;
    private Cursor returnCursor;


    private Intent sel_sound;
    private ImageButton play_mp;
    private ImageButton stop_mp;
    private Button select_aud;
    private Button delete_aud;
    private Button bNext2;
    private TextView filename_TV;



    /** Setup layout: ListView with arr_jams[] elems, Buttons for Media Player,
                  Buttons for profile customization                             **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jams_activity);
        upl_progress = new ProgressDialog(this);
        storage = FirebaseStorage.getInstance();
        stor_ref = storage.getReference();


        // 'SELECT JAM' button is on --> Single audio file can be selected from device
        select_aud = (Button)findViewById(R.id.select_amj) ;
        select_aud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Prompt runtime permissions page iff SDK is compatible
                if (Build.VERSION.SDK_INT < 23) {
                    Toast.makeText(getApplicationContext(), "Setting > Apps > Jam With Me > Permissions > Storage", Toast.LENGTH_LONG).show();
                }
                else {
                    // Continue with upload process if permission was GRANTED
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        sel_sound = new Intent(Intent.ACTION_GET_CONTENT);
                        sel_sound.setType("audio/*");
                        startActivityForResult(sel_sound, AUDIO_INTENT);
                    }
                    else {
                        // Prompt runtime permission for the first time
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_READ_EXT_STORAGE);
                    }
                }
            }
        });


        delete_aud = (Button)findViewById(R.id.delet_amj);
        delete_aud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    mp.stop();
                    mp.release();
                }
                aud_uri = null;
                filename_TV = (TextView)findViewById(R.id.curr_jam);
                filename_TV.setText(R.string.def);
            }
        });


        bNext2 = (Button)findViewById(R.id.bNext2);
        bNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp != null) {
                    if (mp.isPlaying()) {
                        mp.stop();
                        mp.release();
                    }
                }
                if (aud_uri != null) { upload_aud(AUDIO_INTENT, RESULT_OK, aud_uri); }
                startActivity(new Intent(add_jams_activity.this,experience.class));
            }
        });
        //nextPage2();

    }

    private void upload_aud(int requestCode, int resultCode, Uri toUpload) {
        if(requestCode == AUDIO_INTENT && resultCode == RESULT_OK) {
            upl_progress.setMessage("Uploading Audio...");
            upl_progress.show();

            String upl_path = "MyJam/" + UUID.randomUUID() + ".mp3";
            StorageReference upl_ref = storage.getReference(upl_path);

            upl_progress.setMessage("Uploading Audio...");
            upl_progress.show();
            UploadTask upl_task = upl_ref.putFile(toUpload);
            upl_task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(add_jams_activity.this, "Upload Successful", Toast.LENGTH_LONG).show();
                }
            });

            //stor_ref = storage.getReference().child("MyJam").child(toUpload.getPath());
            /*stor_ref.putFile(toUpload).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    upl_progress.dismiss();
                    Toast.makeText(add_jams_activity.this, "Upload Successful", Toast.LENGTH_LONG).show();
                }
            }); */
        }
    }

    /* NANCY AND MAYA
    public void nextPage2() {

        bNext2 = (Button) findViewById(R.id.bNext2);
        bNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< Updated upstream
                if (mp != null) {
                    if (mp.isPlaying()) {
                        mp.stop();
                        mp.release();
                    }
                }
                startActivity(new Intent(add_jams_activity.this,UserProfileActivity.class));
=======
                startActivity(new Intent(add_jams_activity.this, experience.class));
>>>>>>> Stashed changes
            }
        } );
    } */



    /** Handle results for runtime permissions page to READ_EXTERNAL_STORAGE **/
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Cannot UPLOAD JAM without permission to external storage!", Toast.LENGTH_LONG).show();
        }
        else {
            sel_sound = new Intent(Intent.ACTION_GET_CONTENT);
            sel_sound.setType("audio/*");
            startActivityForResult(sel_sound, AUDIO_INTENT);
        }
    }


    /** Single sound file is chosen --> file uploaded to FireBase server **/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) { return; }

        aud_uri = data.getData();
        mp = MediaPlayer.create(this, aud_uri);

        returnCursor = getContentResolver().query(aud_uri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();

        filename_TV = (TextView)findViewById(R.id.curr_jam);
        filename_TV.setText(returnCursor.getString(nameIndex));

        play_mp = (ImageButton)findViewById(R.id.ib_play_amj);
        play_mp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
            }
        });


        stop_mp = (ImageButton)findViewById(R.id.ib_stop_amj);
        stop_mp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                mp = MediaPlayer.create(add_jams_activity.this, aud_uri);
            }
        });



        /*if(requestCode == AUDIO_INTENT && resultCode == RESULT_OK) {
            upl_progress.setMessage("Uploading Audio...");
            upl_progress.show();

            //File prof_jam = new File(uri.getPath());
            //final String next_jam_name = next_jam.getName();

            StorageReference file_path = store_aud.child("MyJams").child(uri.getPath());
            file_path.putFile(uri).addOnSuccessListener(
                                   new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    upl_progress.dismiss();
                    Toast.makeText(add_jams_activity.this, "Upload Successful", Toast.LENGTH_LONG).show();
                }
            });
        } */
    }






}


