package cse110.jamwithme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class logina_ctivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logina_ctivity);
        final EditText Login_name = (EditText)findViewById(R.id.Login_name);
        final EditText Login_Pass = (EditText)findViewById(R.id.Login_pass);
        final Button Login_button = (Button)findViewById(R.id.Login_botton);
        final TextView register_link = (TextView)findViewById(R.id.Login_tv);

        register_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(logina_ctivity.this,RegisterActivity.class);
                logina_ctivity.this.startActivity(registerIntent);
            }
        });
    }
}
