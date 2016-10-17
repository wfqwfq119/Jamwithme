package cse110.jamwithme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final ImageView loading_screen = (ImageView) findViewById(R.id.imageView3);
        final Animation an = AnimationUtils.loadAnimation(getBaseContext(),R.anim.rotate);

        loading_screen.startAnimation(an);
        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
                Intent after_loading = new Intent(Splash.this,logina_ctivity.class);
                startActivity(after_loading);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
