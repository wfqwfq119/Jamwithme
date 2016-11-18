package cse110.jamwithme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

public class experience extends AppCompatActivity {

    Button bNext3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience);

        nextPage3();
    }

    // NANCY AND MAYA
    public void nextPage3() {
        bNext3 = (Button) findViewById(R.id.bNext3);
        final DatabaseWatcher d = new DatabaseWatcher(this);
        bNext3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RatingBar currRat = (RatingBar) findViewById(R.id.ratingBar2);
                float cR = currRat.getRating();
                d.saveRating("rating", cR);

                startActivity(new Intent(experience.this, Biography.class));
            }
        } );

    }
}
