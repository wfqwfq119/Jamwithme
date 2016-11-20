package cse110.jamwithme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Biography extends AppCompatActivity {

    Button bNext5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biography);

        nextPage5();
    }

    // NANCY AND MAYA
    public void nextPage5() {
        bNext5 = (Button) findViewById(R.id.bNext5);
        final DatabaseWatcher d = new DatabaseWatcher(this);
        bNext5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] b = {"personalBio"};
                final int[] r_id = {R.id.eTBiography};
                d.saveData(b, r_id);
                startActivity(new Intent(Biography.this, UserProfileActivity.class));
            }
        } );

    }
}
