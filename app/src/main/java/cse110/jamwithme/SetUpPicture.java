package cse110.jamwithme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SetUpPicture extends AppCompatActivity {
    private Intent prev_intent;
    private String prev_activ;
    private StorageReference storage;
    private ProgressDialog upl_progress;
    UsingCamera camObj;
    ImageView imageView;
    ImageButton camButton;
    Button bConf;
    Button bNext;
    Uri img_uri;

    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = FirebaseStorage.getInstance().getReference();
        prev_intent = getIntent();
        prev_activ = prev_intent.getStringExtra("activity");
        upl_progress = new ProgressDialog(this);

        setContentView(R.layout.activity_set_up_picture);

        bConf = (Button)findViewById(R.id.bUpload);
        bConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_uri != null) {
                    upload_img(img_uri);
                }
            }
        });

        imageView = (ImageView) findViewById(R.id.ivProfile);
        camButton = (ImageButton) findViewById(R.id.camButton);
        camObj = new UsingCamera(this, prev_activ);

        camObj.cameraButton(camButton);
        //camObj.dialogBox(); //TODO remove later
        nextPage();

    }

    /** Upload audio file to FireBase Storage w/authentication and handle results **/
    private void upload_img(Uri toUpload) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //Get key to the user node in database
        String key = "users/" + user.getUid() + "/myimg";

        final UploadTask upl_task = storage.child(key).putFile(toUpload);
        upl_task.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int progress = (int) ((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                upl_progress.setMessage("Upload is " + progress + "% done");
                upl_progress.show();

                upl_task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(SetUpPicture.this, "Upload Successful!", Toast.LENGTH_LONG).show();
                        upl_progress.dismiss();
                    }
                });
                upl_task.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SetUpPicture.this, "Upload Failed!", Toast.LENGTH_LONG).show();
                        upl_progress.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            img_uri = data.getData();
        }
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            camObj.usingCamera(data, imageView);
        }
        if (requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
            camObj.selectFromGallery(data, imageView);
        }

    }

    /*------------------------------- GO TO NEXT PAGE ---------------------------------------*/
    public void nextPage() {
        bNext = (Button) findViewById(R.id.bNext);
        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prev_activ.equals("RegisterActivity")) {
                    Intent next = new Intent(SetUpPicture.this, add_jams_activity.class);
                    next.putExtra("activity", "SetUpPicture");
                    startActivity(next);
                }
                else if (prev_activ.equals("UserProfileActivity")) {
                    startActivity(new Intent(SetUpPicture.this, UserProfileActivity.class));
                }
            }
        });
    }
}
