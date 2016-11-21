package cse110.jamwithme;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by Nancy on 11/20/2016.
 */

public class UsingCamera {

    Activity activity;
    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_CAMERA = 2;
    String userChoosenTask;
    private StorageReference imgStorage;
    MediaPlayer mp = new MediaPlayer();

    /* Constructor: when you want to use camera you have to pass in the activity you want to use it
                    in
     */
    public UsingCamera(Activity activity) {
        this.activity = activity;
    }

    /** GIVE CAMERA BUTTON FUNCTIONALITY **/
    public void cameraButton(ImageButton camButton) {
        //imageView = (ImageView) findViewById(R.id.ivProfile);
        camButton = (ImageButton) activity.findViewById(R.id.camButton);
        camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent image = new Intent(activity,
                        SetUpPicture.class);
                activity.startActivity(image);
            }
        });
    }

    /** CREATE DIALOG BOX **/
    public void dialogBox() {
        // create diaglog with three options
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
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
    public void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent,"Select File"), REQUEST_GALLERY);
    }

    /** GET IMAGE FROM GALLERY **/
    @SuppressWarnings("deprecation")
    public void selectFromGallery(Intent data, ImageView imageView) {
        Bitmap bm = null;
        if(data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(activity.getApplicationContext().getContentResolver(),
                        data.getData());

                // show image to user
                imageView.setImageBitmap(bm);

                String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(),bm,
                        "Your image", null);
                Picasso.with(activity).load(path).resize(100, 100).into(imageView);
                Toast.makeText(activity, "Looking good!", Toast.LENGTH_LONG).show();
            }
            catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(activity, "Unable to open image", Toast.LENGTH_SHORT).show();
            }
        }

    }
    /*----------------------------------------CAMERA----------------------------------------------*/

    /** OPEN THE CAMERA ON THE PHONE **/
    public void cameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }

    /*
       TAKE A PHOTO USING CAMERA
    */
    public void usingCamera(Intent data, ImageView imageView) {
        Bundle bundle = new Bundle();
        bundle = data.getExtras();
        Bitmap bitMap = (Bitmap) bundle.get("data");
        imageView.setImageBitmap(bitMap);
        String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(),bitMap,
                "Your image", null);
        Picasso.with(activity).load(path).resize(100, 100).into(imageView);
        Toast.makeText(activity, "Looking good!", Toast.LENGTH_LONG).show();
    }


}
