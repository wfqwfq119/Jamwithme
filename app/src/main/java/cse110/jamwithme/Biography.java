package cse110.jamwithme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Biography extends AppCompatActivity {

    Button bNext4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biography);

        nextPage4();
    }

    // NANCY AND MAYA
    public void nextPage4() {
        bNext4 = (Button) findViewById(R.id.bNext4);
        final DatabaseWatcher d = new DatabaseWatcher(this);
        bNext4.setOnClickListener(new View.OnClickListener() {
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
