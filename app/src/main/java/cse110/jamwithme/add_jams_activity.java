package cse110.jamwithme;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class add_jams_activity extends AppCompatActivity {
    private static final int AUDIO_INTENT = 1;
    private static final int REQ_READ_EXT_STORAGE = 2;
    private MediaPlayer mp;
    private Uri aud_uri;
    private ProgressDialog upl_progress;
    private FirebaseStorage storage;
    private FirebaseAuth firebaseAuth;
    private StorageReference stor_ref;
    private Cursor returnCursor;

    private Intent prev_intent;
    private String prev_activ;
    private Intent sel_sound;
    private ImageButton play_mp;
    private ImageButton stop_mp;
    private Button select_aud;
    private Button upl_aud;
    private Button delete_aud;
    private Button bNext2;
    private TextView filename_TV;


    /** Setup layout: ListView with arr_jams[] elems, Buttons for Media Player,
                  Buttons for profile customization                             **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get previous activity context to redirect accordingly when this activity finished
        prev_intent = getIntent();
        prev_activ = prev_intent.getStringExtra("activity");
        upl_progress = new ProgressDialog(this);

        setContentView(R.layout.activity_add_jams_activity);
        storage = FirebaseStorage.getInstance();
        stor_ref = storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        // Get previous activity context to redirect accordingly when this activity finished
        prev_intent = getIntent();
        prev_activ = prev_intent.getStringExtra("activity");


        // 'SELECT JAM' button is on --> Single audio file can be selected from device
        select_aud = (Button)findViewById(R.id.select_amj) ;
        select_aud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sel_sound = new Intent(Intent.ACTION_GET_CONTENT);
                sel_sound.setType("audio/*");
                // Prompt runtime permissions page iff SDK is compatible
                if (Build.VERSION.SDK_INT < 23 && (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(getApplicationContext(), "Setting > Apps > Jam With Me > Permissions > Storage", Toast.LENGTH_LONG).show();
                }
                else {
                    // Continue with upload process if permission was GRANTED
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        startActivityForResult(sel_sound, AUDIO_INTENT);
                    }
                    else {
                        // Prompt runtime permission for the first time
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_READ_EXT_STORAGE);
                    }
                }
            }
        });

        upl_aud = (Button)findViewById(R.id.bUpload);
        upl_aud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aud_uri != null) {
                    upload_aud(AUDIO_INTENT, RESULT_OK, aud_uri);
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

                if (prev_activ.equals("UserProfileActivity")) {
                    startActivity(new Intent(add_jams_activity.this, UserProfileActivity.class));
                }
                else if (prev_activ.equals("SetUpPicture")) {
                    startActivity(new Intent(add_jams_activity.this, InstrumentSelect.class));
                }
            }
        });

    }

    /** Upload audio file to FireBase Storage w/authentication and handle results **/
    private void upload_aud(int requestCode, int resultCode, Uri toUpload) {
        if(requestCode == AUDIO_INTENT && resultCode == RESULT_OK) {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            //Get key to the user node in database
            String key = "users/" + user.getUid() + "/myjam";

            final UploadTask upl_task = stor_ref.child(key).putFile(toUpload);
            upl_task.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    int progress = (int) ((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                    upl_progress.setMessage("Upload is " + progress + "% done");
                    upl_progress.show();

                    upl_task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(add_jams_activity.this, "Upload Successful!", Toast.LENGTH_LONG).show();
                            upl_progress.dismiss();
                        }
                    });
                    upl_task.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(add_jams_activity.this, "Upload Failed!", Toast.LENGTH_LONG).show();
                            upl_progress.dismiss();
                        }
                    });
                }
            });
        }
    }


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
    }
}




