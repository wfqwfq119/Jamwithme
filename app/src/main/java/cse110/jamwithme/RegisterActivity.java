package cse110.jamwithme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText Re_Email;
    private EditText Re_Instr;
    private EditText Re_Name;
    private EditText Re_Pass;
    private Button Re_Button;

    private ProgressDialog Re_Pro;

    private FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Re_Pro = new ProgressDialog(RegisterActivity.this);

        fAuth = FirebaseAuth.getInstance();

        Re_Email = (EditText)findViewById(R.id.Register_Email);
        Re_Instr = (EditText)findViewById(R.id.Re_Instr);
        Re_Name = (EditText)findViewById(R.id.Re_Username);
        Re_Pass = (EditText)findViewById(R.id.Register_password);
        Re_Button = (Button)findViewById(R.id.Re_button);

        Re_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }
    private void registerUser(){
        String email = Re_Email.getText().toString().trim();
        String password = Re_Pass.getText().toString().trim();

        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this,"email or password fild is empty",Toast.LENGTH_LONG).show();
            return;
        }
        Re_Pro.setMessage("Registering User...");
        Re_Pro.show();
        fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Re_Pro.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"Registered Successfully",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this,UserProfileActivity.class));
                }
                else{
                    Toast.makeText(RegisterActivity.this,"Cannot register, please try again",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
