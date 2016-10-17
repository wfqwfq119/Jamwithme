package cse110.jamwithme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText Re_Name = (EditText)findViewById(R.id.Register_Name);
        final EditText Re_Pass = (EditText)findViewById(R.id.Register_password);
        EditText Re_instr = (EditText)findViewById(R.id.Re_Instr1);
        EditText Re_Exp = (EditText)findViewById(R.id.Re_Exp);

        final Button Re_Button = (Button)findViewById(R.id.Re_button);
    }
}
