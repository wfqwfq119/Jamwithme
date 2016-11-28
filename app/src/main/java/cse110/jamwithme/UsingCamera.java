package cse110.jamwithme;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
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
    private String prev_activ;
    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_CAMERA = 2;
    String userChoosenTask;
    private StorageReference imgStorage;

    /* Constructor: when you want to use camera you have to pass in the activity you want to use it
                    in
     */
    public UsingCamera(Activity activity, String prev) {
        this.activity = activity;
        this.prev_activ = prev;
    }

    /** GIVE CAMERA BUTTON FUNCTIONALITY **/
    public void cameraButton(ImageButton camButton) {
        camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBox();    //This was PQ
                //Intent image = new Intent(activity,
                //        SetUpPicture.class);
                //image.putExtra("activity", prev_activ);
                //activity.startActivity(image);
            }
        });

    }
    /** default profile picture **/
    public void setDefaultPhoto(ImageView imageView, int width, int height) {
        Bitmap bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.default_pic);
        // show image to user
        imageView.setImageBitmap(bm);
        String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(),bm,
                "Your image", null);
        Picasso.with(activity).load(path).resize(width ,height).into(imageView); // width/height
        /*Picasso.with(this.activity)
               .load(path)
               .fit()
               .centerInside()
               .into(imageView); */
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
    public Uri selectFromGallery(Intent data, ImageView imageView, int width, int height) {
        Bitmap bm = null;
        String path = null;
        if(data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(activity.getApplicationContext().getContentResolver(),
                        data.getData());

                // show image to user
                imageView.setImageBitmap(bm);

                path = MediaStore.Images.Media.insertImage(activity.getContentResolver(),bm,
                        "Your image", null);
                Picasso.with(activity).load(path).resize(width, height).into(imageView);
                Toast.makeText(activity, "Looking good!", Toast.LENGTH_LONG).show();
            }
            catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(activity, "Unable to open image", Toast.LENGTH_SHORT).show();
            }
        }
        if(path != null) { return Uri.parse(path); }
        return null;

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
    public Uri usingCamera(Intent data, ImageView imageView, int width, int height) {
        Bundle bundle = new Bundle();
        bundle = data.getExtras();
        Bitmap bitMap = (Bitmap) bundle.get("data");
        imageView.setImageBitmap(bitMap);
        String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(),bitMap,
                "Your image", null);
        Picasso.with(activity).load(path).resize(width, height).into(imageView);
        Toast.makeText(activity, "Looking good!", Toast.LENGTH_LONG).show();
        return Uri.parse(path);
    }


}
