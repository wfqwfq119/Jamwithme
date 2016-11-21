package cse110.jamwithme;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class SetUpPicture extends AppCompatActivity {
    UsingCamera camObj;
    ImageView imageView;
    ImageButton camButton;
    Button bNext;

    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_picture);

        imageView = (ImageView) findViewById(R.id.ivProfile);
        camObj = new UsingCamera(this);

        camObj.cameraButton(camButton);
        camObj.dialogBox();
        nextPage();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                startActivity(new Intent(SetUpPicture.this,add_jams_activity.class));
            }
        });
    }
}
