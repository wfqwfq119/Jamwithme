package cse110.jamwithme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
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
import com.squareup.picasso.Picasso;

public class SetUpPicture extends AppCompatActivity {
    private Intent prev_intent;
    private String prev_activ;
    private StorageReference storage;
    private ProgressDialog upl_progress;
    UsingCamera camObj;
    ImageView imageView;
    ImageButton camButton;
    Button bNext;
    Uri img_uri;

    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_CAMERA = 2;
    private static int picWidth = 55;
    private static int picHeight = 60;

    /*
     * Description: this method is called to set up the activity when it starts
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = FirebaseStorage.getInstance().getReference();
        prev_intent = getIntent();
        prev_activ = prev_intent.getStringExtra("activity");
        upl_progress = new ProgressDialog(this);

        setContentView(R.layout.activity_set_up_picture);
        imageView = (ImageView) findViewById(R.id.ivProfile);
        camButton = (ImageButton) findViewById(R.id.camButton);
        camObj = new UsingCamera(this, prev_activ);

        camObj.cameraButton(camButton);

        // set Default picture
        camObj.setDefaultPhoto(imageView, picWidth, picHeight);

        nextPage();
    }

    /* Determine whether to take a picture from camera or upload from gallery*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            img_uri = camObj.usingCamera(data, imageView, picWidth, picHeight);
        }
        if (requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
            img_uri = camObj.selectFromGallery(data, imageView, picWidth, picHeight);
        }
    }

    /** Set up next activity page **/
    public void next_activ() {
        if (prev_activ.equals("RegisterActivity")) {
            Intent next = new Intent(SetUpPicture.this, add_jams_activity.class);
            next.putExtra("activity", "SetUpPicture");
            startActivity(next);
        }
        else if (prev_activ.equals("UserProfileActivity")) {
            startActivity(new Intent(SetUpPicture.this, UserProfileActivity.class));
        }
    }

    /** Go to next activity page **/
    public void nextPage() {
        bNext = (Button) findViewById(R.id.bNext);
        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (img_uri != null) {
                upl_progress.setMessage("Uploading profile picture...");
                upl_progress.show();
                camObj.upload_img(img_uri,storage, upl_progress);
                //upload_img(img_uri);
                next_activ();
            }
            else { next_activ(); }
            }
        });
    }
}
