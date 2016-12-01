package cse110.jamwithme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

/*
 * This activity lets the user enter the experience they've had with music
 * based on the amount of years they have been playing.
 */
public class experience extends AppCompatActivity {

    Button bNext4;

    /*
    * Description: this method is called to set up the activity when it starts
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience);

        nextPage4();
    }

    /*
     * This method enables the user to move on to the next activity by clicking the
     * next button.
     */
    public void nextPage4() {
        bNext4 = (Button) findViewById(R.id.bNext4);
        final DatabaseWatcher d = new DatabaseWatcher(this);
        bNext4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.saveRating(R.id.ratingBar2);

                startActivity(new Intent(experience.this, Biography.class));
            }
        } );
    }
}
