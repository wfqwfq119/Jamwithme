package cse110.jamwithme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserProfileActivity extends AppCompatActivity {

    public Button add_jams;
    private EditText add_Instr;

    // Give 'add_jams' button functionality
    public void init() {
        add_jams = (Button)findViewById(R.id.add_my_jams);
        add_jams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent access_jams = new Intent(UserProfileActivity.this,
                                                add_jams_activity.class);
                startActivity(access_jams);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        init();
        add_Instr = (EditText) findViewById(R.id.Profile_Instr);
        add_Instr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfileActivity.this,InstrumentSelect.class));
            }
        });
    }
}


