package cse110.jamwithme;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class camera extends AppCompatActivity {
    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_CAMERA = 2;
    private ImageView imageView;
    private ImageButton imageButton;

          //private Bitmap bitmap;
          //private ProgressDialog dialog;
          //Uri imageUri;

    String userChoosenTask;
    private StorageReference imgStorage;
    MediaPlayer mp = new MediaPlayer();

    //static final int REQUEST_IMAGE_CAPTURE = 1;

    /** CALLED WHEN ACTIVITY IS FIRST CREATED **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_camera);
        //imageView = (ImageView) findViewById(R.id.ivProfile2);
        selectImage();
        setContentView(R.layout.activity_user_profile);
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

    /** OPEN THE CAMERA ON THE PHONE **/
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    /** OPEN THE GALLERY **/
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select File"), REQUEST_GALLERY);
    }

    /** GETS CALLED WHEN THE ACTIVITY IS LAUNCHED AND GIVES THE REQUEST CODE
      *  AND THE RESULT IT RETURNS
      * @param requestCode
      * @param resultCode
      * @param data
      */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA)
                 onCaptureImageResult(data);
            if (requestCode == REQUEST_GALLERY)
                onSelectFromGalleryResult(data);
        }
    }

    /** CAPTURE IMAGE FROM CAMERA **/
        private void onCaptureImageResult(Intent data) {
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
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        /** GET IMAGE FROM GALLERY **/
        @SuppressWarnings("deprecation")
        private void onSelectFromGalleryResult(Intent data) {
            Bitmap bm = null;
            if(data != null) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),
                    data.getData());
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            imageView.setImageBitmap(bm);
          }

        /** CONNECT TO FIREBASE **/
        private void connectToFirebase(Intent data) {
            Uri uri = data.getData();
            StorageReference filepath = imgStorage.child("Photos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Toast.makeText(Camera.this, "Upload Done", Toast.LENGTH_LONG.show();

                }
            });
        }


}