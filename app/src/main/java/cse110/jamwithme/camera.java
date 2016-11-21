package cse110.jamwithme;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class camera extends AppCompatActivity {

    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_CAMERA = 2;
    private ImageView imageView;
    private ImageButton camButton;
    private Button upl_img;
    private Button bNext;
    private ProgressDialog upl_progress;

    String userChoosenTask;
    private Uri img_uri;
    private FirebaseAuth firebaseAuth;
    private StorageReference stor_ref;
    MediaPlayer mp = new MediaPlayer();

    /*
     * onCreate
     * Description: CALLED WHEN ACTIVITY IS FIRST CREATED
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        upl_progress = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        stor_ref = FirebaseStorage.getInstance().getReference();

        setContentView(R.layout.activity_camera);
        imageView = (ImageView) findViewById(R.id.ivProfile);

        // the little man
        //Drawable myDrawable = getResources().getDrawable(R.drawable.defaultphoto);
        //imageView.setImageDrawable(myDrawable);
        Bitmap bm = BitmapFactory.decodeResource(this.getResources(), R.drawable.defaultphoto);
        // show image to user
        imageView.setImageBitmap(bm);
        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(),bm,
                "Your image", null);
        Picasso.with(camera.this).load(path).resize(130, 100).into(imageView); // width/height


        selectImage();
        cameraButton();

        upl_img = (Button)findViewById(R.id.bConfirm);
        upl_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_uri != null) {
                    upload_img(img_uri);
                }
            }
        });

        nextPage();
    }

    /*
    private Drawable resize(Drawable myDrawable) {
        Bitmap b = ((BitmapDrawable)myDrawable).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 5, 5, false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }
    */

    /** GETS CALLED WHEN THE ACTIVITY IS LAUNCHED AND GIVES THE REQUEST CODE
     *  AND THE RESULT IT RETURNS
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        img_uri = data.getData();
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            usingCamera(data);
            //onCaptureImageResult(data);
            //Uri uri = data.getData();

            // Firebase video
            /*StorageReference filepath = imgStorage.child("Photos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {


                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(camera.this, "Upload Done", Toast.LENGTH_LONG).show();

                }
            }); */
        }
        if (requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
            onSelectFromGalleryResult(data);
        }

    }

    /** GIVE CAMERA BUTTON FUNCTIONALITY **/
    public void cameraButton() {
        //imageView = (ImageView) findViewById(R.id.ivProfile);
        camButton = (ImageButton) findViewById(R.id.camButton);
        camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent image = new Intent(camera.this,
                        camera.class);
                startActivity(image);
            }
        });
    }

    /** CREATE DIALOG BOX **/
    private void selectImage() {
        // create diaglog with three options
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(camera.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            // check which option user chose
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    cameraIntent();
                }

                else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    galleryIntent();
                }
                else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /*--------------------------------------GALLERY-----------------------------------------------*/
    /** OPEN THE GALLERY **/
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select File"), REQUEST_GALLERY);
    }

    /** GET IMAGE FROM GALLERY **/
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if(data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),
                        data.getData());

                // show image to user
                imageView.setImageBitmap(bm);

                String path = MediaStore.Images.Media.insertImage(this.getContentResolver(),bm,
                                "Your image", null);
                Picasso.with(camera.this).load(path).resize(100, 100).into(imageView);
                Toast.makeText(camera.this, "Looking good!", Toast.LENGTH_LONG).show();
            }
            catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Unable to open image", Toast.LENGTH_SHORT).show();
            }
        }

    }

    /*----------------------------------------CAMERA----------------------------------------------*/

    /** OPEN THE CAMERA ON THE PHONE **/
    private void cameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }

    /*
        TAKE A PHOTO USING CAMERA
     */
    public void usingCamera(Intent data) {
        Bundle bundle = new Bundle();
        bundle = data.getExtras();
        Bitmap bitMap = (Bitmap) bundle.get("data");
        imageView.setImageBitmap(bitMap);
        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(),bitMap,
                "Your image", null);
        Picasso.with(camera.this).load(path).resize(100, 100).into(imageView);
        Toast.makeText(camera.this, "Looking good!", Toast.LENGTH_LONG).show();
    }

    /** CAPTURE IMAGE FROM CAMERA **/
        /*private void onCaptureImageResult(Intent data) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            FileOutputStream fo;

            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            }

            catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Unable to open image", Toast.LENGTH_SHORT).show();
            }
            catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Unable to open image", Toast.LENGTH_SHORT).show();
            }
        }*/


        /** -------------------------CONNECT TO FIREBASE ------------------------------------**/
        /*
        private void connectToFirebase(Intent data) {
            // file from intent
            Uri uri = data.getData();

            StorageReference filepath = imgStorage.child("Photos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(camera.this, "Upload Done", Toast.LENGTH_LONG.show();

                }
            });
        }*/

    /** Upload image file to FireBase Storage w/authentication and handle results **/
    private void upload_img(Uri toUpload) {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //Get key to the user node in database
        String key = "users/" + user.getUid() + "/myimg";
        final UploadTask upl_task = stor_ref.child(key).putFile(toUpload);
        upl_task.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int progress = (int) ((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                upl_progress.setMessage("Upload is " + progress + "% done");
                upl_progress.show();
            }
        });


        upl_task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(camera.this, "Upload Successful!", Toast.LENGTH_LONG).show();
                upl_progress.dismiss();
            }
        });

        upl_task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(camera.this, "Upload Failed!", Toast.LENGTH_LONG).show();
                upl_progress.dismiss();
            }
        });

    }


    /*------------------------------- GO TO NEXT PAGE ---------------------------------------*/
    public void nextPage() {
        bNext = (Button) findViewById(R.id.bNext);
        bNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent next = new Intent(camera.this, add_jams_activity.class);
                    next.putExtra("activity", "camera");
                    startActivity(next);
                }

        });

    }
}